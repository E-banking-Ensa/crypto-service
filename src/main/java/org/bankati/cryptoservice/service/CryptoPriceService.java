package org.bankati.cryptoservice.service;

import org.bankati.cryptoservice.dto.CoinGeckoPriceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CryptoPriceService {

    @Value("${coingecko.api.base-url}")
    private String baseUrl;

    @Value("${coingecko.api.endpoint}")
    private String endpoint;

    @Value("${coingecko.api.currencies}")
    private String currency;

    private final RestTemplate restTemplate = new RestTemplate();


    @Cacheable(value = "cryptoPrices", key = "'prices'")
    public CoinGeckoPriceResponse getPrices() {
        String url = baseUrl + endpoint +
                "?ids=bitcoin,ethereum&vs_currencies=" + currency;

        return restTemplate.getForObject(url, CoinGeckoPriceResponse.class);
    }

    public Double getBitcoinPriceMAD() {
        return getPrices().get("bitcoin").getMad();
    }

    public Double getEthereumPriceMAD() {
        return getPrices().get("ethereum").getMad();
    }
}