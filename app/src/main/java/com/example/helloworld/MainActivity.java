package com.example.helloworld; // Déclaration du package de l'application

import androidx.appcompat.app.AppCompatActivity; // Importation des classes nécessaires pour les activités Android
import android.app.Activity; // Importation de la classe de base pour les activités

import android.os.Bundle; // Importation de la classe pour gérer l'état de l'activité

// Déclaration de la classe principale de l'application
public class MainActivity extends Activity {
    // Déclaration d'une variable pour la vue personnalisée du jeu
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Appel à la méthode de la classe parent pour initialiser l'activité
        setContentView(R.layout.activity_main); // Définir la vue principale à partir de la ressource XML
        gameView = findViewById(R.id.gameView); // Lier l'élément de la vue à la variable `gameView` par son ID
    }

    @Override
    protected void onPause() {
        super.onPause(); // Appel à la méthode parent pour gérer la mise en pause de l'activité
    }
}
