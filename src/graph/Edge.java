package graph;

public class Edge {
	
	private Vertex toVertex;

}

class MazeCellCLI extends MazeCell{
	
	private boolean wallUp,
					wallDown,
					wallLeft,
					wallRight;
	
	public MazeCellCLI(int x, int y) {
		super(x, y);
		this.wallUp = false;
		this.wallDown = false;
		this.wallLeft = false;
		this.wallRight = false;
	}
	
	public void addWallUp() {
		this.wallUp = true;
	}
	
	public void addWallDown() {
		this.wallDown = true;
	}
	
	public void addWallLeft() {
		this.wallLeft = true;
	}
	
	public void addWallRight() {
		this.wallRight = true;
	}
	
	public String toString() {
		String top = this.wallUp?"  --  ":"      ";
		String mid = this.wallLeft?" |": "  ";
		mid = mid + "**";
		mid = mid + (this.wallRight?"| ":"  ");
		String bottom = this.wallDown?"  --  ":"      ";
	}
}
