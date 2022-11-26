package com.libra.ptcgt.ptcgtool.objects;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Immutable Deck Class representing a set of Cards. It may be legal or illegal at any point in time.
 */
public final class Deck {

    private final List<Card> cards;
    private final List<Card> pokemon = new ArrayList<>();
    private final List<Card> trainer = new ArrayList<>();
    private final List<Card> energy = new ArrayList<>();

    private final Map<Card, Integer> count = new HashMap<>();

    public Deck(List<Card> cards) {
        this.cards = cards;
        //TODO: refactor
        pokemon.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.POKEMON).toList());
        trainer.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.TRAINER).toList());
        energy.addAll(cards.parallelStream().filter(card -> card.getCardType() == CardType.ENERGY).toList());
        cards.forEach(c -> {
            if(count.containsKey(c))
                count.replace(c, count.get(c) + 1);
            else
                count.put(c, 1);
        });
    }

    public boolean isLegal() {
        return cards.size() == 60 && cards.parallelStream().allMatch(Card::isLegal) && pokemon.size() >= 1;
    }

    public List<Card> getCards() {
        return cards;
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

    public Deck shuffle() {
        Collections.shuffle(cards);
        return new Deck(cards);
    }

    @Override
    public String toString() {
        StringBuilder deckString = new StringBuilder();

        deckString
                .append("Pokémon (")
                .append(pokemon.size())
                .append(") \n");
        appendCardList(deckString, pokemon);

        deckString
                .append("Trainer (")
                .append(trainer.size())
                .append(") \n");
        appendCardList(deckString, trainer);

        deckString
                .append("Energy (")
                .append(energy.size())
                .append(") \n");
        appendCardList(deckString, energy);

        return deckString.toString();
    }

    private void appendCardList(StringBuilder builder, List<Card> list) {
        Set.copyOf(list).forEach(
                c -> builder
                .append("\t- ")
                .append(count.get(c))
                .append(" ")
                .append(c.toString())
                .append(" ")
                .append(c.getId())
                .append("\n")
        );
    }
}
