package edu.drzam.crush;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import javax.swing.JFrame;

import com.google.gson.Gson;

public class DrZam extends JFrame {
	
	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = -4722429764792514382L;

	/**
	 * The number of milliseconds per frame.
	 */
	private static final long FRAME_TIME = 1000L / 50L;
	
	/**
	 * The number of pieces that exist.
	 */
	private static final int TYPE_COUNT = PillColor.values().length;
		
	/**
	 * The BoardPanel instance.
	 */
	private LivePanel liveBoard;

	/**
	 * The ReplySidePanel instance.
	 */
	private ReplySidePanel replyBoard;
	
	/**
	 * The SidePanel instance.
	 */
	private SidePanel centerPanel;
	
	/**
	 * Whether or not the game is paused.
	 */
	private boolean isPaused;
	
	/**
	 * Whether or not we've played a game yet. This is set to true
	 * initially and then set to false when the game starts.
	 */
	private boolean isNewGame;
	
	/**
	 * Whether or not the game is over.
	 */
	private boolean isGameOver;;
	
	/**
	 * The current level we're on.
	 */
	private int level;
	
	/**
	 * The current score.
	 */
	private int score;
	
	private int secondScore;
	
	/**
	 * The random number generator. This is used to
	 * spit out pieces randomly.
	 */
	private Random random;
	
	/**
	 * The clock that handles the update logic.
	 */
	private Clock logicTimer;
				
	/**
	 * The current type of tile.
	 */
	private PillColor currentType;
	
	/**
	 * The next type of tile.
	 */
	private PillColor nextType;
		
	/**
	 * The current column of our tile.
	 */
	private int currentCol;
	
	/**
	 * The current row of our tile.
	 */
	private int currentRow;
	
	/**
	 * The current rotation of our tile.
	 */
	private int currentRotation;
		
	/**
	 * Ensures that a certain amount of time passes after a piece is
	 * spawned before we can drop it.
	 */
	private int dropCooldown;
	
	/**
	 * The speed of the game.
	 */
	private float gameSpeed;
	
	public Config config;
	
	/**
	 * Creates a new DrZam instance. Sets up the window's properties,
	 * and adds a controller listener.
	 */
	private DrZam() {
		/*
		 * Set the basic properties of the window.
		 */
		super("Dr. Zam");
		
		config = new Config();
		
		if (config.getPlayerNumber() == 1) {
			Thread mainserver = new Thread(new DrZamServer());
			mainserver.start();
		}
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		/*
		 * Initialize the BoardPanel and SidePanel instances.
		 */
		this.liveBoard = new LivePanel(this);
		this.replyBoard = new ReplySidePanel(this);
		this.centerPanel = new SidePanel(this);
		this.liveBoard.setPlayerNumber(config.getPlayerNumber());
		
		/*
		 * Add the BoardPanel and SidePanel instances to the window.
		 */
		add(liveBoard, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(replyBoard, BorderLayout.EAST);
		
		/*
		 * Adds a custom anonymous KeyListener to the frame.
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
								
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When pressed, we check to see that the game is not
				 * paused and that there is no drop cooldown, then set the
				 * logic timer to run at a speed of 25 cycles per second.
				 */
				case KeyEvent.VK_DOWN:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
					}
					break;
					
				/*
				 * Move Left - When pressed, we check to see that the game is
				 * not paused and that the position to the left of the current
				 * position is valid. If so, we decrement the current column by 1.
				 */
				case KeyEvent.VK_LEFT:
					if(!isPaused && liveBoard.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
						currentCol--;
					}
					break;
					
				/*
				 * Move Right - When pressed, we check to see that the game is
				 * not paused and that the position to the right of the current
				 * position is valid. If so, we increment the current column by 1.
				 */
				case KeyEvent.VK_RIGHT:
					if(!isPaused && liveBoard.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
						currentCol++;
					}
					break;
					
				/*
				 * Pause Game - When pressed, check to see that we're currently playing a game.
				 * If so, toggle the pause variable and update the logic timer to reflect this
				 * change, otherwise the game will execute a huge number of updates and essentially
				 * cause an instant game over when we unpause if we stay paused for more than a
				 * minute or so.
				 */
//				case KeyEvent.VK_P:
//					if(!isGameOver && !isNewGame) {
//						isPaused = !isPaused;
//						logicTimer.setPaused(isPaused);
//					}
//					break;
				
				/*
				 * Start Game - When pressed, check to see that we're in either a game over or new
				 * game state. If so, reset the game.
				 */
				case KeyEvent.VK_ENTER:
					if(isGameOver || isNewGame) {
						resetGame();
					}
					break;
					
				}
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {
				
				/*
				 * Drop - When released, we set the speed of the logic timer
				 * back to whatever the current game speed is and clear out
				 * any cycles that might still be elapsed.
				 */
				case KeyEvent.VK_DOWN:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
				
			}
			
		});
		
		/*
		 * Here we resize the frame to hold the BoardPanel and SidePanel instances,
		 * center the window on the screen, and show it to the user.
		 */
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Starts the game running. Initializes everything and enters the game loop.
	 */
	private void startGame() {
		/*
		 * Initialize our random number generator, logic timer, and new game variables.
		 */
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;
		
		/*
		 * Setup the timer to keep the game from running before the user presses enter
		 * to start it.
		 */
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);
		
		if (config.getPlayerNumber() == 1) {
			Thread mainserver = new Thread(new DrZamServer());
			mainserver.start();
		}
		
//		boolean repeat = true;
//		while (repeat) {
//			repeat = true;
//			String liveInfoTest = liveBoard.prepareLiveBoardInfo();
//			System.out.println("liveInfoTest:"+liveInfoTest);
//			String replyBoardInfoTest = synchronizeReplyBoard(liveInfoTest);
//			System.out.println("replyBoardInfoTest:"+replyBoardInfoTest);
//			if (replyBoardInfoTest!=null && !replyBoardInfoTest.equals("") && !replyBoardInfoTest.equals("null")) {
//				System.out.println("detenido");
//				repeat = false;
//			}
//		}
//		
//		this.isNewGame = true;
		
		while(true) {
			
			//Get the time that the frame started.
			long start = System.nanoTime();
			
			//Update the logic timer.
			logicTimer.update();
			
			/*
			 * If a cycle has elapsed on the timer, we can update the game and
			 * move our current piece down.
			 */
			if(logicTimer.hasElapsedCycle()) {
				String replyBoardInfo = synchronizeReplyBoard(liveBoard.prepareLiveBoardInfo());
				if (replyBoardInfo!=null) {
					Gson gson = new Gson();
					PlayerInfo info = gson.fromJson(replyBoardInfo, PlayerInfo.class);
					if (info!=null && info.isGameOver()) {
						//System.out.println(liveBoard.getPlayerNumber()+": GAME OVER VS");
						this.isGameOver = true;
					}
					replyBoard.sinchronizeReplyBoard(replyBoardInfo);
				}
				updateGame();
				if(liveBoard.isGameOver()) {
					this.isGameOver = true;
				}
			}
		
			//Decrement the drop cool down if necessary.
			if(dropCooldown > 0) {
				dropCooldown--;
			}
			
			//Display the window to the user.
			renderGame();
			
			/*
			 * Sleep to cap the framerate.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String synchronizeReplyBoard(String jSonLiveBoardInfo) {
		String jSonReplyBoardInfo = null;
		try {			
			String host = new String();
			host = config.getServerIP();
			InetAddress address = InetAddress.getByName(host);
			Socket connection = new Socket(address, config.getServerPort());
			
			BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "US-ASCII");
	        outputStreamWriter.write(jSonLiveBoardInfo+(char)13);
	        outputStreamWriter.flush();
	        		        
	        //StringBuffer strBuffer = new StringBuffer();
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			jSonReplyBoardInfo = input.readLine();
			//strBuffer = new StringBuffer(jSonReplyBoardInfo);
	    	
	    	//System.out.println("Message Response: " + jSonReplyBoardInfo);
	    	
	    	outputStream.close();
	    	outputStreamWriter.close();
			
	    	input.close();
	    	
	    	connection.close();
			
	    	if (jSonReplyBoardInfo!=null) {
				Gson gson = new Gson();
				PlayerInfo info = gson.fromJson(jSonReplyBoardInfo, PlayerInfo.class);
				setSecondScore((info!=null)?(int)info.getScore():0);
			}
		} catch (IOException e) {
			System.out.println("conexion no establecida...");
			e.printStackTrace();
		}
		return jSonReplyBoardInfo;
	}
	
	/**
	 * Updates the game and handles the bulk of it's logic.
	 */
	private void updateGame() {
		/*
		 * Check to see if the piece's position can move down to the next row.
		 */
		if(liveBoard.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			//Increment the current row if it's safe to do so.
			currentRow++;
		} else {
			
			/*
			 * We've either reached the bottom of the board, or landed on another piece, so
			 * we need to add the piece to the board.
			 */
			liveBoard.addPiece(currentType, currentCol, currentRow, currentRotation);
			
			/*
			 * Check to see if adding the new piece resulted in any cleared lines. If so,
			 * increase the player's score. (Up to 4 lines can be cleared in a single go;
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
			int cleared = liveBoard.checkLines();
			if(cleared > 0) {
				score += 50 << cleared;
			}
			
			/*
			 * Increase the speed slightly for the next piece and update the game's timer
			 * to reflect the increase.
			 */
			gameSpeed += 0.035f;
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			
			/*
			 * Set the drop cooldown so the next piece doesn't automatically come flying
			 * in from the heavens immediately after this piece hits if we've not reacted
			 * yet. (~0.5 second buffer).
			 */
			dropCooldown = 25;
			
			/*
			 * Update the difficulty level. This has no effect on the game, and is only
			 * used in the "Level" string in the SidePanel.
			 */
			level = (int)(gameSpeed * 1.70f);
			
			/*
			 * Spawn a new piece to control.
			 */
			spawnPiece();
		}		
	}
	
	/**
	 * Forces the BoardPanel and SidePanel to repaint.
	 */
	private void renderGame() {
		liveBoard.repaint();
		centerPanel.repaint();
		replyBoard.repaint();
	}
	
	/**
	 * Resets the game variables to their default values at the start
	 * of a new game.
	 */
	private void resetGame() {
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = PillColor.values()[random.nextInt(TYPE_COUNT)];
		this.isNewGame = false;
		this.isGameOver = false;
		liveBoard.setGameOver(false);
		liveBoard.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
		
	/**
	 * Spawns a new piece and resets our piece's variables to their default
	 * values.
	 */
	private void spawnPiece() {
		/*
		 * Poll the last piece and reset our position and rotation to
		 * their default variables, then pick the next piece to use.
		 */
		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = PillColor.values()[random.nextInt(TYPE_COUNT)];
		
		/*
		 * If the spawn point is invalid, we need to pause the game and flag that we've lost
		 * because it means that the pieces on the board have gotten too high.
		 */
		if(!liveBoard.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			liveBoard.setGameOver(true);
			//System.out.println(liveBoard.getPlayerNumber()+": GAME OVER");
			String xx = liveBoard.prepareLiveBoardInfo();
			//System.out.println("xx:"+xx);
			String replyBoardInfo = synchronizeReplyBoard(xx);
			if (replyBoardInfo!=null) {
				Gson gson = new Gson();
				PlayerInfo info = gson.fromJson(replyBoardInfo, PlayerInfo.class);
				if (info!=null && info.isGameOver()) {
					this.isGameOver = true;
				}
				replyBoard.sinchronizeReplyBoard(replyBoardInfo);
				
			}
			logicTimer.setPaused(true);
			
		}		
	}
	
	/**
	 * Checks to see whether or not the game is paused.
	 * @return Whether or not the game is paused.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/**
	 * Checks to see whether or not the game is over.
	 * @return Whether or not the game is over.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Checks to see whether or not we're on a new game.
	 * @return Whether or not this is a new game.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Gets the current score.
	 * @return The score.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Gets the current level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Gets the current type of piece we're using.
	 * @return The piece type.
	 */
	public PillColor getPieceType() {
		return currentType;
	}
	
	/**
	 * Gets the next type of piece we're using.
	 * @return The next piece.
	 */
	public PillColor getNextPieceType() {
		return nextType;
	}
	
	/**
	 * Gets the column of the current piece.
	 * @return The column.
	 */
	public int getPieceCol() {
		return currentCol;
	}
	
	/**
	 * Gets the row of the current piece.
	 * @return The row.
	 */
	public int getPieceRow() {
		return currentRow;
	}
	
	/**
	 * Gets the rotation of the current piece.
	 * @return The rotation.
	 */
	public int getPieceRotation() {
		return currentRotation;
	}

	/**
	 * Entry-point of the game. Responsible for creating and starting a new
	 * game instance.
	 * @param args Unused.
	 */
	public static void main(String[] args) {
		DrZam drZam = new DrZam();
		drZam.startGame();
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSecondScore() {
		return secondScore;
	}

	public void setSecondScore(int secondScore) {
		this.secondScore = secondScore;
	}

}
