package Model;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import Controller.TokenController;

/**
 * This class main game is the main jFrame and holds all the components of the game. It consists of the various panels in which other game
 * logic is contained.
 * @author Chin Patel
 *
 */
public class GameFrame extends JFrame implements Observer {
	SwordAndShieldGame game;
	ButtonPanel buttonPanel;
	public BoardPanel boardPanel;
	public TokenPanel tokenPanelY;
	TokenPanel tokenPanelG;
	GraveyardPanel graveyardY;
	GraveyardPanel graveyardG;
	boolean yellowTurn = true;
	boolean greenTurn = false;
	public boolean pastCreation = false;
	public boolean disableBoard = false;
	public BoardPiece creationPiece = null;
	public boolean deadOnce = false;
	public Player currentPlayer;
	JLayeredPane layer = new JLayeredPane();
	int turn = 1;
	GlassPanel glassPanel;
	TokenController tokenController;
	public GameFrame() {
		game = new SwordAndShieldGame();
		this.setTitle("~~Sword And Shiled Game~~");
		//Create the 6 panels
		boardPanel = new BoardPanel(game, this);
		tokenPanelY = new TokenPanel(game, game.getYellow(), this);
		tokenPanelG = new TokenPanel(game, game.getGreen(), this);
		graveyardY = new GraveyardPanel(game, game.getYellow());
		graveyardG = new GraveyardPanel(game, game.getGreen());
		buttonPanel = new ButtonPanel(game, this);
		//Board panel needs focusable for mouse listeners
		boardPanel.setFocusable(true);
		boardPanel.requestFocusInWindow();
		//Create all the split panels
		//Green tokens with board
		JSplitPane firstSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tokenPanelG, boardPanel);
		firstSplit.setResizeWeight(1);
		//(Green tokens + board) with yellow tokens
		JSplitPane secondSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, firstSplit, tokenPanelY);
		secondSplit.setResizeWeight(0.1);
		//(Green tokens + board + yellow tokens) with buttons
		JSplitPane thirdSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonPanel, secondSplit);
		thirdSplit.setResizeWeight(0);
		// Green grave yard with yellow grave yard
		JSplitPane fourthSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graveyardG, graveyardY);
		fourthSplit.setResizeWeight(0.5);
		// (Green tokens + board + yellow tokens + buttons) with grave yards
		JSplitPane fifthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, thirdSplit, fourthSplit);
		fifthSplit.setResizeWeight(0.75);
		// Add two panels to the jLayers (the split, and glass panel)
		layer.add(fifthSplit, new Integer(0), 0);
		fifthSplit.setBounds(0, 0, returnWidth(), 1000);
		glassPanel = new GlassPanel(game, this, buttonPanel, boardPanel, tokenPanelY, tokenPanelG);
		layer.add(glassPanel, new Integer(1), 0);
		// Add these jLayers to this frame
		this.add(layer);
		// Create a timer to repaint
		new Timer(50, (e) -> {
			repaint();
		}).start();
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainGame();
	}


	/**
	 * Method is called when one of the player is killed
	 * @param winner --- the player that whose turn it is
	 */
	public void playerKilled(Player winner) {
		String msg = winner.getName() + " wins!";
		JOptionPane.showMessageDialog(this, msg);
		MenuFrame menu = new MenuFrame();
		this.dispose();
	}

	/**
	 * If the player surrenders the opponent wins
	 */
	public void surrender() {
		String surrenderMsg = "";
		if (currentPlayer.getName().equals("yellow")) {
			surrenderMsg = "Green Wins";
		} else {
			surrenderMsg = "Yellow Wins";
		}
		JOptionPane.showMessageDialog(this, surrenderMsg);
		MenuFrame menu = new MenuFrame();
		this.dispose();
	}


	/**
	 * Alternates between two players
	 * Sets the current player to the who evers turn it is
	 */
	public void mainGame() {
		if (turn % 2 == 0) {
			currentPlayer = game.getGreen();
		} else {
			currentPlayer = game.getYellow();
		}
	}

	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		Graphics2D _g = (Graphics2D) g;
	}

	public Dimension getPreferredSize() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((int) dimension.getWidth(), 1000);
	}


	public int returnWidth() { // To get the width for the glass panel
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		return (int) dimension.getWidth();
	}

	public void setBoardReactionsTrue() {
		boardPanel.reactions = true;
	}

	public void setBoardReactionsFalse() {
		boardPanel.reactions = false;
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

}
