package Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Game.Direction;
import Game.PacMan;

public class PacManTest {
	PacMan pacman;
	int unitSize;
	
	@Before
	public void setup() {
		unitSize = 16; // ennyi a játékban is
		// x=6, y=17 kezdõpozícióból léphet mind a négy irányba
		pacman = new PacMan(6, 17, 16);
	}
	
	@Test
	public void pacManMoveTest() {
		pacman.move(Direction.UP);
		
		assertEquals(6*unitSize, pacman.getX());
		assertEquals(16*unitSize, pacman.getY());
		
		pacman.move(Direction.DOWN);
		
		assertEquals(6*unitSize, pacman.getX());
		assertEquals(17*unitSize, pacman.getY());
		
		pacman.move(Direction.LEFT);
		
		assertEquals(5*unitSize, pacman.getX());
		assertEquals(17*unitSize, pacman.getY());
		
		pacman.move(Direction.RIGHT);
		
		assertEquals(6*unitSize, pacman.getX());
		assertEquals(17*unitSize, pacman.getY());
	}
}
