package org.poo.main.dependencies;

import org.poo.fileio.CommerciantInput;

import java.util.List;

public class Commerciant {
    private final int id;
    private final String description;
    private final List<String> commerciants;
    public Commerciant(final CommerciantInput commerciant) {
        this.id = commerciant.getId();
        this.description = commerciant.getDescription();
        this.commerciants = commerciant.getCommerciants();
    }
}
