package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;


public class Sudoku {
	private final static String PATH = "src/input.txt";
	public SudokuData[][] sudoku; 
	
	private final static Sudoku INSTANCE = new Sudoku();
	public static Sudoku getInstance() {
	        return INSTANCE;
	}

	private Sudoku() {
		/** INITIALISATION **/
		sudoku = new SudokuData[9][9];
		for (int i = 0; i < sudoku.length; i++) {
			for (int j = 0; j < sudoku[i].length; j++) {
				sudoku[i][j] = new SudokuData();
			}
		}
		/**
		 * Read input file and stock in sudoku of type SudokuData
		 */
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(PATH));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] strArray = sCurrentLine.split(" ");
				for(int j = 0; j < strArray.length; j++) {
					sudoku[i][j].setVal(Integer.parseInt(strArray[j]));
					sudoku[i][j].setPossibleValues(new ArrayList<Integer>());
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SudokuData[][] getSudoku() {
		return this.sudoku;
	}
	
	public void setSudoku(SudokuData[][] s) {
		this.sudoku = s;
	}
	
	public void displaySudoku() {
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				System.out.printf(sudoku[i][j].getVal() + " ");
				if(j==8){
					System.out.println("");
				}
			}
		}
	}
	
	/** Get sudoku row copy*/
	public SudokuData[] getRowData(int rowIndex) {
		SudokuData[] rowData = new SudokuData[9];
		rowData = sudoku[rowIndex];
		return rowData;
	}

	/** Get sudoku column copy*/
	public SudokuData[] getColumnData(int colIndex) {
		SudokuData[] colData = new SudokuData[9];
		int i = 0;
		for(i = 0; i< sudoku.length; i++) {
			colData[i] = sudoku[i][colIndex];
		}
		return colData;
	}
	
	/** Get sudoku square copy*/
	public SudokuData[] getSquareData(int squareIndex){
		int i = squareIndex / 3;
		int j = squareIndex % 3;
		SudokuData[] squareData = new SudokuData[9];
		int k = 0;
		for(int m = 3 * i; m <= 3 * i + 2; m++){
			for(int n = 3 * j; n <= 3 * j + 2; n++)
				squareData[k++] = sudoku[m][n];
		}
		return squareData;
	}
	
	/**
	 * Stock sudoku column data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param col: column number
	 */
	private void setRowSudoku(SudokuData[] sd, int rowIndex){
		for(int i = 0; i < sd.length; i++){
			if (sudoku[rowIndex][i].getVal() == 0){
				sudoku[rowIndex] = sd;
			}
		}
	}
	
	/**
	 * Stock sudoku column data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param col: column number
	 */
	private void setColSudoku(SudokuData[] sd, int col){
		for(int i = 0; i < sd.length; i++){
			if(sudoku[i][col].getVal() == 0){
				sudoku[i][col] = sd[i];
			}
		}
	}
	
	/**
	 * Stock sudoku square data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param squreNum: square number
	 * squreNum example:
	 * 0 1 2
	 * 3 4 5
	 * 6 7 8
	 */
	private void setSquSudoku(SudokuData[] sd, int squreNum){
		int i = squreNum / 3;
		int j = squreNum % 3;
		int k = 0;
		for(int m = 3 * i; m <= 3 * i + 2; m++){
			for(int n = 3 * j; n <= 3 * j + 2; n++)
			{
				if (sudoku[m][n].getVal() == 0) 
					sudoku[m][n] = sd[k];
				k++;
			}
		}
	}
	/**
	 * Stock sudoku column data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param col: column number
	 */
	public void setRowData(SudokuData[] sd, int rowIndex) {
		for(int i = 0; i < sd.length; i++) {
			if(sudoku[rowIndex][i].getVal() != 0) continue;
			if(sudoku[rowIndex][i].getPossibleValues().isEmpty()){
				sudoku[rowIndex][i].setPossibleValues(sd[i].getPossibleValues());
			} else {
				sd[i].getPossibleValues().retainAll(sudoku[rowIndex][i].getPossibleValues());
			}
		}
		useAlgos(sd);
		setRowSudoku(sd, rowIndex);
	}
	
	/**
	 * Stock sudoku column data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param col: column number
	 */
	public void setColumnData(SudokuData[] sd, int colIndex) {
		for(int j = 0; j < sd.length; j++){
			if(sudoku[j][colIndex].getVal() != 0) continue;
			if(sudoku[j][colIndex].getPossibleValues().isEmpty()){
				sudoku[j][colIndex].setPossibleValues(sd[j].getPossibleValues());
			} else {
				sd[j].getPossibleValues().retainAll(sudoku[j][colIndex].getPossibleValues());
			}
		}
		useAlgos(sd);
		setColSudoku(sd, colIndex);
	}
	
	/**
	 * Stock sudoku square data returned by analyse agent
	 * @param sd: sudokuData array
	 * @param squreNum: square number
	 * squreNum example:
	 * 0 1 2
	 * 3 4 5
	 * 6 7 8
	 */
	public void setSquareData(SudokuData[] sd, int squreNum){
//		
		int i = squreNum / 3;
		int j = squreNum % 3;
		int k = 0;
		
		for(int m = 3 * i; m <= 3 * i + 2; m++){
			for(int n = 3 * j; n <= 3 * j + 2; n++){
				if(sudoku[m][n].getVal() != 0) {
					k++;
					continue;
				}
				if(sudoku[m][n].getPossibleValues().isEmpty()){
					sudoku[m][n].setPossibleValues(sd[k++].getPossibleValues());
				} else {
					sd[k++].getPossibleValues().retainAll(sudoku[m][n].getPossibleValues());
				}
			}
		}
		useAlgos(sd);
		setSquSudoku(sd, squreNum);
	}
	/**
	 * Verify if the sudoku is resolved.
	 * @return boolean
	 */
	public boolean sudokuResolved() {
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(sudoku[i][j].getVal() == 0) {
					return false;
				}
			}
		}
		return true;	
	}
	
	/**
	 * Remove the given possibilities
	 * @param sd: sudokuData array
	 * @param toRemoves: list of possibility to remove 
	 * @param exception: list of cell index which will ignore these removes
	 */
	public void removePossibilities(SudokuData[] sd, HashSet<Integer> toRemoves, HashSet<Integer> exception){
		for(int i = 0; i < sd.length; i++){
			if(exception != null){
				if(exception.contains(i)) continue; // ignore if this index is in the exception
			}
			if(sd[i].getVal() != 0) continue; // ignore if this index has already a certain value

			for(int toRemove : toRemoves){
				for(int j = sd[i].getPossibleValues().size() - 1; j >= 0;  j--){
					if(sd[i].getPossibleValues().get(j) == toRemove){
						sd[i].getPossibleValues().remove(j);
					}
				}
			}
		}
	}

	/**
	 * Generate the reverse table whose key is possibility while value is list a cell index.
	 * @param sd: sudokuData array
	 * @return reverse table of type HashMap
	 */
	public HashMap<Integer, ArrayList<Integer>> generatePossibilitiesRevers(SudokuData[] sd){
		HashMap<Integer, ArrayList<Integer>> possibilitReverse = new HashMap<Integer, ArrayList<Integer>>();
		for(int i = 0; i < sd.length; i++){
			for(Iterator<Integer> items = sd[i].getPossibleValues().iterator(); items.hasNext();){
				Integer currentItem = (Integer) items.next();
				if(possibilitReverse.containsKey(currentItem)){
					possibilitReverse.get(currentItem).add(i);
				}else{
					ArrayList<Integer> arrayIndex = new ArrayList<Integer>();
					arrayIndex.add(i);
					possibilitReverse.put(currentItem, arrayIndex);
				}
			}
		}
		return possibilitReverse;
	}

	/**
	 * 1er algorithm
	 * Lorsqu'une cellule n'a plus qu'une valeur possible, celle-ci en devient son contenu et la liste des possibles est vidée.
	 * @param cell: sudokuData
	 * @param sd: sudokuData array
	 */
	public void algo1(SudokuData[] sd){
		HashSet<Integer> toRemoveSet = new HashSet<Integer>();
		for(int i = 0; i < sd.length; i++){
			if(sd[i].getVal() == 0){
				if(sd[i].getPossibleValues().size() == 1){
					int onlyPossible = sd[i].getPossibleValues().get(0);
					toRemoveSet.add(onlyPossible);
					// clear all the possibility and set cell value by its last possibility
					sd[i].getPossibleValues().clear();
					sd[i].setVal(onlyPossible);
				}	
			}
			
		}
		// for debug
//		if(!toRemoveSet.isEmpty()){
//			System.out.println("algo1: " + toRemoveSet);	
//		}
		
		removePossibilities(sd, toRemoveSet, null);
	}
	
	/**
	 * 3rd algorithm
	 * Une valeur ne se trouvant que dans une seule liste de possibles est la valeur de cette cellule.
	 * @param sd: sudokuData array
	 * @param possibilitReverse: Reverse table whose key is possibility while value is list a cell index.
	 */
	public void algo3(SudokuData[] sd, HashMap<Integer, ArrayList<Integer>> possibilitReverse){
		
		HashSet<Integer> toRemoveSet = new HashSet<Integer>();
		for (Entry<Integer, ArrayList<Integer>> entry : possibilitReverse.entrySet()) {
			if(entry.getValue().size() == 1){ // if any possibility exists only in one cell 
				int i = entry.getValue().get(0); // the value of cell i can be determined
				int onlyPossible = entry.getKey();
				sd[i].setVal(onlyPossible);
				sd[i].getPossibleValues().clear();
				toRemoveSet.add(onlyPossible);
			}
		}
		// for debug
//		if(!toRemoveSet.isEmpty()){
//			System.out.println("algo3: " + toRemoveSet);	
//		}
		
		removePossibilities(sd, toRemoveSet, null);
	}

	/**
	 * 4th algorithm
	 * Si seulement deux cellules contiennent les deux mêmes valeurs possibles alors les possibles des autres cellules ne peuvent contenir ces valeurs.
	 * @param sd: sudokuData array
	 */
	public void algo4(SudokuData[] sd){
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		ArrayList<Integer> hasTwoPossibilitiesList = new ArrayList<Integer>(); // A list contain all cellules that has a two possibile values
		HashSet<Integer> toRemoveSet = new HashSet<Integer>();
		HashSet<Integer> exceptionSet = new HashSet<Integer>();

		for (int i = 0; i < sd.length; i++){
			if(sd[i].getVal() == 0){
				int count = 0;
				for(int possible : sd[i].getPossibleValues()) {
					// stock in HashMap for we could compare later
					// if cell 1 possible list is [3, 4] we have map[1] = "34"
					if(!map.containsKey(i)){
						map.put(i, "" + possible);
					}else{
						map.put(i, map.get(i) + possible);
					}
					count++;
					// if length is more than 2, break the loop
					if(count > 2) break;
				}
				
				if(count == 2){
					// hasTwoPossibilitiesList contains all cell number having exactely 2 possibilities
					hasTwoPossibilitiesList.add(i);
				}
			}
		}
		
		if(hasTwoPossibilitiesList.size() >= 2){
			for(int i = 0; i < hasTwoPossibilitiesList.size(); i++){
				for(int j = i + 1; j < hasTwoPossibilitiesList.size(); j++){
					// Problem: it won't work if twos pairs of cell satisfy the condition
					if(map.get(hasTwoPossibilitiesList.get(i)).equals(map.get(hasTwoPossibilitiesList.get(j)))){
						toRemoveSet.addAll(sd[hasTwoPossibilitiesList.get(i)].getPossibleValues());
						exceptionSet.add(hasTwoPossibilitiesList.get(i));
						exceptionSet.add(hasTwoPossibilitiesList.get(j));
					}
				}
			} 
		}
		// for debug
//		if(!toRemoveSet.isEmpty()){
//			System.out.println("algo4: " + toRemoveSet);	
//		}
		
		removePossibilities(sd, toRemoveSet, exceptionSet);
	}
	
	public void useAlgos(SudokuData[] sd){
		algo1(sd);
		algo4(sd);
		HashMap<Integer, ArrayList<Integer>> possibilitReverse = generatePossibilitiesRevers(sd);
		algo3(sd, possibilitReverse);
	}
	
	/**
	 * Verify if the final result of sudoku is OK
	 * @return boolean
	 */
	public boolean verify(){
		boolean isValid;
		for(int i = 0; i < 9; i++){
			 isValid = isEveryCellUniqe(getRowData(i)) && isEveryCellUniqe(getColumnData(i)) && isEveryCellUniqe(getSquareData(i));
			 if (isValid == false){
				 return false;
			 }
		}
		return true;
	}
	
	public boolean isEveryCellUniqe(SudokuData[] sd){
		HashMap<Integer, Integer> mapIndexValue = new HashMap<Integer, Integer>();
		for(SudokuData s: sd){
			if(mapIndexValue.get(s) != null){
				return false;
			} else {
				mapIndexValue.put(s.getVal(), 1); // the value is not important
			}
		}
		return true;
	}
}
	
