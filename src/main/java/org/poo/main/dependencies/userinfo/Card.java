package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Card {
    private String cardNumber;
    private String status;
    private boolean oneTime;
    public Card(final String cardNumber, final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }
    public Card(final String cardNumber, final String status, final boolean oneTime) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.oneTime = oneTime;
    }

    /**
     * Updates the card status in case it has a minBalance issue:
     * <p>either:</p>
     *      <p><li>The difference between the account balance and account's minBalance is
     *      less than/equal with 30</li> </p>
     *      <p><li>The account balance is less than the account's minimum balance </li></p>
     * @param account information of the account that the card is being issued
     */
    public void cardStatusUpdate(final Account account) {
        if (account.getBalance() - account.getMinBalance() <= account.getMinBalanceInitial()) {
            status = "warning";
        } else if (account.getBalance() - account.getMinBalance() <= account.getMinBalance()) {
            status = "frozen";
        }
    }
}
