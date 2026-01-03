package org.bankati.cryptoservice.service;


import jakarta.transaction.Transactional;
import org.bankati.cryptoservice.model.*;
import org.bankati.cryptoservice.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WalletService {

    private final CryptoWalletRepository walletRepository;
    private final CryptoBalanceRepository balanceRepository;
    private final CryptoTransactionRepository transactionRepository;

    public WalletService(
            CryptoWalletRepository walletRepository,
            CryptoBalanceRepository balanceRepository,
            CryptoTransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
    }

    // =====================================================
    // BUY CRYPTO
    // =====================================================
    public void buyCrypto(Long userId,
                          CryptoCurrency currency,
                          Double cryptoAmount,
                          String fiatCurrency,
                          Double fiatAmount,
                          Long bankAccountId) {

        if (cryptoAmount <= 0 || fiatAmount <= 0) {
            throw new IllegalArgumentException("Amounts must be positive");
        }

        // 1️⃣ Récupérer ou créer le wallet
        CryptoWallet wallet = walletRepository
                .findByUserId(userId)
                .orElseGet(() -> walletRepository.save(new CryptoWallet(userId)));

        // 2️⃣ Récupérer ou créer la balance
        CryptoBalance balance = balanceRepository
                .findByWalletAndCurrency(wallet, currency)
                .orElseGet(() ->
                        balanceRepository.save(new CryptoBalance(wallet, currency, 0.0))
                );

        // 3️⃣ Mettre à jour le solde crypto
        balance.setAmount(balance.getAmount() + cryptoAmount);
        balanceRepository.save(balance);

        // 4️⃣ Créer la transaction
        CryptoTransaction transaction = new CryptoTransaction(
                userId,
                currency,
                CryptoTransactionType.BUY,
                cryptoAmount,
                fiatCurrency,
                fiatAmount,
                AccountType.BANK_ACCOUNT,
                bankAccountId,
                AccountType.CRYPTO_WALLET,
                wallet.getId()
        );

        transactionRepository.save(transaction);
    }

    // =====================================================
    // TRANSFER CRYPTO (USER → USER)
    // =====================================================
    public void transferCrypto(Long fromUserId,
                               Long toUserId,
                               CryptoCurrency currency,
                               Double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }

        CryptoWallet fromWallet = walletRepository.findByUserId(fromUserId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        CryptoWallet toWallet = walletRepository.findByUserId(toUserId)
                .orElseGet(() -> walletRepository.save(new CryptoWallet(toUserId)));

        CryptoBalance fromBalance = balanceRepository
                .findByWalletAndCurrency(fromWallet, currency)
                .orElseThrow(() -> new RuntimeException("Source balance not found"));

        if (fromBalance.getAmount() < amount) {
            throw new RuntimeException("Insufficient crypto balance");
        }

        // Débit source
        fromBalance.setAmount(fromBalance.getAmount() - amount);
        balanceRepository.save(fromBalance);

        // Crédit destination
        CryptoBalance toBalance = balanceRepository
                .findByWalletAndCurrency(toWallet, currency)
                .orElseGet(() ->
                        balanceRepository.save(new CryptoBalance(toWallet, currency, 0.0))
                );

        toBalance.setAmount(toBalance.getAmount() + amount);
        balanceRepository.save(toBalance);

        // Transaction (côté expéditeur)
        CryptoTransaction transaction = new CryptoTransaction(
                fromUserId,
                currency,
                CryptoTransactionType.TRANSFER,
                amount,
                null,
                null,
                AccountType.CRYPTO_WALLET,
                fromWallet.getId(),
                AccountType.CRYPTO_WALLET,
                toWallet.getId()
        );

        transactionRepository.save(transaction);
    }

    // =====================================================
    // TRANSFER CRYPTO → FIAT
    // =====================================================
    public void transferCryptoToFiat(Long userId,
                                     CryptoCurrency currency,
                                     Double cryptoAmount,
                                     String fiatCurrency,
                                     Double fiatAmount,
                                     Long bankAccountId) {

        if (cryptoAmount <= 0 || fiatAmount <= 0) {
            throw new IllegalArgumentException("Amounts must be positive");
        }

        CryptoWallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        CryptoBalance balance = balanceRepository
                .findByWalletAndCurrency(wallet, currency)
                .orElseThrow(() -> new RuntimeException("Crypto balance not found"));

        if (balance.getAmount() < cryptoAmount) {
            throw new RuntimeException("Insufficient crypto balance");
        }

        // Débit crypto
        balance.setAmount(balance.getAmount() - cryptoAmount);
        balanceRepository.save(balance);

        // Transaction
        CryptoTransaction transaction = new CryptoTransaction(
                userId,
                currency,
                CryptoTransactionType.TRANSFER_TO_FIAT,
                cryptoAmount,
                fiatCurrency,
                fiatAmount,
                AccountType.CRYPTO_WALLET,
                wallet.getId(),
                AccountType.BANK_ACCOUNT,
                bankAccountId
        );

        transactionRepository.save(transaction);
    }

    // =====================================================
    // TRANSACTION HISTORY
    // =====================================================
    public List<CryptoTransaction> getTransactionHistory(Long userId) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // =====================================================
    // GET CRYPTO BALANCE (utile pour frontend)
    // =====================================================
    public Double getBalance(Long userId, CryptoCurrency currency) {
        return walletRepository.findByUserId(userId)
                .flatMap(wallet ->
                        balanceRepository.findByWalletAndCurrency(wallet, currency)
                )
                .map(CryptoBalance::getAmount)
                .orElse(0.0);
    }
}