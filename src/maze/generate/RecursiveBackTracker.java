// A01203967 José Ricardo Cuenca Enríquez
// RecursiveBackTracker
// 02/05/18

package maze.generate;

import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import maze.Maze;
import maze.MazeCell;
import maze.MazeGraph;

public class RecursiveBackTracker extends Generator {
	
	public static final String NAME = "Recursive Backtracker";
	
	
	private MazeCell current,
					 old;
	
	private MazeGraph maze;
	private Stack<MazeCell> fringe;
	private Set<MazeCell> unvisitedSet;
	
	public RecursiveBackTracker(int height, int width) {
		this.maze = new MazeGraph(height, width, true);
		this.fringe = new Stack<MazeCell>();
		this.unvisitedSet = new HashSet<>();
		this.started = false;
		LinkedList<MazeCell> vertices = this.maze.getAllCells();
		for (MazeCell vertex : vertices) {
			this.unvisitedSet.add(vertex);
		}
	}
	
	
	public boolean finished() {
		//return this.unvisitedSet.isEmpty();
		return this.fringe.empty();
	}
	
	public void step() throws UninitializedGeneratorException {
		super.step();
		if (this.maze.hasUnvisitedNeighbors(current)) {
			this.fringe.add(current);
			old = current;
			current = this.getUnvisitedNeighbor(current);
			current.fillBlue();
			this.removeWalls(old, current);
			this.visit(current);
		}
		else if (!this.fringe.empty()){
			current.noFill();
			current = this.fringe.pop();
			
		}
	}
	
	public Maze getMaze() {
		return this.maze;
	}
	
	public void start() {
		super.start();
		this.current = this.selectInit();
		this.visit(current);
		this.old = current;
		try {
			this.step();	// Ensure Fringe initialization
		} catch (UninitializedGeneratorException e) {
			e.printStackTrace();
		}	
	}
	
	
	private void visit(MazeCell vertex) {
		vertex.markVisited();
		this.unvisitedSet.remove(vertex);
	}
	
	private MazeCell selectInit() {
		LinkedList<MazeCell> vertices = this.maze.getAllCells(); 
		int index = this.randomInt(0, vertices.size());
		return vertices.get(index);
	}
	
	private MazeCell getUnvisitedNeighbor(MazeCell vertex) {
		MazeCell neighbor;
		do {
			LinkedList<MazeCell> neighbors = this.maze.getAdjacentVertices(vertex);
			int index = this.randomInt(0, neighbors.size());
			neighbor = neighbors.get(index);
			this.maze.removeAdjacentNode(vertex, neighbor);
		} while(neighbor.hasBeenVisited());
		return neighbor;
	}

}
