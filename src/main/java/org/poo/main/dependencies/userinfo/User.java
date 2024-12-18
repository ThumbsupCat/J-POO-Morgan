package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;

import java.util.ArrayList;

public class User {
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private String email;
    @Getter @Setter private ArrayList<Account> accounts;
    @Getter @Setter private ArrayList<TransactionHelper> transactions;
    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
}
