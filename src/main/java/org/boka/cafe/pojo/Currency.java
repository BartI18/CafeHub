package org.boka.cafe.pojo;

public class Currency {

    private NameCurrency fromCurr;
    private NameCurrency toCurr;
    private String sale;
    private String buy;

    public Currency() {
    }

    public Currency(NameCurrency fromCurr, NameCurrency toCurr) {
        this.fromCurr = fromCurr;
        this.toCurr = toCurr;
    }

    public boolean isEmpty() {
        return getFromCurr() == null || getToCurr() == null || getBuy() == null || getSale() == null;
    }

    public NameCurrency getToCurr() {
        return toCurr;
    }

    public void setToCurr(NameCurrency toCurr) {
        this.toCurr = toCurr;
    }

    public NameCurrency getFromCurr() {
        return fromCurr;
    }

    public void setFromCurr(NameCurrency fromCurr) {
        this.fromCurr = fromCurr;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "baseCurr=" + toCurr +
                ", toCurr=" + fromCurr +
                ", sale='" + sale + '\'' +
                ", buy='" + buy + '\'' +
                '}';
    }
}
