package edu.umass;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**Code for problem 2(Sudoku) of Homework Two in AI course
 * Assess the difficulty of different puzzles by the total
 * number of remaining values
 * Assume that there will be only one unique solution
 * @author Liu Yang & Wei Hong
 * @mail lyang@cs.umass.edu & weihong@cs.umass.edu 
 */

public class SudokuDifficultyAssess {
	
	static int [][] sudokuTable = new int [9][9]; // Store 81 numbers in sudokuTable
	
	static int numOfTotalRV = 0;
	
	static String inputDataFolder = "data/input/";
	
	static String outputDataFolder = "data/output/";
	
	static Map<String, String> RVMap = new HashMap<String, String>();// Store the remaining values of all empty squares
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(File sFile : new File(inputDataFolder).listFiles()){
			//File sFile = new File(inputDataFolder + "puz-001.txt");
			//System.out.println(sFile.getAbsolutePath());
			numOfTotalRV = 0;
			RVMap.clear();
			String sudokuInputFile = inputDataFolder + sFile.getName();
			SudokuComFunc.readSudokuTable(sudokuInputFile, sudokuTable);
			updateRVMap();
			System.out.println(sFile.getName() + "\t" + numOfTotalRV);
		}
	}
	
	//update RVMap according the current Sudoku table
	private static boolean updateRVMap() {
		// TODO Auto-generated method stub
		RVMap.clear();
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(sudokuTable[i][j] == 0){
					String RVString = "";
					for(int k = 1; k <=9; k++){
						sudokuTable[i][j] = k;
						if(SudokuComFunc.checkVaild(i,j, sudokuTable)){
							numOfTotalRV++;
							RVString = RVString + k + " ";
						}
					}
					sudokuTable[i][j] = 0;
					//update RVMap
					if(RVString == ""){//There is no remaining value for the empty square
						return false;
					} else {
						RVMap.put(i + " " + j, RVString);
					}
					
				}
			}
		}
		//System.out.println("Test: RVMap " + RVMap);
		return true;
	}
}
