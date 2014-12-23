package edu.umass;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**Code for problem 2(Sudoku) of Homework Two in AI course
 * Sudoku Solver with shared subgroup inference method
 * Assume that there will be only one unique solution
 * @author Liu Yang & Wei Hong
 * @mail lyang@cs.umass.edu & weihong@cs.umass.edu 
 */

public class SudokuSolverSharedSubgroup {

	static int [][] sudokuTable = new int [9][9]; // Store 81 numbers in sudokuTable
	
	static int numOfGuess = 0;
	
	static String inputDataFolder = "data/input/";
	
	static String outputDataFolder = "data/output/";
	
	static Map<String, String> RVMap = new HashMap<String, String>();// Store the remaining values of all empty squares
	
	static boolean foundInRowCOLOther = false;
	
	static boolean foundInBoxOther = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(File sFile : new File(inputDataFolder).listFiles()){
			//File sFile = new File(inputDataFolder + "puz-026.txt");
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
		if(!updateRVMapApplyInference()){
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
	
	//update RVMap according the current Sudoku table and remove 
	//candidate value if there is conflicts
	//Afterwards, apply shared subgroup rule to further remove
	//some remaining values
	private static boolean updateRVMapApplyInference() {
		// TODO Auto-generated method stub
		RVMap.clear();
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(sudokuTable[i][j] == 0){
					String RVString = "";
					for(int k = 1; k <=9; k++){
						sudokuTable[i][j] = k;
						if(SudokuComFunc.checkVaild(i,j, sudokuTable)){
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
		//Apply shared subgroup rule
		Set<String> RVSinShareGroup = new HashSet<String>();
		Map<String, String> RVMapCopy = new HashMap<String, String>(RVMap);
		if(!checkRowShareGroup(RVSinShareGroup)){
			RVMap = RVMapCopy;
		}
		if(!checkColShareGroup(RVSinShareGroup)){
			RVMap = RVMapCopy;
		}
		
		return true;
	}
	
	private static boolean checkColShareGroup(Set<String> RVSinShareGroup) {
		//Convert to RVMap and sudokuTable
		//int vars[];
		//boolean varDomian[];
		//int rv[];
		
		
		
		// TODO Auto-generated method stub
				for(int j = 0; j < 9; j++){
					for(int subGroupID = 0; subGroupID < 3; subGroupID ++){
						RVSinShareGroup.clear();
						for(int i = subGroupID * 3; i < subGroupID * 3 + 3; i++){
							if(RVMap.containsKey(i + " " + j)){
								String [] IJRVs = RVMap.get(i + " " + j).split(" ");
								for(String rv : IJRVs){
									RVSinShareGroup.add(rv);
								}
							}
						}
						if(!RVSinShareGroup.isEmpty()){
							for(String rv : RVSinShareGroup){
								//System.out.println("rv in share subgroup: " + rv);
								foundInRowCOLOther = false;
								foundInBoxOther = false;
								//System.out.println(foundInRowCOLOther + " " + foundInBoxOther);
								checkRVLocation(rv, j, subGroupID, "col");
								if(foundInRowCOLOther && !foundInBoxOther){
									//remove all rv in rowColOther
									for(int ii = 0; ii < subGroupID * 3; ii++){
										if(RVMap.containsKey(ii + " " + j)){
											String newRVs = RVMap.get(ii + " " + j).replace(rv + " ", "");
											if(newRVs.equals("")){
												return false;
											} else {
												RVMap.put(ii + " " + j, newRVs);
											}
											//System.out.println("test0 hehe");
										}
									}
									
									for(int ii = subGroupID * 3 + 3; ii < 9; ii++){
										if(RVMap.containsKey(ii + " " + j)){
											String newRVs = RVMap.get(ii + " " + j).replace(rv + " ", "");
											if(newRVs.equals("")){
												return false;
											} else {
												RVMap.put(ii + " " + j, newRVs);
											}
											//System.out.println("test0 hehe");
										}
									}
									
								} else if(!foundInRowCOLOther && foundInBoxOther){
									//remove all rv in boxOther
									int rowStart = subGroupID * 3; 
									int colStart = (j / 3) * 3;
									for(int bi = rowStart; bi < rowStart + 3; bi++){
										for(int bj = colStart; bj < colStart + 3; bj++){
											if(bj != j){
												if(RVMap.containsKey(bi + " " +bj)){
													String newRVs = RVMap.get(bi + " " + bj).replace(rv + " ", "");
													if(newRVs.equals("")){
														return false;
													} else {
														RVMap.put(bi + " " + bj, newRVs);
													}
													//System.out.println("test2 hehe");
												}
											}
										}
									}
								} else {
									//do nothing
									
								}
							}
						}
						
					}
				}
				return true;
	}

	private static boolean checkRowShareGroup(Set<String> RVSinShareGroup) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 9; i++){
			for(int subGroupID = 0; subGroupID < 3; subGroupID ++){
				RVSinShareGroup.clear();
				for(int j = subGroupID * 3; j < subGroupID * 3 + 3; j++){
					if(RVMap.containsKey(i + " " + j)){
						String [] IJRVs = RVMap.get(i + " " + j).split(" ");
						for(String rv : IJRVs){
							RVSinShareGroup.add(rv);
						}
					}
				}
				//System.out.print("i subGroupID and RVSinShareGroup" + i + " " + subGroupID + " " + RVSinShareGroup);
				if(!RVSinShareGroup.isEmpty()){
					for(String rv : RVSinShareGroup){
					//	System.out.println("current check rv: " + rv);
						//System.out.println("rv in share subgroup: " + rv);
						//System.out.println(foundInRowCOLOther + " " + foundInBoxOther);
						foundInBoxOther = false;
						foundInRowCOLOther = false;
						checkRVLocation(rv, i, subGroupID, "row");
						if(foundInRowCOLOther && !foundInBoxOther){
							//remove all rv in rowColOther
							for(int jj = 0; jj < subGroupID * 3; jj++){
								if(RVMap.containsKey(i + " " +jj)){
									String newRVs = RVMap.get(i + " " + jj).replace(rv + " ", "");
									if(newRVs.equals("")){
										return false;
									} else {
										RVMap.put(i + " " + jj, newRVs);
									}
									//System.out.println("test0 hehe");
								}
							}
							
							for(int jj = subGroupID * 3 + 3; jj < 9; jj++){
								if(RVMap.containsKey(i + " " +jj)){
									String newRVs = RVMap.get(i + " " + jj).replace(rv + " ", "");
									if(newRVs.equals("")){
										return false;
									} else {
										RVMap.put(i + " " + jj, newRVs);
									}
									//System.out.println("test1 haha");
								}
							}
							
						} else if(!foundInRowCOLOther && foundInBoxOther){
							//remove all rv in boxOther
							int rowStart = (i / 3) * 3;
							int colStart = subGroupID * 3;
							for(int bi = rowStart; bi < rowStart + 3; bi++){
								for(int bj = colStart; bj < colStart + 3; bj++){
									if(bi != i){
										if(RVMap.containsKey(bi + " " +bj)){
											String newRVs = RVMap.get(bi + " " + bj).replace(rv + " ", "");
											if(newRVs.equals("")){
												return false;
											} else {
												RVMap.put(bi + " " + bj, newRVs);
											}
											//System.out.println("test2 hehe");
										}
									}
								}
							}
						} else {
							//do nothing
							
						}
					}
				}
				
			}
		}
		return true;
	}

	private static void checkRVLocation(String rv, int i, int subGroupID, String type) {
		// TODO Auto-generated method stub
		//checkInRow
		for(int jj = 0; jj < subGroupID * 3; jj++){
			String rvKey;
			if(type.equals("row")){
				rvKey = i + " " +jj;
			} else {
				rvKey = jj + " " + i;
			}
			if(RVMap.containsKey(rvKey)){
				if(RVMap.get(rvKey).contains(rv)){
					foundInRowCOLOther = true;
					break;
				}
			}
		}
		
		for(int jj = subGroupID * 3 + 3; jj < 9; jj++){
			String rvKey;
			if(type.equals("row")){
				rvKey = i + " " +jj;
			} else {
				rvKey = jj + " " + i;
			}
			if(RVMap.containsKey(rvKey)){
				if(RVMap.get(rvKey).contains(rv)){
					foundInRowCOLOther = true;
					break;
				}
			}
		}
		
		//checkInBox
		int rowStart, colStart;
		if(type.equals("row")){
			rowStart = (i / 3) * 3;
			colStart = subGroupID * 3;
		} else {
			rowStart = subGroupID * 3;
			colStart = (i / 3) * 3;
		}
		for(int bi = rowStart; bi < rowStart + 3; bi++){
			for(int bj = colStart; bj < colStart + 3; bj++){
				if(type.equals("row")){
					if(bi != i){
						if(RVMap.containsKey(bi + " " +bj)){
							if(RVMap.get(bi + " " + bj).contains(rv)){
								foundInBoxOther = true;
								break;
							}
						}
					}
				} else {
					if(bj != i){
						if(RVMap.containsKey(bi + " " +bj)){
							if(RVMap.get(bi + " " + bj).contains(rv)){
								foundInBoxOther = true;
								break;
							}
						}
					}
				}
			}
		}
	}

	

	


	
}
