public class TrainPart {
	int posX;
	int posY;
	
	
	
	public TrainPart(int posX, int posY) {
		super();
		this.posX = posX;
		this.posY = posY;
	}
	
	public void moveUp() {
		posY -= 1;
	}
	
	public void moveDown() {
		posY += 1;
	}
	
	public void moveLeft() {
		posX -= 1;
	}
	
	public void moveRight() {
		posX += 1;
	}
	
	
	
}
