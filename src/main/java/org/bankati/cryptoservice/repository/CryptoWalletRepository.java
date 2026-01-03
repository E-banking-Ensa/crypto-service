package org.bankati.cryptoservice.repository;

import org.bankati.cryptoservice.model.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {

    Optional<CryptoWallet> findByUserId(Long userId);
}