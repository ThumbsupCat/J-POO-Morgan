package org.poo.main.dependencies;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;

public class ExchangeRate {
    @Getter @Setter private String from;
    @Getter @Setter private String to;
    @Getter @Setter private double rate;
    public ExchangeRate(final ExchangeInput exchangeRate) {
        this.from = exchangeRate.getFrom();
        this.to = exchangeRate.getTo();
        this.rate = exchangeRate.getRate();
    }
    public ExchangeRate(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public static ArrayList<ExchangeRate> addReverseRates(final ArrayList<ExchangeRate> rates) {
        ArrayList<ExchangeRate> allRates = new ArrayList<>(rates); // Copy of existing rates
        for (ExchangeRate rate : rates) {
            // Compute reverse rate
            double reverseRate = 1 / rate.getRate();
            ExchangeRate reverse = new ExchangeRate(rate.getTo(), rate.getFrom(), reverseRate);
            allRates.add(reverse);
        }
        return allRates;
    }
    public static double reverseConvert(final ArrayList<ExchangeRate> rates,
                                        final double amount,
                                        final String fromCurrency, final String toCurrency) {
        for (ExchangeRate rate1 : rates) {
            if (rate1.getFrom().contentEquals(fromCurrency) && rate1.getTo().contentEquals(toCurrency)) {
                return amount * rate1.getRate();
            }
        }

        for (ExchangeRate rate1 : rates) {
            if (rate1.getFrom().contentEquals(fromCurrency)) {
                String inBetween = rate1.getTo();
                for (ExchangeRate rate2 : rates) {
                    if (rate2.getFrom().contentEquals(inBetween) && rate2.getTo().contentEquals(toCurrency)) {
                        return amount * rate1.getRate() * rate2.getRate();
                    }
                }
            }
        }
        return amount;
    }
}
