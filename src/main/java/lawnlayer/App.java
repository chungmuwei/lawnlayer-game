package lawnlayer;


import lawnlayer.character.player.Player;
import lawnlayer.character.powerup.Powerup;
import lawnlayer.character.powerup.speedup.SpeedUp;
import lawnlayer.character.enemy.Enemy;

import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;


public class App extends PApplet implements UpdateTiles, SetUp, Draw, Status {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    /**
     * Path to configuration file
     */
    public String configPath;

    /**
     * A JSON object containing configuration of the game
     */
    public JSONObject config;

    /**
     * A JSON array containing the outlay, enemies and goal of the levels
     */
    public JSONArray levels;

    /**
     * The number of levels in total
     */
    public int numOfLevels;

    /**
     * Current level
     */
    public int currentLevel;

    /**
     * A JSON array containing the type and spawn of the enemies of current level
     */
    public JSONArray enemies;

    /**
     * The number of grass tiles in the grids
     */
    public int numOfGrass;

    /**
     * The number of tiles which can be filled with grass
     */
    public int soilArea;

    /**
     * The current percentage of grass filling area
     */
    public int progress;

    /**
     * Goal of the current level
     */
    public float goal;

    /**
     * Whether to end the game
     */
    public boolean gameEnd;

    /**
     * Total lives of the player
     */
    public int lives;
	
    /**
     * Font of the text
     */
    public PFont font;

    /**
     * Represent the type of tile of all the grids
     * 0: Dirt(blank), 1: Concrete, 2: Path in progress, 3: Path hit by enemies, 4: Grass
     */
    public int[][] tile;

    /**
     * countdown for propagate red tiles
     */
    public int propagateRedTile;

    /**
     * countdown for creating powerup
     */
    public int remainingFramesToCreatePowerup;

    /**
     * Whether the arrow key is pressed
     */
    public boolean keyIsReleased;

    /**
     * <code>Player</code> object
     */
    public Player player;

    /**
     * List of <code>Enemy</code> objects
     */
    public List<Enemy> enemiesList;

    /**
     * <code>Powerup</code> object
     */
    public Powerup powerup;

    /**
     * Player image
     */
    public PImage ball;

    /**
     * Grass tile image
     */
	public PImage grass;

    /**
     * Concrete tile image
     */
    public PImage concrete;

    /**
     * Worm enemy image
     */
    public PImage worm;

    /**
     * Beetle enemy image
     */
    public PImage beetle;

    /**
     * Speed up powerup image
     */
    public PImage lightning;

    /**
     * Constructor of <code>App</code>
     */
    public App() {
        this.configPath = "config.json";
        this.currentLevel = 1;
        this.numOfGrass = 0;
        this.tile = new int[(HEIGHT - TOPBAR) / SPRITESIZE][WIDTH / SPRITESIZE];
        keyIsReleased = true;
    }

    /**
     * Initialise the setting of the window size.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load images and configurations. Initialise player, enemies, powerup, font and other attributes.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		this.ball = loadImage(this.getClass().getResource("ball.png").getPath());
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.worm = loadImage(this.getClass().getResource("worm.png").getPath());
        this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        this.lightning = loadImage(this.getClass().getResource("lightning.png").getPath());

        // Load config
        this.config = loadJSONObject(this.configPath);
        this.levels = this.config.getJSONArray("levels");
        this.numOfLevels = this.levels.size();
        this.lives = this.config.getInt("lives");
        this.goal = this.levels.getJSONObject(this.currentLevel - 1).getFloat("goal");
        this.remainingFramesToCreatePowerup = 10 * FPS;
        
        // Instantiate player
        this.player = new Player(this.lives);
        this.player.setSprite(this.ball);

        // Initialise enemiesList
        this.enemiesList = new ArrayList<Enemy>();
        // Instantiate all enemies and add them to enemiesList
        createEnemy(this);

        // Instantiate powerup
        this.powerup = new SpeedUp(this.tile);
        this.powerup.setSprite(this.lightning);

        // Setup font
        font = createFont("Arvo", 40, true);
        textFont(font, 36);
        fill(255);
        
        // Fill concrete tiles
        readLevelFile(this);
        
    }

    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {
        // Exit game
        if (gameEnd) {
            delay(5000);
            exit();
        }

        // Draw brown background
        background(92, 61, 32);
        
        // Update the player
        this.player.tick(this);

        // Update all the enemies
        for (Enemy e : this.enemiesList) {
            e.tick(this);
        }

        // Update the powerup
        this.powerup.tick(this);
        
        // Update tile array
        updateTiles(this);
        
        // Draw all tiles
        drawTiles(this);

        // Draw info
        drawInfoBar(this);

        // Then draw the player
        this.player.draw(this);

        // Draw all the enemies
        for (Enemy e : this.enemiesList) {
            e.draw(this);
        }

        // Draw the powerup
        if (this.powerup.getRemainingFramesToDraw() == 0) {
            this.powerup.draw(this);
        }

        // check remaining lives
        if (gameOver(this.player)) {
            fill(255);
            textFont(this.font, 72);
            textAlign(CENTER, CENTER);
            text("Game over", WIDTH/2, (HEIGHT - TOPBAR)/2+TOPBAR);
            this.gameEnd = true;
        }

        // reach goal level up
        if (this.progress >= this.goal * 100) {
            // no more levels
            if (this.currentLevel == this.numOfLevels) {
                // you win
                fill(255);
                textFont(this.font, 72);
                textAlign(CENTER, CENTER);
                text("You win", WIDTH/2, (HEIGHT - TOPBAR)/2+TOPBAR);
                this.gameEnd = true;
            // level up
            } else {
                levelUp(this);
            }
        }
    }

    /**
     * Called every frame if a key is down.
     * <p>
     * Set player direction by the key code. 
     * <p>
     * Left: 37, Up: 38, Right: 39, Down: 40
     */
    public void keyPressed() {
        this.keyIsReleased = false;
        if (this.keyCode == 37 && this.player.getDirection() != 39) {
            this.player.setDireciton(keyCode);

        } else if (this.keyCode == 39 && this.player.getDirection() != 37) {
            this.player.setDireciton(keyCode);

        } else if (this.keyCode == 38 && this.player.getDirection() != 40) {
            this.player.setDireciton(keyCode);
        
        } else if (this.keyCode == 40 && this.player.getDirection() != 38) {
            this.player.setDireciton(keyCode);
        }
    }

    /**
     * Called every frame if a key is released.
     */
    public void keyReleased() {
        this.keyIsReleased = true;
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
