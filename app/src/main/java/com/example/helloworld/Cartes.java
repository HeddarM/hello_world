package com.example.helloworld;
import android.graphics.Color;
import java.io.Serializable;

public class Cartes implements Serializable {

    private CartesType type; // Couleur de la carte (Coeur, Carreau, Trèfle, Pique)
    private int valeur; // Valeur de la carte (1 pour As, 11 pour Valet, etc.)
    private boolean visible; // État de la carte : visible ou retournée

    // Constructeur 1 avec état non visible par défaut
    public Cartes(CartesType type, int valeur) {
        this.type = type;
        this.valeur = valeur;
        this.visible = false;
    }

    // Constructeur 2 avec possibilité de choisir si la carte est visible ou non
    public Cartes(CartesType type, int valeur, boolean visible) {
        this.setType(type);
        this.setValeur(valeur);
        this.setVisible(visible);
    }

    public CartesType getType() {
        return type;
    }

    public void setType(CartesType type) {
        this.type = type;
    }

    public int getColor() {
        switch (this.type) {
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
        return type.toString() + " " + getNom();
    }

    public enum CartesType {
        COEUR, CARREAU, PIQUE, TREFLE;
    }
}

