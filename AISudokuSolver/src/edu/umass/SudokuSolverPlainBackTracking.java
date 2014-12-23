package edu.umass;

import java.io.File;

/**Code for problem 2(Sudoku) of Homework Two in AI course
 * Sudoku Solver with plain back tracking
 * Assume that there will be only one unique solution
 * @author Liu Yang & Wei Hong
 * @mail lyang@cs.umass.edu & weihong@cs.umass.edu 
 */

public class SudokuSolverPlainBackTracking {
	
	static int [][] sudokuTable = new int [9][9]; // Store 81 numbers in sudokuTable
	
	static int numOfGuess = 0;
	
	static String inputDataFolder = "data/input/";
	
	static String outputDataFolder = "data/output/";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(File sFile : new File(inputDataFolder).listFiles()){
			numOfGuess = 0;
			String sudokuInputFile = inputDataFolder + sFile.getName();
			String sudokuOutputFile = outputDataFolder + sFile.getName() + ".res";
			SudokuComFunc.readSudokuTable(sudokuInputFile, sudokuTable);
			DFS(0,0);
			SudokuComFunc.printSudokuTable(sudokuOutputFile, sudokuTable);
			System.out.println( sFile.getName() + "\t" + numOfGuess);
		}
	}
	
	//Note to return boolean type to label whether find the right solution
	private static boolean DFS(int i, int j) {
		// TODO Auto-generated method stub
		if(i >= 9 || j >= 9) return true;
		if(sudokuTable[i][j] != 0){
			int newJ = j + 1;
			int newI;
			if(newJ >= 9){
				newI = i + 1;
				newJ -= 9;
			} else {
				newI = i;
			}
			//System.out.println("test: newI and newJ " + newI+ " " + newJ);
			return DFS(newI, newJ);
		} else {
			//from 1 to 9, try to fill numbers
			int oldV = sudokuTable[i][j];
			numOfGuess += 8;
			for(int k = 1; k <= 9; k++){
				sudokuTable[i][j] = k;
				if(SudokuComFunc.checkVaild(i, j, sudokuTable)){
					int newJ = j + 1;
					int newI;
					if(newJ >= 9){
						newI = i + 1;
						newJ -= 9;
					} else {
						newI = i;
					}
					//System.out.println("test: newI and newJ " + newI+ " " + newJ);
					if(DFS(newI, newJ))
						return true;
				} else {
					continue;
				}
			}
			//Until the last number 9, still can't find a number to fill in, do backtracking
			//System.out.println("backtracking!");
			sudokuTable[i][j] = oldV; //restore the old value which is actually 0
			return false;
		}
	}
}
