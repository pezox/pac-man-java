package Game;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {
	private final int WIDTH = 448;
	private final int HEIGHT = 576;
	private final int UNIT_SIZE = 16;
	private final int DELAY = 60; // 50
	
	private boolean keyHeld = false;
	private boolean running = false;
	private boolean gameWon;
	private Timer timer;
	private Image backgroundImage;
	
	private String playerName;
	ArrayList<Position> reachablePositions;
	ArrayList<Pellet> pellets;
	ArrayList<Ghost> ghosts;
	PacMan pacman;
	private Direction pacmanDirection;
	
	private long startTime;
	private long finishTime;
	
	public GamePanel(String playerName) {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new GameKeyAdapter());
		this.playerName = playerName;
		backgroundImage = new ImageIcon("maze.png").getImage();
		reachablePositions = new ArrayList<>();
		pellets = new ArrayList<>();
		ghosts = new ArrayList<>();
		loadReachablePositions();
		loadPellets();
		loadGhosts();
		pacman = new PacMan(14, 26, UNIT_SIZE); // 14 26
		pacmanDirection = Direction.RIGHT;
		startGame();
	}
	
	private void loadReachablePositions() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("mazedata.txt"));
			while (true) {
				String line = br.readLine();
				if (line == null) break;
				String[] coords = line.split(";");
				reachablePositions.add(new Position(
						Integer.parseInt(coords[0])*UNIT_SIZE, Integer.parseInt(coords[1])*UNIT_SIZE));
			}
			br.close();
		} catch (IOException e) {	
			e.printStackTrace();
		}	
	}
	
	private void loadPellets() {
		pellets.add(new PowerPellet(1 * UNIT_SIZE, 6 * UNIT_SIZE, UNIT_SIZE));
		pellets.add(new PowerPellet(26 * UNIT_SIZE, 6 * UNIT_SIZE, UNIT_SIZE));
		pellets.add(new PowerPellet(1 * UNIT_SIZE, 26 * UNIT_SIZE, UNIT_SIZE));
		pellets.add(new PowerPellet(26 * UNIT_SIZE, 26 * UNIT_SIZE, UNIT_SIZE));
		for (Position pos : reachablePositions) {
			if ((pos.getY() < 13 * UNIT_SIZE || pos.getY() > 22 * UNIT_SIZE) &&
					(pos.getX() != 1 * UNIT_SIZE || pos.getY() != 6 * UNIT_SIZE) &&
					(pos.getX() != 26 * UNIT_SIZE || pos.getY() != 6 * UNIT_SIZE) &&
					(pos.getX() != 1 * UNIT_SIZE || pos.getY() != 26 * UNIT_SIZE) &&
					(pos.getX() != 26 * UNIT_SIZE || pos.getY() != 26 * UNIT_SIZE)) {
				pellets.add(new SimplePellet(pos.getX(), pos.getY(), UNIT_SIZE));
			}
		}
		for (int y = 12; y < 23; y++) {
			pellets.add(new SimplePellet(6 * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE));
			pellets.add(new SimplePellet(21 * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE));
		}
	}
	
	private void loadGhosts() {
		ghosts.add(new Ghost(1, 4, UNIT_SIZE, "blinky.gif"));
		ghosts.add(new Ghost(26, 4, UNIT_SIZE, "inky.gif"));
		ghosts.add(new Ghost(1, 29, UNIT_SIZE, "pinky.gif"));
		ghosts.add(new Ghost(26, 29, UNIT_SIZE, "clyde.gif"));
	}

	private ArrayList<Position> getNeighbours(Position p) {
		ArrayList<Position> matches = new ArrayList<>();
		for (Position pos : reachablePositions) {
			if (pos.getX() == p.getX() && pos.getY() == p.getY() - UNIT_SIZE)
				matches.add(pos);
			if (pos.getX() == p.getX() && pos.getY() == p.getY() + UNIT_SIZE)
				matches.add(pos);
			if (pos.getX() == p.getX() - UNIT_SIZE && pos.getY() == p.getY())
				matches.add(pos);
			if (pos.getX() == p.getX() + UNIT_SIZE && pos.getY() == p.getY())
				matches.add(pos);
		}
		return matches;
	}
	
	private void startGame() {
		running = true;
		startTime = System.currentTimeMillis();
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	private void draw(Graphics g) {
		if (running) {
			// háttér
			g.drawImage(backgroundImage, 0, 0, null);
			// rács (tesztelni)
			for(int i=0;i<HEIGHT/UNIT_SIZE;i++) {
				//g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, HEIGHT);
				//g.drawLine(0, i*UNIT_SIZE, WIDTH, i*UNIT_SIZE);
			}
			// elérhetõ koordináták (tesztelni)
			for(Position p : reachablePositions) {
				//g.fillRect(p.getX(), p.getY(), UNIT_SIZE, UNIT_SIZE);
			}
			// pontok
			for (Pellet p : pellets) {
				p.draw(g);
			}
			// szellemek
			for (Ghost gh : ghosts) {
				gh.draw(g);
			}
			// pacman
			pacman.draw(g);
			// pont
			g.setColor(Color.white);
			g.setFont(new Font("Consolas",Font.BOLD, 35));
			FontMetrics scoreMetrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: " + pacman.getScore(), (WIDTH - scoreMetrics.stringWidth("SCORE: " + pacman.getScore()))/2, g.getFont().getSize());
			// életek
			g.setColor(Color.white);
			g.setFont(new Font("Consolas",Font.BOLD, 20));
			g.drawString("LIVES: " + pacman.getLives(), 10, HEIGHT - 10);
			// játékos neve
			FontMetrics nameMetrics = getFontMetrics(g.getFont());
			g.drawString("PLAYER: " + playerName, WIDTH - nameMetrics.stringWidth("PLAYER: " + playerName) - 10, HEIGHT - 10);
		} else {
			gameOver(g);
		}
	}
	
	private void handleMovement() {
		// pacman mozgása
		switch(pacmanDirection) {
		case UP:
			if (keyHeld && reachablePositions.stream().anyMatch(p -> 
				(p.getX() == pacman.getX() && p.getY() == pacman.getY() - UNIT_SIZE))) {
				pacman.move(pacmanDirection);
			}
			break;
		case DOWN:
			if (keyHeld && reachablePositions.stream().anyMatch(p ->
				(p.getX() == pacman.getX() && p.getY() == pacman.getY() + UNIT_SIZE))) {
				pacman.move(pacmanDirection);
			}
			break;
		case LEFT:
			if (keyHeld && (reachablePositions.stream().anyMatch(p ->
				(p.getX() == pacman.getX() - UNIT_SIZE && p.getY() == pacman.getY() ||
				pacman.getX() == 0 && pacman.getY() == 17 * UNIT_SIZE)))) {
				pacman.move(pacmanDirection);
			}
			break;
		case RIGHT:
			if (keyHeld && (reachablePositions.stream().anyMatch(p ->
				(p.getX() == pacman.getX() + UNIT_SIZE && p.getY() == pacman.getY() ||
				pacman.getX() == 27 * UNIT_SIZE && pacman.getY() == 17 * UNIT_SIZE)))) {
				pacman.move(pacmanDirection);
			}
			break;
		}
		// szellemek mozgása
		for (Ghost gh : ghosts) {
			gh.move(getNeighbours(new Position(gh.getX(), gh.getY())));
		}
	}
	
	private void checkCollisions() {
		// pacman ütközik egy ponttal
		int removePelIdx = -1;
		for (Pellet pel : pellets) {
			if (pel.getX() == pacman.getX() && pel.getY() == pacman.getY()) {
				removePelIdx = pellets.indexOf(pel);
				pel.CollideWith(pacman);
			}
		}
		if (removePelIdx != -1)
			pellets.remove(removePelIdx);
		// pacman ütközik egy szellemmel
		int removeGhostIdx = -1;
		for (Ghost gh : ghosts) {
			if (gh.getX() == pacman.getX() && gh.getY() == pacman.getY()) {
				if (gh.getIsFrightened()) {
					removeGhostIdx = ghosts.indexOf(gh);
				}
				gh.CollideWith(pacman);
			}
		}
		if (removeGhostIdx != -1) {
			ghosts.remove(removeGhostIdx);
			for (Ghost gh : ghosts) {
				gh.setIsFrightened(false);
			}
		}
	}
	
	private void checkGameOver() {
		if (pacman.getLives() == 0) {
			gameWon = false;
			running = false;
			timer.stop();
		} else if (pellets.size() == 0) {
			gameWon = true;
			finishTime = System.currentTimeMillis() - startTime;
			running = false;
			GameFrame.scoreData.addScore(playerName, (finishTime/1000)/60 + "." + (finishTime/1000)%60, pacman.getScore());
			timer.stop();
		}
	}
	
	private void gameOver(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.BOLD, 45));
		FontMetrics gameOverMetrics = getFontMetrics(g.getFont());
		if (gameWon) {
			g.drawString("Victory Royale!", (WIDTH - gameOverMetrics.stringWidth("Victory Royale!")) / 2, HEIGHT / 4);
			g.setFont(new Font("Consolas",Font.BOLD, 35));
			FontMetrics statMetrics = getFontMetrics(g.getFont());
			g.drawString("PLAYER: " + playerName, (WIDTH - statMetrics.stringWidth("PLAYER: " + playerName)) / 2, HEIGHT / 2);
			g.drawString("SCORE: " + pacman.getScore(), (WIDTH - statMetrics.stringWidth("SCORE: " + pacman.getScore())) / 2, HEIGHT / 2 + 55);
			g.drawString("TIME: " + (finishTime/1000)/60 + "." + (finishTime/1000)%60,
					(WIDTH - statMetrics.stringWidth("TIME: " + (finishTime/1000)/60 + "." + (finishTime/1000)%60)) / 2, HEIGHT / 2 + 110);
		} else {
			g.drawString("You Lose!", (WIDTH - gameOverMetrics.stringWidth("You Lose!")) / 2, HEIGHT / 2);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			handleMovement();
			checkCollisions();
			checkGameOver();
		}
		repaint();
	}
	
	public class GameKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			keyHeld = true;
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				pacmanDirection = Direction.LEFT;
				pacman.setAnimDirection(pacmanDirection);
				break;
			case KeyEvent.VK_RIGHT:
				pacmanDirection = Direction.RIGHT;
				pacman.setAnimDirection(pacmanDirection);
				break;
			case KeyEvent.VK_UP:
				pacmanDirection = Direction.UP;
				pacman.setAnimDirection(pacmanDirection);
				break;
			case KeyEvent.VK_DOWN:
				pacmanDirection = Direction.DOWN;
				pacman.setAnimDirection(pacmanDirection);
				break;
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			keyHeld = false;
		}
	}
}
