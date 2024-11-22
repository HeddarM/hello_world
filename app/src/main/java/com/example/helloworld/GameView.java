package com.example.helloworld;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private float ColonneWidth;
    private float ColonneHeight;
    private float ColonneMargin;

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

        ColonneMargin = width * 0.025f;
        ColonneWidth = (width - (Game.COLONNE_COUNT + 1) * ColonneMargin) / Game.COLONNE_COUNT;
        ColonneHeight = ColonneWidth * 1.4f;

        try {
            int imageSize = (int) (ColonneWidth * 0.66);
            int imageLittleSize = (int) (ColonneWidth / 3);


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
            imgBack = Bitmap.createScaledBitmap(imgBack, (int) ColonneWidth, (int) ColonneHeight, true);

        } catch (Exception exception) {
            Log.e("ERROR", "Cannot load card images");
        }


}

    }

