package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Game.Ghost;
import Game.PacMan;
import Game.Position;

public class GhostTest {
	PacMan pacman;
	Ghost ghost;
	int unitSize;
	
	@Before
	public void setup() {
		unitSize = 16; // ennyi a játékban is
		pacman = new PacMan(6, 17, 16);
		ghost = new Ghost(6, 17, 16, "blinky.gif");
	}
	
	@Test
	public void ghostMoveTest() {
		ArrayList<Position> neighbours = new ArrayList<>();
		neighbours.add(new Position(5, 17));
		neighbours.add(new Position(7, 17));
		neighbours.add(new Position(5, 16));
		neighbours.add(new Position(5, 18));
		ghost.move(neighbours);
		
		// az új pozíciójának valamelyik szomszédos koordinátának kell lennie
		boolean result = neighbours.stream().anyMatch(p -> (p.getX() == ghost.getX() && p.getY() == ghost.getY()));
		
		assertEquals(true, result);
	}
	
	@Test
	public void collideWithTest() {
		ghost.CollideWith(pacman);
		// alapesetben pacman életet veszít
		assertEquals(2, pacman.getLives());
		
		// de ha a szellem rémült, pontot kap amiért megette
		ghost.setIsFrightened(true);
		ghost.CollideWith(pacman);
		assertEquals(15000, pacman.getScore());
	}	
}
