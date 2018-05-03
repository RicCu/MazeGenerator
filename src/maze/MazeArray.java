package maze;

import java.util.Arrays;
import java.util.LinkedList;

public class MazeArray extends Maze{
	
	private MazeCell[] cells;
	
	public MazeArray(int numRows, int numCols) {
		super(numRows, numCols);
		boolean initWithWalls = true;
		this.cells = new MazeCell[numRows*numCols];
		for (int y=0; y<numRows; y++) {
			for (int x=0; x<numCols; x++) {
				int id = this.getIdOf(x, y);
				this.cells[id] = new MazeCell(x, y, initWithWalls);
			}
		}
	}
	
	public MazeCell[] getRow(int row) {
		MazeCell[] rowCells = new MazeCell[this.numCols];
		for (int col=0; col<this.numCols; col++) {
			rowCells[col] = this.getCell(col, row);
		}
		return rowCells;
	}
	
	@Override
	public MazeCell getCell(int row, int col) {
		int id = this.getIdOf(row, col);
		return this.getCell(id);
	}

	@Override
	public MazeCell getCell(int id) {
		return this.cells[id];
	}

	@Override
	public LinkedList<MazeCell> getAllCells() {
		return new LinkedList<>(Arrays.asList(this.cells));
	}
	
}
