package Game;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;

import ScoreSystem.Score;
import ScoreSystem.ScoreData;


@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	static GamePanel gamePanel;
	static ScoreData scoreData;
	
	public GameFrame() {
		loadScoreData();
		
		String playerName = JOptionPane.showInputDialog(this,
                "What is your nickname?", "Insert nickname", JOptionPane.QUESTION_MESSAGE);
		if (playerName == null || playerName.isEmpty())
			playerName = "Player";
		
		gamePanel = new GamePanel(playerName);
		this.add(gamePanel);
		this.setTitle("Pac-Man");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		JMenuBar menubar = new JMenuBar();
		JMenu scoreMenu = new JMenu("Scoreboard");
		JMenuItem scoreBoard = new JMenuItem("Show scoreboard");
		menubar.add(scoreMenu);
		scoreMenu.add(scoreBoard);
		this.setJMenuBar(menubar);
		
		Image icon = new ImageIcon("pacmanicon.png").getImage();
		this.setIconImage(icon);
		
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		JTable scoreTable = new JTable(scoreData);
		scoreTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(scoreTable);
		
		scoreBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              JOptionPane.showMessageDialog(null, scrollPane, "Scoreboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("scores.dat"));
                    oos.writeObject(scoreData.scores);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
	}
	
	@SuppressWarnings("unchecked")
	private void loadScoreData() {
		try {
			scoreData = new ScoreData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("scores.dat"));
            scoreData.scores = (ArrayList<Score>)ois.readObject();
            ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}