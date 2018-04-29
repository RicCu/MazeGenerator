import java.util.LinkedList;

public class MazeCell {

	private int x,
				y;
	private LinkedList<MazeCell> edges;
	
	public MazeCell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
