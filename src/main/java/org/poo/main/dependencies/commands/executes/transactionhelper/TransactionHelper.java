package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

public interface TransactionHelper {
    ObjectNode printTransactions();
    boolean matchesType(String type);
}
