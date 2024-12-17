package org.poo.main.dependencies;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.commands.CommandProcessor;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class InternalActivities {
    private final ArrayList<User> users;
    private final ArrayList<ExchangeRate> exchangeRates;
    private final ArrayList<Commerciant> commerciants;
    private final CommandInput[] commandInputs;
    private final CommandProcessor commandProcessor;
    private final ArrayNode output;

    public InternalActivities(final ArrayList<User> users,
                              final ArrayList<ExchangeRate> exchangeRates,
                              final ArrayList<Commerciant> commerciants,
                              final CommandInput[] commandInputs,
                              final ArrayNode output) {
        this.users = users;
        this.exchangeRates = exchangeRates;
        this.commerciants = commerciants;
        this.commandInputs = commandInputs;
        this.output = output;
        this.commandProcessor = new CommandProcessor(users, exchangeRates, commerciants, output);
    }
    public void startActivities() {
        // So now I can start to parse commands
        for (CommandInput commandInput : commandInputs) {
            commandProcessor.process(commandInput);
        }
    }
}
