package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class Game implements Serializable {

    public static class Stack extends java.util.Stack<Cartes> {}
    public static class Deck extends java.util.Stack<Cartes> {}

    public static final int STACK_COUNT = 4;
    public static final int DECK_COUNT = 7;

    public Stack[] stacks = new Stack[STACK_COUNT];
    public Deck[] decks = new Deck[DECK_COUNT];
    public Vector<Cartes> pioche = new Vector<>();
    public Vector<Cartes> returnedPioche = new Vector<>();

    public Game() {

        // Step 1 - Toutes les cartes sont instanciées
        for (int i = 1; i <= 13; i++) {
            pioche.add(new Cartes(Cartes.CardType.CARREAU, i));
            pioche.add(new Cartes(Cartes.CardType.COEUR, i));
            pioche.add(new Cartes(Cartes.CardType.PIQUE, i));
            pioche.add(new Cartes(Cartes.CardType.TREFLE, i));
        }

        // Step 2 - On mélange les cartes
        for (int round = 0; round < 200; round++) {
            int position = (int) (Math.random() * pioche.size());
            Cartes removedCard = pioche.elementAt(position);
            pioche.removeElementAt(position);
            pioche.add(removedCard);
        }

        // Step 3 - On crée les sept decks avec des cartes tirées aléatoirement dans la pioche
        for (int deckIndex = 0; deckIndex < DECK_COUNT; deckIndex++) {
            decks[deckIndex] = new Deck();
            for (int cardIndex = 0; cardIndex < deckIndex + 1; cardIndex++) {
                int position = (int) (Math.random() * pioche.size());
                Cartes removedCard = pioche.elementAt(position);
                pioche.removeElementAt(position);
                decks[deckIndex].push(removedCard);
                if (cardIndex == deckIndex) removedCard.setVisible(true);
            }
        }

        // Step 4 - On initialise les quatre stacks.
        for (int stackIndex = 0; stackIndex < STACK_COUNT; stackIndex++) {
            stacks[stackIndex] = new Stack();
        }
    }

    // Méthode pour vérifier si une carte peut être déplacée dans une pile (stack)
    public int canMoveCardToStack(Cartes card) {
        // Si une stack est vide et que la carte est un as
        if (card.getValeur() == 1) {
            for (int stackIndex = 0; stackIndex < STACK_COUNT; stackIndex++) {
                if (this.stacks[stackIndex].isEmpty()) {
                    return stackIndex;
                }
            }
        }

        // Si ce n'est pas un as, peut-on empiler la carte sur une carte de
        // valeur inférieure dans l'une des piles.
        for (int stackIndex = 0; stackIndex < STACK_COUNT; stackIndex++) {
            Stack stack = this.stacks[stackIndex];
            if (!stack.isEmpty()) {
                if (stack.lastElement().getCouleur() != card.getCouleur()) continue;
                if (stack.lastElement().getValeur() == card.getValeur() - 1) return stackIndex;
            }
        }

        return -1;
    }

    // Méthode pour vérifier si une carte peut être déplacée dans un deck
    public int canMoveCardToDeck(Cartes card) {
        // Si la carte est un roi et qu'un deck est vide, alors OK
        if (card.getValeur() == 13) {
            for (int deckIndex = 0; deckIndex < DECK_COUNT; deckIndex++) {
                if (this.decks[deckIndex].isEmpty()) return deckIndex;
            }
        }

        // Est-ce que la carte peut être placée sur un deck ?
        for (int deckIndex = 0; deckIndex < DECK_COUNT; deckIndex++) {
            Deck deck = this.decks[deckIndex];
            if (deck.size() > 0) {
                if (deck.lastElement().getColor() == card.getColor()) continue;
                if (deck.lastElement().getValeur() == card.getValeur() + 1) return deckIndex;
            }
        }

        return -1;
    }

    // Méthode pour vérifier si la partie est terminée
    public boolean isFinish() {
        return !stacks[0].isEmpty() && stacks[0].lastElement().getValeur() == 13 &&
                !stacks[1].isEmpty() && stacks[1].lastElement().getValeur() == 13 &&
                !stacks[2].isEmpty() && stacks[2].lastElement().getValeur() == 13 &&
                !stacks[3].isEmpty() && stacks[3].lastElement().getValeur() == 13;
    }

    // Méthode pour vérifier si toutes les cartes sont retournées
    public boolean allIsReturned() {
        for (int i = 0; i < DECK_COUNT; i++) {
            Deck deck = decks[i];
            if (deck.size() > 0 && !deck.firstElement().isVisible()) return false;
        }
        return true;
    }
}
