package ScoreSystem;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ScoreData extends AbstractTableModel {
	public ArrayList<Score> scores = new ArrayList<>();
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return scores.size();
	}

	@Override
	public Object getValueAt(int rowIdx, int colIdx) {
		Score match = scores.get(rowIdx);
		switch (colIdx) {
		case 0: return match.getPlayerName();
		case 1: return match.getTime();
		default: return match.getScore();
		}
	}
	
	@Override
    public String getColumnName(int idx) {
		switch (idx) {
		case 0: return "Player Name";
		case 1: return "Time";
		case 2: return "Score";
		default: return null;
		}
    }
	
	public void addScore(String playerName, String time, int score) {
		scores.add(new Score(playerName, time, score));
	}
}
