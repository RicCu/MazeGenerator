// A01203967 Jos� Ricardo Cuenca Enr�quez
// MazeGraph
// 02/05/18

package maze;

import java.util.LinkedList;
import java.util.Stack;


public class MazeGraph extends Maze {

	private LinkedList<MazeCell>[] adjacencyList;
	
	
	@SuppressWarnings("unchecked")
	public MazeGraph(int numRows, int numCols, boolean initWithWalls) {
		super(numRows, numCols);
		int numVertices = numRows * numCols;
		this.adjacencyList = new LinkedList[numVertices];
		this.createAllCells(initWithWalls);
	}
	
	private void createAllCells(boolean initWithWalls) {
		for (int r=0; r<this.numCols; r++) {
			for (int c=0; c<this.numRows; c++) {
				MazeCell cell = new MazeCell(r, c, initWithWalls);
				int id = this.getIdOf(r, c);
				this.addVertex(id,  cell);
			}
		}
		for (LinkedList<MazeCell> vertexList : this.adjacencyList) {
			MazeCell vertex = vertexList.peekFirst();
			int vertexId = this.getIdOf(vertex);
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
			neighbors.add(this.getCell(this.getIdOf(coord)));
		}
		return neighbors;
	}
	
	protected Stack<Coordinate> findNeighbors(Coordinate center) {
		Stack<Coordinate> neighbors = new Stack<>();
		Coordinate[] coords = {new Coordinate(center.getX()-1, center.getY()),
				new Coordinate(center.getX()+1, center.getY()),
				new Coordinate(center.getX(), center.getY()-1),
				new Coordinate(center.getX(), center.getY()+1)};
		for (Coordinate coord : coords) {
			if (this.canContain(coord)) {
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
	
	public MazeCell getCell(int vertex) {
		return this.adjacencyList[vertex].peekFirst();
	}
	
	public LinkedList<MazeCell> getAdjacentVertices(MazeCell vertex){
		int id = this.getIdOf(vertex);
		return this.getAdjacentNodes(id);
	}
	
	public LinkedList<MazeCell> getAdjacentNodes(int vertexId){
		@SuppressWarnings("unchecked")
		LinkedList<MazeCell> neighbors = (LinkedList<MazeCell>) this.adjacencyList[vertexId].clone();
		neighbors.removeFirst();
		return neighbors;
	}
	
	
	public boolean areConnected(int fromId, int toId) {
		MazeCell toVertex = this.getCell(toId);
		return this.adjacencyList[fromId].contains(toVertex);
	}
	
	public LinkedList<MazeCell> getAllCells(){
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
		int id = this.getIdOf(vertex);
		this.adjacencyList[id].remove(neighbor);
	}
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int i=0; i<this.adjacencyList.length; i++) {
			strBuilder.append("["+i+"] ");
			strBuilder.append("Vertex: ");
			strBuilder.append(this.getCell(i));
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
