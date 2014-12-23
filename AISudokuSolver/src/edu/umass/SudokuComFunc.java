package edu.umass;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**SudokuComFunc class
 * @author Liu Yang & Wei Hong
 * @mail lyang@cs.umass.edu & weihong@cs.umass.edu 
 */

public class SudokuComFunc {

	public static void readSudokuTable(String sudokuInputFile, int [][] sudokuTable) {
		// TODO Auto-generated method stub
		ArrayList<String> sudokuLines = new ArrayList<String>();
		FileUtil.readLines(sudokuInputFile, sudokuLines);
		for(int i = 0; i < 9; i++){
			String[] tokens = sudokuLines.get(i).split(" ");
			if(tokens.length != 9) {
				System.err.println("input error!");
				return;
			}
			for(int j = 0; j < 9; j++){
				if(tokens[j].equals("-")){
					sudokuTable[i][j] = 0;//For empty square, we put 0
				} else {
					sudokuTable[i][j] = Integer.parseInt(tokens[j]);
				}
				//System.out.print(sudokuTable[i][j] + " ");
			}
			//System.out.println();
		}
	}
	
	//check the validity of the Sudoku Table
	public static boolean checkVaild(int i, int j, int [][] sudokuTable) {
		// TODO Auto-generated method stub
		//check the same row
		for(int k = 0; k < 9; k++){
			if(k != j && sudokuTable[i][k] == sudokuTable[i][j]){
				return false;
			}
		}
		
		//check the same column
		for(int k = 0; k < 9; k++){
			if(k != i && sudokuTable[k][j] == sudokuTable[i][j]){
				return false;
			}
		}
		
		//check the same box
		for(int ii = (i/3) * 3; ii <= (i/3) * 3 + 2; ii++){
			for(int jj = (j/3) * 3; jj <= (j/3) * 3 + 2; jj++){
				if(ii != i && jj != j && sudokuTable[ii][jj] == sudokuTable[i][j]){
					return false;
				}
			}
		}
		return true;
	}
	
	public static TreeMap<String, String> getSortedByValueTreeMap(
			Map<String, String> hashMap) {
		// TODO Auto-generated method stub
		ValueComparator bvc = new ValueComparator(hashMap);
	    TreeMap<String, String> sortedTMap = new TreeMap<String, String>(bvc);
	    sortedTMap.putAll(hashMap);
		return sortedTMap;
	}
	
	public static void printSudokuTable(String sudokuOutputFile, int [][] sudokuTable) {
		// TODO Auto-generated method stub
		ArrayList<String> outLines = new ArrayList<String>();
		for(int i = 0; i < 9; i++){
			String line = "";
			for(int j = 0; j < 9; j++){
				line += sudokuTable[i][j] + " ";
			}
			outLines.add(line);
		}
		FileUtil.writeLines(sudokuOutputFile, outLines);
	}
}
