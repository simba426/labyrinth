package labyrinth;

public class Location {
	private int row;
	private int col;

	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
        
        public void setRow(int x) {
            this.row = x;
        }
        
        public void setCol(int y) {
            this.col = y;
        }
}
