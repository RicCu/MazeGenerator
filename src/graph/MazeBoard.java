package graph;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

public class MazeBoard extends JPanel {
	
	private MazeGraph maze;
	
	private Generator generator;
	
	private int height,
				width,
				cellH,
				cellW,
				numRows,
				numCols;
	
	private long stepDelay;
	
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
		LinkedList<MazeCell> vertices = this.maze.getVertices();
		for (MazeCell vertex : vertices) {
			vertex.paintComponent(g, this.cellH, this.cellW);
		}
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
					System.out.println("Finished");
				} catch(InterruptedException ex) {
					System.out.println("Interupted");
				} catch (UninitializedException ex) {
					System.out.println("Attempted to generate without initializing");
				}
			}
			
		});
		solver.start();
	}
	
	public void setup() {
		this.generator = new RecursiveBackTracker(this.numRows, this.numCols);
		this.maze = this.generator.getMaze();
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
	
	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
}
