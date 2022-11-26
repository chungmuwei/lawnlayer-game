package lawnlayer;


import org.junit.jupiter.api.Disabled;
import processing.core.PApplet;
import org.junit.jupiter.api.Test;

import lawnlayer.character.enemy.Enemy;

import static org.junit.jupiter.api.Assertions.*;


public class AppTest{

    @Test
    @Disabled
    public void setUpApp() {
        App app = new App();
        app.noLoop(); //optional
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000); //to give time to initialise stuff before drawing begins

        assertEquals(2, app.numOfLevels);
        assertEquals(1, app.currentLevel);
        assertEquals(0, app.numOfGrass);
        assertEquals(0.8, app.goal, 0.01f);
        assertEquals(3, app.lives);
        assertFalse(app.gameEnd);

        // read level 1 file
        for (int r = 0; r < app.tile.length; r++) {
            for (int c = 0; c < app.tile[0].length; c++) {
                int actual = app.tile[r][c];
                int expected = 0;
                if (r == 0 || c == 0 || r == app.tile.length-1 || c == app.tile[0].length-1) {
                    expected = 1;
                }
                assertEquals(expected, actual);
            }
        }

        assertEquals(2, app.enemiesList.size());
        for (Enemy e : app.enemiesList) {
            // enemy must be spawn on dirt not concrete
            assertEquals(0, app.tile[e.getRow()][e.getCol()]); 
        }
    }
}
