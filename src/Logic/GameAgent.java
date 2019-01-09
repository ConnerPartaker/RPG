package Logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import Entities.*;
import Frontend.GamePanel;

/**
 * Game Logic Manager, Holds all components of the game and holds main method.
 * Responsible for all master game calculations.
 */
public class GameAgent implements KeyListener {
	
	public static double gameTimer = .02;
	
	private Player    player;
	private Room      room;
	private GamePanel panel;
	
	private RomAgent    romCheck;
	private HitboxAgent hitCheck;
	
	//CONSTRUCTORS//////////////////////////////////////////////////////////////////////////////////////
	public GameAgent() {
		
		player = new Player(0x0000);
		room   = new Room(player);
	   (panel  = new GamePanel(room)).addKeyListener(this);
	   	
	    romCheck = new RomAgent   (room.rom );
		hitCheck = new HitboxAgent(room.ents);
		
		new Timer().schedule(new TimerTask() {public void run() {
			
			updateAll();
			
			room.update();
			panel.repaint();
			
		}}, 0, (int)(gameTimer*1000));
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Updates for the next frame by going through all logic, including updating entity positions,
	 * checking for events, and checking hitboxes.
	 */
	private void updateAll() {
		
		for (Entity i : room.ents) {
			
			i.update();
			
			hitCheck.calc(i);
			
			i.react();
			
			romCheck.move(i);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//KEYLISTENERS//////////////////////////////////////////////////////////////////////////////////////
	public void keyPressed (KeyEvent e) {
		switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT  : player.left   = true;	 break;
			case KeyEvent.VK_RIGHT : player.right  = true;	 break;
			case KeyEvent.VK_SPACE : player.space  = true;   break;
			case KeyEvent.VK_ESCAPE: System.exit(0);		 break;
			default				   :						 break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		
			case KeyEvent.VK_LEFT  : player.left  = false;	 break;
			case KeyEvent.VK_RIGHT : player.right = false;	 break;
			case KeyEvent.VK_ESCAPE: System.exit(0); 		 break;
			default				   :						 break;
		}
	}
	public void keyTyped   (KeyEvent e) {}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//MAIN METHOD///////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		JFrame f = new JFrame();
		GameAgent agent = new GameAgent();
		
		f.setSize(GamePanel.SDIM);
		f.setUndecorated(true);
		f.add(agent.panel);
		
		f.setVisible(true);
		agent.panel.request();
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
}