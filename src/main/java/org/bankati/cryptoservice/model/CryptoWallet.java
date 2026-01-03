package org.bankati.cryptoservice.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "crypto_wallets")
public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToMany(
            mappedBy = "wallet",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CryptoBalance> balances;


    public CryptoWallet() {}

    public CryptoWallet(Long userId) {
        this.userId = userId;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CryptoBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<CryptoBalance> balances) {
        this.balances = balances;
    }
}
