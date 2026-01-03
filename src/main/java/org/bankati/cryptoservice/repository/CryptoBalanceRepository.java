package org.bankati.cryptoservice.repository;

import org.bankati.cryptoservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoBalanceRepository extends JpaRepository<CryptoBalance, Long> {

    Optional<CryptoBalance> findByWalletAndCurrency(
            CryptoWallet wallet,
            CryptoCurrency currency
    );
}
