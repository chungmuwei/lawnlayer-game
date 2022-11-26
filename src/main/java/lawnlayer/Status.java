package lawnlayer;

import lawnlayer.character.player.Player;

/**
 * Methods for changing game status
 */
public interface Status {

    /**
     * Whether game is over
     * @param player Represent the player.
     * @return <code>player.getRemainingLives()</code>
     */
    public default boolean gameOver(Player player) {
        return player.getRemainingLives() == 0;
    }

    /**
     * Move to next level
     * @param app The window to draw onto.
     */
    public default void levelUp(App app) {
        app.currentLevel++;
        app.progress = 0;
        app.numOfGrass = 0;
        app.soilArea = 0;
        app.setup();
        // reset all tile
        for (int i = 0; i < app.tile.length; i++) {
            for (int j = 0; j < app.tile[0].length; j++) {
                if (app.tile[i][j] != 1) {
                    app.tile[i][j] = 0;
                }
            }
        }
    }   
}
