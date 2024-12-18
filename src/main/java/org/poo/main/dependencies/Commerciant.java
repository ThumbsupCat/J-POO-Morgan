package org.poo.main.dependencies;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

import java.util.List;

public class Commerciant {
    @Getter @Setter private String name;
    @Getter @Setter private double amount;

    public Commerciant(final String name, final double amount) {
        this.name = name;
        this.amount = amount;
    }
}
