package edu.umass;

/**Sudoku Solver with AC3 inference method
 * @author Wei Hong & Liu Yang
 * @mail weihong@cs.umass.edu & lyang@cs.umass.edu
 */

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class SudokuAC3 {
  static final int NUM_VARS = 81;
  static int vars[] = new int[NUM_VARS];
  static boolean varDomain[][] = new boolean[NUM_VARS][9];
  static int rv[] = new int[NUM_VARS];//remaining value
  static int numOfGuesses = 0;
  static ArrayList<Inference> inferences = new ArrayList<Inference>();
  //Takes input from a file and creates a Sudoku CSP from that information
  public static void makeSudokuPuzzle(String path){
    try{
      FileReader file = new FileReader(path);
      for (int a = 0; a < NUM_VARS; a++){
        char ch;
        do{
          ch = (char)file.read();
        }while ((ch == '\n') || (ch == '\r') || (ch == ' '));
        if (ch == '-'){
          vars[a] = 0;
          rv[a] = 9;
          for (int j = 0; j < 9; j++){            
                varDomain[a][j] = true;
            }
        }else{
          String  s = "" + ch;
          Integer i = new Integer(s);
          vars[a] = i.intValue();
          rv[a] = 0;
          for (int j = 0; j < 9; j++){
            if (j == i.intValue() - 1)
              varDomain[a][j] = true;
            else
              varDomain[a][j] = false;
          }
        }
      }     
      file.close();
    }
    catch(IOException e){System.out.println("File read error: " + e);}
  }
  
  //Outputs the Sudoku board to the console and a file
  public static void printSudoku(){
    try{
      FileWriter ofile = new FileWriter("output.txt", true);
      for (int a = 0; a < 9; a++){
        for (int b = 0; b < 9; b++){
          int c = 9*a + b;
          if (vars[c] == 0){
            System.out.print("- ");
            ofile.write("- ");
          }
          else{
            System.out.print(vars[c] + " ");
            ofile.write(vars[c] + " ");
          }
        }
        System.out.println("");
        ofile.write("\r\n");
      }
      ofile.write("\r\n");
      ofile.close();
    }
    catch(IOException e){System.out.println("File read error: " + e);}
  }
  
  
  //check if an assignment is legal for the line/column/square it's located at
  public static boolean isLegal(int i, int j){
	//check row
	 int x;
	 int y;
	 x = i%9;
	 y = i/9;
	  
    //check column
	for(int k = 0; k < 9; k++ ){
		if(vars[9*y + k] == j) return false; 
	}
	//check row
	for(int k = 0; k < 9; k++){
		if(vars[x + 9*k] == j) return false;
	}
	//check square
	int i0 = (x/3)*3 + (y/3)*27;
	
	for(int b = 0; b < 3; b++){
		for(int a = 0; a < 3; a++){
			if(vars[9*b + a + i0] == j) return false;
		}
	}	  
	return true;
	  
  }
  
  public static void assignValues(){
		//assign possible values for each variable
	  for(int i = 0; i < NUM_VARS; i++){
		  if(vars[i] == 0){ // if not assigned value		 
			  rv[i] = 9;
			  Arrays.fill(varDomain[i], true);
			  for(int j = 1; j < 10; j++){			
				 if(!isLegal(i,j)){ // check what values are feasible
					 varDomain[i][j-1] = false;
					 rv[i]--;
				 }	
			 }
		  }
	  }
  }
  
  
  public static int nextVar(int ind){
	//find the variable with least number of possible values among un-assigned variables
	  int mark = 0;
	  for(int i = 0; i < NUM_VARS; i++){
		  if(vars[i] == 0){// the first un-assigned variable			 
				  mark = i;
				  break;
		  }
	  }
	  
	  for(int i = 0; i < NUM_VARS; i++){
		  if(vars[i] == 0){// select among un-assigned variables
			  if(rv[mark] > rv[i] && vars[i] == 0){
				  mark = i;
			  }
		  }
	  }
	//  System.out.println(mark);
	  return mark;	  
  }
  
  public static boolean checkSuccess(){	
	for(int i : vars){
		if(i==0)return false;
	}	  
	return true;	  
  }
  
  
  public static ArrayList<Integer> getNeighbors(int i){
	    ArrayList<Integer> neighbors = new ArrayList<Integer>();
	    HashSet<Integer> tmp = new HashSet<Integer>();
		//calculate x, y coordinates
		int x = i%9;
		int y = i/9;		
		 //check column
		for(int k = 0; k < 9; k++ ){
			tmp.add(9*y+k);
		}
		//check row
		for(int k = 0; k < 9; k++){
			tmp.add(x + 9*k);
		}
		//check square
		int i0 = (x/3)*3 + (y/3)*27;		
		for(int b = 0; b < 3; b++){
			for(int a = 0; a < 3; a++){	
              tmp.add(9*b + a + i0);
			}
		}	
		tmp.remove(i);
		neighbors = new ArrayList<Integer>(tmp);
		
	  return neighbors;	  
  }
  
  public static boolean AC3(){
	Queue<Arc> arcs = new LinkedList<Arc>();
	inferences = new ArrayList<Inference>();
	//Initiate the queue, put all arcs in the queue
	for(int i = 0; i < NUM_VARS; i++){
		ArrayList<Integer> neighbors = getNeighbors(i);
		for(int j : neighbors){
			Arc arc = new Arc(i,j);	
			if(!arcs.contains(arc)){
				arcs.add(arc);
			}	
		}		
	}		
	while(!arcs.isEmpty()){
		Arc pair = arcs.remove();
		if(revise(pair)){
			//check the domain of the left variable
			if(rv[pair.left] == 0)return false;
			ArrayList<Integer> neighbors = getNeighbors(pair.left);
			for(int k : neighbors){
				if(k != pair.right && k != pair.left){
					Arc arc = new Arc(k,pair.left);
					arcs.add(arc);	
				}
			}
		}
	}
	return true;	  
  }
  
  public static boolean revise(Arc pair){
	boolean revised = false;
	int rVar = pair.right;
	int lVar = pair.left;
	for(int i = 0; i < 9; i++){//check domain of left variable
		if(varDomain[lVar][i]){
			boolean flag = false;
			for(int j = 0; j < 9; j++){
				if(varDomain[lVar][i] != varDomain[rVar][j])flag = true;
			}
		
			if(!flag){
				varDomain[lVar][i] = false;
				rv[lVar]--;
				inferences.add(new Inference(lVar, i));
				revised = true;
			}
		}
	}	  
	return revised;
	  
  }
  
  
  public static boolean solveSudoku(int i){
	//check if the assignment is complete.
	if(checkSuccess()){
		System.out.println(numOfGuesses);
		return true;
	}
  	if(vars[i]!=0){
		int next = nextVar(i);
		return solveSudoku(next);	
	}
	
  	numOfGuesses = numOfGuesses + rv[i] - 1;
	boolean[] domainCopy = varDomain[i];
	int re = rv[i];
	for(int j = 1; j < 10; j++){// try all candidates for position i	    
		if(varDomain[i][j-1] && isLegal(i,j)){
			vars[i] = j;			
			boolean varDomainCopy[][] = varDomain;
		    int rvCopy[] = rv;//remaining value
		    assignValues();
		    AC3(); 
		    int next = nextVar(i);
		    if(solveSudoku(next)) return true;
		    for(Inference inf: inferences){
			   varDomain[inf.index][inf.value] = true;
			   rv[inf.index]++;
		    }
		    varDomain = varDomainCopy;
		    rv = rvCopy;
		    varDomain[i][j-1] = false;
		    rv[i]--;
		}		
	}
	vars[i] = 0;
	varDomain[i] = domainCopy;
	rv[i] = re;
	return false;	  
  }
  
  public static void main(String[] args) {	  
	  File directory = new File("puzzles");
	  File[] fList = directory.listFiles();  

	  for (File file : fList) {
		 makeSudokuPuzzle(file.getAbsolutePath());
		 System.out.println(file.toString());
		 assignValues();
		 int min = 0;
		 for(int i = 0; i < NUM_VARS; i++){
			 if(rv[min] > rv[i] && rv[min]!= 0){
				 min = i;
			 }
		 }
		  if(!solveSudoku(min))System.out.println("No solution");
		  else{
	/*		  printSudoku();
			  System.out.println("//////////////////////////");*/
		  }
	  }
  }
}