package nl.tudelft.jpacman.level;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.sprite.AnimatedSprite;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Test suite to confirm that extra-life behaviour on {@link Player} works
 * as intended: a new player starts with 3 lives, losing a life decrements
 * the counter but keeps the player alive as long as lives remain, and the
 * player only actually dies once lives reach 0.
 *
 * @author Jeroen Roosen
 */
class PlayerTest {

    /**
     * The number of lives a new player starts with.
     */
    private static final int STARTING_LIVES = 3;

    /**
     * Animation delay for the stub death sprite, in milliseconds.
     */
    private static final int DEATH_ANIMATION_DELAY = 100;


    /**
     * The player under test.
     */
    private Player player;

    /**
     * Creates a fresh player before each test, using minimal stub sprites
     * since the visual rendering itself is not under test here.
     */
    @BeforeEach
    void setUp() {
        Map<Direction, Sprite> spriteMap = new HashMap<>();
        for (Direction direction : Direction.values()) {
            spriteMap.put(direction, new StubSprite());
        }
        AnimatedSprite deathAnimation = new AnimatedSprite(
            new Sprite[] {new StubSprite()}, DEATH_ANIMATION_DELAY, false);

        player = new Player(spriteMap, deathAnimation);
    }

    /**
     * A new player should start with exactly 3 lives.
     */
    @Test
    void newPlayerHasThreeLives() {
        assertThat(player.getLives()).isEqualTo(STARTING_LIVES);
    }

    /**
     * Losing a life decrements the counter but the player stays alive
     * as long as lives remain.
     */
    @Test
    void losingALifeDecrementsButKeepsPlayerAlive() {
        player.loseLife();

        assertThat(player.getLives()).isEqualTo(2);
        assertThat(player.isAlive()).isTrue();
    }

    /**
     * The player only actually dies once lives reach 0, not before.
     */
    @Test
    void playerOnlyDiesWhenLivesReachZero() {
        player.loseLife();
        player.loseLife();
        assertThat(player.isAlive()).isTrue();

        player.loseLife();
        assertThat(player.getLives()).isEqualTo(0);
        assertThat(player.isAlive()).isFalse();
    }

    /**
     * Minimal no-op sprite used purely to satisfy constructor requirements;
     * its visual behaviour is irrelevant to these tests.
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
