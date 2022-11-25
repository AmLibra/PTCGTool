package com.libra.ptcgt.ptcgtool.objects;

import java.util.List;

public final class Deck { // TODO; THIS CLASS IS A WIP

    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;

    }

    public boolean isLegal() {
        return cards.size() == 60;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card c){
        cards.add(c);
    }

    public void removeCard(Card c){
        cards.remove(c);
    }
}
