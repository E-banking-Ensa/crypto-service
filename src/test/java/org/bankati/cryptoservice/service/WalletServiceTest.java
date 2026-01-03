package org.bankati.cryptoservice.service;

import org.bankati.cryptoservice.model.*;
import org.bankati.cryptoservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private CryptoWalletRepository walletRepository;

    @Mock
    private CryptoBalanceRepository balanceRepository;

    @Mock
    private CryptoTransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    private CryptoWallet wallet;

    @BeforeEach
    void setup() {
        wallet = new CryptoWallet(1L);
        wallet.setId(10L);
    }

    // =========================
    // TEST BUY CRYPTO
    // =========================
    @Test
    void buyCrypto_shouldCreateWalletBalanceAndTransaction() {

        when(walletRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        when(walletRepository.save(any()))
                .thenReturn(wallet);

        when(balanceRepository.findByWalletAndCurrency(wallet, CryptoCurrency.BTC))
                .thenReturn(Optional.empty());

        when(balanceRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        walletService.buyCrypto(
                1L,
                CryptoCurrency.BTC,
                0.01,
                "MAD",
                6000.0,
                100L
        );

        verify(walletRepository).save(any(CryptoWallet.class));
        verify(balanceRepository, times(2)).save(any(CryptoBalance.class));
        verify(transactionRepository).save(any(CryptoTransaction.class));

    }

    // =========================
    // TEST GET BALANCE
    // =========================
    @Test
    void getBalance_shouldReturnBalanceAmount() {

        CryptoBalance balance = new CryptoBalance(wallet, CryptoCurrency.ETH, 2.5);

        when(walletRepository.findByUserId(1L))
                .thenReturn(Optional.of(wallet));

        when(balanceRepository.findByWalletAndCurrency(wallet, CryptoCurrency.ETH))
                .thenReturn(Optional.of(balance));

        Double result = walletService.getBalance(1L, CryptoCurrency.ETH);

        assertEquals(2.5, result);
    }

    // =========================
    // TEST TRANSACTION HISTORY
    // =========================
    @Test
    void getTransactionHistory_shouldReturnList() {

        when(transactionRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(new CryptoTransaction()));

        List<CryptoTransaction> transactions =
                walletService.getTransactionHistory(1L);

        assertEquals(1, transactions.size());
    }

}