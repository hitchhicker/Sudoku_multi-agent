package model;

import java.util.ArrayList;
import java.util.Iterator;

public class SudokuData {
	private ArrayList<Integer> possibleValues;
	private int val;
	
	public ArrayList<Integer> getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(ArrayList<Integer> possibleValues) {
		this.possibleValues = possibleValues;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	
	public Iterator<String> iterator() {
		return null;
	}
}
