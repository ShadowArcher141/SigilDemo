package com.demoSpring.demo;

public class Sigil {
    private String name;
    private String permutation;
    private String baseDefinition;
    private String baseCasting;
    private String spellDescription;

    public Sigil() {
    }

    public Sigil(String name, String permutation, String baseDefinition, String baseCasting, String spellDescription) {
        this.name = name;
        this.permutation = permutation;
        this.baseDefinition = baseDefinition;
        this.baseCasting = baseCasting;
        this.spellDescription = spellDescription;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermutation() {
        return permutation;
    }

    public void setPermutation(String permutation) {
        this.permutation = permutation;
    }

    public String getBaseDefinition() {
        return baseDefinition;
    }

    public void setBaseDefinition(String baseDefinition) {
        this.baseDefinition = baseDefinition;
    }

    public String getBaseCasting() {
        return baseCasting;
    }

    public void setBaseCasting(String baseCasting) {
        this.baseCasting = baseCasting;
    }

    public String getSpellDescription() {
        return spellDescription;
    }

    public void setSpellDescription(String spellDescription) {
        this.spellDescription = spellDescription;
    }
}
