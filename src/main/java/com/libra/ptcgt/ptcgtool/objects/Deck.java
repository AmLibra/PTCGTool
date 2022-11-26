package com.libra.ptcgt.ptcgtool.objects;

import java.util.*;

/**
 * Immutable Deck Class representing a set of Cards. It may be legal or illegal at any point in time.
 */
public final class Deck {
    private final List<Card> cards;
    private final List<Card> pokemon = new ArrayList<>();
    private final List<Card> trainer = new ArrayList<>();
    private final List<Card> energy = new ArrayList<>();
    private final Map<Card, Integer> count = new HashMap<>();
    private static final String POKEMON_HEADER_BOL = "Pokémon (";
    private static final String TRAINER_HEADER_BOL = "Trainer (";
    private static final String ENERGY_HEADER_BOL = "Energy (";
    private static final String CARDTYPE_HEADER_EOL = ") \n";
    private static final String SPACE_DELIMITER = " ";
    private static final String CARD_PRINT_HEADER = "\t- ";
    private static final String END_OF_LINE = "\n";

    public Deck(List<Card> cards) {
        this.cards = cards;
        //TODO: refactor
        pokemon.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.POKEMON).toList());
        trainer.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.TRAINER).toList());
        energy.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.ENERGY).toList());
        cards.forEach(c -> {
            if (count.containsKey(c))
                count.replace(c, count.get(c) + 1);
            else
                count.put(c, 1);
        });
    }

    public boolean isLegal() {
        return cards.size() == 60 && cards.parallelStream().allMatch(Card::isLegal) && pokemon.size() >= 1;
    }

    public List<Card> getPokemonCards() {
        return pokemon;
    }

    public List<Card> getTrainerCards() {
        return trainer;
    }

    public List<Card> getEnergyCards() {
        return energy;
    }

    public Deck withCard(Card c) {
        cards.add(c);
        return new Deck(cards);
    }

    public Deck withoutCard(Card c) {
        cards.remove(c);
        return new Deck(cards);
    }

    @Override
    public String toString() {
        StringBuilder deckString = new StringBuilder();
        appendSection(deckString, POKEMON_HEADER_BOL, pokemon);
        appendSection(deckString, TRAINER_HEADER_BOL, trainer);
        appendSection(deckString, ENERGY_HEADER_BOL, energy);
        return deckString.toString();
    }

    private void appendSection(StringBuilder builder, String headerPrefix, List<Card> cards) {
        builder
                .append(headerPrefix)
                .append(cards.size())
                .append(CARDTYPE_HEADER_EOL);
        appendCardList(builder, cards);
    }

    private void appendCardList(StringBuilder builder, List<Card> cards) {
        Set.copyOf(cards).forEach(
                c -> builder
                        .append(CARD_PRINT_HEADER)
                        .append(count.get(c))
                        .append(SPACE_DELIMITER)
                        .append(c.toString())
                        .append(SPACE_DELIMITER)
                        .append(c.getId())
                        .append(END_OF_LINE)
        );
    }
}
