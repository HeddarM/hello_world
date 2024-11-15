package com.example.helloworld;
import android.graphics.Color;
import java.io.Serializable;

public class Cartes implements Serializable {

    private CardType couleur; // Couleur de la carte (Coeur, Carreau, Trèfle, Pique)
    private int valeur; // Valeur de la carte (1 pour As, 11 pour Valet, etc.)
    private boolean visible; // État de la carte : visible ou retournée

    // Constructeur 1 avec état non visible par défaut
    public Cartes(CardType couleur, int valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
        this.visible = false;
    }

    // Constructeur 2 avec possibilité de choisir si la carte est visible ou non
    public Cartes(CardType couleur, int valeur, boolean visible) {
        this.setCouleur(couleur);
        this.setValeur(valeur);
        this.setVisible(visible);
    }

    public CardType getCouleur() {
        return couleur;
    }

    public void setCouleur(CardType couleur) {
        this.couleur = couleur;
    }

    public int getColor() {
        switch (this.couleur) {
            case COEUR:
            case CARREAU:
                return Color.RED;
            case PIQUE:
            case TREFLE:
            default:
                return Color.BLACK;
        }
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        if (valeur < 1 || valeur > 13)
            throw new IllegalArgumentException("Valeur de carte non supportée : " + valeur + ". Elle doit être comprise entre 1 (As) et 13 (Roi).");
        this.valeur = valeur;
    }

    public String getNom() {
        switch (this.valeur) {
            case 1: return "As";
            case 11: return "Valet";
            case 12: return "Reine";
            case 13: return "Roi";
            case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
                return String.valueOf(this.valeur);
            default:
                throw new IllegalArgumentException("Valeur de carte non valide : " + this.valeur);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return couleur.toString() + " " + getNom();
    }

    public enum CardType {
        COEUR, CARREAU, PIQUE, TREFLE;
    }
}

