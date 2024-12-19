package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class MoneySendTransaction implements TransactionHelper {
    private final ObjectMapper mapper;
    private final int timestamp;
    private final String description;
    private final String senderIBAN;
    private final String receiverIBAN;
    private final double amount;
    private final String amountCurrency;
    private final String transferType;
    public MoneySendTransaction(final int timestamp, final String description,
                                final String senderIBAN, final String receiverIBAN,
                                final double amount, final String amountCurrency,
                                final String transferType) {
        mapper = new ObjectMapper();
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = Double.parseDouble(String.valueOf(amount));
        this.amountCurrency = amountCurrency;
        this.transferType = transferType;
    }

    /**
     * Constructs and returns an ObjectNode containing the transaction details
     * for a money-sending transaction. The ObjectNode includes the timestamp,
     * description, sender IBAN, receiver IBAN, amount, and transfer type.
     *
     * @return an ObjectNode representing the transaction details
     */
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("senderIBAN", senderIBAN);
        node.put("receiverIBAN", receiverIBAN);
        node.put("amount", toString());
        node.put("transferType", transferType);
        return node;
    }

    /**
     * Returns a string representation of the transaction amount and its currency.
     * The format of the resulting string is "{amount} {currency}".
     *
     * @return a string combining the amount and its associated currency
     */
    public String toString() {
        return amount + " " + amountCurrency;
    }

    /**
     * Checks if the given type matches the specific transaction type "MoneySendTransaction".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "MoneySendTransaction", false otherwise
     */
    public boolean matchesType(final String type) {
        return "MoneySendTransaction".contentEquals(type);
    }
}
