package lawnlayer;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import lawnlayer.UpdateTiles;
import lawnlayer.character.player.Player;

public class UpdateTileTest {
    
    @Test
    public void testFillGrass() {
        Player player = new Player(3);
        int[][] tile = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 4, 4, 4, 0, 0, 0, 0, 0, 1},
                        {1, 4, 4, 4, 0, 0, 0, 0, 0, 1},
                        {1, 4, 4, 4, 4, 4, 4, 4, 4, 1},
                        {1, 4, 4, 4, 0, 0, 2, 0, 0, 1},
                        {1, 4, 4, 4, 0, 0, 3, 0, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    };
        int numOfGrass = 20;
        ArrayList<int[]> cycle = new ArrayList<int[]>();
        int[][] vv = {{4,6}, {5,6}, {6,6}, {6,7}, {6,8}, {6,9}, {5,9}, {4,9}, {3,9}, {3,8}, {3,7}, {3,6}};
        for (int[] v : vv) {
            cycle.add(v);
        }
        boolean inside = true;
        // UpdateTiles.fillGrass(player, tile, numOfGrass, cycle, inside);



    }
}
