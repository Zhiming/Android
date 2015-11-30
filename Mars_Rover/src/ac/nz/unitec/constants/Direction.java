package ac.nz.unitec.constants;

public enum Direction {
	LEFT(0), MAIN(1), RIGHT(2);
	
	private int direction;
	
	private Direction(int direct){
		this.direction = direct;
	}
	
	public int direction(){
		return this.direction;
	}
}
