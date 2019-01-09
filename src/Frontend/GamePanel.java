package Frontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Entities.Room;

/**
 * Manages all graphic output for the game
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	public static final Dimension SDIM = Toolkit.getDefaultToolkit().getScreenSize();	//Screen Dimensions
	public static final double    SRAT = SDIM.getHeight()/SDIM.getWidth(); 				//Screen Ratio
	
	private BufferedImage room;
	
	
	public GamePanel(Room room) {
		
		this.room = room.room;
		
		setSize(SDIM);
	}
	
	public void request() {requestFocusInWindow();}
	
	
	protected void paintComponent(Graphics g) {	
		g.drawImage(room, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}