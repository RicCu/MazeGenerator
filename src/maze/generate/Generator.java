package maze.generate;

import java.util.concurrent.ThreadLocalRandom;

import maze.Maze;
import maze.MazeCell;

public abstract class Generator {
	
	protected Maze maze;
	
	protected boolean started;
	
	public static String NAME;
	

	public abstract boolean finished();
	
	public void start() {
		this.started = true;
	}
	
	public void step() throws UninitializedGeneratorException{
		if (!this.started) {
			throw new UninitializedGeneratorException("Generator attmepted to step "+
						"before initializing. Call start() before.");
		}
	}
	
	public abstract Maze getMaze();
	
	protected int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	protected void removeWalls(MazeCell old, MazeCell current) {
		if (old.isAbove(current)) {
			old.removeWallDown();
			current.removeWallUp();
		}
		else if (old.isBelow(current)) {
			current.removeWallDown();
			old.removeWallUp();
		}
		else if (old.isLeftFrom(current)) {
			old.removeWallRight();
			current.removeWallLeft();
		}
		else if (old.isRightFrom(current)) {
			current.removeWallRight();
			old.removeWallLeft();
		}
	}
}
