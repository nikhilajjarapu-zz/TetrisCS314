package assignment;

import java.util.*;

import assignment.Board.Action;
import assignment.Piece.PieceType;


public class MediocreBrain implements Brain {


    public Board.Action nextMove(Board currentBoard) {
    	
    	Board testBoard = currentBoard.testMove(Action.DROP);
    	
    	System.out.println("CURRENT BOARD: ");
    	
    	System.out.println("TEST BOARD: ");
    	printBoard(((TetrisBoard)testBoard).board);
    	
    	return Action.NOTHING;
    }
    
	public void printBoard(PieceType[][] board)
	{
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

}

