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

    --aggiorno il nomecategoria della transazione
    UPDATE smu.Transazione
    SET NomeCategoria = NEW.NomeCategoria
    WHERE IdTransazione = NEW.IdTransazione;

    RETURN NEW;  -- Restituisce la nuova riga con il campo NomeCategoria aggiornato
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER InserisciCategoriaInTransazione
    AFTER INSERT ON smu.Transazione
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
----------------------------------------------------------------------------------------------------------------------
    --FUNZIONI--

-- Procedura che gestisce le spese programmate: trasforma le spese programmate in transazioni in uscita quando la data di scadenza è uguale alla data attuale,
-- ed aggiorna la data di scadenza in base alla periodicità per il prossimo rinnovo; se la data di fine rinnovo è uguale alla data attuale, allora elimina la spesa programmata.

CREATE OR REPLACE PROCEDURE smu.ProceduraSpesaProgrammata() AS
$$
    DECLARE
    destinatarioS smu.speseprogrammate.destinatario%TYPE;
    descrizioneS smu.speseprogrammate.descrizione%TYPE;
    numerocartaS smu.speseprogrammate.numerocarta%TYPE;
    intervalloS smu.speseprogrammate.periodicita%TYPE;
    IdSpesaS smu.speseprogrammate.idspesa%TYPE;
    FineRinnovo smu.SpeseProgrammate.dataFineRinnovo%TYPE;
    importoS FLOAT := 0;
    cursore REFCURSOR;

    BEGIN

        OPEN cursore FOR (SELECT S.Importo, S.Descrizione, s.Destinatario, S.NumeroCarta, S.Periodicita, S.IdSpesa, S.DataFineRinnovo
                                  FROM smu.SpeseProgrammate AS S
                                  WHERE S.DataScadenza = CURRENT_DATE);
        LOOP
            FETCH cursore INTO importoS, descrizioneS, destinatarioS, numerocartaS, intervalloS, IdSpesaS, FineRinnovo;
            EXIT WHEN NOT FOUND;

            -- Inserimento della transazione
            INSERT INTO smu.Transazione(importo, data, ora, causale, tipo, mittente, destinatario, numerocarta)
            VALUES (importoS, CURRENT_DATE, CURRENT_TIME, descrizioneS, 'Uscita', NULL, destinatarioS, numerocartaS);

            -- Se la data di fine rinnovo è uguale alla CURRENT_DATE, allora elimino la spesa programmata.
            IF FineRinnovo = CURRENT_DATE THEN
                DELETE FROM smu.SpeseProgrammate
                WHERE IdSpesa = IdSpesaS;
            END IF;

            --Aggiornamento della data di scadenza del prossimo pagamento programmato
            UPDATE smu.SpeseProgrammate
            SET DataScadenza = DataScadenza + (CASE
                    WHEN intervalloS = '7 giorni' THEN INTERVAL '7 days'
                    WHEN intervalloS = '15 giorni' THEN INTERVAL '15 days'
                    WHEN intervalloS = '1 mese' THEN INTERVAL '1 month'
                    WHEN intervalloS = '3 mesi' THEN INTERVAL '3 months'
                    WHEN intervalloS = '6 mesi' THEN INTERVAL '6 months'
                    WHEN intervalloS = '1 anno' THEN INTERVAL '1 year'
                END)
            WHERE IdSpesa = IdSpesaS;

        END LOOP;
        CLOSE cursore;
    END;
$$ LANGUAGE plpgsql;

CALL smu.ProceduraSpesaProgrammata();



