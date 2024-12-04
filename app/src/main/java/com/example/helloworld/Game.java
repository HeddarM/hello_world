package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class Game implements Serializable {

    public static class Pile extends java.util.Stack<Cartes> {}
    public static class Colonne extends java.util.Stack<Cartes> {}

    public static final int PILE_COUNT = 4;
    public static final int COLONNE_COUNT = 7;

    public Pile[] pile = new Pile[PILE_COUNT];
    public Colonne[] colonne = new Colonne[COLONNE_COUNT];
    public Vector<Cartes> pioche = new Vector<>();
    public Vector<Cartes> Piocheretourne = new Vector<>();

    public Game() {

        // Step 1 - Toutes les cartes sont instanciées
        for (int i = 1; i <= 13; i++) {
            pioche.add(new Cartes(Cartes.CartesType.CARREAU, i));
            pioche.add(new Cartes(Cartes.CartesType.COEUR, i));
            pioche.add(new Cartes(Cartes.CartesType.PIQUE, i));
            pioche.add(new Cartes(Cartes.CartesType.TREFLE, i));
        }

        // Step 2 - On mélange les cartes
        for (int round = 0; round < 200; round++) {
            int position = (int) (Math.random() * pioche.size());
            Cartes removedCartes = pioche.elementAt(position);
            pioche.removeElementAt(position);
            pioche.add(removedCartes);
        }

        // Step 3 - On crée les sept colonnes avec des cartes tirées aléatoirement dans la pioche
        for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
            colonne[colonneIndex] = new Colonne();
            for (int cartesIndex = 0; cartesIndex < colonneIndex + 1; cartesIndex++) {
                int position = (int) (Math.random() * pioche.size());
                Cartes removedCartes = pioche.elementAt(position);
                pioche.removeElementAt(position);
                colonne[colonneIndex].push(removedCartes);
                if (cartesIndex == colonneIndex) removedCartes.setVisible(true);
            }
        }

        // Step 4 - On initialise les quatre stacks.
        for (int colonneIndex = 0; colonneIndex < PILE_COUNT; colonneIndex++) {
            pile[colonneIndex] = new Pile();
        }
    }

    // Méthode pour vérifier si une carte peut être déplacée dans une pile
    public int CartesVersPile(Cartes cartes) {
        // Si une stack est vide et que la carte est un as
        if (cartes.getValeur() == 1) {
            for (int pileIndex = 0; pileIndex < PILE_COUNT; pileIndex++) {
                if (this.pile[pileIndex].isEmpty()) {
                    return pileIndex;
                }
            }
        }

        // Si ce n'est pas un as, peut-on empiler la carte sur une carte de
        // valeur inférieure dans l'une des piles.
        for (int pileIndex = 0; pileIndex < PILE_COUNT; pileIndex++) {
            Pile pile = this.pile[pileIndex];
            if (!pile.isEmpty()) {
                if (pile.lastElement().getType() != cartes.getType()) continue;
                if (pile.lastElement().getValeur() == cartes.getValeur() - 1) return pileIndex;
            }
        }

        return -1;
    }

    // Méthode pour vérifier si une carte peut être déplacée dans une colonne
    public int CartesVersColonne(Cartes cartes) {
        // Si la carte est un roi et qu'un colonne est vide, alors OK
        if (cartes.getValeur() == 13) {
            for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
                if (this.colonne[colonneIndex].isEmpty()) return colonneIndex;
            }
        }

        // Est-ce que la carte peut être placée sur une colonne ?
        for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
            Colonne colonne = this.colonne[colonneIndex];
            if (colonne.size() > 0) {
                if (colonne.lastElement().getColor() == cartes.getColor()) continue;
                if (colonne.lastElement().getValeur() == cartes.getValeur() + 1) return colonneIndex;
            }
        }

        return -1;
    }

    // Méthode pour vérifier si la partie est terminée
    public boolean partieFinie() {
        return !pile[0].isEmpty() && pile[0].lastElement().getValeur() == 13 &&
                !pile[1].isEmpty() && pile[1].lastElement().getValeur() == 13 &&
                !pile[2].isEmpty() && pile[2].lastElement().getValeur() == 13 &&
                !pile[3].isEmpty() && pile[3].lastElement().getValeur() == 13;

    }


    // Méthode pour vérifier si toutes les cartes sont retournées
    public boolean allRetourne() {
        for (int i = 0; i < COLONNE_COUNT; i++) {
            Colonne colonne = this.colonne[i];
            if (colonne.size() > 0 && !colonne.firstElement().isVisible()) return false;
        }
        return true;
    }
}
