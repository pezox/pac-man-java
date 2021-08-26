package Game;
import java.awt.*;
import javax.swing.*;

public class PacMan implements Drawable{
	private int X;
	private int Y;
	private int unitSize;
	private int score;
	private int lives;
	
	private Image pacmanUp;
	private Image pacmanDown;
	private Image pacmanRight;
	private Image pacmanLeft;
	private Image pacmanImage;
	
	public PacMan(int x, int y, int unitSize) {
		this.unitSize = unitSize;
		score = 0;
		lives = 3;
		X = x * unitSize;
		Y = y * unitSize;
		pacmanUp = new ImageIcon("pacmanU.gif").getImage().getScaledInstance(24, 24, 1);
		pacmanDown = new ImageIcon("pacmanD.gif").getImage().getScaledInstance(24, 24, 1);
		pacmanRight = new ImageIcon("pacmanR.gif").getImage().getScaledInstance(24, 24, 1);
		pacmanLeft = new ImageIcon("pacmanL.gif").getImage().getScaledInstance(24, 24, 1);
		pacmanImage = pacmanRight;
	}
	
	public void move(Direction d) {
		switch(d) {
		case UP:
			Y -= unitSize;
			break;
		case DOWN:
			Y += unitSize;
			break;
		case LEFT:
			if (X == 0 && Y == 17 * unitSize) {
				X = 27 * unitSize;
				Y = 17 * unitSize;
			} else {
				X -= unitSize;
			}
			break;
		case RIGHT:
			if (X == 27 * unitSize && Y == 17 * unitSize) {
				X = 0;
				Y = 17 * unitSize;
			} else {
				X += unitSize;
			}
			break;
		}
	}
	
	public void setAnimDirection(Direction d) {
		switch(d) {
		case UP:
			pacmanImage = pacmanUp;
			break;
		case DOWN:
			pacmanImage = pacmanDown;
			break;
		case LEFT:
			pacmanImage = pacmanLeft;
			break;
		case RIGHT:
			pacmanImage = pacmanRight;
			break;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(pacmanImage, X - ((24-unitSize)/2), Y - ((24-unitSize)/2), null);
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int points) {
		score += points;
	}
	
	public void decreaseLives() {
		lives--;
	}
	
	public int getLives() {
		return lives;
	}
}
