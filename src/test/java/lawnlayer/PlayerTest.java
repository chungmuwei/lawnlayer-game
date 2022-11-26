package lawnlayer;


import lawnlayer.character.player.Player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void createPlayer() {
        Player p = new Player(3);
        assertEquals(0, p.getRow());
        assertEquals(0, p.getCol());
        assertEquals(0, p.getX());
        assertEquals(80, p.getY());
        assertEquals(3, p.getRemainingLives());
    }

}
