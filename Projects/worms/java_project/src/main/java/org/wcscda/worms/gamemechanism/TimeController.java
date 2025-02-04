package org.wcscda.worms.gamemechanism;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;
import org.wcscda.worms.Config;
import org.wcscda.worms.Helper;
import org.wcscda.worms.Player;
import org.wcscda.worms.Worm;
import org.wcscda.worms.gamemechanism.phases.AbstractPhase;
import org.wcscda.worms.gamemechanism.phases.WormMovingPhase;
import org.wcscda.worms.gamemechanism.playerrecorder.KeyboardControllerPlayer;
import org.wcscda.worms.gamemechanism.playerrecorder.KeyboardControllerRecorder;
import org.wcscda.worms.gamemechanism.sound.WormSoundPlayer;

public class TimeController implements ActionListener {
	private static TimeController instance;

	public KeyboardController getKeyboardController() {
		return keyboardController;
	}

	private final KeyboardController keyboardController;
	private PhysicalController board;
	private Timer timer;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int activePlayerIndex = 0;
	private AbstractPhase currentPhase;
	private int phaseCount = 0;
	private boolean delayedSetNextWorm;
	private ScriptPlayer scriptPlayer;
	

	public ScriptPlayer getScriptPlayer() {
		return scriptPlayer;
	}

	public TimeController() {
		instance = this;
		initGame();
		keyboardController = createController();
		board.addKeyListener(keyboardController);

		timer = new Timer(Config.getClockDelay(), this);
		timer.start();
	}

	private KeyboardController createController() {
		if (Config.getRecordGame()) {
			return new KeyboardControllerRecorder(this.board);
		} else if (Config.getPlayRecord()) {
			return new KeyboardControllerPlayer();
		} else {
			return new KeyboardController();
		}
	}

	private void initGame() {
		board = new PhysicalController();
//		createPlayersAndWorms();
//		isBeginer();
		
		 Player Nico = createPlayer("Nico", Color.RED);
		 Player Sylvain = createPlayer("Sylvain", Color.BLUE);
		 Player Eleonore = createPlayer("Eleonore", Color.PINK);

		    for (String name : new String[] {"Tintin", "Milou"}) {
		      Worm worm = Nico.createWorm(name);
		      board.wormInitialPlacement(worm);
		    }
		    for (String name : new String[] {"Yoda", "luke skywalker"}) {
			      Worm worm = Sylvain.createWorm(name);
			      board.wormInitialPlacement(worm);
			    }
		    for (String name : new String[] {"Trinity", "Cat Woman"}) {
			      Worm worm = Eleonore.createWorm(name);
			      board.wormInitialPlacement(worm);
			    }
		    Eleonore.setBeginer(true);
		doSetNextWorm();
		new Scores();
		musicSound("src/resources/sound/wormInGame.wav");
	}
	HashMap<String, Clip> wavMapping = new HashMap<>();
	  public void musicSound(String filename) {
		  
		    if (!wavMapping.containsKey(filename)) {
		      loadWav(filename);
		    }

		    Clip clip = wavMapping.get(filename);
		    // loading didn't work properly
		    if (clip == null) return;
		    clip.setFramePosition(0);
		    clip.start();
		    clip.loop(10);
		    
		  }

	private void loadWav(String filename) {
		wavMapping.put(filename, tryLoadWav(filename));
		
	}
	private Clip tryLoadWav(String filename) {
	    try {
	      AudioInputStream audioInputStream =
	          AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());
	      Clip clip = AudioSystem.getClip();
	      clip.open(audioInputStream);
	      return clip;
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	

//	public void createPlayersAndWorms() {
//		//Création des equipes et des worms qui leur appartient
//		Map<String, String[]> playerAndWorms = new HashMap<>();
//		Scanner scan1 = new Scanner(System.in);
//		System.out.println("Nombre de joueur ? ");
//		int nbPlayer = scan1.nextInt();
//		Scanner scan2 = new Scanner(System.in);
//		System.out.println("Nombre de worms ? ");
//		int nbWorms = scan2.nextInt();
//
//		for(int i = 0; i < nbPlayer; i++) {
//			Scanner scan3 = new Scanner(System.in);
//			System.out.println("Nom du joueur "+(i+1)+" : ");
//			String namePlayer = scan3.nextLine();
//			System.out.println("Le joueur "+(i+1)+" est "+namePlayer);
//			playerAndWorms.put(namePlayer, new String[nbWorms]);
//			for(int j = 0; j < nbWorms;j++) {
//				Scanner scan4 = new Scanner(System.in);
//				System.out.println("Nom du worms "+(1+j)+" : ");
//				playerAndWorms.get(namePlayer)[j] = scan4.nextLine();
//			}
//		}
//
//
//		Color color[] = {Color.RED, Color.blue, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.PINK};
//
//		//Color color[] = {Color.RED, Color.blue, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
//
//		int colorIndex = 0;
//		for (Entry<String, String[]> player : playerAndWorms.entrySet()) {        //parcourir map [player] et ensuite [player][worms]
//			String joueurMap = player.getKey();										//clef de la map
//			String[] worms = player.getValue();										//tableau de valeur de la map
//			Player joueur = createPlayer(joueurMap, color[colorIndex]);						//creation de l'equipe
//			colorIndex++;
//			for (String nomWorm : worms) {												// valeur des clefs de la map
//				Worm worm = joueur.createWorm(nomWorm);								//ajout des worms
//				board.wormInitialPlacement(worm);
//			}
//		}
//	}
//
//
//	public void isBeginer() {
//		for (int i = 0; i < players.size(); i++) {
//			Scanner scan1 = new Scanner(System.in);
//			System.out.println("Le joueur "+players.get(i).getName()+" est il débutant ? (oui/non) : ");
//			String beginer = scan1.nextLine();
//			if(beginer.equals("oui")) {
//				players.get(i).setBeginer(true);
//			}
//		}
//	}



  public void setNextWorm() {
    delayedSetNextWorm = true;
  }

  protected void delayedActions() {
    if (delayedSetNextWorm) {
      delayedSetNextWorm = false;
      doSetNextWorm();
    }
  }

  protected void doSetNextWorm() {
    for (int i = 0; i < players.size(); ++i) {
      activePlayerIndex += 1;
      activePlayerIndex %= players.size();
      if (getActivePlayer().hasWorms()) break;
    }

    // No player have any worm, it is sad ...
    if (!getActivePlayer().hasWorms()) {
      return;
    }

    getActivePlayer().setNextWorm();
    getActivePlayer().initWeapon();

    AbstractPhase phase = new WormMovingPhase();
	new Inventory();
	Helper.getActivePlayer().setInventory(true);
    this.setCurrentPhase(phase);
  }

  private Player createPlayer(String name, Color color) {
    Player player = new Player(name, color);
    players.add(player);

    return player;
  }

  public Component getBoard() {
    return board;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    phaseCount++;
    board.actionPerformed(e);
  }

  public static TimeController getInstance() {
    if (instance == null) {
      instance = new TimeController();
    }
    return instance;
  }

  public AbstractPhase getCurrentPhase() {
    return currentPhase;
  }

  public void setCurrentPhase(AbstractPhase currentPhase) {
    if ((this.currentPhase != null) && this.currentPhase != currentPhase) {
      this.currentPhase.removeSelf();
    }
    this.currentPhase = currentPhase;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public int getPhaseCount() {
    return phaseCount;
  }

  public void setPhaseCount(int phaseCount) {
    this.phaseCount = phaseCount;
  }

  public Player getActivePlayer() {
    return players.get(activePlayerIndex);
  }
}
