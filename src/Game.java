import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				gameFrame();
			}
		});

	}
	
	private static void gameFrame() {
		JFrame gameFrame = new JFrame("Train");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.add(new GamePanel());
		gameFrame.pack();
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		gameFrame.setLocationRelativeTo(null);
	}

}
