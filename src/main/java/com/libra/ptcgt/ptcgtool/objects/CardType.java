package com.libra.ptcgt.ptcgtool.objects;

import java.io.IOException;

public enum CardType {
    TRAINER,
    POKEMON,
    ENERGY;

    public static CardType of(String cardSuperType) {
        return switch (cardSuperType) {
            case "Pokémon" -> POKEMON;
            case "Trainer" -> TRAINER;
            case "Energy" -> ENERGY;
            default -> null;
        };
    }
}
