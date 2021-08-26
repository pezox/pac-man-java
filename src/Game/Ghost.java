package Game;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

public class Ghost implements Drawable{
	private int X;
	private int Y;
	private ArrayList<Position> prevPositions;
	private int unitSize;
	private Image actualImage;
	private Image ghostImage;
	private Image frightenedImage;
	private boolean isFrightened;
	private int points;
	Random random;
	
	public Ghost(int x, int y, int unitSize, String resourceName) {
		this.unitSize = unitSize;
		X = x * unitSize;
		Y = y * unitSize;
		prevPositions = new ArrayList<>();
		ghostImage = new ImageIcon(resourceName).getImage().getScaledInstance(24, 24, 1);
		frightenedImage = new ImageIcon("frightened.gif").getImage().getScaledInstance(24, 24, 1);
		actualImage = ghostImage;
		isFrightened = false;
		points = 15000;
		random = new Random();
	}
	
	public void CollideWith(PacMan pacman) {
		if (isFrightened) {
			pacman.addScore(points);
		} else {
			pacman.decreaseLives();
		}
	}
	
	public void move(ArrayList<Position> neighbours) {
		// a szomszédos koordináták közül mehet egyre random, elõzõ 5-re nem mehet azt is el kell menteni
		if (X == 0 && Y == 17 * unitSize)
			neighbours.add(new Position(27 * unitSize, 17 * unitSize));
		if (X == 27 * unitSize && Y == 17 * unitSize)
			neighbours.add(new Position(0, 17 * unitSize));
		if (prevPositions.size() == 5) {
			prevPositions.remove(0);
		}
		prevPositions.add(new Position(X, Y));
		int randPosIdx = random.nextInt(neighbours.size());
		boolean validIdx = false;
		boolean isMatch;
		while (!validIdx) {
			isMatch = false;
			for (int i = 0; i < prevPositions.size(); i++) {
				if (prevPositions.get(i).getX() == neighbours.get(randPosIdx).getX() && prevPositions.get(i).getY() == neighbours.get(randPosIdx).getY()) {
					isMatch = true;
				}
			}
			if (isMatch) {
				randPosIdx = random.nextInt(neighbours.size());
			} else {
				validIdx = true;
			}
		}
		if (validIdx) {
			X = neighbours.get(randPosIdx).getX();
			Y = neighbours.get(randPosIdx).getY();
		}
	}
	
	public void setIsFrightened(boolean value) {
		if (value) {
			isFrightened = true;
			actualImage = frightenedImage;
		} else {
			isFrightened = false;
			actualImage = ghostImage;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(actualImage, X - ((24-unitSize)/2), Y - ((24-unitSize)/2), null);
	}
	
	public boolean getIsFrightened() {
		return isFrightened;
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
}
