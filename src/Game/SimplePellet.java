package Game;
import java.awt.Color;
import java.awt.Graphics;

public class SimplePellet extends Pellet{
	public SimplePellet(int x, int y, int unitSize) {
		this.unitSize = unitSize;
		X = x;
		Y = y;
		points = 100;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(X + ((unitSize-4)/2), Y + ((unitSize-4)/2), 4, 4);
	}

	@Override
	public void CollideWith(PacMan pacman) {
		pacman.addScore(points);
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
