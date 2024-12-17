package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Account {
    /*

      "IBAN" : "RO58POOB7344468893732422",
      "balance" : 0.0,
      "currency" : "USD",
      "type" : "classic",
      "cards" : [ {
        "cardNumber" : "8207906978464446",
        "status" : "active"

     */
    @Getter @Setter private String iBAN;
    @Getter @Setter private double balance;
    @Getter @Setter private String currency;
    @Getter @Setter private String type;
    @Getter @Setter private ArrayList<Card> cards;
    @Getter @Setter private double interestRate;

    public Account(final String iBAN, final String currency, final String type) {
        this.iBAN = iBAN;
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
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
    }
}
