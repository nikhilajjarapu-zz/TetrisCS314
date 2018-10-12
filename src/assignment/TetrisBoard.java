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
	public PieceType[][] board; // 2d board matrix
	private Piece currentPiece; // current piece to mutate
	private Point[] currentPieceCoords; // holds (x,y) values of piecetypes
	private Point currentPiecePosition;
	private int[] columnHeights;
	private int[] rowFill;
	private Result lastResult;
	private Action lastAction;
	private int rowsCleared;
	public int maxColHeight;

	// JTetris will use this constructor
	public TetrisBoard(int width, int height)
	{
		this.width = width;
		this.height = height;
		board = new PieceType[height][width];
		currentPiece = null;
		currentPiecePosition = null;
		columnHeights = new int[width];
		rowFill = new int[height];
		lastResult = Result.NO_PIECE;
		lastAction = Action.NOTHING;
		rowsCleared = 0;
		maxColHeight = 0;
		currentPieceCoords = new Point[4];
	}

	public TetrisBoard(int width, int height, PieceType[][] board, int[] rowFill, int[] columnHeights, int maxColHeight,
			Piece currentPiece, Point currentPiecePosition, Point[] currentPieceCoords, Result lastResult,
			Action lastAction)
	{
		this.width = width;
		this.height = height;
		this.rowFill = rowFill;
		this.columnHeights = columnHeights;
		this.maxColHeight = maxColHeight;
		this.currentPiece = currentPiece;
		this.currentPiecePosition = currentPiecePosition;
		this.currentPieceCoords = currentPieceCoords;
		this.lastResult = lastResult;
		this.lastAction = lastAction;
		this.board = new PieceType[board.length][board[0].length];
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				this.board[i][j] = board[i][j];
			}
		}
	}

	@Override
	public Result move(Action act)
	{

		lastAction = act;

		if (currentPiece == null)
		{
			lastResult = Result.NO_PIECE;
			return Result.NO_PIECE;
		}

		for (int i = 0; i < height; i++)
		{
			if (rowFill[i] >= width)
			{
				clearLines();
			}
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
					updateRowFill(currentPieceCoords);
					updateColumnFill(currentPieceCoords);
					lastResult = Result.PLACE;
					return Result.PLACE;
				}
				if (!Arrays.asList(currentPieceCoords).contains(modifiedP))
				{
					if (board[modifiedP.x][modifiedP.y] != null)
					{
						updateRowFill(currentPieceCoords);
						updateColumnFill(currentPieceCoords);
						lastResult = Result.PLACE;
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

		// ***check bounds if piece collides with another piece for left and right
		if (act.equals(Action.LEFT))
		{
			// check bounds
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				Point modifiedP = new Point(currentPieceCoords[i]);
				modifiedP.translate(0, -1);
				if (modifiedP.y < 0)
				{
					lastResult = Result.OUT_BOUNDS;
					return Result.OUT_BOUNDS;
				}
				if (!Arrays.asList(currentPieceCoords).contains(modifiedP))
				{
					if (board[modifiedP.x][modifiedP.y] != null)
					{
						lastResult = Result.OUT_BOUNDS;
						return Result.OUT_BOUNDS;
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
			// check bounds
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				Point modifiedP = new Point(currentPieceCoords[i]);
				modifiedP.translate(0, 1);
				if (modifiedP.y > width - 1)
				{
					lastResult = Result.OUT_BOUNDS;
					return Result.OUT_BOUNDS;
				}
				if (!Arrays.asList(currentPieceCoords).contains(modifiedP))
				{
					if (board[modifiedP.x][modifiedP.y] != null)
					{
						lastResult = Result.OUT_BOUNDS;
						return Result.OUT_BOUNDS;
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
			Point[] wallKicks;
			currentPiece = currentPiece.clockwisePiece();
			if (currentPiece.getType() != PieceType.STICK)
			{
				wallKicks = Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
			} else
			{
				wallKicks = Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
			}
			// remove piece
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// instantiate new piece at current piece position with a clockwise rotation
			try
			{
				nextPiece(currentPiece, currentPiecePosition);
			} catch (IllegalArgumentException e)
			{
				boolean worked = false;
				for (int i = 0; i < wallKicks.length; i++)
				{
					Point wallKick = wallKicks[i];
					// System.out.println("WallKick #" + i + ": " + wallKick);
					try
					{
						nextPiece(currentPiece,
								new Point(currentPiecePosition.x + wallKick.y, currentPiecePosition.y - wallKick.x));
					} catch (IllegalArgumentException iae)
					{
						// System.out.println("Failed.");
						continue;
					}
					// System.out.println("Worked!");
					worked = true;
					break;
				}
				if (!worked)
				{
					currentPiece = currentPiece.counterclockwisePiece();
					for (int i = 0; i < currentPieceCoords.length; i++)
					{
						board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
					}
				}
			}
		}

		if (act.equals(Action.COUNTERCLOCKWISE))
		{
			Point[] wallKicks;
			currentPiece = currentPiece.counterclockwisePiece();
			if (currentPiece.getType() != PieceType.STICK)
			{
				wallKicks = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
			} else
			{
				wallKicks = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()];
			}
			// remove piece
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;
			}
			// instantiate new piece at current piece position with a clockwise rotation
			try
			{
				nextPiece(currentPiece, currentPiecePosition);
			} catch (IllegalArgumentException e)
			{
				boolean worked = false;
				for (int i = 0; i < wallKicks.length; i++)
				{
					Point wallKick = wallKicks[i];
					try
					{
						nextPiece(currentPiece,
								new Point(currentPiecePosition.x + wallKick.y, currentPiecePosition.y - wallKick.x));
					} catch (IllegalArgumentException iae)
					{
						continue;
					}
					worked = true;
					break;
				}
				if (!worked)
				{
					currentPiece = currentPiece.clockwisePiece();
					for (int i = 0; i < currentPieceCoords.length; i++)
					{
						board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
					}
				}
			}
		}

		if (act.equals(Action.DROP))
		{

			int lowestDifference = Integer.MAX_VALUE;
			// step 1
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = null;

				int colPosition = height - getColumnHeight(currentPieceCoords[i].y) - 1;
				int tempLowestDifference = colPosition - currentPieceCoords[i].x;
				if (tempLowestDifference < lowestDifference)
				{
					lowestDifference = tempLowestDifference;
				}
			}
			// step 2
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				currentPieceCoords[i].x += lowestDifference;
			}
			// step 3
			for (int i = 0; i < currentPieceCoords.length; i++)
			{
				board[currentPieceCoords[i].x][currentPieceCoords[i].y] = currentPiece.getType();
			}
			currentPiecePosition.x += lowestDifference;// update current piece position

			updateRowFill(currentPieceCoords);
			updateColumnFill(currentPieceCoords);
			lastResult = Result.PLACE;
			return Result.PLACE;
		}

		lastResult = Result.SUCCESS;
		return Result.SUCCESS;
	}

	@Override
	public Board testMove(Action act)
	{
		TetrisBoard b = new TetrisBoard(width, height, board, rowFill, columnHeights, maxColHeight, currentPiece,
				currentPiecePosition, currentPieceCoords, lastResult, lastAction);
		b.move(act);
		return b;
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
		for (int i = 0; i < absoluteBoardPoints.length; i++)
		{
			if (absoluteBoardPoints[i].x < 0 || absoluteBoardPoints[i].x >= board.length)
			{
				throw new IllegalArgumentException();
			}
			if (absoluteBoardPoints[i].y < 0 || absoluteBoardPoints[i].y >= board[0].length)
			{
				throw new IllegalArgumentException();
			}
			if (board[absoluteBoardPoints[i].x][absoluteBoardPoints[i].y] != null)
			{
				throw new IllegalArgumentException();
			}
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
		return lastResult;
	}

	@Override
	public Action getLastAction()
	{
		return lastAction;
	}

	@Override
	public int getRowsCleared()
	{
		return rowsCleared;
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
		return maxColHeight;
	}

	@Override
	public int dropHeight(Piece piece, int x)
	{

		return 0;
	}

	@Override
	public int getColumnHeight(int x)
	{
		return columnHeights[x];
	}

	@Override
	public int getRowWidth(int y)
	{
		int boardX = height - y - 1;
		return rowFill[boardX];
	}

	@Override
	public Piece.PieceType getGrid(int x, int y)
	{
		for (int i = 0; i < currentPieceCoords.length; i++)
		{
			if (new Point(height - y - 1, x).equals(currentPieceCoords[i]))
			{
				return null;
			}
		}
		return board[height - y - 1][x];
	}

	public Point gameToBoard(Point gamePoint)
	{
		Point boardPoint = new Point();
		boardPoint.y = gamePoint.x;
		boardPoint.x = height - gamePoint.y - 1;
		return boardPoint;
	}

	public void updateRowFill(Point[] currentCoords)
	{
		for (int i = 0; i < currentCoords.length; i++)
		{
			rowFill[currentCoords[i].x]++;
		}
	}

	public void updateColumnFill(Point[] currentCoords)
	{
		for (int i = 0; i < currentCoords.length; i++)
		{
			if ((height - currentCoords[i].x) > columnHeights[currentCoords[i].y])
			{
				columnHeights[currentCoords[i].y] = (height - currentCoords[i].x);
			}
			if ((height - currentCoords[i].x) > maxColHeight)
			{
				maxColHeight = height - currentCoords[i].x;
			}
		}
	}

	public void clearLines()
	{

		PieceType[][] newBoard = new PieceType[board.length][board[0].length];
		int counter = height - 1;

		for (int i = height - 1; i > maxColHeight; i--)
		{
			if (rowFill[i] != width)
			{
				newBoard[counter] = board[i];
				counter--;
			} else
			{
				// decrement column heights
				for (int j = 0; j < columnHeights.length; j++)
				{
					columnHeights[j]--;
				}
				maxColHeight--;
				rowsCleared++;
			}
		}

		board = newBoard;

		// reset rowFill
		for (int i = 0; i < height; i++)
		{
			rowFill[i] = 0;
			for (int j = 0; j < width; j++)
			{
				if (newBoard[i][j] != null)
				{
					rowFill[i]++;
				}
			}
		}

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
	}

}
