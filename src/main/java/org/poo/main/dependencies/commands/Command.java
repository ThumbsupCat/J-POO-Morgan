package org.poo.main.dependencies.commands;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public interface Command {
    void execute(CommandInput input, ArrayList<User> users,
                 ArrayList<ExchangeRate> exchangeRates,
                 ArrayList<Commerciant> commerciants);
}
