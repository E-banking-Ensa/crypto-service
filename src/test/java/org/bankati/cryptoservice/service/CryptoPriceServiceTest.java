package org.bankati.cryptoservice.service;

import org.bankati.cryptoservice.dto.CoinGeckoPriceResponse;
import org.bankati.cryptoservice.dto.CurrencyPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoPriceServiceTest {

    @Spy
    private CryptoPriceService cryptoPriceService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getPrices_shouldReturnPricesFromCoinGecko() {

        // Arrange
        CoinGeckoPriceResponse response = new CoinGeckoPriceResponse();

        CurrencyPrice btcPrice = new CurrencyPrice();
        btcPrice.setMad(650000.0);

        CurrencyPrice ethPrice = new CurrencyPrice();
        ethPrice.setMad(35000.0);

        response.put("bitcoin", btcPrice);
        response.put("ethereum", ethPrice);

        ReflectionTestUtils.setField(cryptoPriceService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(cryptoPriceService, "baseUrl", "http://fake-url");
        ReflectionTestUtils.setField(cryptoPriceService, "endpoint", "/price");
        ReflectionTestUtils.setField(cryptoPriceService, "currency", "mad");

        when(restTemplate.getForObject(anyString(), eq(CoinGeckoPriceResponse.class)))
                .thenReturn(response);

        // Act
        CoinGeckoPriceResponse result = cryptoPriceService.getPrices();

        // Assert
        assertNotNull(result);
        assertEquals(650000.0, result.get("bitcoin").getMad());
        assertEquals(35000.0, result.get("ethereum").getMad());
    }
}
