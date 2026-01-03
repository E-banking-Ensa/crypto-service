package org.bankati.cryptoservice.model;

public enum CryptoTransactionType {

    BUY,                // Achat de crypto avec de la monnaie fiat (EUR, USD)
    SELL,               // Vente de crypto vers monnaie fiat
    TRANSFER,           // Transfert de crypto entre utilisateurs
    TRANSFER_TO_FIAT,   // Conversion crypto → compte bancaire
    RECEIVE             // Réception de crypto (utile pour historique)
}
