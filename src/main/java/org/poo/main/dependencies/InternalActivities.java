package org.poo.main.dependencies;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.commands.CommandProcessor;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public class InternalActivities {
    private final CommandInput[] commandInputs;
    private final CommandProcessor commandProcessor;

    public InternalActivities(final List<User> users,
                              final List<ExchangeRate> exchangeRates,
                              final CommandInput[] commandInputs,
                              final ArrayNode output) {
        this.commandInputs = commandInputs;
        this.commandProcessor = new CommandProcessor(users, exchangeRates, output);
    }

    /**
     * <p>Initiates processing of a sequence of command inputs.
     * This method iterates over an array of {@code CommandInput} objects
     * and processes each one using an instance of {@code CommandProcessor}.</p>
     * <p>Each command input corresponds to a specific operation or task
     * that the {@code CommandProcessor} will handle based on its
     * internal implementation of commands.</p>
     * <p><b>The processing workflow includes</b>
     * <ul>
     * <li> Fetching the command details from each {@code CommandInput}.</li>
     * <li> Executing the command through the {@code CommandProcessor}.
     * </ul>
     * <b>Assumptions:</b>
     * <li> The {@code commandInputs} array is properly initialized and contains valid command data.
     * <li> The {@code CommandProcessor} instance is ready for processing commands.</p>
     * <p>This method does not handle any exceptions that may occur in the
     * {@code process} method of {@code CommandProcessor}, relying on
     * the callee to manage any potential runtime issues.</p>
     */
    public void startActivities() {
        for (CommandInput commandInput : commandInputs) {
            commandProcessor.process(commandInput);
        }
    }
}
