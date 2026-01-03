package org.bankati.cryptoservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "crypto_balances",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"wallet_id", "currency"})})

public class CryptoBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private CryptoWallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CryptoCurrency currency;

    @Column(nullable = false)
    private Double amount;



    public CryptoBalance() {
    }

    public CryptoBalance(CryptoWallet wallet, CryptoCurrency currency, Double amount) {
        this.wallet = wallet;
        this.currency = currency;
        this.amount = amount;
    }



    public Long getId() {
        return id;
    }

    public CryptoWallet getWallet() {
        return wallet;
    }

    public void setWallet(CryptoWallet wallet) {
        this.wallet = wallet;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

