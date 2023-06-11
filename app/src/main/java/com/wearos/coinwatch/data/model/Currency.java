package com.wearos.coinwatch.data.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Currency {

    ARS("ars", "ARS", "$"),
    AUD("aud", "AUD", "$"),
    BDT("bdt", "BDT", "৳"),
    BRL("brl", "BRL", "R$"),
    CAD("cad", "CAD", "$"),
    CLP("clp", "CLP", "$"),
    CNY("cny", "CNY", "¥"),
    CZK("czk", "CZK", "Kč"),
    DKK("dkk", "DKK", "kr"),
    EURO("eur", "EUR", "€"),
    GBP("gbp", "GBP", "£"),
    HKD("hkd", "HKD", "$"),
    KRW("krw", "KRW", "₩"),
    MXN("mxn", "MXN", "$"),
    MYR("myr", "MYR", "RM"),
    NOK("nok", "NOK", "kr"),
    NZD("nzd", "NZD", "$"),
    PHP("php", "PHP", "₱"),
    PLN("pln", "PLN", "zł"),
    RUB("rub", "RUB", "₽"),
    INR("inr", "INR", "₹"),
    SEK("sek", "SEK", "kr"),
    SGD("sgd", "SGD", "$"),
    THB("thb", "THB", "฿"),
    TRY("try", "TRY", "₺"),
    TWD("twd", "TWD", "$"),
    UAH("uah", "UAH", "₴"),
    USD("usd", "USD", "$"),
    VND("vnd", "VND", "₫"),
    JPY("jpy", "JPY", "¥");

    private final String currencyQueryId;
    private final String currencyName;
    private final String symbol;
    private static final Map<String, Currency> map;

    static {
        Map<String, Currency> currencyMap = Arrays.stream(Currency.values())
                .collect(Collectors.toMap(s -> s.currencyQueryId, Function.identity()));
        map = Collections.unmodifiableMap(currencyMap);
    }

    public static Optional<Currency> of(String queryId) {
        return Optional.ofNullable(map.get(queryId));
    }

    Currency(String currencyQueryId, String currencyName, String symbol) {
        this.currencyQueryId = currencyQueryId;
        this.currencyName = currencyName;
        this.symbol = symbol;
    }

    public String getCurrencyQueryId() {
        return currencyQueryId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getSymbol() {
        return symbol;
    }
}
