--------------------------------------------------------------------------------------------------------------------------------------
-- DATABASE
--------------------------------------------------------------------------------------------------------------------------------------
DROP SCHEMA IF EXISTS smu CASCADE;
CREATE SCHEMA smu;

--Tabella Famiglia
CREATE TABLE smu.Famiglia(
    IdFamiglia   SERIAL,
    NomeFamiglia  VARCHAR(32)  NOT NULL,

    CONSTRAINT PK_famiglia PRIMARY KEY (IdFamiglia)
);

--Tabella Utente
CREATE TABLE smu.Utente(
    Username VARCHAR(32),
    Nome     VARCHAR(32)  NOT NULL,
    Cognome  VARCHAR(32)  NOT NULL,
    Telefono VARCHAR(13)  NOT NULL UNIQUE,
    Email    VARCHAR(128) NOT NULL UNIQUE,
    Password VARCHAR(32)  NOT NULL,

    CONSTRAINT PK_Utente PRIMARY KEY (Username),
    CONSTRAINT UK_Utente UNIQUE (Username, Email),
    CONSTRAINT CK_Telefono CHECK (Telefono ~ '\+[0-9]{2}[0-9]{10}'),
    CONSTRAINT CK_Email CHECK (Email ~ '[a-zA-Z0-9._%+\-]@[a-zA-Z0-9.-]\d*.*[A-Za-z]{2,4}'),
    CONSTRAINT CK_Password CHECK (Password ~ '[a-zA-Z0-9! " # $ % & ( ) * + , - . / : ; < = > ? @ \[ \] \ ^ _` \{ | \} ~ ]{8,32}')
);


--Tabella Conto Corrente
CREATE TABLE smu.ContoCorrente(
    NumeroConto VARCHAR(12),
    IBAN        VARCHAR(27)  UNIQUE NOT NULL,
    Saldo       FLOAT        NOT NULL,
    NomeBanca   VARCHAR(128) NOT NULL,
    BIC         VARCHAR(11)  UNIQUE NOT NULL,
    Username    VARCHAR(32),

    CONSTRAINT PK_Conto PRIMARY KEY (NumeroConto),
    CONSTRAINT FK_Utente FOREIGN KEY (Username) REFERENCES smu.Utente (Username) ON DELETE CASCADE,
    CONSTRAINT CK_IBAN CHECK (IBAN ~ '[A-Z]{2}[0-9]{2}[A-Z]{1}[0-9]{5}[0-9]{5}[0-9A-Z]{5}'),
    CONSTRAINT CK_BIC CHECK (BIC ~ '[A-Z]{6}[0-9A-Z]{2}[0-9A-Z]{0,3}')
);


--Tabella Carta
CREATE TABLE smu.Carta(
    NumeroCarta VARCHAR(16),
    Nome        VARCHAR(32),
    CVV         VARCHAR(3) NOT NULL,
    Scadenza    DATE       NOT NULL,
    Saldo       FLOAT      NOT NULL,
    TipoCarta   VARCHAR(7) NOT NULL,
    Plafond     FLOAT,
    NumeroConto VARCHAR(12),

    CONSTRAINT PK_Carta PRIMARY KEY (NumeroCarta),
    CONSTRAINT FK_Conto FOREIGN KEY (NumeroConto) REFERENCES smu.ContoCorrente (NumeroConto),
    CONSTRAINT CK_NumeroCarta CHECK (NumeroCarta ~ '[0-9]{16}'),
    CONSTRAINT CK_CVV CHECK (CVV ~ '[0-9]{3}'),
    CONSTRAINT CK_TipoCarta CHECK (TipoCarta IN ('Credito', 'Debito'))
);


--Tabella Spese Programmate
CREATE TABLE smu.SpeseProgrammate(
    IdSpesa         SERIAL,
    Descrizione     VARCHAR(64),
    Periodicita     VARCHAR(9) NOT NULL,
    DataScadenza    DATE       NOT NULL,
    DataFineRinnovo DATE,
    Importo         FLOAT      NOT NULL,
    Destinatario    VARCHAR(32),
    NumeroCarta     VARCHAR(16),
    Stato           BOOLEAN,

    CONSTRAINT PK_Spesa PRIMARY KEY (IdSpesa),
    CONSTRAINT FK_Carta FOREIGN KEY (NumeroCarta) REFERENCES smu.Carta (NumeroCarta) ON DELETE CASCADE,
    CONSTRAINT CK_Periodicita CHECK (Periodicita IN
                                                      ('7 giorni', '15 giorni', '1 mese', '3 mesi', '6 mesi', '1 anno'))
);

--Tabella Categoria
CREATE TABLE smu.Categoria(
  NomeCategoria  VARCHAR(32),
  ParoleChiavi   VARCHAR(256)  NOT NULL,

  CONSTRAINT PK_Nome PRIMARY KEY (NomeCategoria)
);

--Tabella Transazione
CREATE TABLE smu.Transazione(
    IdTransazione SERIAL,
    CRO           VARCHAR(16),
    Importo       FLOAT NOT NULL,
    Data          DATE  NOT NULL,
    Ora           TIME  NOT NULL,
    Causale       VARCHAR(128),
    Tipo          VARCHAR(7),
    Mittente      VARCHAR(32),
    Destinatario  VARCHAR(32),
    NumeroCarta   VARCHAR(16),
    NomeCategoria VARCHAR(32),

    CONSTRAINT PK_Transazione PRIMARY KEY (IdTransazione),
    CONSTRAINT FK_Carta FOREIGN KEY (NumeroCarta) REFERENCES smu.Carta (NumeroCarta) ON DELETE CASCADE,
    CONSTRAINT FK_Categoria FOREIGN KEY (NomeCategoria) REFERENCES smu.Categoria (NomeCategoria) ON DELETE CASCADE,

    CONSTRAINT CK_CRO CHECK (CRO ~ '[0-9]{11,16}'),
    CONSTRAINT CK_Tipo CHECK (Tipo IN ('Entrata', 'Uscita')),
    CONSTRAINT CK_Data CHECK (data <= CURRENT_DATE)
);


--Tabella Portafoglio
CREATE TABLE smu.Portafoglio(
    IdPortafoglio   SERIAL,
    NomePortafoglio VARCHAR(32) NOT NULL,
    Saldo           FLOAT,
    IdFamiglia      INTEGER,

    CONSTRAINT PK_Portafoglio PRIMARY KEY (IdPortafoglio),
    CONSTRAINT FK_Famiglia FOREIGN KEY (IdFamiglia) REFERENCES smu.Famiglia (IdFamiglia) ON DELETE CASCADE,
    CONSTRAINT UK_Portafoglio_Nome_Famiglia UNIQUE (NomePortafoglio, IdFamiglia)
);


--tabella ponte tra Portafoglio e Carta  *a*
CREATE TABLE smu.AssociazioneCartaPortafoglio(
    IdPortafoglio INTEGER,
    NumeroCarta   VARCHAR(16),

    CONSTRAINT FK_Carta FOREIGN KEY (NumeroCarta) REFERENCES smu.Carta (NumeroCarta) ON DELETE CASCADE,
    CONSTRAINT FK_Portafoglio FOREIGN KEY (IdPortafoglio) REFERENCES smu.Portafoglio (IdPortafoglio) ON DELETE CASCADE,
    CONSTRAINT PK_Associazione PRIMARY KEY (IdPortafoglio, NumeroCarta)
);


--tabella ponte tra Portafoglio e Transazione  *a*
CREATE TABLE smu.TransazioniInPortafogli(
    IdTransazione INTEGER,
    IdPortafoglio INTEGER,

    CONSTRAINT FK_Transazione FOREIGN KEY (IdTransazione) REFERENCES smu.Transazione (IdTransazione) ON DELETE CASCADE,
    CONSTRAINT FK_Portafoglio FOREIGN KEY (IdPortafoglio) REFERENCES smu.Portafoglio (IdPortafoglio) ON DELETE CASCADE
);

CREATE TABLE smu.UtentiInFamiglie(
    NomeUtente VARCHAR(32),
    IdFamiglia INTEGER,

        CONSTRAINT FK_NomeUtenre FOREIGN KEY (NomeUtente) REFERENCES smu.Utente (Username) ON DELETE CASCADE,
        CONSTRAINT FK_IdFamiglia FOREIGN KEY (IdFamiglia) REFERENCES smu.Famiglia (IdFamiglia) ON DELETE CASCADE
);

--------------------------------------------------------------------------------------------------------------------------------------
-- TRIGGER E FUNZIONI
--------------------------------------------------------------------------------------------------------------------------------------

-- 1. Trigger che imposta a negativo il valore di una transazione in uscita ed aggiorna il valore del saldo della carta e del conto a seguito di una transazione.

CREATE OR REPLACE FUNCTION smu.triggerTransazione() RETURNS TRIGGER AS
$$
BEGIN
    -- imposta l'importo delle transazioni in uscita a negativo.
    IF NEW.Tipo = 'Uscita' THEN
        UPDATE smu.Transazione
        SET Importo = -(NEW.Importo)
        WHERE IdTransazione = NEW.IdTransazione;
    END IF;

    --aggiorna il saldo della carta.
    UPDATE smu.Carta
    SET Saldo = Saldo + (SELECT T.Importo
                         FROM smu.Transazione AS T
                         WHERE T.IdTransazione = NEW.IdTransazione)
    WHERE NumeroCarta = NEW.NumeroCarta;


    --aggiorna il saldo del conto.
    UPDATE smu.ContoCorrente
    SET Saldo = Saldo + (SELECT T.Importo
                         FROM smu.Transazione AS T
                         WHERE T.IdTransazione = NEW.IdTransazione)
    WHERE NumeroConto = (SELECT Ca.NumeroConto
                         FROM smu.Carta AS Ca
                         WHERE Ca.NumeroCarta = NEW.NumeroCarta);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER ModificaImporto
    AFTER INSERT
    ON smu.Transazione
    FOR EACH ROW
EXECUTE FUNCTION smu.triggerTransazione();


---------------------------------------------------------------------------------------------------------------
-- 2. Trigger che aggiunge una transazione ad una categoria in base alle parole chiave, dopo l'inserimento di una nuova transazione.
--Questa funzione cerca di categorizzare automaticamente la nuova transazione basandosi sulle parole chiave definite nella tabella smu.Categoria.
-- Se non viene trovata alcuna corrispondenza, la transazione viene assegnata alla categoria "Altro".

CREATE OR REPLACE FUNCTION smu.triggerCategorizzaTransazione() RETURNS TRIGGER AS
$$
DECLARE
    nome_categoria smu.Categoria.NomeCategoria%TYPE;    --variabile per iterare sul nome categoria
    ArrayParoleChiavi TEXT[];            -- Array di testo per memorizzare le parole chiave di una categoria
    parola TEXT;                    -- Variabile per memorizzare temporaneamente ogni parola chiave
    matched BOOLEAN := FALSE;       -- Variabile booleana per indicare se è stata trovata una corrispondenza, inizializzata a false
BEGIN

     -- Se NomeCategoria è già impostato, non fare nulla
    IF NEW.NomeCategoria IS NOT NULL AND NEW.NomeCategoria <> '' THEN
        RETURN NEW;
    END IF;

    FOR nome_categoria IN
        SELECT NomeCategoria
        FROM smu.Categoria
    LOOP
        --la funzione string_to_array mi crea una stringa di valori a partire da una stringa di parole separate da virgole
        --successivamente memorizzo tale array in ArrayParoleChiavi
        SELECT string_to_array(ParoleChiavi, ',') INTO ArrayParoleChiavi
            FROM smu.Categoria
            WHERE NomeCategoria = nome_categoria;

        --memorizzo nella variabile parola l'elemento dell'array ParoleChiave
        FOREACH parola IN ARRAY ArrayParoleChiavi
        LOOP
            -- Verifica se la causale contiene la parola chiave
            IF NEW.Causale ILIKE '%' || parola || '%' THEN
                NEW.NomeCategoria := nome_categoria;
                matched := TRUE;
                EXIT;  -- Esce dal loop delle parole chiave una volta trovata una corrispondenza
            END IF;
        END LOOP;

        -- Se è stata trovata una corrispondenza interrompe il ciclo delle categorie
        IF matched THEN
            EXIT;
        END IF;
    END LOOP;

    -- Se nessuna categoria è stata trovata, assegno la transazione alla categoria "Altro"
    IF NOT matched THEN
       NEW.NomeCategoria := 'Altro';
    END IF;

    RETURN NEW;  -- Restituisce la nuova riga con il campo NomeCategoria aggiornato
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER InserisciCategoriaInTransazione
    BEFORE INSERT ON smu.Transazione
    FOR EACH ROW EXECUTE FUNCTION smu.triggerCategorizzaTransazione();


--------------------------------------------------------------------------------------------------------------
-- 3. Trigger che prima dell'inserimento di una transazione controlli che:
-- se è di tipo 'Uscita' allora Mittente è NULL, se è di tipo 'Entrata' allora Destinatario è NULL.

CREATE OR REPLACE FUNCTION smu.triggerTipoTransazione() RETURNS TRIGGER AS
    $$
    BEGIN
        IF NEW.Tipo = 'Uscita' AND NEW.Mittente IS NOT NULL THEN
            RAISE EXCEPTION 'ERRORE: Mittente non nullo in una transazione in uscita';
        END IF;

        IF NEW.Tipo = 'Entrata' AND NEW.Destinatario IS NOT NULL THEN
            RAISE EXCEPTION 'ERRORE: Destinatario non nullo in una transazione in entrata';
        END IF;

        RETURN NEW;

    END;
    $$LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER ControlloTipoTransazione
    BEFORE INSERT ON smu.Transazione
    FOR EACH ROW EXECUTE FUNCTION smu.triggerTipoTransazione();

-- INSERT PER TESTARE IL TRIGGER 3
--INSERT INTO smu.Transazione(CRO, Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta, NomeCategoria)VALUES( 12345678910, 20.00, '2024-03-19', '09:45:00', 'Acquisto online', 'Entrata', NULL, 'E-commerce', '1234567890123456', NULL);
--INSERT INTO smu.Transazione(CRO, Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta, NomeCategoria)VALUES( 46173636910, 50.00, '2024-01-29', '13:35:00', 'Acquisto online', 'Uscita', 'Amazon', NULL, '1234567890123456', NULL);



------------------------------------------------------------------------------------------------------------------------
-- 4. Trigger che prima dell'inserimento di una carta controlli che:
-- se il tipo è 'Debito' allora plafond è NULL, se il tipo è 'Credito' allora Plafond è NOT NULL.

CREATE OR REPLACE FUNCTION smu.triggerTipoCarta() RETURNS TRIGGER AS
$$
    BEGIN
        IF NEW.TipoCarta = 'Debito' AND NEW.Plafond IS NOT NULL THEN
            RAISE EXCEPTION 'ERRORE: Plafond non nullo in una carta di debito';
        END IF;

        IF NEW.TipoCarta = 'Credito' AND NEW.Plafond IS NULL THEN
            RAISE EXCEPTION 'ERRORE: Plafond nullo in una carta di credito';
        END IF;

        RETURN NEW;

    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER ControlloTipoCarta
    BEFORE INSERT ON smu.Carta
    FOR EACH ROW EXECUTE FUNCTION smu.triggerTipoCarta();

-- INSERT PER TESTARE IL TRIGGER 4

--INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5355284922617884', 'Poste Pay Evolution', 100, '2025-12-31', 13.00, 'Credito', NULL, 1);
--INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5334628274884783', 'Carta prepagata', 345, '2024-08-31', 500.00, 'Debito', 1000.00, 1);


----------------------------------------------------------------------------------------------------------------------
--5. Trigger che mi genera automaticamente il valore del CRO

CREATE OR REPLACE FUNCTION smu.triggerGeneraCro() RETURNS TRIGGER AS $$
DECLARE
    randomCro TEXT;
BEGIN
    -- Genera una stringa di 11 cifre casuali
    randomCro := LPAD((random() * 99999999999)::BIGINT::TEXT, 11, '0');
    -- Assegna la stringa di cifre casuali all'attributo CRO della nuova riga
    NEW.CRO := randomCro;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER GeneraCro
    BEFORE INSERT
    ON smu.Transazione
    FOR EACH ROW EXECUTE FUNCTION smu.triggerGeneraCro();


----------------------------------------------------------------------------------------------------------------------

--6. Trigger che inizializza il saldo di un portafoglio a 0 dopo l'inserimento di un nuovo portafoglio
CREATE OR REPLACE FUNCTION smu.triggerInizializzaSaldoPortafoglio() RETURNS TRIGGER AS
$$
    BEGIN
        UPDATE smu.Portafoglio
        SET Saldo = 0
        WHERE IdPortafoglio = NEW.IdPortafoglio;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER InizializzaSaldoPortafoglio
    AFTER INSERT
    ON smu.Portafoglio
    FOR EACH ROW EXECUTE FUNCTION smu.triggerInizializzaSaldoPortafoglio();

----------------------------------------------------------------------------------------------------------------------

--7. Trigger che aggiorna il saldo del portafoglio a seguito dell'inserimento di una transazione

CREATE OR REPLACE FUNCTION smu.triggerAggiornaSaldoPortafoglio() RETURNS TRIGGER AS
$$
    BEGIN
    UPDATE smu.Portafoglio
        SET Saldo = Saldo + (SELECT T.Importo
                             FROM (smu.Transazione AS T JOIN smu.TransazioniInPortafogli AS TP on T.IdTransazione = TP.IdTransazione)
                                 JOIN smu.Portafoglio AS P on TP.IdPortafoglio = P.IdPortafoglio
                             WHERE T.IdTransazione = NEW.IdTransazione)
        WHERE IdPortafoglio = NEW.IdPortafoglio;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER ModificaSaldoPortafoglio
    AFTER INSERT
    ON smu.TransazioniInPortafogli
    FOR EACH ROW EXECUTE FUNCTION smu.triggerAggiornaSaldoPortafoglio();


----------------------------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION smu.trigger_spesa_programmata() RETURNS TRIGGER AS
$$

    BEGIN
        -- Assicurati che l'aggiornamento non attivi il trigger ricorsivo
    IF NEW.Stato = TRUE THEN
        -- Aggiungi la logica per aggiornare la tabella Transazione
        INSERT INTO smu.Transazione(importo, data, ora, causale, tipo, mittente, destinatario, numerocarta)
        VALUES (NEW.Importo, CURRENT_DATE, CURRENT_TIME, NEW.Descrizione, 'Uscita', NULL, NEW.Destinatario, NEW.NumeroCarta);

        -- Modifica la data di scadenza o elimina la spesa programmata
        IF NEW.DataFineRinnovo <= CURRENT_DATE THEN
            DELETE FROM smu.SpeseProgrammate WHERE IdSpesa = NEW.IdSpesa;
        END IF;

        IF (CURRENT_DATE >= NEW.Datascadenza AND NEW.Stato = TRUE)  THEN
            UPDATE smu.SpeseProgrammate
            SET DataScadenza =
                CASE
                    WHEN NEW.Periodicita = '7 giorni' THEN NEW.Datascadenza + INTERVAL '7 days'
                    WHEN NEW.Periodicita = '15 giorni' THEN NEW.Datascadenza + INTERVAL '15 days'
                    WHEN NEW.Periodicita = '1 mese' THEN NEW.Datascadenza + INTERVAL '1 month'
                    WHEN NEW.Periodicita = '3 mesi' THEN NEW.Datascadenza + INTERVAL '3 months'
                    WHEN NEW.Periodicita = '6 mesi' THEN NEW.Datascadenza + INTERVAL '6 months'
                    WHEN NEW.Periodicita = '1 anno' THEN NEW.Datascadenza + INTERVAL '1 year'
                END,
                Stato = FALSE
            WHERE IdSpesa = NEW.IdSpesa;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_spesa_programmata
AFTER UPDATE ON smu.SpeseProgrammate
FOR EACH ROW
EXECUTE FUNCTION smu.trigger_spesa_programmata();


--------------------------------------------------------------------------------------------------------------------------------------
-- INSERT
--------------------------------------------------------------------------------------------------------------------------------------

-- Famiglie
INSERT INTO smu.Famiglia(NomeFamiglia) VALUES('Famiglia Durso');
INSERT INTO smu.Famiglia(NomeFamiglia) VALUES('Famiglia Gargiulo');
INSERT INTO smu.Famiglia(NomeFamiglia) VALUES('Famiglia Progetto');

-- Utenti
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password) VALUES('Giulia28', 'Giulia', 'Gargiulo', '+393662648291', 'giulia.gargiulo3@studenti.unina.it', 'Password1');
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password) VALUES('MirGae', 'Miriam', 'Gaetano', '+393316581941', 'miriam.gaetano@studenti.unina.it', 'Password2');
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password ) VALUES('TataDur', 'Fortunata', 'DUrso', '+398765481948', 'fotunata.duso@studenti.unina.it', 'Password3');
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password) VALUES('Lrgarg', 'Lorenzo', 'Gargiulo', '+393662048291', 'loreg.y@gmail.com', 'Password4');


INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('MirGae', 1);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('TataDur', 1);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('Giulia28', 2);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('Lrgarg', 2);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('MirGae', 3);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('Giulia28', 3);
INSERT INTO smu.UtentiInFamiglie(NomeUtente, IdFamiglia) VALUES('TataDur', 3);


-- Conti Correnti da fare
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000123456', 'IT60X0542811101000000123456', 3104.16, 'UniCredit', 'BICABCD1234', 'Giulia28');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username) VALUES ('000000666666', 'IT60X0542811101000000666666', 19.50, 'Banca Monte dei Paschi di Siena (MPS)', 'BICMNOPQR78', 'Giulia28');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000987654', 'IT60X0542811101000000987654', 2460.51, 'Intesa Sanpaolo', 'BICWXYZ5678', 'MirGae');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username) VALUES ('000000555555', 'IT60X0542811101000000555555', 990.50, 'Banco BPM', 'BICDEFG9012', 'MirGae');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000888888', 'IT60X0542811101000000888888', 789.31, 'Poste Italiane', 'BICSTUV7890','MirGae');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000777777', 'IT60X0542811101000000777777', 186.50, 'Banca Mediolanum', 'BICUVWX3456', 'TataDur');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000026354100', 'IT60X0542811101000026354100', 2000.50, 'Revolut', 'BICDEFP5012', 'TataDur');


--Carte
---carte MirGae
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('9999000011112222', 'Mastercard', 222, '2026-11-30', 2460.51, 'Credito', 2000.00, '000000987654');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1234567890443456', 'Carta Fedelta', 345, '2024-08-31', 00.00, 'Credito', 1000.00, '000000987654');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5555666677778888', 'Visa', 333, '2025-07-31', 990.50, 'Debito', NULL, '000000555555');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1212121212121212', 'Carta di Debito', 555, '2023-09-30', 19.40, 'Debito', NULL, '000000888888');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5355284927482884', 'Poste Pay Evolution', 100, '2025-12-31', 769.91, 'Credito', 14000.00, '000000888888');
---carte Giulia28
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5337589274884783', 'Carta di Giulia', 345, '2024-08-31', 1456.76, 'Credito', 1000.00, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1234567890123456', 'Visa', 789, '2023-05-31', 806.20, 'Credito', 500.00, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('6666999988887777', 'Carta Oro', 444, '2024-04-30', 60.20, 'Debito', NULL, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1515151515151515', 'Carta Viaggio', 888, '2027-06-30', 19.50, 'Debito', NULL, '000000666666');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('9876543210987654', 'Carta Lorenzo', 789, '2023-05-31', 781.00, 'Credito', 500.00, '000000123456');
---carte TataDur
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES ('5638291746532891', 'Mastercard Platinum', 123, '2028-05-31', 320.00, 'Credito', 1500.00, '000026354100');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1414141414141414', 'Carta Studenti', 777, '2023-12-31', 191.25, 'Credito', 200.00, '000026354100');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1665151666151515', 'Carta Conad', 808, '2027-06-30', 4.75, 'Debito', NULL, '000000777777');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES ('4728194738192746', 'PostePay Evolution', 678, '2026-10-31', 120.75, 'Credito', 1000.00, '000026354100');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES ('1928374650918273', 'American Express', 456, '2025-09-30', 780.50, 'Debito', NULL, '000000777777');

-- Categorie
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Cibo e spesa', 'spesa,supermercato,supermarket,alimentari,discount,frutta,verdura,salumeria');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Bar e ristoranti', 'ristorante,pizzeria,fastfood,bar,caffe,cena');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Motori e Trasporti', 'assicurazione,trasporti,carburante,auto,treno,viaggi,rifornimento,gasolio');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Shopping', 'abbigliamento,calzature,vestiti,zara,acquisto');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Salute', 'visita,farmacia,sanitaria,benessere,cosmesi,medicine');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Bollette e Tasse', 'bollette,imposta,mutuo,tasse');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Altro', '');

-- Transazioni
----transazioni conti MirGae
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(600.00, '2024-01-30', '09:00:00', 'Rimborso mutuo', 'Entrata', 'Banca Intesa', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(600.00, '2024-03-20', '10:15:30', 'Pensione', 'Entrata', 'INPS', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-03-14', '12:15:30', 'Rimborso assicurazione', 'Entrata', 'Assicurazione Generali', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.00, '2023-09-05', '16:30:50', 'Cena ristorante', 'Uscita', NULL, 'Ristorante Bella Napoli', '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.50, '2021-02-25', '16:20:10', 'Rimborso spese mediche', 'Entrata', 'Mutua Salute', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2021-07-01', '13:30:30', 'Pagamento tasse', 'Uscita', NULL, 'Agenzia delle Entrate', '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2022-03-14', '12:15:30', 'Rimborso assicurazione', 'Entrata', 'Assicurazione Generali', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2020-06-11', '17:55:55', 'Viaggio in treno', 'Uscita', NULL, 'Trenitalia', '5555666677778888');
--8
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(2500.00, '2024-03-29', '12:20:45', 'Rimborso pensione integrativa', 'Entrata', 'Assicurazione Vita S.p.A.', NULL, '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1200.00, '2021-04-05', '10:30:45', 'Bonus annuale', 'Entrata', 'Azienda Innovazioni', NULL, '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(89.99, '2023-11-22', '09:55:12', 'Acquisto online Amazon', 'Uscita', NULL, 'Amazon', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(30.50, '2020-03-15', '18:35:45', 'Ristorante', 'Uscita', NULL, 'Ristorante Buon Gusto', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.00, '2020-05-15', '09:09:09', 'Servizio fotografico', 'Uscita', NULL, 'Fotografo Rossi', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(999.00, '2022-09-18', '15:15:15', 'Acquisto smartphone', 'Uscita', NULL, 'Apple Store', '9999000011112222');
--14
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(800.00, '2024-04-07', '18:30:30', 'Bonus produzione', 'Entrata', 'Azienda Automobili', NULL, '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(60.99, '2023-08-08', '15:50:50', 'Acquisto biglietti cinema', 'Uscita', NULL, 'UCI Cinema', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(302.40, '2023-06-20', '09:30:11', 'Carburante auto', 'Uscita', NULL, 'Esso', '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2024-03-10', '11:12:13', 'Pagamento bonus', 'Entrata', 'Company XYZ', NULL, '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(48.30, '2024-02-15', '14:10:45', 'Ricarica telefonica', 'Uscita', NULL, 'Tim Mobile', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.80, '2024-03-18', '17:30:45', 'Pagamento bollette', 'Uscita', NULL, 'Fornitore energia', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(55.20, '2024-03-12', '16:40:15', 'Pagamento bollo auto', 'Uscita', NULL, 'Ufficio Motorizzazione', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(32.40, '2023-06-20', '09:30:11', 'Carburante auto', 'Uscita', NULL, 'Esso', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.00, '2024-01-15', '13:20:30', 'Stipendio', 'Entrata', 'Agenzia', NULL, '5355284927482884');
--23
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2023-10-10', '13:45:32', 'Visita medica', 'Uscita', NULL, 'Centro Medico Salus', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.00, '2023-09-30', '14:40:40', 'Acquisto giardino', 'Uscita', NULL, 'Brico', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(70.60, '2024-03-10', '14:00:45', 'Acquisto vestiti', 'Uscita', NULL, 'Negozio di abbigliamento online', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(300.00, '2024-02-23', '13:45:40', 'Rimborso danni assicurativi', 'Entrata', 'Assicurazione Vittoria', NULL, '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.00, '2024-02-15', '10:00:45', 'Rimborso biglietti concerto', 'Entrata', 'TicketOne', NULL, '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-11-17', '20:20:20', 'Cena ristorante', 'Uscita', NULL, 'Ristorante Milano', '1212121212121212');
--29
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.00, '2024-01-10', '11:30:30', 'Rimborso tasse comunali', 'Entrata', 'Comune di Milano', NULL, '1234567890443456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2024-01-26', '14:30:10', 'Rimborso elettrodomestici', 'Entrata', 'Mediaworld', NULL, '1234567890443456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.00, '2023-09-09', '11:11:11', 'Regalo di compleanno', 'Entrata', 'Patty e Marc', NULL, '1234567890443456');
--32
----transazioni conti Giulia28
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(125.00, '2024-02-17', '14:25:55', 'Rimborso viaggio treno', 'Entrata', 'Trenitalia', NULL, '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.00, '2023-02-05', '14:40:33', 'Cena di lavoro', 'Uscita', NULL, 'Ristorante Gourmet', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(205.55, '2024-03-02', '18:25:30', 'Acquisto elettrodomestici', 'Uscita', NULL, 'Mediaworld', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(100.00, '2024-01-12', '12:15:30', 'Rimborso tasse universitarie', 'Entrata', 'Università degli Studi', NULL, '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(135.75, '2021-03-13', '09:55:20', 'Vendita libri', 'Uscita', NULL, 'Libreria XYZ', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(15.00, '2020-12-03', '17:10:10', 'Acquisto rivista', 'Uscita', NULL, 'Mondadori', '6666999988887777');
--38
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(900.00, '2020-03-18', '12:00:00', 'Rimborso ferie', 'Entrata', 'Agenzia Viaggi Expedia', NULL, '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2020-03-12', '09:30:22', 'Rimborso vacanze', 'Entrata', 'Agenzia Viaggi BestTrips', NULL, '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(20.00, '2020-01-12', '18:12:12', 'Acquisto libro', 'Uscita', NULL, 'Feltrinelli', '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(99.00, '2023-06-06', '12:12:12', 'Abbigliamento online', 'Uscita', NULL, 'Zalando', '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2023-12-31', '23:59:59', 'Donazione', 'Uscita', NULL, 'Croce Rossa Italiana', '9876543210987654');
--43
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(40.30, '2024-03-09', '17:45:30', 'Ricarica wiifii', 'Uscita', NULL, 'Operatore Telefonico ', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1500.00, '2023-02-01', '09:00:00', 'Stipendio mensile', 'Entrata', 'Azienda S.p.A.', NULL, '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(85.75, '2021-03-08', '11:20:10', 'Pagamento tasse', 'Uscita', NULL, 'Agenzia delle Entrate', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(35.40, '2020-01-01', '15:25:36', 'Pagamento supermercato', 'Uscita', NULL,'Supermercato Deco', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.75, '2020-05-01', '11:45:10', 'Spesa settimanale', 'Uscita', NULL, 'Supermercato Coop', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(15.99, '2021-04-10', '19:00:00', 'Abbonamento Spotify', 'Uscita', NULL, 'Spotify', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.25, '2023-03-16', '14:20:10', 'Carburante', 'Uscita', NULL, 'Stazione di servizio ESSO', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.00, '2022-02-11', '10:50:45', 'Bonus progetto', 'Entrata', 'Tech Innovators', NULL, '5337589274884783');
--51
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2024-01-15', '14:45:22', 'Rimborso viaggio', 'Entrata', 'Agenzia Viaggi Global', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(450.00, '2024-03-14', '12:10:25', 'Rimborso acquisto auto', 'Entrata', 'Concessionaria Auto', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2023-12-15', '14:50:50', 'Bonifico stipendio', 'Entrata', 'Azienda S.p.A.', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(42.30, '2024-03-17', '11:10:55', 'Pagamento affitto', 'Uscita', NULL, 'Proprietario immobile', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(20.00, '2024-03-19', '09:45:00', 'Acquisto online', 'Uscita', NULL, 'E-commerce', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(210.75, '2024-07-12', '10:10:10', 'Acquisto elettronica', 'Uscita', NULL, 'Euronics', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.75, '2023-12-10', '11:05:22', 'Pagamento bollette', 'Uscita', NULL, 'Enel Energia', '1234567890123456');
--58
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2024-03-06', '11:15:30', 'Rimborso biglietti teatro', 'Entrata', 'Teatro Nazionale', NULL, '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(300.00, '2024-03-03', '08:45:55', 'Rimborso polizza auto', 'Entrata', 'Assicurazione Allianz', NULL, '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(70.50, '2024-08-14', '16:15:15', 'Spesa mensile', 'Uscita', NULL, 'Esselunga', '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2023-07-07', '12:30:22', 'Abbigliamento', 'Uscita', NULL, 'Zara', '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(210.00, '2024-04-12', '12:12:12', 'Affitto mensile', 'Uscita', NULL, 'Agenzia Immobiliare Roma', '1515151515151515');
--63
----transazioni carte TataDur
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.50, '2023-11-07', '10:00:15', 'Pagamento assicurazione', 'Uscita', NULL, 'Generali Assicurazioni', '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-11-11', '20:30:00', 'Acquisto scarpe', 'Uscita', NULL, 'Foot Locker', '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(140.50, '2023-05-20', '13:00:00', 'Riparazione auto', 'Uscita', NULL, 'Officina Auto', '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(275.75, '2024-03-14', '15:20:30', 'Rimborso spese aziendali', 'Entrata', 'Company Italia', NULL, '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.50, '2024-04-01', '13:15:15', 'Rimborso spese di viaggio', 'Entrata', 'Compagnia Viaggi Global', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.50, '2023-11-07', '10:00:15', 'Pagamento assicurazione', 'Uscita', NULL, 'Generali Assicurazioni', '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.30, '2024-02-14', '08:45:10', 'Pagamento bolletta luce', 'Uscita', NULL, 'Enel Energia', '1665151666151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.50, '2023-11-30', '11:10:30', 'Rimborso spese universitarie', 'Entrata', 'Università Statale', NULL, '1665151666151515');
--71
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.20, '2024-01-12', '16:40:20', 'Rimborso visite mediche', 'Entrata', 'ASL Regionale', NULL, '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(95.20, '2024-01-20', '15:00:10', 'Rimborso viaggio aereo', 'Entrata', 'Compagnia Aerea FlyEasy', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(60.00, '2024-03-03', '16:35:55', 'Parrucchiere', 'Uscita', NULL, 'Salone Chic', '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(99.90, '2023-08-22', '17:22:11', 'Biglietti concerto', 'Uscita', NULL, 'TicketOne', '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(180.75, '2023-10-25', '11:45:22', 'Acquisto mobili', 'Uscita', NULL, 'IKEA', '1414141414141414');
--76
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.00, '2023-12-19', '19:15:30', 'Cena ristorante', 'Uscita', NULL, 'Ristorante La Pergola', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.50, '2024-02-22', '09:30:20', 'Acquisto libri', 'Uscita', NULL, 'Libreria Feltrinelli', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(230.00, '2024-01-10', '18:00:50', 'Pagamento palestra', 'Uscita', NULL, 'Gym Club', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(40.20, '2024-03-01', '15:45:15', 'Ricarica internet', 'Uscita', NULL, 'Vodafone', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.75, '2024-01-15', '07:20:33', 'Noleggio auto', 'Uscita', NULL, 'Hertz', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(110.00, '2024-03-05', '10:25:55', 'Visita medica', 'Uscita', NULL, 'Poliambulatorio Salute', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(15.40, '2022-01-15', '09:30:20', 'Colazione al bar', 'Uscita', NULL, 'Bar Centrale', '1928374650918273');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(220.30, '2022-02-10', '14:20:50', 'Spesa supermercato', 'Uscita', NULL, 'Carrefour', '1928374650918273');
--84
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(310.00, '2024-01-10', '09:30:45', 'Bonus aziendale', 'Entrata', 'Tech Solutions', NULL, '4728194738192746');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(180.50, '2024-02-08', '12:45:00', 'Pagamento abbonamento TV', 'Uscita', NULL, 'Netflix', '4728194738192746');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.90, '2024-01-25', '10:15:30', 'Acquisto fiori', 'Uscita', NULL, 'Fioraio Milano', '4728194738192746');
--87
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.00, '2025-02-01', '14:20:00', 'Rifornimento carburante', 'Uscita', NULL, 'Esso Stazione', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(130.00, '2024-01-10', '10:35:40', 'Spesa supermercato', 'Uscita', NULL, 'Esselunga', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(40.50, '2024-12-05', '16:10:25', 'Taxi', 'Uscita', NULL, 'Uber', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2024-11-30', '20:15:50', 'Pagamento bollo auto', 'Uscita', NULL, 'ACI', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(95.00, '2024-01-20', '08:30:00', 'Visita medica', 'Uscita', NULL, 'Ospedale San Raffaele', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(40.25, '2024-03-20', '16:15:33', 'Cinema', 'Uscita', NULL, 'UCI Cinemas', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(95.60, '2024-01-22', '10:45:10', 'Abbonamento palestra', 'Uscita', NULL, 'Gym Wellness', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.80, '2024-02-28', '16:30:40', 'Acquisto vestiti', 'Uscita', NULL, 'Zara', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.00, '2024-02-05', '10:30:15', 'Rimborso spese mediche', 'Entrata', 'Ospedale San Raffaele', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.00, '2024-03-01', '14:50:25', 'Rimborso viaggio d’affari', 'Entrata', 'Azienda XYZ', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.00, '2024-04-10', '12:05:30', 'Pagamento di stipendio', 'Entrata', 'Employer ABC', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.50, '2024-05-18', '17:25:40', 'Rimborso spese professionali', 'Entrata', 'Studio Legale Rossi', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2024-06-05', '09:10:20', 'Bonus annuo', 'Entrata', 'Azienda ABC', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(220.00, '2024-01-07', '19:40:35', 'Rimborso tasse universitarie', 'Entrata', 'Università Milano', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(300.00, '2024-08-15', '13:00:00', 'Pagamento assicurazione', 'Entrata', 'Compagnia Assicurativa Alfa', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(180.50, '2024-09-22', '11:45:30', 'Indennità di disoccupazione', 'Entrata', 'INPS', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(95.75, '2024-10-10', '20:00:10', 'Rimborso spese viaggio', 'Entrata', 'Compagnia Aerea EasyJet', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(140.00, '2024-11-02', '16:30:50', 'Rimborso pensione', 'Entrata', 'INPS', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.00, '2024-12-15', '14:10:20', 'Pagamento bonus natalizio', 'Entrata', 'Azienda XYZ', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.00, '2025-01-10', '11:55:00', 'Rimborso acquisto materiale', 'Entrata', 'Fornitore ABC', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2025-02-01', '08:00:00', 'Rimborso per danno auto', 'Entrata', 'Compagnia Assicurativa Beta', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(110.00, '2025-02-01', '12:40:15', 'Pagamento per collaborazione', 'Entrata', 'Azienda Creativa', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.00, '2023-01-12', '10:30:00', 'Acquisto elettrodomestici', 'Uscita', NULL, 'MediaWorld', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.50, '2023-02-20', '16:45:20', 'Pagamento bollette', 'Uscita', NULL, 'Enel Energia', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.25, '2023-03-15', '08:15:10', 'Spesa supermercato', 'Uscita', NULL, 'Esselunga', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(300.00, '2023-04-08', '14:05:35', 'Prenotazione hotel', 'Uscita', NULL, 'Booking.com', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.00, '2023-05-21', '19:30:50', 'Cena al ristorante', 'Uscita', NULL, 'Ristorante Bella Italia', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.75, '2023-12-10', '12:00:15', 'Abbonamento palestra', 'Uscita', NULL, 'GymFit Club', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.30, '2023-07-14', '18:45:30', 'Biglietto treno', 'Uscita', NULL, 'Trenitalia', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(220.00, '2023-08-19', '09:20:40', 'Pagamento tasse universitarie', 'Uscita', NULL, 'Università Milano', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(130.50, '2023-09-05', '13:10:55', 'Riparazione smartphone', 'Uscita', NULL, 'Apple Store', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.75, '2023-10-27', '17:50:25', 'Spesa mercato', 'Uscita', NULL, 'Mercato Centrale', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.00, '2023-11-11', '20:40:30', 'Acquisto libri', 'Uscita', NULL, 'Feltrinelli', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(180.90, '2023-12-02', '22:10:10', 'Regali di Natale', 'Entrata', 'Nonna', NULL, '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.40, '2022-01-15', '09:10:00', 'Acquisto benzina', 'Uscita', NULL, 'IP', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.90, '2022-02-20', '18:35:25', 'Rifornimento gasolio', 'Uscita', NULL, 'Q8', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(60.00, '2022-03-10', '07:50:40', 'Carburante', 'Uscita', NULL, 'Total Erg', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(52.75, '2022-04-22', '11:15:10', 'Diesel', 'Uscita', NULL, 'Esso', '5638291746532891');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.20, '2022-05-18', '16:00:55', 'Carburante', 'Uscita', NULL, 'Tamoil', '5638291746532891');
--127

-- Portafogli
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Viaggi', 1);
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Casa', 1);
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Cura e salute', 1);
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Viaggio Laurea', 3);
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Casa coinquiline', 3);
INSERT INTO smu.Portafoglio(NomePortafoglio) VALUES('Spese Mensili');
INSERT INTO smu.Portafoglio(NomePortafoglio) VALUES('Vacanze');


-- Associazione portafoglio e carta
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (1, '5555666677778888');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (1, '5638291746532891');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '9999000011112222');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '5355284927482884');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '1212121212121212');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '1234567890443456');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '1665151666151515');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '1414141414141414');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '5638291746532891');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '5555666677778888');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '1212121212121212');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '1234567890443456');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '1414141414141414');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '1928374650918273');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (3, '5638291746532891');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '9999000011112222');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '5355284927482884');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '1212121212121212');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '6666999988887777');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '5337589274884783');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '9876543210987654');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '1234567890123456');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '1515151515151515');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '1665151666151515');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '1928374650918273');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (4, '4728194738192746');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (5, '1928374650918273');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (5, '5638291746532891');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (6, '9999000011112222');

INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (7, '1212121212121212');


-- Transazioni in portafogli
--id 1 famiglia mia e tata per viaggi
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(4,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(8,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(17,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(15,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(68,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(73,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(81,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(83,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(100,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(113,1);
--id 2 famiglia mia e tata casa
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(11,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(20,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(23,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(25,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(30,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(70,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(76,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(98,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(90,2);
--id 3 famiglia mia e tata cura e salute
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(5,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(24,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(32,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(74,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(75,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(79,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(92,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(94,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(116,3);
--id 4 famiglia progetto viaggio
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(13,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(22,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(27,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(28,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(36,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(37,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(41,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(44,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(51,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(54,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(57,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(63,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(67,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(80,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(86,4);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(97,4);
--id 5 famiglia progetto casa coinqui
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(84,5);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(89,5);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(91,5);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(93,5);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(118,5);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(110,5);
--id 6 7 spese mensili utente singolo
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(46,6);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(47,7);

-- Spese programmate
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Paghetta Armando', '15 giorni', '2024-03-27', '2024-03-27', 20.00, 'Armando figlio', '5355284927482884', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Signora delle pulizie', '7 giorni', '2025-03-27', '2027-03-27', 24.00, 'Antonietta', '5355284927482884', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Netflix', '1 mese', '2025-01-25', '2027-03-27', 17.00, 'Netflix', '1212121212121212', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Affitto Mensile', '1 mese', '2024-04-05', '2025-04-05', 800.00, 'Proprietario', '5555666677778888', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Abbonamento Palestra', '1 anno', '2024-03-27', '2025-03-28', 40.00, 'Palestra XYZ', '9876543210987654', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Pagamento Bolletta Elettrica', '6 mesi', '2024-04-02', '2024-12-02', 70.00, 'Fornitore Elettrico', '6666999988887777', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Rata Mutuo', '1 mese', '2024-04-10', '2024-12-10', 1000.00, 'Banca XYZ', '1234567890123456', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Assicurazione Auto Annuale', '1 anno', '2024-03-31', '2025-03-31', 600.00, 'Assicurazioni S.p.A.', '1234567890123456', 'false');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato) VALUES('Pagamento Affitto Garage', '6 mesi', '2024-04-20', '2024-10-20', 150.00, 'Proprietario Garage', '1414141414141414', 'false');

