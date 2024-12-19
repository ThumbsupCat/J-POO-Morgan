package org.poo.main.dependencies;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

@Setter @Getter
public final class ExchangeRate {
    private String from;
    private String to;
    private double rate;
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

    /**
     * Adds reverse exchange rates to the provided list of exchange rates for simplicity.
     * For each exchange rate in the input list, a reverse rate is calculated
     * by inverting the original rate and flipping the currencies.
     * The reverse rates are then added to the list.
     *
     * @param rates the list of original exchange rates, each containing source currency,
     *              target currency, and their respective exchange rate
     * @return a new list containing all given exchange rates,
     * including their corresponding reverse rates
     */
    public static ArrayList<ExchangeRate> addReverseRates(final List<ExchangeRate> rates) {
        ArrayList<ExchangeRate> allRates = new ArrayList<>(rates);
        for (ExchangeRate rate : rates) {
            double reverseRate = 1 / rate.getRate();
            ExchangeRate reverse = new ExchangeRate(rate.getTo(), rate.getFrom(), reverseRate);
            allRates.add(reverse);
        }
        return allRates;
    }

    /**
     * Converts a given amount between two currencies, using a list of exchange rates.
     * If a direct exchange rate from the source currency to the target currency is not found,
     * the method attempts to find an intermediate currency and calculate the resulting
     * amount through two conversion steps.
     *
     * @param rates        the list of exchange rates available for conversions
     * @param amount       the amount to be converted
     * @param fromCurrency the currency code of the source currency
     * @param toCurrency   the currency code of the target currency
     * @return the converted amount in the target currency, rounded to 9 decimal places,
     * or the original amount if neither a direct nor an indirect conversion is available
     */
    public static double reverseConvert(final List<ExchangeRate> rates,
                                        final double amount,
                                        final String fromCurrency, final String toCurrency) {
        final int exponent = 15;
        final int base = 10;
        for (ExchangeRate rate1 : rates) {
            if (rate1.getFrom().contentEquals(fromCurrency)
                    && rate1.getTo().contentEquals(toCurrency)) {
                return Math.ceil(
                        amount * rate1.getRate() * pow(base, exponent)) / pow(base, exponent
                );
            }
        }

        for (ExchangeRate rate1 : rates) {
            if (rate1.getFrom().contentEquals(fromCurrency)) {
                String inBetween = rate1.getTo();
                double newAmount = amount * rate1.getRate();
                for (ExchangeRate rate2 : rates) {
                    if (rate2.getFrom().contentEquals(inBetween)
                            && rate2.getTo().contentEquals(toCurrency)) {
                        return Math.ceil(
                                newAmount * rate2.getRate()
                                        * pow(base, exponent)) / pow(base, exponent
                        );
                    }
                }
            }
        }
        return amount;
    }
}
