package org.poo.main.dependencies;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;
import java.util.List;

import static org.poo.main.dependencies.ExchangeRate.addReverseRates;

public final class InputParser {
    private final ObjectInput input;
    private final ArrayNode output;
    public InputParser(final ObjectInput inputData, final ArrayNode output) {
        this.input = inputData;
        this.output = output;
    }

    /**
     * Parses input data and initializes internal structures for processing.
     * The method processes a set of user and exchange rate inputs,
     * converts them into internal representations, and prepares
     * commands for execution based on the provided input data.
     * <p><b>Workflow: </b>
     * <li>Extracts user input data and converts it into a list of {@code User} objects.</li>
     * <li>Extracts exchange rate input data and converts it into a list</li>
     * of {@code ExchangeRate} objects.
     * <li>Augments the list of exchange rates by adding reverse rates.</li>
     * <li>Initializes an {@code InternalActivities} object with the processed users,
     *   augmented exchange rates, commands, and output.</li>
     * <li>Triggers execution of activities through the
     * {@code InternalActivities} instance.</li></p>
     * <p> </p><b>Assumptions:</b>
     * <li>Input data is provided through the {@code ObjectInput} instance.</li>
     * <li>Output data is written to the {@code ArrayNode} instance.</li>
     * <li>Exchange rates assume that any reverse conversion not explicitly stated</li>
     *   can be derived from the existing rates.
     * <li>Commands for activities are provided as part of the input.</li>
     */
    public void parseData() {
        List<User> users = new ArrayList<>();
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (UserInput user : input.getUsers()) {
            users.add(new User(user));
        }
        for (ExchangeInput exchangeRate : input.getExchangeRates()) {
            exchangeRates.add(new ExchangeRate(exchangeRate));
        }
        exchangeRates = addReverseRates(exchangeRates);
        InternalActivities bankActivities = new InternalActivities(
                users, exchangeRates, input.getCommands(), output);
        bankActivities.startActivities();
    }
}
