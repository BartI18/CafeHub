package org.boka.cafe.pojo;

import java.util.Arrays;
import java.util.List;

public enum NameCurrency {
    USD("840", "\uD83C\uDDFA\uD83C\uDDF8"), EUR("978", "\uD83C\uDDEA\uD83C\uDDFA"),
    UAH("980", "\uD83C\uDDFA\uD83C\uDDE6"), RUR("643", "\uD83C\uDDF7\uD83C\uDDFA"),
    BTC("NaN", "\uD83E\uDE99"), PLN("985", "\uD83C\uDDF5\uD83C\uDDF1");

    private String code;
    private String flag;

    private NameCurrency(String code, String flag) {
        this.code = code;
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public String getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.name(), this.flag);
    }
}
