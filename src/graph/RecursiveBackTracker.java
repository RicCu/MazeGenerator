package graph;

import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class RecursiveBackTracker implements Generator {
	
	private MazeGraph graph;
	
	private MazeCell current,
					 old;
	
	private boolean started;
	
	private Stack<MazeCell> fringe;
	private Set<MazeCell> unvisitedSet;
	
	public RecursiveBackTracker(int height, int width) {
		this.graph = new MazeGraph(height, width, true);
		this.fringe = new Stack<MazeCell>();
		this.unvisitedSet = new HashSet<>();
		this.started = false;
		LinkedList<MazeCell> vertices = this.graph.getVertices();
		for (MazeCell vertex : vertices) {
			this.unvisitedSet.add(vertex);
		}
	}
	
	public MazeGraph getMaze() {
		return this.graph;
	}
	
	public boolean finished() {
		//return this.unvisitedSet.isEmpty();
		return this.fringe.empty();
	}
	
	public void step() throws UninitializedException {
		if (!this.started) {
			throw new UninitializedException("Start the algorithm first!");
		}
		if (this.graph.hasUnvisitedNeighbors(current)) {
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
			//current.fillBlue();
			
		}
	}
	
	public void start() {
		this.current = this.selectInit();
		this.visit(current);
		this.old = current;
		this.started = true;
		try {
			this.step();	// Ensure Fringe initialization
		} catch (UninitializedException e) {
			e.printStackTrace();
		}	
	}
	
	private void removeWalls(MazeCell old, MazeCell current) {
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
	
	private void visit(MazeCell vertex) {
		vertex.markVisited();
		this.unvisitedSet.remove(vertex);
	}
	
	private MazeCell selectInit() {
		LinkedList<MazeCell> vertices = this.graph.getVertices(); 
		int index = this.randomInt(0, vertices.size());
		return vertices.get(index);
	}
	
	private int randomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	private MazeCell getUnvisitedNeighbor(MazeCell vertex) {
		MazeCell neighbor;
		do {
			LinkedList<MazeCell> neighbors = this.graph.getAdjacentVertices(vertex);
			int index = this.randomInt(0, neighbors.size());
			neighbor = neighbors.get(index);
			this.graph.removeAdjacentNode(vertex, neighbor);
		} while(neighbor.hasBeenVisited());
		return neighbor;
	}

}
