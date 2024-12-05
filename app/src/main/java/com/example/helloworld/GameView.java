package com.example.helloworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View implements GestureDetector.OnGestureListener {

    // Couleurs pour l'affichage
    private int CouleurTete;       // Couleur de l'arrière-plan de l'en-tête
    private int CouleurFond;       // Couleur de l'arrière-plan général
    private int CouleurPolice;     // Couleur du texte

    // Gestion du temps
    private long tempsDebut;          // Temps de départ
    private long tempsEcoule;         // Temps écoulé
    private boolean compteurEnCours;  // Indique si le compteur tourne
    private Handler gestionnaireTemps; // Gère les mises à jour du temps
    private Runnable tacheTemps;      // Action répétée pour mettre à jour le temps

    // Compteur pour le nombre de coups joués
    private int compteurCoups = 0;

    // Instance du jeu
    public Game game = new Game();

    // Images des cartes
    private Bitmap imgPique;
    private Bitmap imgTreffle;
    private Bitmap imgCarreau;
    private Bitmap imgCoeur;
    private Bitmap imgPiqueMini;
    private Bitmap imgTreffleMini;
    private Bitmap imgCoeurMini;
    private Bitmap imgCarreauMini;
    private Bitmap imgBack;

    // Dimensions des colonnes
    private float colonneWidth;   // Largeur d'une colonne
    private float colonneHeight;  // Hauteur d'une colonne
    private float colonneMargin;  // Marge entre les colonnes

    // Détecteur de gestes
    private GestureDetector gestureDetector;

    // Constructeur avec initialisation
    public GameView(Context context) {
        super(context);
        postConstruct();
        demarrerCompteur();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        postConstruct();
        demarrerCompteur();
    }

    // Initialisation des ressources et du détecteur de gestes
    private void postConstruct() {
        gestureDetector = new GestureDetector(getContext(), this);
        Resources res = getResources();
        CouleurTete = res.getColor( R.color.white);
        CouleurFond = res.getColor( R.color.CouleurFond );
        CouleurPolice = res.getColor( R.color.CouleurPolice );
        gestionnaireTemps = new Handler();
        // Tâche pour mettre à jour le compteur de temps
        tacheTemps = new Runnable() {
            @Override
            public void run() {
                if (compteurEnCours) {
                    tempsEcoule = System.currentTimeMillis() - tempsDebut;
                    invalidate(); // Redessine l'écran pour afficher le temps mis à jour
                    gestionnaireTemps.postDelayed(this, 1000); // Mise à jour toutes les secondes
                }
            }
        };
        compteurEnCours = false; // Compteur initialement arrêté
    }

    // Démarrer le compteur de temps
    public void demarrerCompteur() {
        tempsDebut = System.currentTimeMillis();
        compteurEnCours = true;
        gestionnaireTemps.post(tacheTemps); // Lance le compteur
    }

    // Arrêter le compteur de temps
    public void arreterCompteur() {
        compteurEnCours = false;
        gestionnaireTemps.removeCallbacks(tacheTemps); // Arrête les mises à jour
    }

    // Revenir en arrière (annuler le dernier mouvement)
    public void revenir() {
        if (!game.historiqueMouvements.isEmpty()) {
            Game.Mouvement dernierMouvement = game.historiqueMouvements.pop();

            // Annuler le mouvement selon la destination précédente
            if (dernierMouvement.typeDestination == 2) { // Si destination est une pile
                game.pile[dernierMouvement.indexDestination].pop();
                game.colonne[dernierMouvement.indexSource].push(dernierMouvement.carte);
            } else if (dernierMouvement.typeDestination == 1) { // Si destination est une colonne
                game.colonne[dernierMouvement.indexDestination].pop();
                game.colonne[dernierMouvement.indexSource].push(dernierMouvement.carte);
            } else if (dernierMouvement.typeDestination == 0) { // Si destination est la pioche
                game.pioche.add(dernierMouvement.carte);
                game.Piocheretourne.remove(dernierMouvement.carte);
            }

            // Mettre à jour l'affichage
            postInvalidate();
        }
    }

    // Définir les dimensions des colonnes lorsque la taille change
     protected void onSizeChanged( int width, int height, int oldw, int oldh ) {
        super.onSizeChanged( width, height, oldw, oldh );

        colonneMargin = width * 0.025f;
        colonneWidth = (width - (Game.COLONNE_COUNT + 1) * colonneMargin) / Game.COLONNE_COUNT;
        colonneHeight = colonneWidth * 1.4f;

        try {
            // Charger et redimensionner les images
            int tailleImage = (int) (colonneWidth * 0.66);
            int tailleImageMini = (int) (colonneWidth / 3);


            imgPique = BitmapFactory.decodeResource(getResources(), R.drawable.pique);
            imgPiqueMini = Bitmap.createScaledBitmap(imgPique, tailleImageMini, tailleImageMini, true);
            imgPique = Bitmap.createScaledBitmap(imgPique, tailleImage, tailleImage, true);

            imgTreffle = BitmapFactory.decodeResource(getResources(), R.drawable.treffle);
            imgTreffleMini = Bitmap.createScaledBitmap(imgTreffle, tailleImageMini, tailleImageMini, true);
            imgTreffle = Bitmap.createScaledBitmap(imgTreffle, tailleImage, tailleImage, true);

            imgCoeur = BitmapFactory.decodeResource(getResources(), R.drawable.coeur);
            imgCoeurMini = Bitmap.createScaledBitmap(imgCoeur, tailleImageMini, tailleImageMini, true);
            imgCoeur = Bitmap.createScaledBitmap(imgCoeur, tailleImage, tailleImage, true);

            imgCarreau = BitmapFactory.decodeResource(getResources(), R.drawable.carreau);
            imgCarreauMini = Bitmap.createScaledBitmap(imgCarreau, tailleImageMini, tailleImageMini, true);
            imgCarreau = Bitmap.createScaledBitmap(imgCarreau, tailleImage, tailleImage, true);

            imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            imgBack = Bitmap.createScaledBitmap(imgBack, (int) colonneWidth, (int) colonneHeight, true);

        } catch (Exception exception) {
            Log.e("ERREUR", "Impossible de charger les images");
        }
    }

    // Méthodes pour calculer les positions des éléments sur l'écran

    private RectF calcRectPile(int index ) {
        float x = colonneMargin + (colonneWidth + colonneMargin) * index;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }

    private RectF calcRectColonne(int index, int cartesIndex ) {
        float x = colonneMargin + (colonneWidth + colonneMargin) * index;
        float y = getHeight() * 0.30f + cartesIndex * calcDecalY();
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }

    private RectF calcRectPiocheRet() {
        float x = colonneMargin + (colonneWidth + colonneMargin) * 5;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }

    private RectF calcRectPiocheVis() {
        float x = colonneMargin + (colonneWidth + colonneMargin) * 6;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }

    public float calcDecalY() {
        return ( getHeight()*0.9f - getHeight()*0.3f ) / 17f;
    }

    private Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );


    /**
     * Cette méthode permet de tracer une carte sur le canvas à la position spécifiée.
     * Si la carte est nulle, seul le contour de la carte sera dessiné.
     *
     * @param canvas Le canvas sur lequel la carte sera dessinée.
     * @param cartes L'objet `Cartes` représentant la carte à dessiner.
     * @param x La coordonnée en X où la carte sera dessinée.
     * @param y La coordonnée en Y où la carte sera dessinée.
     */
    public void AffichageCartes(Canvas canvas, Cartes cartes, float x, float y) {
        float cornerWidth = colonneWidth / 10f; // Rayon des coins arrondis pour la carte

        // Définition du rectangle où la carte sera dessinée
        RectF rectF = new RectF(x, y, x + colonneWidth, y + colonneHeight);

        // Si `cartes` est null, on ne dessine que le contour de la carte
        if (cartes == null) {
            paint.setStyle(Paint.Style.STROKE); // Style contour
            paint.setColor(0xff_40_40_40); // Couleur grise pour le contour
            canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint); // Dessin du contour
            paint.setStyle(Paint.Style.FILL); // Style rempli (pour d'autres éléments si nécessaire)
            return; // Fin de la méthode
        }

        // Dessin de l'arrière-plan de la carte
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(cartes.isVisible() ? 0xff_ff_ff_ff : 0xff_a0_c0_a0); // Couleur selon visibilité
        canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint);

        // Dessin du contour de la carte
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xff_00_00_00); // Couleur noire pour le contour
        canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint);

        // Si la carte est visible, dessiner ses détails
        if (cartes.isVisible()) {
            Bitmap image; // Image principale de la carte (gros symbole)
            Bitmap imageMini; // Petite image dans le coin supérieur
            int color; // Couleur du texte et des symboles

            // Déterminer l'image et la couleur selon le type de carte
            switch (cartes.getType()) {
                case CARREAU:
                    image = imgCarreau;
                    imageMini = imgCarreauMini;
                    color = 0xff_e6_14_08; // Rouge
                    break;
                case COEUR:
                    image = imgCoeur;
                    imageMini = imgCoeurMini;
                    color = 0xff_e6_14_08; // Rouge
                    break;
                case PIQUE:
                    image = imgPique;
                    imageMini = imgPiqueMini;
                    color = 0xff_00_00_00; // Noir
                    break;
                default: // TREFLE
                    image = imgTreffle;
                    imageMini = imgTreffleMini;
                    color = 0xff_00_00_00; // Noir
            }

            // Définir le style et la couleur pour le texte
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(colonneWidth / 2.4f); // Taille du texte
            paint.setFakeBoldText(true); // Texte en gras
            paint.setTextAlign(Paint.Align.LEFT); // Alignement à gauche
            paint.setColor(color); // Couleur du texte

            // Afficher le nom de la carte (valeur)
            if (cartes.getValeur() != 10) {
                canvas.drawText(cartes.getNom(), x + colonneWidth * 0.1f, y + colonneHeight * 0.32f, paint);
            } else { // Cas particulier pour le chiffre "10"
                canvas.drawText("1", x + colonneWidth * 0.1f, y + colonneHeight * 0.32f, paint);
                canvas.drawText("0", x + colonneWidth * 0.3f, y + colonneHeight * 0.32f, paint);
            }

            // Dessiner la petite image dans le coin supérieur droit
            canvas.drawBitmap(imageMini, x + colonneWidth * 0.9f - imageMini.getWidth(),
                    y + colonneHeight * 0.1f, paint);

            // Dessiner l'image principale au centre
            canvas.drawBitmap(image, x + (colonneWidth - image.getWidth()) / 2f,
                    y + (colonneHeight * 0.9f - image.getHeight()), paint);

            paint.setFakeBoldText(false); // Remettre le texte en style normal
        } else {
            // Si la carte n'est pas visible, dessiner le dos de la carte
            canvas.drawBitmap(imgBack, x, y, paint);
        }
    }


    // Méthode pour dessiner les éléments sur l'écran
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Afficher le bouton "Retour"
        paint.setColor(CouleurPolice); // Couleur pour le contour
        paint.setStyle(Paint.Style.STROKE); // Style contour pour le rectangle
        RectF boutonRect = new RectF(0, getHeight() - getHeight() / 10f, getWidth() / 5f, getHeight()); // Rectangle en bas à gauche
        canvas.drawRect(boutonRect, paint); // Dessiner le rectangle

        paint.setStyle(Paint.Style.FILL); // Style pour remplir le texte
        paint.setTextSize(getWidth() / 20f); // Taille du texte
        paint.setTextAlign(Paint.Align.CENTER); // Alignement centré
        canvas.drawText("Retour", boutonRect.centerX(), boutonRect.centerY() + paint.getTextSize() / 3, paint); // Texte centré

        //Fond
        paint.setColor(CouleurFond);
        paint.setStyle( Paint.Style.FILL );
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        //Affiche l'en tete

        float widthDiv10 = getWidth() / 10f;
        float heightDiv10 = getHeight() / 10f;

        paint.setColor(CouleurTete);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight() * 0.15f);
        canvas.drawRect(rectF, paint);

        paint.setColor(CouleurPolice);
        paint.setTextAlign( Paint.Align.CENTER );
        paint.setTextSize( (int) (getWidth() / 8.5) );
        canvas.drawText( getResources().getString(R.string.app_name),
                widthDiv10 * 5, (int) (heightDiv10 * 0.8), paint );

        paint.setColor( CouleurPolice );
        paint.setTextAlign( Paint.Align.LEFT );
        paint.setTextSize( getWidth() / 20f );
        paint.setStrokeWidth(1);
        canvas.drawText( "Coups : " + compteurCoups, (int) (widthDiv10 * 0.5), (int) (heightDiv10 * 1.3), paint );

        paint.setColor(CouleurPolice);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(getWidth() / 20f);

        int secondes = (int) (tempsEcoule / 1000) % 60;
        int minutes = (int) (tempsEcoule / 60000);
        String texteTemps = String.format("%02d:%02d", minutes, secondes);

        canvas.drawText("Temps : " + texteTemps, widthDiv10 * 9.5f, (int) (heightDiv10 * 1.3), paint);


        for (int i = 0; i < Game.PILE_COUNT; i++) {
            Game.Pile pile = game.pile[i];
            rectF = calcRectPile( i );
            AffichageCartes( canvas, pile.isEmpty() ? null : pile.lastElement(), rectF.left, rectF.top );
        }

        // Affiche la pioche
        rectF = calcRectPiocheRet();
        AffichageCartes( canvas, game.Piocheretourne.isEmpty() ? null : game.Piocheretourne.lastElement(),
                rectF.left, rectF.top );

        rectF = calcRectPiocheVis();
        AffichageCartes(canvas, game.pioche.isEmpty() ? null : game.pioche.lastElement(), rectF.left, rectF.top);

        // Affiche les colonnes
        for ( int i = 0; i < Game.COLONNE_COUNT; i++ ) {
            Game.Colonne colonne = game.colonne[i];

            if ( colonne.isEmpty() ) {
                rectF = calcRectColonne(i, 0);
                AffichageCartes( canvas, null, rectF.left, rectF.top );
            } else {
                for ( int cartesIndex = 0; cartesIndex < colonne.size(); cartesIndex++ ) {
                    Cartes cartes = colonne.get(cartesIndex);
                    rectF = calcRectColonne(i, cartesIndex);
                    AffichageCartes(canvas, cartes, rectF.left, rectF.top);
                }
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);     // Le widget repasse la main au GestureDetector.
    }

    // On réagit à un appui simple sur le widget.
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        RectF rect;

        RectF zoneRetour = new RectF(0, 0, getWidth() / 5f, getHeight() / 10f);
        if (zoneRetour.contains(e.getX(), e.getY())) {
            revenir(); // Appelle la méthode pour revenir en arrière
            return true;
        }

        // Un clic sur les cartes non retournées de la pioche
        rect = calcRectPiocheVis();
        if ( rect.contains( e.getX(), e.getY() ) ) {
            if ( ! game.pioche.isEmpty() ) {
                Cartes cartes = game.pioche.remove(0);
                cartes.setVisible( true );
                game.Piocheretourne.add( cartes );
                compteurCoups++; // Incrémentation du compteur
                game.historiqueMouvements.push(new Game.Mouvement(cartes, 0, -1, 0, -1)); // Ajouter à l'historique
            } else {
                game.pioche.addAll( game.Piocheretourne);
                game.Piocheretourne.clear();
                for( Cartes card : game.pioche ) card.setVisible( false );
                compteurCoups++; // Incrémentation du compteur
            }
            postInvalidate();
            return true;
        }

        // Un clic sur les cartes retournées de la pioche
        rect = calcRectPiocheRet();
        if ( rect.contains( e.getX(), e.getY() ) && ! game.Piocheretourne.isEmpty() ) {
            final int pileIndex = game.CartesVersPile( game.Piocheretourne.lastElement() );
            if ( pileIndex > -1 ) {
                Cartes selectedCard = game.Piocheretourne.remove(game.Piocheretourne.size() - 1);
                game.pile[pileIndex].add( selectedCard );
                compteurCoups++; // Incrémentation du compteur
                game.historiqueMouvements.push(new Game.Mouvement(selectedCard, 0, -1, 2, pileIndex)); // Ajouter à l'historique
                postInvalidate();
                return true;
            }

            final int colonneIndex = game.CartesVersColonne( game.Piocheretourne.lastElement() );
            if ( colonneIndex > -1 ) {
                Cartes selectedCard = game.Piocheretourne.remove( game.Piocheretourne.size() - 1 );
                game.colonne[colonneIndex].add( selectedCard );
                compteurCoups++; // Incrémentation du compteur
                game.historiqueMouvements.push(new Game.Mouvement(selectedCard, 0, -1, 1, colonneIndex)); // Ajouter à l'historique
                postInvalidate();
                return true;
            }
        }

        // Un clic sur une carte d'une colonne
        for( int colonneIndex=0; colonneIndex<Game.COLONNE_COUNT; colonneIndex++ ) {
            final Game.Colonne colonne = game.colonne[colonneIndex];
            if ( ! colonne.isEmpty() ) {
                for( int i=colonne.size()-1; i>=0; i-- ) {
                    rect = calcRectColonne(colonneIndex, i);
                    if ( rect.contains(e.getX(), e.getY()) ) {
                        // Click sur carte non retournée de la deck => on sort
                        Cartes currentCard = colonne.get(i);
                        if ( ! currentCard.isVisible() ) return true;

                        // Peut-on déplacer la carte du sommet de la colonne vers une pile ?
                        if ( i == colonne.size() - 1 ) {       // On vérifie de bien être sur le sommet
                            int pileIndex = game.CartesVersPile(currentCard);
                            if (pileIndex > -1) {
                                Cartes selectedCard = colonne.remove(colonne.size() - 1);
                                if ( ! colonne.isEmpty() ) colonne.lastElement().setVisible(true);
                                game.pile[pileIndex].add( selectedCard );
                                compteurCoups++; // Incrémentation du compteur de coups
                                game.historiqueMouvements.push(new Game.Mouvement(currentCard, 1, colonneIndex, 2, pileIndex)); // Ajouter à l'historique
                                postInvalidate();
                                return true;
                            }
                        }

                        // Peut-on déplacer la carte de la colonne vers une autre colonne ?
                        final int colonneIndex2 = game.CartesVersColonne( currentCard );
                        if (colonneIndex2 > -1) {
                            if ( i == colonne.size() - 1 ) {
                                // On déplace qu'une carte
                                Cartes selectedCard = colonne.remove(colonne.size() - 1);
                                if ( ! colonne.isEmpty() ) {
                                    colonne.lastElement().setVisible(true);
                                }
                                game.colonne[colonneIndex2].add( selectedCard );
                                compteurCoups++; // Incrémentation du compteur de coups
                                game.historiqueMouvements.push(new Game.Mouvement(currentCard, 1, colonneIndex, 1, colonneIndex2)); // Ajouter à l'historique
                            } else {
                                // On déplace plusieurs cartes
                                final ArrayList<Cartes> selectedCards = new ArrayList<>();
                                for( int ci=colonne.size()-1; ci>=i; ci-- ) {
                                    selectedCards.add( 0, colonne.remove( ci ) );
                                }
                                if ( ! colonne.isEmpty() ) {
                                    colonne.lastElement().setVisible(true);
                                }
                                game.colonne[colonneIndex2].addAll( selectedCards );
                                for (Cartes cartes : selectedCards) {
                                    game.historiqueMouvements.push(new Game.Mouvement(cartes, 1, colonneIndex, 1, colonneIndex2)); // Ajouter à l'historique
                                }
                                compteurCoups++; // Incrémentation du compteur de coups
                            }
                            postInvalidate();
                            return true;
                        }

                        return true;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}


