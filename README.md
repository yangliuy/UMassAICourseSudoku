------------------------------------------------------
/** Code for problem 2 of Homework Two in AI course
* @author Liu Yang & Wei Hong
* @mail lyang@cs.umass.edu & weihong@cs.umass.edu
* */
------------------------------------------------------

The Eclipse project AISudokuSolver includes the implementation of plain back tracking, backtracking with the MRV heuristic, AC3 inference, Naked Pair inference, Shared Subgroup inference method and difficulty assessment.

  For plain back tracking, just run SudokuSolverPlainBackTracking.java.

  For backtracking with the MRV heuristic, just run SudokuSolverMRV.java.
  
  For backtracking with MRV and AC3 heuristics, please run SudokuAC3.java

  The two additional inference methods are Naked Pairs and Shared Subgroup. 
  
  For NakedPairs inference method with AC3 and MRV, please run SudokuNakedPairs.java

  For Shared Subgroup inference method with MRV, please run SudokuSolverSharedSubgroup.java
  
  For difficulty assessment, just run SudokuDifficultyAssess.java.

The waterfall is implemented with AC3 and Naked Pairs, based on MRV. Please see SudokuNakedPairs.java for details. The sharedsubgroup inference method has not been integrated into the waterfall, but it can decrease the number of guesses significantly even though as a stand-alone method. 

