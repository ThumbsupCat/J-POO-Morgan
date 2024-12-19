package org.poo.main.dependencies.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;
    private List<TransactionHelper> transactions;
    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
}
