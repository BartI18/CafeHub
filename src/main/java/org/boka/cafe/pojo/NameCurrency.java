package org.boka.cafe.pojo;

import java.util.Arrays;
import java.util.List;

public enum NameCurrency {
    USD("840", "\uD83C\uDDFA\uD83C\uDDF8"), EUR("978", "\uD83C\uDDEA\uD83C\uDDFA"),
    UAH("980", "\uD83C\uDDFA\uD83C\uDDE6"), RUR("643", "\uD83C\uDDF7\uD83C\uDDFA");

    private String code;
    private String flag;

    private NameCurrency(String code, String flag) {
        this.code = code;
        this.flag = flag;
    }

    public NameCurrency getCurrencyByCode(String code) {
        List<NameCurrency> currencies = Arrays.asList(NameCurrency.values());
        for (NameCurrency nameCurrency : currencies) {
            if (nameCurrency.code.equalsIgnoreCase(code)) {
                return nameCurrency;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.name(), this.flag);
    }
}
