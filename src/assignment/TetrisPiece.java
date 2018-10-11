package assignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece
{
	private PieceType type;
	private int[] skirt;
	private int rotationIndex;
	private Point[][] allRotations;
	private Point[] currentPiece;

	/**
	 * Construct a tetris piece of the given type. The piece should be in it's spawn
	 * orientation, i.e., a rotation index of 0.
	 * 
	 * You may freely add additional constructors, but please leave this one - it is
	 * used both in the runner code and testing code.
	 */

	public TetrisPiece(PieceType type, int rotationIndex)
	{
		// initalize variables
		this.type = type;
		this.rotationIndex = rotationIndex;
		this.skirt = new int[this.type.getBoundingBox().width];

		// intialize all rotations based on rotation
		this.allRotations = this.getAllRotations();
		this.currentPiece = this.allRotations[rotationIndex];

		// initalize skirt with values of -1
		for (int i = 0; i < this.skirt.length; i++)
		{
			this.skirt[i] = -1;
		}
		// loop through each point and check for lowest y
		for (Point p : currentPiece)
		{
			if (this.skirt[p.x] < p.y)
			{
				this.skirt[p.x] = p.y;
			}
		}
		// if any skirt values are -1, change them to Integer.MAX_VALUE
		for (int i = 0; i < this.skirt.length; i++)
		{
			if (this.skirt[i] == -1)
			{
				this.skirt[i] = Integer.MAX_VALUE;
			}
		}

	}

	private Point[][] getAllRotations()
	{
		Point[][] allRotations = new Point[4][this.type.getSpawnBody().length];
		allRotations[0] = this.type.getSpawnBody();
		if (this.type == PieceType.STICK)
		{
			allRotations[1] = new Point[]
			{ new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) };
			allRotations[2] = allRotations[0];
			allRotations[3] = allRotations[1];
		} else if (this.type == PieceType.LEFT_L)
		{
			allRotations[1] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) };
			allRotations[2] = new Point[]
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) };
			allRotations[3] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) };

		} else if (this.type == PieceType.RIGHT_L)
		{
			allRotations[1] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) };
			allRotations[2] = new Point[]
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) };
			allRotations[3] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) };

		} else if (this.type == PieceType.SQUARE)
		{
			allRotations[1] = allRotations[0];
			allRotations[2] = allRotations[0];
			allRotations[3] = allRotations[0];

		} else if (this.type == PieceType.RIGHT_DOG)
		{
			allRotations[1] = new Point[]
			{ new Point(1, 2), new Point(1, 1), new Point(2, 1), new Point(2, 0) };
			allRotations[2] = new Point[]
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) };
			allRotations[3] = new Point[]
			{ new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0) };

		} else if (this.type == PieceType.T)
		{
			allRotations[1] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 1) };
			allRotations[2] = new Point[]
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0) };
			allRotations[3] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 1) };
		} else
		{
			allRotations[1] = new Point[]
			{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) };
			allRotations[2] = new Point[]
			{ new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(2, 0) };
			allRotations[3] = new Point[]
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) };

		}
		return allRotations;

	}

	public TetrisPiece(PieceType type)
	{
		this(type, 0);
	}

	@Override
	public PieceType getType()
	{
		return type;
	}

	@Override
	public int getRotationIndex()
	{
		return this.rotationIndex;
	}

	@Override
	public Piece clockwisePiece()
	{
		this.rotationIndex = this.rotationIndex + 1;
		if (this.rotationIndex > 3)
		{
			this.rotationIndex = 0;
		}
		return new TetrisPiece(this.type, this.rotationIndex);
	}

	@Override
	public Piece counterclockwisePiece()
	{
		this.rotationIndex = this.rotationIndex - 1;
		if (rotationIndex < 0)
		{
			rotationIndex = 3;
		}

		return new TetrisPiece(this.type, this.rotationIndex);
	}

	@Override
	public int getWidth()
	{
		return this.type.getBoundingBox().width;
	}

	@Override
	public int getHeight()
	{
		return this.type.getBoundingBox().height;
	}

	@Override
	public Point[] getBody()
	{
		return this.currentPiece;
	}

	@Override
	public int[] getSkirt()
	{
		return this.skirt;
	}

	@Override
	public boolean equals(Object other)
	{
		// Ignore objects which aren't also tetris pieces.
		if (!(other instanceof TetrisPiece))
			return false;
		TetrisPiece otherPiece = (TetrisPiece) other;

		return this.type == otherPiece.type && this.rotationIndex == otherPiece.rotationIndex;
	}

}
