package org.bankati.cryptoservice.controller;

import org.bankati.cryptoservice.service.CryptoPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/crypto/prices")
public class CryptoPriceController {

    private final CryptoPriceService cryptoPriceService;

    public CryptoPriceController(CryptoPriceService cryptoPriceService) {
        this.cryptoPriceService = cryptoPriceService;
    }

    /**
     * Récupère les prix BTC et ETH en MAD (temps réel + cache)
     */
    @GetMapping
    public ResponseEntity<?> getAllPrices() {
        return ResponseEntity.ok(cryptoPriceService.getPrices());
    }

    /**
     * Prix du Bitcoin en MAD
     */
    @GetMapping("/bitcoin")
    public ResponseEntity<Double> getBitcoinPriceMAD() {
        return ResponseEntity.ok(cryptoPriceService.getBitcoinPriceMAD());
    }

    /**
     * Prix de l'Ethereum en MAD
     */
    @GetMapping("/ethereum")
    public ResponseEntity<Double> getEthereumPriceMAD() {
        return ResponseEntity.ok(cryptoPriceService.getEthereumPriceMAD());
    }
}