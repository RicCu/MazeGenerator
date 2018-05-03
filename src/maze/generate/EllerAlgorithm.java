// A01203967 José Ricardo Cuenca Enríquez
// EllerAlgorithm
// 02/05/18

package maze.generate;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import maze.Maze;
import maze.MazeCell;
import maze.MazeGraph;

public class EllerAlgorithm extends Generator {
	
	public static final String NAME = "Eller";
	
	private final int MIN_NUM_SETS_BUFFER = 1;
	private final int OPEN_CONNECTION_PROBABILITY = 35;
	
	private Set<EllerCell>[] cellSets;
	
	private EllerMaze maze;
	
	private int currentRowIdx;
	
	// Only select cells for joining within this range to avoid checking boundaries
	private final int minX,
					  maxX,
					  numRows;
	
	private EllerCell[] currentRow,
					   previousRow;
	
	@SuppressWarnings("unchecked")
	public EllerAlgorithm(int numRows, int numCols) {
		this.maze = new EllerMaze(numRows, numCols);
		this.currentRowIdx = 0;
		this.minX = 1;
		this.maxX = numCols-1; 
		this.numRows = numRows;
		//this.cellSets = (Set<EllerCell>[]) new Object[numCols];
	}

	@Override
	public boolean finished() {
		return this.currentRowIdx == this.numRows;
	}
	
	private int[] getRandomPair() {
		int[] indices = new int[2];
		indices[0] = this.randomInt(0, this.maxX);
		indices[1] = indices[0]+1;
		return indices;
	}
	
	private int getNumSetsIn(EllerCell[] row) {
		Set<Set<EllerCell>> sets = new HashSet<>();
		for (EllerCell cell : row) {
			if (!sets.contains(cell.getSet())) {
				sets.add(cell.getSet());
			}
		}
		return sets.size();
	}
	
	private boolean canJoinPair(int[] pair) {
		EllerCell cellA = this.currentRow[pair[0]]; //this.maze.getCell(pair[0], this.currentRowIdx);
		EllerCell cellB = this.currentRow[pair[1]];
		return cellA.getSet() != cellB.getSet();
	}
	
	private void joinPair(int[] pair) {
		EllerCell cellA = this.currentRow[pair[0]];
		EllerCell cellB = this.currentRow[pair[1]];
		Set<EllerCell> newSet = cellA.getSet();
		newSet.addAll(cellB.getSet());
		cellB.setSet(newSet);
		this.removeWalls(cellA, cellB);
	}
	
	private void openConnectionsToNextRow(EllerCell[] row) {
		Set<Set<EllerCell>> openSets = new HashSet<>();
		Set<Set<EllerCell>> allSets = new HashSet<>();
		for (EllerCell cell : row) {
			allSets.add(cell.getSet());
			if (this.randomInt(0, 99) < OPEN_CONNECTION_PROBABILITY) {
				cell.removeWallDown();
				openSets.add(cell.getSet());
			}
		}
		allSets.removeAll(openSets);
		for (EllerCell cell : row) {
			if (allSets.contains(cell.getSet())) {
				cell.removeWallDown();
				allSets.remove(cell.getSet());
			}
		}
	}

	@Override
	public void start() {
		this.currentRow = this.maze.getRow(this.currentRowIdx);
		for (int x=0; x<this.currentRow.length; x++) {
			//this.cellSets[x] = new TreeSet<>();
			//this.cellSets[x].add(this.currentRow[x]);
			this.currentRow[x].setSet(new HashSet<>());
		}
		this.randomlyMergeCurrentRow();
		this.openConnectionsToNextRow(this.currentRow);
		this.previousRow = this.currentRow;
		this.currentRowIdx++;
	}
	
	private void randomlyMergeCurrentRow() {
		int numSetsInRow = this.getNumSetsIn(this.currentRow);
		System.out.println("Num sets at row "+currentRowIdx+" is "+numSetsInRow);
		int numAttemptsToJoin = this.randomInt(1, numSetsInRow-MIN_NUM_SETS_BUFFER);
		System.out.println("Attempts: "+numAttemptsToJoin);
		for (int i=0; i<numAttemptsToJoin; i++) {
			int[] idxPair = this.getRandomPair();
			System.out.println("Pair selected"+ idxPair[0]+", "+idxPair[1]);
			if (this.canJoinPair(idxPair)) {
				System.out.println("MERGED");
				this.joinPair(idxPair);
			}
		}
	}

	@Override
	public void step() throws UninitializedGeneratorException {
		this.currentRow = this.maze.getRow(this.currentRowIdx);
		//	Initialize sets for the row
		for (int i=0; i<this.currentRow.length; i++) {
			this.currentRow[i].fillBlue();
			this.previousRow[i].noFill();
			if (this.previousRow[i].hasWallDown()) {
				System.out.println("New set for "+ i);
				this.currentRow[i].setSet(new HashSet<>());
			}
			else {
				System.out.println("Old set for "+ i);
				this.currentRow[i].removeWallUp();
				this.currentRow[i].setSet(this.previousRow[i].getSet());
			}
		}
		this.currentRowIdx++;
		if (!this.finished()) {
			this.randomlyMergeCurrentRow();
			this.openConnectionsToNextRow(this.currentRow);
			this.previousRow = this.currentRow;
		}
		else {
			System.out.println("FINISHING");
			System.out.println("SETS: "+this.getNumSetsIn(this.currentRow));
			for(int i=0; i<this.currentRow.length-1; i++) {
				if (this.canJoinPair(new int[] {i, i+1})) {
					this.joinPair(new int[] {i, i+1});
				}
			}
		}
	}
	
	public Maze getMaze() {
		return this.maze;
	}

}

class EllerCell extends MazeCell{
	
	private Set<EllerCell> set;

	public EllerCell(int row, int col, boolean withWalls) {
		super(row, col, withWalls);
	}
	
	public void setSet(Set<EllerCell> set) {
		if (this.hasSet()) {
			this.set.remove(this);
		}
		this.set = set;
		this.set.add(this);
	}
	
	public Set<EllerCell> getSet() {
		return this.set;
	}
	
	public boolean hasSet() {
		return this.set != null;
	}
	
}

class EllerMaze extends Maze{
	
	private EllerCell[] cells;
	
	public EllerMaze(int numRows, int numCols) {
		super(numRows, numCols);
		boolean initWithWalls = true;
		this.cells = new EllerCell[numRows*numCols];
		for (int y=0; y<numRows; y++) {
			for (int x=0; x<numCols; x++) {
				int id = this.getIdOf(x, y);
				this.cells[id] = new EllerCell(x, y, initWithWalls);
			}
		}
	}
	
	public EllerCell[] getRow(int row) {
		EllerCell[] rowCells = new EllerCell[this.numCols];
		for (int col=0; col<this.numCols; col++) {
			rowCells[col] = this.getCell(col, row);
		}
		return rowCells;
	}
	
	@Override
	public EllerCell getCell(int row, int col) {
		int id = this.getIdOf(row, col);
		return this.getCell(id);
	}

	@Override
	public EllerCell getCell(int id) {
		return this.cells[id];
	}

	@Override
	public LinkedList<MazeCell> getAllCells() {
		return new LinkedList<>(Arrays.asList(this.cells));
	}
	
}
