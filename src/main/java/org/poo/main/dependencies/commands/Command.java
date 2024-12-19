package org.poo.main.dependencies.commands;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public interface Command {
    /**
     * Executes the specific operations defined by the command using the provided input data,
     * user information, and exchange rate details.
     * <p><b>Command Pattern</b></p>
     *
     * @param input          the command input containing operation-specific details such as
     *                       command type, user email, account information, and other parameters.
     * @param users          the list of registered users who may be impacted or referenced
     *                       by the operation.
     * @param exchangeRates  the list of available exchange rates used for currency conversion
     *                       within the operation if applicable.
     */
    void execute(CommandInput input, List<User> users,
                 List<ExchangeRate> exchangeRates);
}
