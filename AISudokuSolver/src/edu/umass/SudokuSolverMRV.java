package edu.umass;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**Code for problem 2(Sudoku) of Homework Two in AI course
 * Sudoku Solver with MRV heuristics 
 * Assume that there will be only one unique solution
 * @author Liu Yang & Wei Hong
 * @mail lyang@cs.umass.edu & weihong@cs.umass.edu 
 */

public class SudokuSolverMRV {
	
	static int [][] sudokuTable = new int [9][9]; // Store 81 numbers in sudokuTable
	
	static int numOfGuess = 0;
	
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
			numOfGuess = 0;
			RVMap.clear();
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
		if(!updateRVMap()){
			return false;
		}
		if(RVMap.size() == 0) return true;
		TreeMap<String, String> sortedRVMap = SudokuComFunc.getSortedByValueTreeMap(RVMap);
		//System.out.println("Test: sortedRVMap " + sortedRVMap);
		String newIJString = "", newRVString = "";
		for(Map.Entry<String, String> entry : sortedRVMap.entrySet()){ // Get the square with minimum remaining values
			newIJString = entry.getKey();
			newRVString = entry.getValue();
			break;
		}
		
		//try to fill the remaining values
		String[] RVs = newRVString.split(" ");
		int newI = Integer.parseInt(newIJString.split(" ")[0]);
		int newJ = Integer.parseInt(newIJString.split(" ")[1]);
		//System.out.println("RVs.length " + RVs.length);
		numOfGuess += (RVs.length - 1);
		for(int k = 0; k < RVs.length; k++){
			//System.out.println("RVs[k] " + RVs[k]);
			//System.out.println("sudokuTable[newI][newJ] " + sudokuTable[newI][newJ]);
			sudokuTable[newI][newJ] = Integer.parseInt(RVs[k]);
			if(DFS(newI, newJ)){
				return true;
			}
		}
		//Backtracking
		sudokuTable[newI][newJ] = 0;
		return false;
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
						if(SudokuComFunc.checkVaild(i,j,sudokuTable)){
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
