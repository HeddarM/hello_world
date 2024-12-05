package com.example.helloworld;

import java.io.Serializable;
import java.util.Stack;
import java.util.Vector;

public class Game implements Serializable {

    // Classe interne pour représenter une pile (Stack pour les piles des cartes terminées)
    public static class Pile extends java.util.Stack<Cartes> {}

    public Stack<Mouvement> historiqueMouvements = new Stack<>();

    // Classe interne pour représenter une colonne (Stack pour les colonnes de cartes du jeu)
    public static class Colonne extends java.util.Stack<Cartes> {}

    // Constantes pour le nombre de piles et de colonnes dans le jeu
    public static final int PILE_COUNT = 4;
    public static final int COLONNE_COUNT = 7;

    // Attributs pour les piles, colonnes, et les vecteurs de cartes
    public Pile[] pile = new Pile[PILE_COUNT];
    public Colonne[] colonne = new Colonne[COLONNE_COUNT];
    public Vector<Cartes> pioche = new Vector<>(); // Contient toutes les cartes mélangées
    public Vector<Cartes> Piocheretourne = new Vector<>(); // Cartes retournées après pioche

    // Constructeur pour initialiser le jeu
    public Game() {

        // Étape 1 - Création de toutes les cartes
        for (int i = 1; i <= 13; i++) {
            pioche.add(new Cartes(Cartes.CartesType.CARREAU, i)); // Cartes de type carreau
            pioche.add(new Cartes(Cartes.CartesType.COEUR, i));   // Cartes de type cœur
            pioche.add(new Cartes(Cartes.CartesType.PIQUE, i));   // Cartes de type pique
            pioche.add(new Cartes(Cartes.CartesType.TREFLE, i));  // Cartes de type trèfle
        }

        // Étape 2 - Mélange des cartes (200 échanges aléatoires)
        for (int round = 0; round < 200; round++) {
            int position = (int) (Math.random() * pioche.size());
            Cartes removedCartes = pioche.elementAt(position); // Choix d'une carte aléatoire
            pioche.removeElementAt(position);                 // Suppression de la carte
            pioche.add(removedCartes);                        // Ajout de la carte à la fin
        }

        // Étape 3 - Création des colonnes de cartes
        for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
            colonne[colonneIndex] = new Colonne(); // Initialisation d'une colonne
            for (int cartesIndex = 0; cartesIndex < colonneIndex + 1; cartesIndex++) {
                int position = (int) (Math.random() * pioche.size());
                Cartes removedCartes = pioche.elementAt(position); // Pioche d'une carte aléatoire
                pioche.removeElementAt(position);                 // Suppression de la carte de la pioche
                colonne[colonneIndex].push(removedCartes);        // Ajout de la carte dans la colonne
                if (cartesIndex == colonneIndex)
                    removedCartes.setVisible(true); // La dernière carte de chaque colonne est visible
            }
        }

        // Étape 4 - Initialisation des piles
        for (int colonneIndex = 0; colonneIndex < PILE_COUNT; colonneIndex++) {
            pile[colonneIndex] = new Pile(); // Chaque pile commence vide
        }
    }

    // Méthode pour vérifier si une carte peut être déplacée dans une pile
    public int CartesVersPile(Cartes cartes) {
        // Si la carte est un As, elle peut aller dans une pile vide
        if (cartes.getValeur() == 1) {
            for (int pileIndex = 0; pileIndex < PILE_COUNT; pileIndex++) {
                if (this.pile[pileIndex].isEmpty()) {
                    return pileIndex; // Retourne l'index de la pile vide
                }
            }
        }

        // Sinon, vérifie si elle peut être empilée sur une carte de la même couleur et de valeur immédiatement inférieure
        for (int pileIndex = 0; pileIndex < PILE_COUNT; pileIndex++) {
            Pile pile = this.pile[pileIndex];
            if (!pile.isEmpty()) {
                if (pile.lastElement().getType() != cartes.getType()) continue;
                if (pile.lastElement().getValeur() == cartes.getValeur() - 1) return pileIndex;
            }
        }

        return -1; // Retourne -1 si la carte ne peut être placée dans aucune pile
    }

    // Méthode pour vérifier si une carte peut être déplacée dans une colonne
    public int CartesVersColonne(Cartes cartes) {
        // Si la carte est un Roi, elle peut aller dans une colonne vide
        if (cartes.getValeur() == 13) {
            for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
                if (this.colonne[colonneIndex].isEmpty()) return colonneIndex;
            }
        }

        // Vérifie si la carte peut être placée sur une carte d'une autre couleur et de valeur immédiatement supérieure
        for (int colonneIndex = 0; colonneIndex < COLONNE_COUNT; colonneIndex++) {
            Colonne colonne = this.colonne[colonneIndex];
            if (colonne.size() > 0) {
                if (colonne.lastElement().getColor() == cartes.getColor()) continue;
                if (colonne.lastElement().getValeur() == cartes.getValeur() + 1) return colonneIndex;
            }
        }

        return -1; // Retourne -1 si la carte ne peut être placée dans aucune colonne
    }

    // Méthode pour vérifier si la partie est terminée (toutes les piles sont complètes avec des Rois)
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
        return true; // Retourne true si toutes les cartes sont retournées
    }

    public static class Mouvement {
        public Cartes carte;
        public int typeSource; // 0: Pioche, 1: Colonne, 2: Pile
        public int indexSource;
        public int typeDestination;
        public int indexDestination;

        public Mouvement(Cartes carte, int typeSource, int indexSource, int typeDestination, int indexDestination) {
            this.carte = carte;
            this.typeSource = typeSource;
            this.indexSource = indexSource;
            this.typeDestination = typeDestination;
            this.indexDestination = indexDestination;
        }
    }
}
