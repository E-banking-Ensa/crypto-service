package org.bankati.cryptoservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "crypto_transactions")
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CryptoCurrency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CryptoTransactionType type;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 10)
    private String fiatCurrency; // EUR, USD

    private Double fiatAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType sourceType;

    @Column(nullable = false)
    private Long sourceRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType destinationType;

    @Column(nullable = false)
    private Long destinationRef;



    public CryptoTransaction() {}

    public CryptoTransaction(Long userId,
                             CryptoCurrency currency,
                             CryptoTransactionType type,
                             Double amount,
                             String fiatCurrency,
                             Double fiatAmount,
                             AccountType sourceType,
                             Long sourceRef,
                             AccountType destinationType,
                             Long destinationRef
                             ) {
        this.userId = userId;
        this.currency = currency;
        this.type = type;
        this.amount = amount;
        this.fiatCurrency = fiatCurrency;
        this.fiatAmount = fiatAmount;
        this.sourceType = sourceType;
        this.sourceRef = sourceRef;
        this.destinationType = destinationType;
        this.destinationRef = destinationRef;
    }

    // ======================
    // Lifecycle callbacks
    // ======================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }



    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public CryptoTransactionType getType() {
        return type;
    }

    public void setType(CryptoTransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public Double getFiatAmount() {
        return fiatAmount;
    }

    public void setFiatAmount(Double fiatAmount) {
        this.fiatAmount = fiatAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AccountType getSourceType() {
        return sourceType;
    }

    public Long getSourceRef() {
        return sourceRef;
    }

    public AccountType getDestinationType() {
        return destinationType;
    }

    public Long getDestinationRef() {
        return destinationRef;
    }
}

