package Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ScoreSystem.ScoreData;

public class ScoreDataTests {
	ScoreData data;
	
	@Before
	public void setup() {
		data = new ScoreData();
		data.addScore("Joska", "1", 1000);
		data.addScore("Pista", "2", 2000);
		data.addScore("Bela", "3", 3000);
	}
	
	@Test
	public void getRowCountTest() {
		assertEquals(3, data.getRowCount());
	}
	
	@Test
	public void getValueAtTest() {
		assertEquals("Pista", data.getValueAt(1, 0));
		assertEquals(3000, data.getValueAt(2, 2));
	}
	
	@Test
	public void getColumnNameTest() {
		assertEquals("Time", data.getColumnName(1));
	}
	
	@Test
	public void getColumnCountTest() {
		assertEquals(3, data.getColumnCount());
	}
}
