package ac.nz.untiec.model;

public class FlatArea {
	private Point start;
	private Point end;

	public FlatArea(Point start, Point end){
		this.start = start;
		this.end = end;
	}
	
	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

}
