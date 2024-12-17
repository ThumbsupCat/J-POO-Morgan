package org.poo.main.dependencies;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

import static org.poo.main.dependencies.ExchangeRate.addReverseRates;

public class InputParser {
    private final ObjectInput input;
    private final ArrayNode output;
    public InputParser(final ObjectInput inputData, final ArrayNode output) {
        this.input = inputData;
        this.output = output;
    }

    public void parseData() {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        ArrayList<Commerciant> commerciants = new ArrayList<>();
        for (UserInput user : input.getUsers()) {
            users.add(new User(user));
        }
        for (ExchangeInput exchangeRate : input.getExchangeRates()) {
            exchangeRates.add(new ExchangeRate(exchangeRate));
        }
        exchangeRates = addReverseRates(exchangeRates);
        if (input.getCommerciants() != null) {
            for (CommerciantInput commerciant : input.getCommerciants()) {
            commerciants.add(new Commerciant(commerciant));
            }
        }
        InternalActivities bankActivities = new InternalActivities(
                users, exchangeRates, commerciants, input.getCommands(), output);
        bankActivities.startActivities();
    }
}
