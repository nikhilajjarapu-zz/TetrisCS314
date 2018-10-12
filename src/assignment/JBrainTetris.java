package assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import assignment.Board.Action;
import assignment.Piece.PieceType;

public class JBrainTetris extends JTetris
{

	/**
	 * 
	 */
	private Brain brain;
	// private Board board;
	private static final long serialVersionUID = 1L;
	Action nextAction = null;
	boolean done = false;

	JBrainTetris()
	{
		brain = new MediocreBrain();
		tick(nextAction);
		timer = new javax.swing.Timer(DELAY, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				nextAction = brain.nextMove(board);
				if (!done)
				{
					tick(nextAction);
					done = true;
				}

			}
		});

	}

	public static void main(String[] args)
	{
		createGUI(new JBrainTetris());
	}

}
