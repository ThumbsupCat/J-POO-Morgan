package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Account {
    private final double minBalanceInitial = 30;
    @Setter private String iBAN;
    @Setter private double balance;
    @Setter private String currency;
    @Setter private String type;
    @Setter private ArrayList<Card> cards;
    @Setter private double interestRate;
    @Setter private double minBalance;
    @Setter private List<TransactionHelper> transactions;
    @Setter private List<Commerciant> commerciants;
    @Setter private String alias;

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
