package maze;
// A01203967 Jos� Ricardo Cuenca Enr�quez
// MazeDisplay
// 02/05/18

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JFrame;

import maze.generate.Generator;
import maze.generate.RecursiveBackTracker;

public class MazeDisplay extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MazeDisplay() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Box box = Box.createHorizontalBox();
		Generator generator = new RecursiveBackTracker(10, 5);
		MazeBoard board = new MazeBoard(generator);
		MazeControls controls = new MazeControls(board);
		box.add(controls);
		box.add(board, BorderLayout.WEST);
		this.add(box);
		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		MazeDisplay display = new MazeDisplay();
	}
}
