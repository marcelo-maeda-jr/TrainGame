import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 50;
	// private static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) /
	// UNIT_SIZE;
	private static final int DELAY = 500;

	private char direction = 'R';
	private boolean running = false;
	private Timer timer;

	//sprites do jogo
	BufferedImage trainLocomotiveImgL, trainLocomotiveImgR, trainLocomotiveImgU, trainLocomotiveImgD;
	BufferedImage trainWagonH, trainWagonV;
	BufferedImage wallImg;
	BufferedImage gameOverImg;

	List<Wall> walls = new ArrayList<Wall>();

	Train train = new Train();

	public GamePanel() {

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.green);
		this.setFocusable(true);
		this.addKeyListener(new TrainControls());
		imageLoader();
		placeWalls();
		initialTrainPos(train, 5, 5);
		startGame();
	}

	public void imageLoader() {

		try {
			// carrega imagens da locomotiva
			trainLocomotiveImgL = ImageIO.read(new File("assets/Train/trainLeft.png"));
			trainLocomotiveImgR = ImageIO.read(new File("assets/Train/trainRight.png"));
			trainLocomotiveImgU = ImageIO.read(new File("assets/Train/trainUp.png"));
			trainLocomotiveImgD = ImageIO.read(new File("assets/Train/trainDown.png"));
			
			// carrega imagens dos vagoes
			trainWagonH = ImageIO.read(new File("assets/wagons/wagonH.png"));
			trainWagonV = ImageIO.read(new File("assets/wagons/wagonV.png"));

			// carrega imagem da parede
			wallImg = ImageIO.read(new File("assets/wall.png"));

			gameOverImg = ImageIO.read(new File("assets/gameOver.png"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startGame() {
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (running) {
			drawCoordinateLines(g);
			drawWall(g);
			drawTrain(g, train);
		} else {
			gameOver(g);
		}
	}

	public void drawCoordinateLines(Graphics g) {
		for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {

			g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
		}
		for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
			g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
		}
	}

	public void placeWalls() {
		for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
			placeWall(0, i);
			placeWall(SCREEN_WIDTH/UNIT_SIZE - 1, i);
		}
		for(int i = 0; i < SCREEN_WIDTH/UNIT_SIZE; i++) {
			placeWall(i, 0);
			placeWall(i, SCREEN_HEIGHT/UNIT_SIZE - 1);
		}
		
	}

	public void drawWall(Graphics g) {
		for(Wall wall: walls) {
			g.drawImage(wallImg, wall.getPosX() * UNIT_SIZE, wall.getPosY() * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
		}
	}
	
	public void placeWall(int posX, int PosY) {
		walls.add(new Wall(posX, PosY));
	}

	public void initialTrainPos(Train t, int posX, int PosY) {
		t.trainParts.add(new TrainLocomotive(posX, PosY));
		t.trainParts.add(new Wagon(posX, PosY + 1));
		t.trainParts.add(new Wagon(posX, PosY + 2));
	}

	public void drawTrain(Graphics g, Train train) {
		g.setColor(Color.red);
		int currentPos = 0;
		for (TrainPart trainPart : train.trainParts) {
			
			if (train.trainParts.indexOf(trainPart) == 0) {
				if (direction == 'L') {
					g.drawImage(trainLocomotiveImgL, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE, null);
				}
				if (direction == 'R') {
					g.drawImage(trainLocomotiveImgR, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE, null);
				}
				if (direction == 'U') {
					g.drawImage(trainLocomotiveImgU, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE, null);
				}
				if (direction == 'D') {
					g.drawImage(trainLocomotiveImgD, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE,
							UNIT_SIZE, null);
				}
				
			}
			else {
				if(train.trainParts.get(currentPos) instanceof Wagon) {
					if(train.trainParts.get(currentPos - 1).posX > train.trainParts.get(currentPos).posX) {
						g.drawImage(trainWagonH, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
					}
					if(train.trainParts.get(currentPos - 1).posX < train.trainParts.get(currentPos).posX){
						g.drawImage(trainWagonH, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
					}
					if(train.trainParts.get(currentPos - 1).posY < train.trainParts.get(currentPos).posY) {
						g.drawImage(trainWagonV, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
					}
					if(train.trainParts.get(currentPos - 1).posY > train.trainParts.get(currentPos).posY) {
						g.drawImage(trainWagonV, trainPart.posX * UNIT_SIZE, trainPart.posY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
					}
				}
				
			}
			currentPos += 1;
		}

	}

	public void move(Train train) {

		// Percorre todo o trem de tras para a frente e coloca as partes de tras nas
		// posições das partes da frente
		for (int i = train.trainParts.size() - 1; i > 0; i--) {
			train.trainParts.get(i).posX = train.trainParts.get(i - 1).posX;
			train.trainParts.get(i).posY = train.trainParts.get(i - 1).posY;
		}
		switch (direction) {
		case 'U':
			train.trainParts.get(0).posY -= 1;
			break;
		case 'D':
			train.trainParts.get(0).posY += 1;
			break;
		case 'R':
			train.trainParts.get(0).posX += 1;
			break;
		case 'L':
			train.trainParts.get(0).posX -= 1;
			break;
		default:
			break;
		}
	}
	
	public void addTrain(Train train, int posX, int posY) {
		train.trainParts.add(new Wagon(posX, posY));
	}

	public void checkCollision(Train train) {
		// Verifica se o trem bate nele mesmo
		for (int i = train.trainParts.size() - 1; i > 0; i--) {
			if ((train.trainParts.get(0).posX == train.trainParts.get(i).posX)
					&& (train.trainParts.get(0).posY == train.trainParts.get(i).posY)) {
				running = false;
			}
		}
		// verifica se o trem bate nas paredes
		for (Wall wall : walls) {
			if ((wall.getPosX() == train.trainParts.get(0).posX) && (wall.getPosY() == train.trainParts.get(0).posY)) {
				running = false;
			}
		}
		
		if (!running) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawImage(gameOverImg, 0 * UNIT_SIZE, 0 * UNIT_SIZE, SCREEN_HEIGHT, SCREEN_HEIGHT, null);
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Gamer Over")) / 2, SCREEN_HEIGHT - 20);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move(train);
			checkCollision(train);
		}
		repaint();
	}

	public class TrainControls extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			default:
				break;
			}
		}

	}
}
