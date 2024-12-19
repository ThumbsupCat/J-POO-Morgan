package org.poo.main.dependencies;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Commerciant {
    // Used for Stage 2
    private String name;
    private double amount;

    public Commerciant(final String name, final double amount) {
        this.name = name;
        this.amount = amount;
    }
}
