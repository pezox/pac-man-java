package Game;
import java.awt.Color;
import java.awt.Graphics;

public class PowerPellet extends Pellet{
	public PowerPellet(int x, int y, int unitSize) {
		this.unitSize = unitSize;
		X = x;
		Y = y;
		points = 5000;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillOval(X, Y, 16, 16);
	}

	@Override
	public void CollideWith(PacMan pacman) {
		pacman.addScore(points);
		// a szellemek megrémülnek
		for (Ghost gh : GameFrame.gamePanel.ghosts) {
			gh.setIsFrightened(true);
		}
	}

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getY() {
		return Y;
	}
}
