package org.poo.main.dependencies.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.executes.*;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, Command> commandClassMap = new HashMap<>();
    private final ArrayList<User> users;
    private final ArrayList<ExchangeRate> exchangeRates;
    private final ArrayList<Commerciant> commerciants;
    private final ArrayNode output;

    public CommandProcessor(final ArrayList<User> users,
                            final ArrayList<ExchangeRate> exchangeRate,
                            final ArrayList<Commerciant> commerciants,
                            final ArrayNode output) {
        this.users = users;
        this.exchangeRates = exchangeRate;
        this.commerciants = commerciants;
        this.output = output;
        commandClassMap.put("printUsers", new PrintUsersCommand(output));
//        commandClassMap.put("printTransactions", "PrintTransactionsCommand");
        commandClassMap.put("addAccount", new AddAccountCommand());
        commandClassMap.put("addFunds", new AddFundsCommand());
        commandClassMap.put("createCard", new CreateCardCommand());
        commandClassMap.put("createOneTimeCard", new CreateCardCommand());
        commandClassMap.put("deleteAccount", new DeleteAccountCommand(output));
        commandClassMap.put("deleteCard", new DeleteCardCommand());
//        commandClassMap.put("setMinBalance", "SetMinBalanceCommand");
//        commandClassMap.put("checkCardStatus", "CheckCardStatusCommand");
        commandClassMap.put("payOnline", new PayOnlineCommand(output));
        commandClassMap.put("sendMoney", new SendMoneyCommand());
//        commandClassMap.put("setAlias", "SetAliasCommand");
//        commandClassMap.put("splitPayment", "SplitPaymentCommand");
//        commandClassMap.put("addInterest", "AddInterestCommand");
//        commandClassMap.put("changeInterestRate", "ChangeInterestRateCommand");
//        commandClassMap.put("report", "ReportCommand");
//        commandClassMap.put("spendingReport", "SpendingReportCommand");
    }

    public void process(final CommandInput command) {
        String commandName = command.getCommand();
        if (commandName == null || !commandClassMap.containsKey(commandName)) {
            return;
        }
        try {
            final Command commandClass = commandClassMap.get(commandName);
            commandClass.execute(command, users, exchangeRates, commerciants);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}