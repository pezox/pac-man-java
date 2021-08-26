package ScoreSystem;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Score implements Serializable {
	private String playerName;
	private String time;
	private int score;
	
	public Score (String playerName, String time, int score) {
		this.playerName = playerName;
		this.time = time;
		this.score = score;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public String getTime() {
		return time;
	}
	
	public int getScore() {
		return score;
	}
}
