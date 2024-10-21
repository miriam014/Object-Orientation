-- Famiglie
INSERT INTO smu.Famiglia(NomeFamiglia) VALUES('Famiglia Gaetano');
INSERT INTO smu.Famiglia(NomeFamiglia) VALUES('Famiglia Gargiulo');


-- Utenti
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password) VALUES('Giulia28', 'Giulia', 'Gargiulo', '+393662648291', 'giulia.gargiulo3@studenti.unina.it', 'Password1');
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password, IdFamiglia) VALUES('MirGae', 'Miriam', 'Gaetano', '+393316581941', 'miriam.gaetano@studenti.unina.it', 'Password2', 1);
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password, IdFamiglia) VALUES('TataDur', 'Fortunata', 'DUrso', '+398765481948', 'fotunata.duso@studenti.unina.it', 'Password3', 1);
INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password, IdFamiglia) VALUES('Lrgarg', 'Lorenzo', 'Gargiulo', '+393662048291', 'loreg.y@gmail.com', 'Password4', 2);

-- Conti Correnti da fare
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000123456', 'IT60X0542811101000000123456', 1500.00, 'UniCredit', 'BICABCD1234', 'Giulia28');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000987654', 'IT60X0542811101000000987654', 2500.00, 'Intesa Sanpaolo', 'BICWXYZ5678', 'MirGae');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username) VALUES ('000000555555', 'IT60X0542811101000000555555', 3000.00, 'Banco BPM', 'BICDEFG9012', 'MirGae');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username) VALUES ('000000666666', 'IT60X0542811101000000666666', 5000.00, 'Banca Monte dei Paschi di Siena (MPS)', 'BICMNOPQR78', 'Giulia28');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000777777', 'IT60X0542811101000000777777', 7000.00, 'Banca Mediolanum', 'BICUVWX3456', 'TataDur');
INSERT INTO smu.ContoCorrente (NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES ('000000888888', 'IT60X0542811101000000888888', 9000.00, 'Poste Italiane', 'BICSTUV7890','MirGae');

--Carte
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5355284927482884', 'Poste Pay Evolution', 100, '2025-12-31', 13.00, 'Credito', 14000.00, '000000888888');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5337589274884783', 'Carta di Giulia', 345, '2024-08-31', 500.00, 'Credito', 1000.00, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1234567890123456', 'Visa', 789, '2023-05-31', 200.00, 'Credito', 500.00, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('9999000011112222', 'Mastercard', 222, '2026-11-30', 1000.00, 'Credito', 2000.00, '000000987654');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('5555666677778888', 'Visa', 333, '2025-07-31', 700.00, 'Debito', NULL, '000000555555');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('6666999988887777', 'Carta Oro', 444, '2024-04-30', 1500.00, 'Debito', NULL, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1515151515151515', 'Carta Viaggio', 888, '2027-06-30', 250.00, 'Debito', NULL, '000000666666');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1414141414141414', 'Carta Studenti', 777, '2023-12-31', 50.00, 'Credito', 200.00, '000000777777');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1212121212121212', 'Carta di Debito', 555, '2023-09-30', 300.00, 'Debito', NULL, '000000888888');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('9876543210987654', 'Carta Lorenzo', 789, '2023-05-31', 200.00, 'Credito', 500.00, '000000123456');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1234567890443456', 'Carta Fedelta', 345, '2024-08-31', 500.00, 'Credito', 1000.00, '000000987654');
INSERT INTO smu.CARTA(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES('1515151665151515', 'Carta Conad', 888, '2027-06-30', 250.00, 'Debito', NULL, '000000777777');

-- Portafogli
INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES('Viaggi', 1);
INSERT INTO smu.Portafoglio(NomePortafoglio) VALUES('Spese Mensili');
INSERT INTO smu.Portafoglio(NomePortafoglio,IdFamiglia) VALUES('Casa', 1);

-- Categorie
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Cibo e spesa', 'supermercato,supermarket,alimentari,discount,frutta,verdura,salumeria');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Bar e ristoranti', 'ristorante,pizzeria,fastfood,bar,caffe');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Motori e Trasporti', 'assicurazione,trasporti,carburante,auto');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Shopping', 'abbigliamento,calzature,vestiti');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Salute', 'farmacia,sanitaria,benessere,comesi');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Bollette e Tasse', 'bollette,imposta');
INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES('Altro', '');

-- Transazioni
----transazioni conti MirGae
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(40.30, '2024-03-09', '17:45:30', 'Ricarica cellulare', 'Uscita', NULL, 'Operatore Telefonico ', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1500.00, '2023-02-01', '09:00:00', 'Stipendio mensile', 'Entrata', 'Azienda S.p.A.', NULL, '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(85.75, '2021-03-08', '11:20:10', 'Pagamento tasse', 'Uscita', NULL, 'Agenzia delle Entrate', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(35.40, '2020-01-01', '15:25:36', 'Pagamento supermercato', 'Uscita', NULL,'Supermercato Deco', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(35.40, '2020-01-01', '15:25:36', 'Pagamento supermercato', 'Uscita', NULL, 'Supermercato Deco', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.75, '2020-05-01', '11:45:10', 'Spesa settimanale', 'Uscita', NULL, 'Supermercato Coop', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(15.99, '2021-04-10', '19:00:00', 'Abbonamento Spotify', 'Uscita', NULL, 'Spotify', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.25, '2023-03-16', '14:20:10', 'Carburante', 'Uscita', NULL, 'Stazione di servizio ESSO', '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1350.00, '2022-02-25', '17:45:12', 'Stipendio Febbraio', 'Entrata', 'Azienda Tecnologia SRL', NULL, '5337589274884783');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(2000.00, '2022-02-11', '10:50:45', 'Bonus progetto', 'Entrata', 'Tech Innovators', NULL, '5337589274884783');

INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(600.00, '2024-01-30', '09:00:00', 'Rimborso mutuo', 'Entrata', 'Banca Intesa', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(600.00, '2024-03-20', '10:15:30', 'Pensione', 'Entrata', 'INPS', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-03-14', '12:15:30', 'Rimborso assicurazione', 'Entrata', 'Assicurazione Generali', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(65.00, '2023-09-05', '16:30:50', 'Cena ristorante', 'Uscita', NULL, 'Ristorante Bella Napoli', '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.50, '2021-02-25', '16:20:10', 'Rimborso spese mediche', 'Entrata', 'Mutua Salute', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2021-07-01', '13:30:30', 'Pagamento tasse', 'Uscita', NULL, 'Agenzia delle Entrate', '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2022-03-14', '12:15:30', 'Rimborso assicurazione', 'Entrata', 'Assicurazione Generali', NULL, '5555666677778888');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2020-06-11', '17:55:55', 'Viaggio in treno', 'Uscita', NULL, 'Trenitalia', '5555666677778888');

INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(125.00, '2024-02-17', '14:25:55', 'Rimborso viaggio treno', 'Entrata', 'Trenitalia', NULL, '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.00, '2023-02-05', '14:40:33', 'Cena di lavoro', 'Uscita', NULL, 'Ristorante Gourmet', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(245.55, '2024-03-02', '18:25:30', 'Acquisto elettrodomestici', 'Uscita', NULL, 'Mediaworld', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(100.00, '2024-01-12', '12:15:30', 'Rimborso tasse universitarie', 'Entrata', 'Università degli Studi', NULL, '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(245.75, '2021-03-13', '09:55:20', 'Acquisto libri', 'Uscita', NULL, 'Libreria XYZ', '6666999988887777');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(15.00, '2020-12-03', '17:10:10', 'Acquisto rivista', 'Uscita', NULL, 'Mondadori', '6666999988887777');

INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(900.00, '2020-03-18', '12:00:00', 'Rimborso ferie', 'Entrata', 'Agenzia Viaggi Expedia', NULL, '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2020-03-12', '09:30:22', 'Rimborso vacanze', 'Entrata', 'Agenzia Viaggi BestTrips', NULL, '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(20.00, '2020-01-12', '18:12:12', 'Acquisto libro', 'Uscita', NULL, 'Feltrinelli', '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(99.00, '2023-06-06', '12:12:12', 'Abbigliamento online', 'Uscita', NULL, 'Zalando', '9876543210987654');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2023-12-31', '23:59:59', 'Donazione', 'Uscita', NULL, 'Croce Rossa Italiana', '9876543210987654');

INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(2500.00, '2024-03-29', '12:20:45', 'Rimborso pensione integrativa', 'Entrata', 'Assicurazione Vita S.p.A.', NULL, '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1200.00, '2021-04-05', '10:30:45', 'Bonus annuale', 'Entrata', 'Azienda Innovazioni', NULL, '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(89.99, '2023-11-22', '09:55:12', 'Acquisto online Amazon', 'Uscita', NULL, 'Amazon', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(30.50, '2020-03-15', '18:35:45', 'Ristorante', 'Uscita', NULL, 'Ristorante Buon Gusto', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.00, '2020-05-15', '09:09:09', 'Servizio fotografico', 'Uscita', NULL, 'Fotografo Rossi', '9999000011112222');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(999.00, '2022-09-18', '15:15:15', 'Acquisto smartphone', 'Uscita', NULL, 'Apple Store', '9999000011112222');

----transazioni altri conti
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(250.00, '2024-01-15', '14:45:22', 'Rimborso viaggio', 'Entrata', 'Agenzia Viaggi Global', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(450.00, '2024-03-14', '12:10:25', 'Rimborso acquisto auto', 'Entrata', 'Concessionaria Auto', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2023-12-15', '14:50:50', 'Bonifico stipendio', 'Entrata', 'Azienda S.p.A.', NULL, '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(42.30, '2024-03-17', '11:10:55', 'Pagamento affitto', 'Uscita', NULL, 'Proprietario immobile', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(20.00, '2024-03-19', '09:45:00', 'Acquisto online', 'Uscita', NULL, 'E-commerce', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(210.75, '2024-07-12', '10:10:10', 'Acquisto elettronica', 'Uscita', NULL, 'Euronics', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.75, '2023-12-10', '11:05:22', 'Pagamento bollette', 'Uscita', NULL, 'Enel Energia', '1234567890123456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.00, '2024-01-10', '11:30:30', 'Rimborso tasse comunali', 'Entrata', 'Comune di Milano', NULL, '1234567890443456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2024-01-26', '14:30:10', 'Rimborso elettrodomestici', 'Entrata', 'Mediaworld', NULL, '1234567890443456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(200.00, '2023-09-09', '11:11:11', 'Regalo di compleanno', 'Uscita', NULL, 'Mediaworld', '1234567890443456');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(800.00, '2024-04-07', '18:30:30', 'Bonus produzione', 'Entrata', 'Azienda Automobili', NULL, '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(60.99, '2023-08-08', '15:50:50', 'Acquisto biglietti cinema', 'Uscita', NULL, 'UCI Cinema', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(32.40, '2023-06-20', '09:30:11', 'Carburante auto', 'Uscita', NULL, 'Esso', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(500.00, '2024-03-10', '11:12:13', 'Pagamento bonus', 'Entrata', 'Company XYZ', NULL, '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(48.30, '2024-02-15', '14:10:45', 'Ricarica telefonica', 'Uscita', NULL, 'Tim Mobile', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.80, '2024-03-18', '17:30:45', 'Pagamento bollette', 'Uscita', NULL, 'Fornitore energia', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(55.20, '2024-03-12', '16:40:15', 'Pagamento bollo auto', 'Uscita', NULL, 'Ufficio Motorizzazione', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(32.40, '2023-06-20', '09:30:11', 'Carburante auto', 'Uscita', NULL, 'Esso', '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.00, '2024-01-15', '13:20:30', 'Stipendio', 'Entrata', 'Agenzia', NULL, '5355284927482884');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(50.00, '2024-03-06', '11:15:30', 'Rimborso biglietti teatro', 'Entrata', 'Teatro Nazionale', NULL, '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(300.00, '2024-03-03', '08:45:55', 'Rimborso polizza auto', 'Entrata', 'Assicurazione Allianz', NULL, '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(80.50, '2023-11-07', '10:00:15', 'Pagamento assicurazione', 'Uscita', NULL, 'Generali Assicurazioni', '1515151665151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(70.50, '2024-08-14', '16:15:15', 'Spesa mensile', 'Uscita', NULL, 'Esselunga', '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2023-07-07', '12:30:22', 'Abbigliamento', 'Uscita', NULL, 'Zara', '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-11-11', '20:30:00', 'Acquisto scarpe', 'Uscita', NULL, 'Foot Locker', '1515151665151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(1250.00, '2024-04-12', '12:12:12', 'Affitto mensile', 'Uscita', NULL, 'Agenzia Immobiliare Roma', '1515151515151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(140.50, '2023-05-20', '13:00:00', 'Riparazione auto', 'Uscita', NULL, 'Officina Auto', '1515151665151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(275.75, '2024-03-14', '15:20:30', 'Rimborso spese aziendali', 'Entrata', 'Company Italia', NULL, '1515151665151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(120.50, '2024-04-01', '13:15:15', 'Rimborso spese di viaggio', 'Entrata', 'Compagnia Viaggi Global', NULL, '1515151665151515');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.20, '2024-01-12', '16:40:20', 'Rimborso visite mediche', 'Entrata', 'ASL Regionale', NULL, '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(95.20, '2024-01-20', '15:00:10', 'Rimborso viaggio aereo', 'Entrata', 'Compagnia Aerea FlyEasy', NULL, '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(60.00, '2024-03-03', '16:35:55', 'Parrucchiere', 'Uscita', NULL, 'Salone Chic', '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(99.90, '2023-08-22', '17:22:11', 'Biglietti concerto', 'Uscita', NULL, 'TicketOne', '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(180.75, '2023-10-25', '11:45:22', 'Acquisto mobili', 'Uscita', NULL, 'IKEA', '1414141414141414');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(150.00, '2023-10-10', '13:45:32', 'Visita medica', 'Uscita', NULL, 'Centro Medico Salus', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(45.00, '2023-09-30', '14:40:40', 'Acquisto giardino', 'Uscita', NULL, 'Brico', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(70.60, '2024-03-10', '14:00:45', 'Acquisto vestiti', 'Uscita', NULL, 'Negozio di abbigliamento online', '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(600.00, '2024-02-23', '13:45:40', 'Rimborso danni assicurativi', 'Entrata', 'Assicurazione Vittoria', NULL, '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(75.00, '2024-02-15', '10:00:45', 'Rimborso biglietti concerto', 'Entrata', 'TicketOne', NULL, '1212121212121212');
INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta) VALUES(90.00, '2023-11-17', '20:20:20', 'Cena ristorante', 'Uscita', NULL, 'Ristorante Milano', '1212121212121212');


-- Transazioni in portafogli
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(1,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(2,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(3,1);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(4,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(5,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(6,2);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(7,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(10,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(11,3);
INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(12,3);

-- Spese programmate
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Paghetta Armando', '15 giorni', '2024-03-27', '2024-03-27', 20.00, 'Armando figlio', '5355284927482884');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Affitto Mensile', '1 mese', '2024-04-05', '2025-04-05', 800.00, 'Proprietario', '5555666677778888');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Abbonamento Palestra', '1 anno', '2024-03-27', '2025-03-28', 40.00, 'Palestra XYZ', '9876543210987654');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Fornitura Gas', '3 mesi', '2024-04-15', '2025-04-15', 120.00, 'GasCo', '1515151665151515');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Pagamento Bolletta Elettrica', '6 mesi', '2024-04-02', '2024-12-02', 70.00, 'Fornitore Elettrico', '6666999988887777');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Rata Mutuo', '1 mese', '2024-04-10', '2024-12-10', 1000.00, 'Banca XYZ', '1234567890123456');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Assicurazione Auto Annuale', '1 anno', '2024-03-31', '2025-03-31', 600.00, 'Assicurazioni S.p.A.', '1234567890123456');
INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta) VALUES('Pagamento Affitto Garage', '6 mesi', '2024-04-20', '2024-10-20', 150.00, 'Proprietario Garage', '1414141414141414');


-- Associazione portafoglio e carta
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (1, '5355284927482884');
INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta)VALUES (2, '9999000011112222');