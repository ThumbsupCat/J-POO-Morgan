package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;

public class Card {
    @Getter @Setter private String cardNumber;
    @Getter @Setter private String status;
    @Getter @Setter private boolean oneTime;
    public Card(final String cardNumber, final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }
    public Card(final String cardNumber, final String status, final boolean oneTime) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = oneTime;
    }
    public void cardStatusUpdate(final Account account) {
        if (account.getBalance() - account.getMinBalance() <= account.getMinBalanceInitial()) {
            this.status = "warning";
        } else if (account.getBalance() - account.getMinBalance() <= account.getMinBalance()) {
            this.status = "frozen";
        }
    }
}
