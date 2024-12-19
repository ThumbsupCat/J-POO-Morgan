package org.poo.main.dependencies.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.executes.AddAccountCommand;
import org.poo.main.dependencies.commands.executes.AddFundsCommand;
import org.poo.main.dependencies.commands.executes.AddInterestCommand;
import org.poo.main.dependencies.commands.executes.ChangeInterestRateCommand;
import org.poo.main.dependencies.commands.executes.CheckCardStatusCommand;
import org.poo.main.dependencies.commands.executes.CreateCardCommand;
import org.poo.main.dependencies.commands.executes.DeleteAccountCommand;
import org.poo.main.dependencies.commands.executes.DeleteCardCommand;
import org.poo.main.dependencies.commands.executes.PayOnlineCommand;
import org.poo.main.dependencies.commands.executes.PrintTransactionsCommand;
import org.poo.main.dependencies.commands.executes.PrintUsersCommand;
import org.poo.main.dependencies.commands.executes.ReportCommand;
import org.poo.main.dependencies.commands.executes.SendMoneyCommand;
import org.poo.main.dependencies.commands.executes.SetAliasCommand;
import org.poo.main.dependencies.commands.executes.SetMinBalanceCommand;
import org.poo.main.dependencies.commands.executes.SpendingReportCommand;
import org.poo.main.dependencies.commands.executes.SplitPaymentCommand;
import org.poo.main.dependencies.userinfo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandProcessor {
    private final Map<String, Command> commandClassMap = new HashMap<>();
    private final List<User> users;
    private final List<ExchangeRate> exchangeRates;

    public CommandProcessor(final List<User> users,
                            final List<ExchangeRate> exchangeRate,
                            final ArrayNode output) {
        this.users = users;
        this.exchangeRates = exchangeRate;
        commandClassMap.put("printUsers", new PrintUsersCommand(output));
        commandClassMap.put("printTransactions", new PrintTransactionsCommand(output));
        commandClassMap.put("addAccount", new AddAccountCommand());
        commandClassMap.put("addFunds", new AddFundsCommand());
        commandClassMap.put("createCard", new CreateCardCommand());
        commandClassMap.put("createOneTimeCard", new CreateCardCommand());
        commandClassMap.put("deleteAccount", new DeleteAccountCommand(output));
        commandClassMap.put("deleteCard", new DeleteCardCommand());
        commandClassMap.put("setMinBalance", new SetMinBalanceCommand());
        commandClassMap.put("checkCardStatus", new CheckCardStatusCommand(output));
        commandClassMap.put("payOnline", new PayOnlineCommand(output));
        commandClassMap.put("sendMoney", new SendMoneyCommand());
        commandClassMap.put("setAlias", new SetAliasCommand());
        commandClassMap.put("splitPayment", new SplitPaymentCommand());
        commandClassMap.put("addInterest", new AddInterestCommand(output));
        commandClassMap.put("changeInterestRate", new ChangeInterestRateCommand(output));
        commandClassMap.put("report", new ReportCommand(output));
        commandClassMap.put("spendingsReport", new SpendingReportCommand(output));
    }

    /**
     * Processes a given command input by checking its validity
     * and executing the corresponding command.
     * <p>If the command is not recognized or is null, no action is performed.</p>
     *
     * @param command the input command to process
     */
    public void process(final CommandInput command) {
        String commandName = command.getCommand();
        if (commandName == null || !commandClassMap.containsKey(commandName)) {
            return;
        }
        try {
            final Command commandClass = commandClassMap.get(commandName);
            commandClass.execute(command, users, exchangeRates);
        } catch (final Exception e) {
            // This is must be used while developing
            /*e.printStackTrace();*/
        }
    }
}
