package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or
 * nulls). Supports tetris pieces and row clearing. Does not do any drawing or
 * have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board
{
	// width and height of board
	private int width;
	private int height;
	private PieceType[][] board; // 2d board matrix
	private Piece currentPiece; // current piece to mutate
	private Point[] currentPieceCoords; // holds (x,y) values of piecetypes
	private Point currentPiecePosition;
	private int[] columnHeights;

	// JTetris will use this constructor
	public TetrisBoard(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.board = new PieceType[height][width];
		this.currentPiece = null;
		this.currentPiecePosition = null;
		this.columnHeights = new int[width];
	}

	@Override
	public Result move(Action act)
	{
		if (currentPiece == null)
		{
			return Result.NO_PIECE;
		}
		// three steps
		// 1. delete current piece off board
		// 2. shift currentPieceCoords depending on action
		// 3. add piece back to board

		if (act.equals(Action.DOWN))
		{
			// check if piece should be stacked
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				Point modifiedP = new Point(currentPieceCoords[i]);
				modifiedP.translate(1, 0);
				if (modifiedP.x + 1 > this.height)
				{
					return Result.PLACE;
				}
				if (!Arrays.asList(currentPieceCoords).contains(modifiedP))
				{
					if (board[modifiedP.x][modifiedP.y] != null)
					{
						return Result.PLACE;
					}
				}

			}
			// step 1
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// step 2
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				currentPieceCoords[i].translate(1, 0);
			}
			// step 3
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
			}
			currentPiecePosition.y--; // update current piece position
		}
		
		//***check bounds if piece collides with another piece for left and right
		if (act.equals(Action.LEFT))
		{
			//check bounds
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				Point tempPoint = currentPieceCoords[i];
				if (tempPoint.y == 0) {
					return Result.OUT_BOUNDS;
				}
			}
			// step 1
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// step 2
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				currentPieceCoords[i].translate(0, -1);
			}
			// step 3
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
			}
			currentPiecePosition.x--; // update current piece position
		}
		if (act.equals(Action.RIGHT))
		{
			//check bounds
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				Point tempPoint = currentPieceCoords[i];
				if (tempPoint.y == width - 1) {
					return Result.OUT_BOUNDS;
				}
			}
			// step 1
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// step 2
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				currentPieceCoords[i].translate(0, 1);
			}
			// step 3
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
			}
			currentPiecePosition.x++; // update current piece position
		}

		if (act.equals(Action.CLOCKWISE))
		{
			// remove piece
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// instantiate new piece at current piece position with a clockwise rotation
			nextPiece(currentPiece.clockwisePiece(), currentPiecePosition);
		}

		if (act.equals(Action.COUNTERCLOCKWISE))
		{
			// remove piece
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// instantiate new piece at current piece position with a counterclockwise
			// rotation
			nextPiece(currentPiece.counterclockwisePiece(), currentPiecePosition);
		}
		
		if (act.equals(Action.DROP)) 
		{
			System.out.println(getColumnHeight(0));
		}
		
		
		return Result.SUCCESS;
	}

	@Override
	public Board testMove(Action act)
	{
		return null;
	}

	@Override
	public Piece getCurrentPiece()
	{
		return this.currentPiece;
	}

	@Override
	public Point getCurrentPiecePosition()
	{
		if (currentPiece == null)
		{
			return null;
		}
		return currentPiecePosition;
	}

	@Override
	public void nextPiece(Piece p, Point spawnPosition)
	{
		currentPiecePosition = spawnPosition;
		Point[] relativePoints = p.getBody();
		Point[] absoluteBoardPoints = new Point[4];

		// adds relative piece points to spawnPosition to get absolute game points
		// and converts to board coordinate system
		for (int i = 0; i < relativePoints.length; i++)
		{
			absoluteBoardPoints[i] = gameToBoard(
					new Point(spawnPosition.x + relativePoints[i].x, spawnPosition.y + relativePoints[i].y));
		}

		// updates board points with piece points
		for (int i = 0; i < absoluteBoardPoints.length; i++)
		{
			board[absoluteBoardPoints[i].x][absoluteBoardPoints[i].y] = p.getType();
		}

		currentPieceCoords = absoluteBoardPoints;
		currentPiece = p;
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Board))
		{
			if (!((Board) other).getCurrentPiece().equals(this.getCurrentPiece()))
			{
				if (!((Board) other).getCurrentPiecePosition().equals(this.getCurrentPiecePosition()))
				{
					for (int i = 0; i < board.length; i++)
					{
						for (int j = 0; j < board[i].length; j++)
						{
							if (!((Board) other).getGrid(i, j).equals(this.getGrid(i, j)))
							{
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public Result getLastResult()
	{
		return null;
	}

	@Override
	public Action getLastAction()
	{
		return null;
	}

	@Override
	public int getRowsCleared()
	{
		return 0;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public int getMaxHeight()
	{
		return -1;
	}

	@Override
	public int dropHeight(Piece piece, int x)
	{
		return 0;
	}

	@Override
	public int getColumnHeight(int x)
	{
		Point boardPoint = new Point(0, x);
		
		while (board[boardPoint.x][boardPoint.y] == null || Arrays.asList(currentPieceCoords).contains(boardPoint)) {
			boardPoint.x ++;
			
			if (boardPoint.x >= height) {
				break;
			}
		}
		
		return height - boardPoint.x;
	}

	@Override
	public int getRowWidth(int y)
	{
		int boardX = height - y - 1;
		int count = 0;
		for (int i = 0; i < width; i ++) {
			if (board[boardX][i] != null) {
				count ++;
			}
		}
		
		return count;
	}

	@Override
	public Piece.PieceType getGrid(int x, int y)
	{
		return board[height - y - 1][x];
	}

	public Point gameToBoard(Point gamePoint)
	{
		Point boardPoint = new Point();
		boardPoint.y = gamePoint.x;
		boardPoint.x = height - gamePoint.y - 1;
		return boardPoint;
	}

}
