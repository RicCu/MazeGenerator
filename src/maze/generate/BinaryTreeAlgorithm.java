package maze.generate;

import maze.Maze;
import maze.MazeArray;
import maze.MazeCell;

public class BinaryTreeAlgorithm extends Generator {

	public static final String NAME = "Binary Tree";
	
	private final int PROB_TO_OPEN_UP = 50;
	
	private MazeArray maze;
	
	private int currentId;
	
	
	public BinaryTreeAlgorithm(int numRows, int numCols) {
		this.maze = new MazeArray(numRows, numCols);
		this.numCols = numCols;
		this.numRows = numRows;
		this.currentId = 0;
	}
	
	public void start() {
		super.start();
		MazeCell current = this.maze.getCell(this.currentId);
		current.paintBlue();
		this.currentId++;
	}
	
	public void step() throws UninitializedGeneratorException {
		super.step();
		MazeCell previous = this.maze.getCell(this.currentId-1);
		previous.removePaint();
		MazeCell current = this.maze.getCell(this.currentId);
		current.paintBlue();
		boolean canOpenUp = this.maze.canContain(current.getCoordinate().getNeighborUp());
		boolean canOpenLeft = this.maze.canContain(current.getCoordinate().getNeighborLeft());
		MazeCell neighbor;
		if (canOpenLeft && canOpenUp) {
			int probabilityUp = this.randomInt(0, 100);
			if (probabilityUp > PROB_TO_OPEN_UP) {
				neighbor = this.getCellAbove(this.currentId);
			}
			else {
				neighbor = this.getCellLeft(this.currentId);
			}
		}
		else if(!canOpenLeft) {
			neighbor = this.getCellAbove(this.currentId);
		}
		else {
			neighbor = this.getCellLeft(this.currentId);
		}
		this.removeWalls(current, neighbor);
		this.currentId++;
		if (this.finished()) {
			current.removePaint();
			this.openBeginEnd();
		}
	}
	
	private MazeCell getCellAbove(int currentId) {
		return this.maze.getCell(currentId-this.numCols);
	}
	
	private MazeCell getCellLeft(int currentId) {
		return this.maze.getCell(currentId-1);
	}
	
	@Override
	public boolean finished() {
		return this.currentId == this.numCols*this.numRows;
	}

	@Override
	public Maze getMaze() {
		return this.maze;
	}

	@Override
	protected void openBeginEnd() {
		this.maze.getCell(0).removeWallLeft();
		this.maze.getCell(0).paintGreen();
		this.maze.getCell((numRows*numCols)-1).removeWallRight();	
		this.maze.getCell((numRows*numCols)-1).paintRed();
	}

}
