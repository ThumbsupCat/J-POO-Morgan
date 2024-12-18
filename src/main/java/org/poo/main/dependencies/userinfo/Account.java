package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Account {
    @Getter private final double minBalanceInitial = 30;
    @Getter @Setter private String iBAN;
    @Getter @Setter private double balance;
    @Getter @Setter private String currency;
    @Getter @Setter private String type;
    @Getter @Setter private ArrayList<Card> cards;
    @Getter @Setter private double interestRate;
    @Getter @Setter private double minBalance;
    @Getter @Setter private String alias;

    public Account(final String iBAN, final String currency, final String type) {
        this.iBAN = iBAN;
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = minBalanceInitial;
        this.alias = null;
    }

    public Account(final String iBAN,
                   final String currency,
                   final String type, final double interestRate) {
        this.iBAN = iBAN;
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.interestRate = interestRate;
        this.cards = new ArrayList<>();
        this.minBalance = minBalanceInitial;
        this.alias = null;
    }
}
