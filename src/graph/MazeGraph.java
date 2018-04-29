package graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class MazeGraph {

	private LinkedList<MazeCell>[] adjacencyList;
	
	private final int height,
					  width,
					  numVertices;
	
	public MazeGraph(int height, int width, boolean initWithWalls) {
		this.height = height;
		this.width = width;
		this.numVertices = height * width;
		this.adjacencyList = new LinkedList[this.numVertices];
		this.createAllCells(initWithWalls);
	}
	
	public int getNumRows() {
		return this.height;
	}
	
	public int getNumCols() {
		return this.width;
	}
	
	private void createAllCells(boolean initWithWalls) {
		for (int r=0; r<this.width; r++) {
			for (int c=0; c<this.height; c++) {
				MazeCell cell = new MazeCell(r, c, initWithWalls);
				int id = this.calculateId(r, c);
				this.addVertex(id,  cell);
			}
		}
		for (LinkedList<MazeCell> vertexList : this.adjacencyList) {
			MazeCell vertex = vertexList.peekFirst();
			int vertexId = this.calculateId(vertex);
			Stack<MazeCell> neighbors = this.getNeighbors(vertex);
			for (MazeCell neighbor : neighbors) {
				this.addEdge(vertexId, neighbor);
			}
		}
	}
	
	private Stack<MazeCell> getNeighbors(MazeCell cell){
		Stack<MazeCell> neighbors = new Stack<>();
		Stack<Coordinate> neighborCoords= this.findNeighbors(cell.getCoordinate());
		for (Coordinate coord : neighborCoords) {
			neighbors.add(this.getVertex(this.calculateId(coord)));
		}
		return neighbors;
	}
	
	private boolean isWithinBounds(Coordinate coord) {
		boolean isAboveMin = coord.getX() >= 0 && coord.getY() >= 0;
		boolean isBelowMax = coord.getX() < this.width && coord.getY() < this.height;
		return isAboveMin && isBelowMax;
	}
	
	protected Stack<Coordinate> findNeighbors(Coordinate center) {
		Stack<Coordinate> neighbors = new Stack<>();
		Coordinate[] coords = {new Coordinate(center.getX()-1, center.getY()),
				new Coordinate(center.getX()+1, center.getY()),
				new Coordinate(center.getX(), center.getY()-1),
				new Coordinate(center.getX(), center.getY()+1)};
		for (Coordinate coord : coords) {
			if (this.isWithinBounds(coord)) {
				neighbors.add(coord);
			}
		}
		return neighbors;
	}
	
	private void addEdge(int vertexId, MazeCell cell) {
		this.adjacencyList[vertexId].add(cell);
	}
	
	private void addVertex(int vertexId, MazeCell vertex) {
		if (this.adjacencyList[vertexId] != null) {
			if (this.adjacencyList[vertexId].isEmpty()) {
				this.adjacencyList[vertexId].add(vertex);
			}
			else {
				throw new IllegalArgumentException("Vertex "+vertexId+" is already in the graph");
			}
		}
		else {
			this.adjacencyList[vertexId] = new LinkedList<>();
			this.adjacencyList[vertexId].add(vertex);
		}
	}
	
	private int calculateId(Coordinate coord) {
		return this.calculateId(coord.getX(), coord.getY());
	}
	
	private int calculateId(int x, int y) {
		return y * this.width + x;
	}
	
	private int calculateId(MazeCell cell) {
		return this.calculateId(cell.getRow(), cell.getCol());
	}
	
	private Coordinate calculateRowCol(int id) {
		int y = id / this.width;
		int x = id % this.width;
		return new Coordinate(x, y);
	}
	
	public MazeCell getVertex(int row, int col) {
		int id = calculateId(row, col);
		return this.getVertex(id);
	}
	
	public MazeCell getVertex(int vertex) {
		return this.adjacencyList[vertex].peekFirst();
	}
	
	public LinkedList<MazeCell> getAdjacentVertices(MazeCell vertex){
		int id = this.calculateId(vertex);
		return this.getAdjacentNodes(id);
	}
	
	public LinkedList<MazeCell> getAdjacentNodes(int vertexId){
		LinkedList<MazeCell> neighbors = (LinkedList<MazeCell>) this.adjacencyList[vertexId].clone();
		neighbors.removeFirst();
		return neighbors;
	}
	
	public void setWalls() {
		for (int vertexId=0; vertexId<this.numVertices; vertexId++) {
			MazeCell vertex = this.getVertex(vertexId);
			LinkedList<MazeCell> neighbors = this.getAdjacentNodes(vertexId);
			for (MazeCell neighbor : neighbors) {
				if (vertex.isAbove(neighbor)) {
					vertex.addWallDown();
				}
				else if (vertex.isBelow(neighbor)) {
					vertex.addWallUp();
				}
				else if (vertex.isLeftFrom(neighbor)) {
					vertex.addWallRight();
				}
				else if (vertex.isRightFrom(neighbor)) {
					vertex.addWallLeft();
				}
			}
		}
	}
	
	public boolean areConnected(int fromId, int toId) {
		MazeCell toVertex = this.getVertex(toId);
		return this.adjacencyList[fromId].contains(toVertex);
	}
	
	public LinkedList<MazeCell> getVertices(){
		LinkedList<MazeCell> vertices = new LinkedList<>();
		for (LinkedList<MazeCell> cellList : this.adjacencyList) {
			vertices.add(cellList.peekFirst());
		}
		return vertices;
	}
	
	public boolean hasUnvisitedNeighbors(MazeCell vertex) {
		LinkedList<MazeCell> neighbors = this.getAdjacentVertices(vertex);
		for (MazeCell neighbor : neighbors) {
			if (!neighbor.hasBeenVisited()) {
				return true;
			}
		}
		return false;
	}
	
	public void removeAdjacentNode(MazeCell vertex, MazeCell neighbor) {
		int id = this.calculateId(vertex);
		this.adjacencyList[id].remove(neighbor);
	}
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int i=0; i<this.adjacencyList.length; i++) {
			strBuilder.append("["+i+"] ");
			strBuilder.append("Vertex: ");
			strBuilder.append(this.getVertex(i));
			strBuilder.append(" Edges to: ");
			for (int j=1; j<this.adjacencyList[i].size(); j++) {
				strBuilder.append(this.adjacencyList[i].get(j)+", ");
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}
	
	public static void main(String[] args) {
		MazeGraph g = new MazeGraph(3, 4, false);
		System.out.println(g);
	}
}
