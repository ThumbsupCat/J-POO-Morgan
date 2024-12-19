package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface TransactionHelper {
    /**
     * Constructs and returns an ObjectNode representing the transaction details.
     * Gets an implementation based on the requirements 'and' it's a helper method which simplifies
     * the code visibility
     *
     * @return an ObjectNode containing the information to be added in the ArrayNode output
     */
    ObjectNode printTransactions();

    /**
     * Checks if the specified type matches the transaction type.
     * <p>Important for the SpendingsReportCommand, filtering the other transaction types
     * and adding in the account's transaction list only online payments.
     * The transaction list also consists of split payments</p>
     * <p>!! Willing to improve the implementation in the future,
     * this solution being temporary. Changes require additional time for testing !!</p>
     * @param type the type to be checked against the transaction type
     * @return true if the specified type matches the transaction type, otherwise false
     */
    boolean matchesType(String type);
}
