package com.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture gameover;

    Texture[] birds;
    int flapState = 0; //Trang thai vo canh bird
    float birdY = 0;  //Tung do bird
    float velocity = 0; //Van toc
    Circle birdCircle;
    int score = 0; //Diem
    int scoringTube = 0;  //Tinh diem ong
    BitmapFont font;


    int gameState = 0; //Trang thai game
    float gravity = 2; //Trong luc
    Texture topTube;
    Texture bottomTube;
    float gap = 800; //Khoang trong
    float maxTubeOffset; //Khoang cach toi da giua 2 ong
    Random randomGenerator;

    float tubeVelocity = 4; //Van toc ong

    int numberOfTubes = 4; //So luong ong
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTheTube; //Khoang cach giua cac ong
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        birdCircle = new Circle();

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("top.png");
        bottomTube = new Texture("bottom.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTheTube = Gdx.graphics.getWidth() * 3 / 4;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();


    }

    public void startGame() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTheTube;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                Gdx.app.log("Score", String.valueOf(score));
                score++;
                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {

                velocity = -30;

            }
            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {

                    tubeX[i] += numberOfTubes * distanceBetweenTheTube;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {

                    tubeX[i] = tubeX[i] - tubeVelocity;

                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }


            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            } else {

                gameState = 2; //2 is the game over state
            }
        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {
                gameState = 1;
            }

        } else if (gameState == 2) {
            batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight());

            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;
            }
        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        for (int i = 0; i < numberOfTubes; i++) {
            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

                gameState = 2;
            }
        }
    }
}
