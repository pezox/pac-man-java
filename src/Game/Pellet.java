package Game;

public abstract class Pellet implements Drawable{
	protected int X;
	protected int Y;
	protected int unitSize;
	protected int points;
	
	public abstract void CollideWith(PacMan pacman);
	
	public abstract int getX();
	
	public abstract int getY();
}
