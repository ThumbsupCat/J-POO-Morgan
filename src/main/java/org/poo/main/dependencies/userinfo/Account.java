package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

public class Account {
    @Getter private final double minBalanceInitial = 30;
    @Getter @Setter private String iBAN;
    @Getter @Setter private double balance;
    @Getter @Setter private String currency;
    @Getter @Setter private String type;
    @Getter @Setter private ArrayList<Card> cards;
    @Getter @Setter private double interestRate;
    @Getter @Setter private double minBalance;
    @Getter @Setter private ArrayList<TransactionHelper> transactions;
    @Getter @Setter private ArrayList<Commerciant> commerciants;
    @Getter @Setter private String alias;

    public Account(final String iBAN, final String currency, final String type) {
        this.iBAN = iBAN;
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = minBalanceInitial;
        this.transactions = new ArrayList<>();
        this.commerciants = new ArrayList<>();
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
        this.transactions = new ArrayList<>();
        this.commerciants = new ArrayList<>();
        this.minBalance = minBalanceInitial;
        this.alias = null;
    }
}
