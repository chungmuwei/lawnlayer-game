package lawnlayer;


import lawnlayer.character.enemy.Enemy;
import lawnlayer.character.enemy.beetle.Beetle;
import lawnlayer.character.enemy.worm.Worm;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.data.JSONArray;

/**
 * Methods for setup
 */
public interface SetUp {
    
    /**
     * Read level file(specifying the position of the concrete) and parse it into tile array
     */
    public default void readLevelFile(App app) {
        // Open level*.txt file
        String levelFile = app.levels.getJSONObject(app.currentLevel - 1).getString("outlay");
        BufferedReader reader = null;
        try {
            reader = app.createReader(Objects.requireNonNull(app.getClass().getResource(levelFile)).getPath());
        } catch (NullPointerException e) {
            System.out.printf("Level file: \"%s\" not found%n", levelFile);
        }
        String line = null;
        char concreteSymbol = 'X';
        try {
            for (int i = 0; i < app.tile.length; i++) {

                assert reader != null;
                line = reader.readLine();
                for (int j = 0; j < app.tile[0].length; j++) {
                   
                    // Parse level file into tile array
                    char c = line.charAt(j);
                    if (c == concreteSymbol) {
                        app.tile[i][j] = 1;
                    } else {
                        app.soilArea++;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create new Enemy 
     */
    public default void createEnemy(App app) {
        JSONArray enemies = app.levels.getJSONObject(app.currentLevel - 1).getJSONArray("enemies");
        for (int i = 0; i < enemies.size(); i++) {
            JSONObject enemy = enemies.getJSONObject(i);
            int enemyType = enemy.getInt("type");
            String enemySpawn = enemy.getString("spawn");
            
            Enemy newEnemy;
            // spawn enemy at random position
            if (enemySpawn.equals("random")) {
                if (enemyType == 0) {
                    newEnemy = new Worm(app.tile);
                } else {
                    newEnemy = new Beetle(app.tile);
                }
                // println(i, newEnemy.getX(), newEnemy.getY());
            
            // spawn enemy at the given position
            } else {
                String[] coordinate = enemySpawn.split(",");
                if (enemyType == 0) {
                    newEnemy = new Worm(PApplet.parseInt(coordinate[0]), PApplet.parseInt(coordinate[1]));
                } else {
                    newEnemy = new Beetle(PApplet.parseInt(coordinate[0]), PApplet.parseInt(coordinate[1]));
                }  
            }

            // Set enemy sprite by type
            if (enemyType == 0) {
                newEnemy.setSprite(app.worm);
            } else if (enemyType == 1) {
                newEnemy.setSprite(app.beetle);
            }

            // Add enemy to enemiesList
            app.enemiesList.add(newEnemy);
        }
    }
}
