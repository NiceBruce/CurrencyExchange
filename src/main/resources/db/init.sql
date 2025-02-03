DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS ExchangeRates;

CREATE TABLE Currencies (
                            ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            Code Varchar NOT NULL UNIQUE,
                            FullName Varchar NOT NULL UNIQUE,
                            Sign Varchar NOT NULL UNIQUE
);

CREATE TABLE ExchangeRates (
                            ID INTEGER PRIMARY KEY AUTOINCREMENT,
                            BaseCurrencyId INTEGER,
                            TargetCurrencyId INTEGER,
                            Rate Decimal(6) NOT NULL,
                            FOREIGN KEY(BaseCurrencyId) REFERENCES Currencies (ID),
                            FOREIGN KEY(TargetCurrencyId) REFERENCES Currencies (ID),
                            UNIQUE(BaseCurrencyId, TargetCurrencyId)
);

INSERT INTO currencies(Code, FullName, Sign)
VALUES('AUD', 'Australian dollar', 'A$');
INSERT INTO currencies(Code, FullName, Sign)
VALUES('CNY', 'Yuan Renminbi', 'CN¥');
INSERT INTO currencies(Code, FullName, Sign)
VALUES('KZT', 'Tenge', '₸');
INSERT INTO currencies(Code, FullName, Sign)
VALUES('TRY', 'Turkish Lira', '₺');
INSERT INTO currencies(Code, FullName, Sign)
VALUES('RSD', 'Serbian Dinar', 'DIN');
INSERT INTO currencies(Code, FullName, Sign)
VALUES('JPY', 'Japan Yen', '¥');

INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(1,2, 4.58);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(3,4, 0.067);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(5,6, 1.40);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(6,4, 0.22);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(5,3, 4.68);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(4,1, 0.045);
INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
VALUES(3,2, 0.014);