package com.example.helloworld;

import android.graphics.Color; // Pour les couleurs (rouge et noir)
import java.io.Serializable;  // Interface pour permettre la sérialisation des objets

// Classe représentant une carte
public class Cartes implements Serializable {

    // Attributs principaux d'une carte
    private CartesType type; // Type ou "couleur" de la carte (Cœur, Carreau, Trèfle, Pique)
    private int valeur;      // Valeur de la carte (1 pour As, 11 pour Valet, etc.)
    private boolean visible; // État de la carte : true si visible, false si retournée

    // --- Constructeurs ---

    // Constructeur 1 : crée une carte non visible par défaut
    public Cartes(CartesType type, int valeur) {
        this.type = type;        // Initialisation du type (Cœur, Pique, etc.)
        this.valeur = valeur;    // Initialisation de la valeur (1 à 13)
        this.visible = false;    // Par défaut, la carte est retournée (non visible)
    }

    // Constructeur 2 : possibilité de définir directement si la carte est visible
    public Cartes(CartesType type, int valeur, boolean visible) {
        this.setType(type);      // Utilisation du setter pour initialiser le type
        this.setValeur(valeur);  // Utilisation du setter pour vérifier et initialiser la valeur
        this.setVisible(visible);// Initialisation de l'état de visibilité
    }

    // --- Getters et Setters ---

    // Récupère le type de la carte
    public CartesType getType() {
        return type;
    }

    // Définit le type de la carte
    public void setType(CartesType type) {
        this.type = type;
    }

    // Récupère la couleur (rouge ou noir) en fonction du type
    public int getColor() {
        switch (this.type) {
            case COEUR:
            case CARREAU:
                return Color.RED;   // Rouge pour Cœur et Carreau
            case PIQUE:
            case TREFLE:
            default:
                return Color.BLACK; // Noir pour Pique et Trèfle
        }
    }

    // Récupère la valeur de la carte
    public int getValeur() {
        return valeur;
    }

    // Définit la valeur de la carte avec vérification
    public void setValeur(int valeur) {
        // Vérifie si la valeur est valide (entre 1 et 13)
        if (valeur < 1 || valeur > 13) {
            throw new IllegalArgumentException(
                    "Valeur de carte non supportée : " + valeur
                            + ". Elle doit être comprise entre 1 (As) et 13 (Roi)."
            );
        }
        this.valeur = valeur; // Attribue la valeur si valide
    }

    // Récupère le nom de la carte (A, J, Q, K ou valeur numérique)
    public String getNom() {
        switch (this.valeur) {
            case 1:  return "A"; // As
            case 11: return "J"; // Valet
            case 12: return "Q"; // Dame
            case 13: return "K"; // Roi
            case 2: case 3: case 4: case 5: case 6:
            case 7: case 8: case 9: case 10:
                return String.valueOf(this.valeur); // Valeur numérique (2 à 10)
            default:
                // Exception si la valeur est invalide (devrait être impossible ici)
                throw new IllegalArgumentException("Valeur de carte non valide : " + this.valeur);
        }
    }

    // Récupère l'état de visibilité de la carte
    public boolean isVisible() {
        return visible;
    }

    // Définit l'état de visibilité de la carte
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // --- Méthodes supplémentaires ---

    // Méthode pour représenter une carte sous forme de chaîne de caractères
    @Override
    public String toString() {
        return type.toString() + " " + getNom(); // Ex : "COEUR A" pour l'As de Cœur
    }

    // --- Enumération pour les types de cartes ---
    public enum CartesType {
        COEUR,   // Représente le type "Cœur"
        CARREAU, // Représente le type "Carreau"
        PIQUE,   // Représente le type "Pique"
        TREFLE;  // Représente le type "Trèfle"
    }
}
