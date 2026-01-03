package org.bankati.cryptoservice.repository;

import org.bankati.cryptoservice.model.CryptoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoTransactionRepository extends JpaRepository<CryptoTransaction, Long> {

    List<CryptoTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}