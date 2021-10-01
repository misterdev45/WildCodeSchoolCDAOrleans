package org.wcscda.worms.gamemechanism;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;
import org.wcscda.worms.Config;
import org.wcscda.worms.Player;
import org.wcscda.worms.Worm;
import org.wcscda.worms.gamemechanism.phases.AbstractPhase;
import org.wcscda.worms.gamemechanism.phases.WormMovingPhase;

public class TimeController implements ActionListener {
  private static TimeController instance;
  private PhysicalController board;
  private Timer timer;
  private ArrayList<Player> players = new ArrayList<Player>();
  private int activePlayerIndex = 0;
  private AbstractPhase currentPhase;
  private int phaseCount = 0;
  private boolean delayedSetNextWorm;

  public TimeController() {
    instance = this;
    initGame();

    board.addKeyListener(new KeyboardController());

    timer = new Timer(Config.getClockDelay(), this);
    timer.start();
  }

  private void initGame() {
    board = new PhysicalController();
    // Lucky luke because for the moment he is a poor lonesome
    // player
    Player luckyLuke = createPlayer("Lucky Luke", Color.RED);

    for (String name : new String[] {"Joly jumper", "rantanplan"}) {
      Worm worm = luckyLuke.createWorm(name);
      board.wormInitialPlacement(worm);
    }

    doSetNextWorm();
  }

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
