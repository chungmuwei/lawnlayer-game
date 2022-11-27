package lawnlayer;

import processing.core.PApplet;

/**
 * Methods for draw
 */
public interface Draw {

    /**
     * Draw tiles
     * @param app The window to draw onto.
     */
    public default void drawTiles(App app) {
        int x = 0, y = App.TOPBAR;
        
        for (int i = 0; i < app.tile.length; i++) {
            
            for (int j = 0; j < app.tile[0].length; j++) {
                
                // Draw concrete
                if (app.tile[i][j] == 1) {
                    app.image(app.concrete, x, y);
                // Draw path in progress
                } else if (app.tile[i][j] == 2) {
                    app.fill(0, 255, 0);
                    app.rect(x+2, y+2, App.SPRITESIZE-4, App.SPRITESIZE-4);
                // Draw path hit by enemy
                } else if (app.tile[i][j] == 3) {
                    app.fill(255, 0, 0);
                    app.rect(x+2, y+2, App.SPRITESIZE-4, App.SPRITESIZE-4);
                } else if (app.tile[i][j] == 4) {
                    app.image(app.grass, x, y);
                }

                // update coordinates
                x += App.SPRITESIZE;
                if (x >= App.WIDTH) {
                    x = 0;
                    y += App.SPRITESIZE;
                }
            }
        } 
    }

    /**
     * Draw information bar
     * @param app The window to draw onto.
     */
    public default void drawInfoBar(App app) {
        app.fill(255);
        app.textFont(app.font, 36);

        // Lives
        app.text(String.format("Lives: %s", app.player.getRemainingLives()), 170, 40);
        
        // Powerup
        app.fill(255, 255, 0);
        app.textFont(app.font, 24);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        if (app.powerup.hasStartedEffect()) {
            app.text(String.format("Powerup: speed-up\nRemain: %d s", app.powerup.getRemainingEffectFrames() / App.FPS), App.WIDTH/2, App.TOPBAR/2);
        }
        
        // Progress
        app.fill(255);
        app.textFont(app.font, 36);
        app.progress = App.ceil(app.numOfGrass * 100 / (float)app.soilArea);
        app.text(String.format("%d%%/%.0f%%", app.progress, app.goal * 100), App.WIDTH-340, 40);
        
        // Level
        app.textFont(app.font, 24);
        app.text(String.format("Level %d of %d", app.currentLevel, app.numOfLevels), App.WIDTH-100, 60);
    }
}
