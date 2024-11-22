package com.example.helloworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View {

    private int headerBackgroundColor;
    private int headerForegroundColor;
    private int backgroundColor;
    private int redColor;
    public Game game = new Game();

    private Bitmap imgPique;
    private Bitmap imgPiqueLittle;
    private Bitmap imgTreffle;
    private Bitmap imgTreffleLittle;
    private Bitmap imgCarreau;
    private Bitmap imgCarreauLittle;
    private Bitmap imgCoeur;
    private Bitmap imgCoeurLittle;

    private Bitmap imgBack;

    private float colonneWidth;
    private float colonneHeight;
    private float colonneMargin;

    public GameView(Context context) {
        super(context);
        postConstruct();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        postConstruct();
    }

    private void postConstruct() {
        Resources res = getResources();
        headerBackgroundColor = res.getColor( R.color.colorPrimaryDark );
        headerForegroundColor = res.getColor( R.color.headerForegroundColor );
        backgroundColor = res.getColor( R.color.backgroundColor );
        redColor = res.getColor( R.color.redColor );
    }

    protected void onSizeChanged( int width, int height, int oldw, int oldh ) {
        super.onSizeChanged( width, height, oldw, oldh );

        colonneMargin = width * 0.025f;
        colonneWidth = (width - (Game.COLONNE_COUNT + 1) * colonneMargin) / Game.COLONNE_COUNT;
        colonneHeight = colonneWidth * 1.4f;

        try {
            int imageSize = (int) (colonneWidth * 0.66);
            int imageLittleSize = (int) (colonneWidth / 3);


            imgPique = BitmapFactory.decodeResource(getResources(), R.drawable.pique);
            imgPiqueLittle = Bitmap.createScaledBitmap(imgPique, imageLittleSize, imageLittleSize, true);
            imgPique = Bitmap.createScaledBitmap(imgPique, imageSize, imageSize, true);

            imgTreffle = BitmapFactory.decodeResource(getResources(), R.drawable.treffle);
            imgTreffleLittle = Bitmap.createScaledBitmap(imgTreffle, imageLittleSize, imageLittleSize, true);
            imgTreffle = Bitmap.createScaledBitmap(imgTreffle, imageSize, imageSize, true);

            imgCoeur = BitmapFactory.decodeResource(getResources(), R.drawable.coeur);
            imgCoeurLittle = Bitmap.createScaledBitmap(imgCoeur, imageLittleSize, imageLittleSize, true);
            imgCoeur = Bitmap.createScaledBitmap(imgCoeur, imageSize, imageSize, true);

            imgCarreau = BitmapFactory.decodeResource(getResources(), R.drawable.carreau);
            imgCarreauLittle = Bitmap.createScaledBitmap(imgCarreau, imageLittleSize, imageLittleSize, true);
            imgCarreau = Bitmap.createScaledBitmap(imgCarreau, imageSize, imageSize, true);

            imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            imgBack = Bitmap.createScaledBitmap(imgBack, (int) colonneWidth, (int) colonneHeight, true);

        } catch (Exception exception) {
            Log.e("ERROR", "Cannot load card images");
        }
    }

    /**
     * Calcul de la "bounding box" de la stack spécifiée en paramètre.
     */
    private RectF computeStackRect( int index ) {
        float x = colonneMargin + (colonneWidth + colonneMargin) * index;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }


    /**
     * Calcul de la "bounding box" de la pile retournée associée à la pioche.
     */
    private RectF computeReturnedPiocheRect() {
        float x = colonneMargin + (colonneWidth + colonneMargin) * 5;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }


    /**
     * Calcul de la "bounding box" de la pile découverte associée à la pioche.
     */
    private RectF computePiocheRect() {
        float x = colonneMargin + (colonneWidth + colonneMargin) * 6;
        float y = getHeight() * 0.17f;
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }


    /**
     * Calcul de la "bounding box" du deck spécifié en paramètre.
     */
    private RectF computeDeckRect( int index, int cardIndex ) {
        float x = colonneMargin + (colonneWidth + colonneMargin) * index;
        float y = getHeight() * 0.30f + cardIndex * computeStepY();
        return new RectF( x, y, x+colonneWidth, y+colonneHeight );
    }


    /**
     * Calcul du décalage en y pour toutes les cartes d'un deck.
     */
    public float computeStepY() {
        return ( getHeight()*0.9f - getHeight()*0.3f ) / 17f;
    }

    private Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );


    /**
     * Cette méthode permet de tracer une carte à la position spécifiée en paramètre.
     * @param canvas Le canvas à utiliser.
     * @param cartes  La carte à dessiner. Si vous passez un pointeur nul,
     *              juste le contour de la carte sera tracé (état initial des stacks par exemple).
     * @param x La position en x de tracé.
     * @param y La position en y de tracé.
     */
    public void drawCartes(Canvas canvas, Cartes cartes, float x, float y ) {
        float cornerWidth = colonneWidth / 10f;

        RectF rectF = new RectF( x, y, x + colonneWidth, y + colonneHeight );

        // Si card == null alors on ne trace que le contour de la carte
        if ( cartes == null ) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xff_40_40_40);
            canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint);
            paint.setStyle(Paint.Style.FILL);
            return;
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor( cartes.isVisible() ? 0xff_ff_ff_ff : 0xff_a0_c0_a0 );
        canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor( 0xff_00_00_00 );
        canvas.drawRoundRect(rectF, cornerWidth, cornerWidth, paint);

        if ( cartes.isVisible() ) {
            Bitmap image;
            Bitmap imageLittle;
            int color;
            switch (cartes.getType()) {
                case CARREAU:
                    image = imgCarreau;
                    imageLittle = imgCarreauLittle;
                    color = 0xff_e6_14_08;
                    break;
                case COEUR:
                    image = imgCoeur;
                    imageLittle = imgCoeurLittle;
                    color = 0xff_e6_14_08;
                    break;
                case PIQUE:
                    image = imgPique;
                    imageLittle = imgPiqueLittle;
                    color = 0xff_00_00_00;
                    break;
                default:
                    image = imgTreffle;
                    imageLittle = imgTreffleLittle;
                    color = 0xff_00_00_00;
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize( colonneWidth / 2.4f );
            paint.setFakeBoldText( true );
            paint.setTextAlign( Paint.Align.LEFT );
            paint.setColor( color );
            if ( cartes.getValeur() != 10 ) {
                canvas.drawText(cartes.getNom(), x + colonneWidth * 0.1f, y + colonneHeight * 0.32f, paint);
            } else {
                canvas.drawText( "1", x + colonneWidth * 0.1f, y + colonneHeight * 0.32f, paint);
                canvas.drawText( "0", x + colonneWidth * 0.3f, y + colonneHeight * 0.32f, paint);
            }
            canvas.drawBitmap( imageLittle, x + colonneWidth*0.9f - imageLittle.getWidth(),
                    y + colonneHeight * 0.1f, paint );
            canvas.drawBitmap( image, x + (colonneWidth - image.getWidth())/ 2f,
                    y + (colonneHeight*0.9f - image.getHeight()) , paint );
            paint.setFakeBoldText( false );
        } else {
            canvas.drawBitmap(imgBack, x, y, paint);
        }
    }


    /**
     * On trace l'aire de jeu
     * @param canvas Le canvas à utiliser.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // --- Background ---
        paint.setColor(backgroundColor);
        paint.setStyle( Paint.Style.FILL );
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // --- Draw the Header ---

        float widthDiv10 = getWidth() / 10f;
        float heightDiv10 = getHeight() / 10f;

        paint.setColor( headerBackgroundColor );
        RectF rectF = new RectF(0, 0, getWidth(), getHeight() * 0.15f);
        canvas.drawRect(rectF, paint);

        paint.setColor(redColor);
        paint.setTextAlign( Paint.Align.CENTER );
        paint.setTextSize( (int) (getWidth() / 8.5) );
        canvas.drawText( getResources().getString(R.string.app_name),
                widthDiv10 * 5, (int) (heightDiv10 * 0.8), paint );

        paint.setColor( headerForegroundColor );
        paint.setTextAlign( Paint.Align.LEFT );
        paint.setTextSize( getWidth() / 20f );
        paint.setStrokeWidth(1);
        canvas.drawText( "V 1.0", (int) (widthDiv10 * 0.5), (int) (heightDiv10 * 1.3), paint );

        paint.setTextAlign( Paint.Align.RIGHT );
        canvas.drawText( "By KooR.fr", (int) (widthDiv10 * 9.5), (int) (heightDiv10 * 1.3), paint );


        // --- Draw the fourth stacks ---
        paint.setStrokeWidth( getWidth() / 200f );

        for (int i = 0; i < Game.PILE_COUNT; i++) {
            Game.Pile stack = game.pile[i];
            rectF = computeStackRect( i );
            drawCartes( canvas, stack.isEmpty() ? null : stack.lastElement(), rectF.left, rectF.top );
        }

        // --- Draw the pioche ---
        rectF = computeReturnedPiocheRect();
        drawCartes( canvas, game.returnedPioche.isEmpty() ? null : game.returnedPioche.lastElement(),
                rectF.left, rectF.top );

        rectF = computePiocheRect();
        drawCartes(canvas, game.pioche.isEmpty() ? null : game.pioche.lastElement(), rectF.left, rectF.top);

        // --- Draw the seven decks ---
        for ( int i = 0; i < Game.COLONNE_COUNT; i++ ) {
            Game.Colonne colonne = game.colonne[i];

            if ( colonne.isEmpty() ) {
                rectF = computeDeckRect(i, 0);
                drawCartes( canvas, null, rectF.left, rectF.top );
            } else {
                for ( int cartesIndex = 0; cartesIndex < colonne.size(); cartesIndex++ ) {
                    Cartes cartes = colonne.get(cartesIndex);
                    rectF = computeDeckRect(i, cartesIndex);
                    drawCartes(canvas, cartes, rectF.left, rectF.top);
                }
            }

        }
    }

}

