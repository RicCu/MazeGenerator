package maze;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

import maze.generate.EllerAlgorithm;
import maze.generate.Generator;
import maze.generate.RecursiveBackTracker;
import maze.generate.UninitializedGeneratorException;

public class MazeBoard extends JPanel {
	
	private Maze maze;
	
	private Generator generator;
	
	private int height,
				width,
				cellH,
				cellW,
				numRows,
				numCols;
	
	private long stepDelay;
	
	private String selectedAlgorithm;
	
	public MazeBoard(Generator generator) {
		super();
		this.maze = generator.getMaze();
		this.generator = generator;
		this.height = 200;
		this.width = 400;
		this.numRows = this.maze.getNumRows();
		this.numCols = this.maze.getNumCols();
		this.setPreferredSize(new Dimension(this.width, this.height));
	}
	
	private void updateDimensions() {
		this.height = this.getHeight();
		this.width = this.getWidth();
		this.cellH = this.height / this.maze.getNumRows();
		this.cellW = this.width / this.maze.getNumCols();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.updateDimensions();
		this.maze.paint(g, this.cellH, this.cellW);
	}
	
	public void setStepDelay(long step_delay) {
		this.stepDelay = step_delay;
	}
	
	public void solve() {
		Thread solver = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Example Hard-coded 3 disk solver
				try {
					MazeBoard.this.generator.start();
					MazeBoard.this.repaint();
					Thread.sleep(MazeBoard.this.stepDelay);
					while (!MazeBoard.this.generator.finished()) {
						MazeBoard.this.generator.step();
						MazeBoard.this.repaint();
						Thread.sleep(MazeBoard.this.stepDelay);
					}
				} catch(InterruptedException ex) {
					System.out.println("Interupted");
				} catch (UninitializedGeneratorException ex) {
					System.out.println("Attempted to generate without initializing");
				}
			}
			
		});
		solver.start();
	}
	
	public void setup() {
		//this.generator = new RecursiveBackTracker(this.numRows, this.numCols);
		this.generator = new EllerAlgorithm(this.numRows, this.numCols);
		switch (this.selectedAlgorithm) {
			case (RecursiveBackTracker.NAME):
				this.generator = new RecursiveBackTracker(this.numRows, this.numCols);
				break;
			case (EllerAlgorithm.NAME):
				this.generator = new EllerAlgorithm(this.numRows, this.numCols);
				break;
	}
		this.maze = this.generator.getMaze();
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}

	public void setAlgorithm(String selectedAlgorithm) {
		this.selectedAlgorithm = selectedAlgorithm;
		
	}
}
