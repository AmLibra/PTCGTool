package com.libra.ptcgt.ptcgtool.objects;

/**
 * Used to represent the type of Card
 */
public enum CardType {
    TRAINER,
    POKEMON,
    ENERGY;

    /**
     * Parses the Json String and finds the corresponding type
     * @param cardSuperType
     * @return
     */
    public static CardType of(String cardSuperType) {
        return switch (cardSuperType) {
            case "Pokémon" -> POKEMON;
            case "Trainer" -> TRAINER;
            case "Energy" -> ENERGY;
            default -> null;
        };
    }
}
