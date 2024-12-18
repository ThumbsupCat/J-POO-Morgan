package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.DeleteAccountTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class DeleteAccountCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public DeleteAccountCommand(final ArrayNode output) {
        this.output = output;
        this.mapper = new ObjectMapper();
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        boolean success = false;
        Account targetedAccount = null;
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(input.getAccount())) {
                        if (account.getBalance() == 0) {
                            targetedAccount = account;
                            success = true;
                        }
                    }
                }
            }
            user.getAccounts().remove(targetedAccount);
        }
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        if (!success) {
            outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        } else {
            outputNode.put("success", "Account deleted");
        }
        outputNode.put("timestamp", input.getTimestamp());
        node.set("output", outputNode);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
