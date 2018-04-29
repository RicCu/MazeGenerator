package graph;

public interface Generator {

	public boolean finished();
	
	public void start();
	
	public void step() throws UninitializedException;
	
	public MazeGraph getMaze();
}

class UninitializedException extends Exception{
	
	public UninitializedException(String message) {
		super(message);
	}
	
}
