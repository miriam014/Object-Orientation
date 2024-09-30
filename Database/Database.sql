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
    IdFamiglia INTEGER,

    CONSTRAINT PK_Utente PRIMARY KEY (Username),
    CONSTRAINT FK_Famiglia FOREIGN KEY (IdFamiglia) REFERENCES smu.Famiglia (IdFamiglia) ON DELETE CASCADE,
    CONSTRAINT UK_Utente UNIQUE (Email, Password),
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
    CONSTRAINT FK_Famiglia FOREIGN KEY (IdFamiglia) REFERENCES smu.Famiglia (IdFamiglia) ON DELETE CASCADE

);


--tabella ponte tra Portafoglio e Carta  *a*
CREATE TABLE smu.AssociazioneCartaPortafoglio(
    IdPortafoglio INTEGER,
    NumeroCarta   VARCHAR(16),

    CONSTRAINT FK_Carta FOREIGN KEY (NumeroCarta) REFERENCES smu.Carta (NumeroCarta) ON DELETE CASCADE,
    CONSTRAINT FK_Portafoglio FOREIGN KEY (IdPortafoglio) REFERENCES smu.Portafoglio (IdPortafoglio) ON DELETE CASCADE
);


--tabella ponte tra Portafoglio e Transazione  *a*
CREATE TABLE smu.TransazioniInPortafogli(
    IdTransazione INTEGER,
    IdPortafoglio INTEGER,

    CONSTRAINT FK_Transazione FOREIGN KEY (IdTransazione) REFERENCES smu.Transazione (IdTransazione) ON DELETE CASCADE,
    CONSTRAINT FK_Portafoglio FOREIGN KEY (IdPortafoglio) REFERENCES smu.Portafoglio (IdPortafoglio) ON DELETE CASCADE
);
