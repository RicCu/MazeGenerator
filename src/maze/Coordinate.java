// A01203967 José Ricardo Cuenca Enríquez
// Coordinate
// 02/05/18

package maze;


public class Coordinate {

	private int x,
				y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coordinate getNeighborUp() {
		return new Coordinate(this.x, this.y-1);
	}
	
	public Coordinate getNeighborDown() {
		return new Coordinate(this.x, this.y+1);
	}
	
	public Coordinate getNeighborLeft() {
		return new Coordinate(this.x-1, this.y);
	}
	
	public Coordinate getNeighborRight() {
		return new Coordinate(this.x+1, this.y);
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}
	
	public boolean isPositive() {
		return this.x >= 0 && this.y >= 0;
	}

	
	public boolean equals(Coordinate other) {
		return this.x == other.x && this.y == other.y;
	}
	
	public boolean isContiguous(Coordinate other) {
		return this.isContiguousInX(other) || this.isContiguousInY(other);
	}
	
	private boolean isContiguousInX(Coordinate other) {
		boolean isContiguousInX = Math.abs(this.x-other.x) == 1;
		boolean isColinearInY = Math.abs(this.y-other.y) == 0;
		return isContiguousInX && isColinearInY;
	}
	
	public String toString() {
		return this.x+ ", "+this.y;
	}
	
	private boolean isContiguousInY(Coordinate other) {
		boolean isContiguousInY = Math.abs(this.x-other.x) == 1;
		boolean isColinearInX = Math.abs(this.y-other.y) == 0;
		return isContiguousInY && isColinearInX;
	}
}
