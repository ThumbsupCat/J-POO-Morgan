package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.CardTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.PayOnlineTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.List;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public final class PayOnlineCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public PayOnlineCommand(final ArrayNode output) {
        mapper = new ObjectMapper();
        this.output = output;
    }

    /**
     * Executes the primitive online payment using a card assigned to a bank account
     * of a specified user.
     * <p>The details provided by the input make a reference
     * to the card of a certain bank account owned by one of the users.
     * The payment takes place if there's enough balance in the account
     * that the card has been issued from, eventually being deducted from the account's balance </p>
     * <p>Handles cases like usage of a disposable card and insufficient balance.</p>
     *
     * @param input the command input containing all details necessary for the operation,
     *              including card details, amount, currency, timestamp, and more.
     * @param users the list of users whose accounts and transactions are checked and updated
     *              based on the command input.
     * @param exchangeRates the list of exchange rates used for converting amounts between
     *                      different currencies when needed.
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean found = false;
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                            found = true;
                            double convertedAmount;
                            if (!input.getCurrency().contentEquals(account.getCurrency())) {
                                /*
                                *   The currency is not the same as the payer's currency, converting
                                */
                                convertedAmount = reverseConvert(
                                        exchangeRates,
                                        input.getAmount(),
                                        input.getCurrency(),
                                        account.getCurrency()
                                );
                            } else {
                                convertedAmount = input.getAmount();
                            }
                            if (account.getBalance() >= convertedAmount
                                    && card.getStatus().contentEquals("active")) {
                                /*
                                *   Deducting from the balance the amount
                                *   Logging the transaction in the account/user details
                                */
                                account.setBalance(account.getBalance() - convertedAmount);
                                user.getTransactions().add(
                                        new PayOnlineTransaction(
                                                convertedAmount,
                                                input.getCommerciant(),
                                                "Card payment",
                                                input.getTimestamp()
                                        )
                                );
                                account.getTransactions().add(
                                        new PayOnlineTransaction(
                                            convertedAmount,
                                            input.getCommerciant(),
                                            "Card payment",
                                            input.getTimestamp()
                                        )
                                );
                                if (card.isOneTime()) {
                                    /*
                                    *   Replacing the one time card
                                    *   Logging into the user/account transaction that
                                    *   the card changed
                                    */
                                    Card newCard = new Card(
                                            Utils.generateCardNumber(), "active", true);
                                    user.getTransactions().add(
                                            new CardTransaction(
                                                    account.getIBAN(),
                                                    card.getCardNumber(),
                                                    user.getEmail(),
                                                    "The card has been destroyed",
                                                    input.getTimestamp()
                                            )
                                    );
                                    user.getTransactions().add(
                                            new CardTransaction(
                                                    account.getIBAN(),
                                                    newCard.getCardNumber(),
                                                    user.getEmail(),
                                                    "New card created",
                                                    input.getTimestamp()
                                            )
                                    );
                                    account.getCards().remove(card);
                                    account.getCards().add(newCard);
                                }
                                /*
                                *   Keeping this commerciant related code, I may need it next stage
                                */
                                boolean foundCommerciant = false;
                                for (Commerciant commerciant : account.getCommerciants()) {
                                    if (commerciant.getName().contentEquals(
                                            input.getCommerciant())) {
                                        foundCommerciant = true;
                                        commerciant.setAmount(
                                                commerciant.getAmount() + convertedAmount);
                                    }
                                }
                                if (!foundCommerciant) {
                                    account.getCommerciants().add(
                                            new Commerciant(
                                                input.getCommerciant(), convertedAmount)
                                    );
                                }
                                /*
                                *   Placing the warning status if
                                *   the balance doesn't meet the requirements
                                */
                                card.cardStatusUpdate(account);
                            } else {
                                /*
                                *   Adding the transaction error in user transactions
                                */
                                String error;
                                if (card.getStatus().contentEquals("frozen")) {
                                    error = "The card is frozen";
                                } else {
                                    error = "Insufficient funds";
                                }
                                user.getTransactions().add(
                                        new ErrorTransaction(
                                                input.getTimestamp(),
                                                error
                                        )
                                );
                            }
                        }
                    }
                }
            }
        }
        if (!found) {
            /*
            *   The card was not found, creating node for output
            */
            ObjectNode node = mapper.createObjectNode();
            node.put("command", input.getCommand());
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", input.getTimestamp());
            outputNode.put("description", "Card not found");
            node.set("output", outputNode);
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }

}
