package nl.tudelft.jpacman.level;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Graphics;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Test suite to confirm that {@link Level#respawn(Player)} correctly places
 * a player back on a start square.
 *
 * @author Jeroen Roosen
 */
class LevelTest {

    /**
     * The level under test.
     */
    private Level level;

    /**
     * The single start square used by the level under test.
     */
    private Square startSquare;

    /**
     * The player under test.
     */
    private Player player;

    /**
     * Builds a minimal 1x1 board with a single ground square as the only
     * start square, and a level with no ghosts and a no-op collision map,
     * since collision behaviour is not what's under test here.
     */
    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        BoardFactory boardFactory = new BoardFactory(sprites);

        startSquare = boardFactory.createGround();
        Square[][] grid = {{startSquare}};
        Board board = boardFactory.createBoard(grid);

        List<Square> startSquares = Collections.singletonList(startSquare);
        CollisionMap collisions = (mover, collidedOn) -> { };

        level = new Level(board, Collections.emptyList(), startSquares, collisions);

        Map<Direction, Sprite> spriteMap = new HashMap<>();
        for (Direction direction : Direction.values()) {
            spriteMap.put(direction, new StubSprite());
        }
        AnimatedSprite deathAnimation = new AnimatedSprite(
            new Sprite[] {new StubSprite()}, 100, false);
        player = new Player(spriteMap, deathAnimation);
    }

    /**
     * After respawning, the player should occupy a start square and be
     * alive again.
     */
    @Test
    void respawnPlacesPlayerOnAStartSquare() {
        player.setAlive(false);

        level.respawn(player);

        assertThat(player.getSquare()).isEqualTo(startSquare);
        assertThat(player.isAlive()).isTrue();
    }

    /**
     * Minimal no-op sprite used purely to satisfy constructor requirements.
     */
    private static final class StubSprite implements Sprite {
        @Override
        public void draw(Graphics graphics, int x, int y, int width, int height) {
            // no-op
        }

        @Override
        public Sprite split(int x, int y, int width, int height) {
            return this;
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }
    }
}