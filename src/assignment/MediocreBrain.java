package assignment;

import java.util.*;

import assignment.Board.Action;
import assignment.Piece.PieceType;

public class MediocreBrain implements Brain
{
	private ArrayList<Board> boards;
	private Board originBoard;
	private ArrayList<Integer> rotations;

	private Queue<Board.Action> actionsToTake = new LinkedList<Board.Action>();
	private int count;
	private int startRightIndex;
	private int startLeftIndex;

	@Override
	public Action nextMove(Board currentBoard)
	{
		count = 0;
		boards = new ArrayList<Board>();
		rotations = new ArrayList<Integer>();
		int minScore = Integer.MAX_VALUE;
		Board minBoard;
		int minIndex = -1;
		originBoard = currentBoard;
		if (!actionsToTake.isEmpty())
		{
			return actionsToTake.remove();
		} else
		{
			updateBoards();
			for (int i = 0; i < boards.size(); i++)
			{
				if (scoreBoard(boards.get(i)) < minScore)
				{
					minBoard = boards.get(i);
					minScore = scoreBoard(boards.get(i));
					minIndex = i;
				}

			}
			if (minIndex >= 0 || minIndex < startLeftIndex)
			{
				actionsToTake.add(Board.Action.DOWN);
				for (int i = 0; i < rotations.get(minIndex); i++)
				{
					actionsToTake.add(Board.Action.CLOCKWISE);
				}
			}
			if (minIndex >= startLeftIndex || minIndex < startRightIndex)
			{
				actionsToTake.add(Board.Action.LEFT);
				for (int i = 0; i < rotations.get(minIndex); i++)
				{
					actionsToTake.add(Board.Action.CLOCKWISE);
				}
			}
			if (minIndex >= startRightIndex)
			{
				actionsToTake.add(Board.Action.RIGHT);
				for (int i = 0; i < rotations.get(minIndex); i++)
				{
					actionsToTake.add(Board.Action.CLOCKWISE);
				}
			}

		}
		return actionsToTake.remove();
	}

	private void updateBoards()
	{

		// drop straight down - with all 4 rotations
		for (int i = 0; i < 4; i++)
		{
			boards.add(originBoard.testMove(Board.Action.DROP));
			rotations.add(i);
			originBoard.move(Board.Action.CLOCKWISE);
		}
		startLeftIndex = rotations.size();
		// iterate towards left - with all 4 rotations at each point
		Board left = originBoard.testMove(Board.Action.LEFT);
		while (left.getLastResult() == Board.Result.SUCCESS)
		{
			for (int i = 0; i < 4; i++)
			{
				boards.add(originBoard.testMove(Board.Action.DROP));
				rotations.add(i);
				originBoard.move(Board.Action.CLOCKWISE);
			}
			left = left.testMove(Board.Action.LEFT);
		}
		startRightIndex = rotations.size();
		// iterate towards right - with all 4 rotations at each point
		Board right = originBoard.testMove(Board.Action.RIGHT);
		while (right.getLastResult() == Board.Result.SUCCESS)
		{
			for (int i = 0; i < 4; i++)
			{
				boards.add(originBoard.testMove(Board.Action.DROP));
				rotations.add(i);
				originBoard.move(Board.Action.CLOCKWISE);
			}
			right = right.testMove(Board.Action.RIGHT);
		}
	}

	private int scoreBoard(Board b)
	{
		// we will penalize if there are holes in the rows and if it is too tall
		int HEIGHT_PENALTY = 3;
		int HOLE_PENALTY = 4;

		// get height and multiply with weight
		int heightPenalty = HEIGHT_PENALTY * b.getMaxHeight();

		// calculate number of holes and multiply with penalty
		int numHoles = 0;
		for (int i = 0; i < b.getHeight(); i++)
		{
			if (b.getRowWidth(i) != 0 && b.getRowWidth(i) != b.getWidth())
			{
				numHoles += b.getWidth() - b.getRowWidth(i);
			}
		}
		int holePenalty = HOLE_PENALTY * numHoles;

		return holePenalty + heightPenalty;
	}

}
