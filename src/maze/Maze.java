package maze;

import java.awt.Graphics;
import java.util.LinkedList;

public abstract class Maze {

	protected final int numCols,
				  		numRows;
	
	public Maze(int numRows, int numCols) {
		if (numRows <= 0 || numCols <= 0) {
			throw new IllegalArgumentException("Cannot initialize a maze with dimensions below 1");
		}
		this.numRows = numRows;
		this.numCols = numCols;
	}

	public int getNumRows() {
		return this.numRows;
	}
	
	public int getNumCols() {
		return this.numCols;
	}
	
	protected boolean canContain(Coordinate coord) {
		boolean isAboveMin = coord.getX() >= 0 && coord.getY() >= 0;
		boolean isBelowMax = coord.getX() < this.numCols && coord.getY() < this.numRows;
		return isAboveMin && isBelowMax;
	}
	
	protected int getIdOf(Coordinate coord) {
		return this.getIdOf(coord.getX(), coord.getY());
	}
	
	protected int getIdOf(int x, int y) {
		return y * this.numCols + x;
	}
	
	protected int getIdOf(MazeCell cell) {
		return this.getIdOf(cell.getRow(), cell.getCol());
	}
	
	public MazeCell getCell(int row, int col) {
		int id = getIdOf(row, col);
		return this.getCell(id);
	}
	
	public void paint(Graphics g, int cellHeight, int cellWidth) {
		LinkedList<MazeCell> cells = this.getAllCells();
		for (MazeCell cell : cells) {
			cell.paintComponent(g, cellHeight, cellWidth);
		}
	}
	
	public abstract MazeCell getCell(int id);
	
	public abstract LinkedList<MazeCell> getAllCells();
}
