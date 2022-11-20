package com.libra.ptcgt.ptcgtool.objects;

import java.util.List;

public final class Deck { // TODO; THIS CLASS IS A WIP

    private boolean legal;
    private List<Card> cards;

    public Deck(boolean legal, List<Card> cards) {
        this.legal = legal;
        this.cards = cards;
    }

    public boolean isLegal() {
        return legal;
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
