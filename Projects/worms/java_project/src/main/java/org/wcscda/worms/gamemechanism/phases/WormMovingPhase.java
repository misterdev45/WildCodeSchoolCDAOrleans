package org.wcscda.worms.gamemechanism.phases;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import org.wcscda.worms.Config;
import org.wcscda.worms.Helper;
import org.wcscda.worms.Worm;
import org.wcscda.worms.board.weapons.AbstractWeapon;
import org.wcscda.worms.board.weapons.Hadoken;
import org.wcscda.worms.board.weapons.SuperGrenade;
import org.wcscda.worms.gamemechanism.sound.WormSoundPlayer;

public class WormMovingPhase extends AbstractPhase {
	private static final double WORM_STEP_SPEED = 3.0;
	private static final double WEAPON_LINE_LENGTH = 30.0;
	private static final double WEAPON_ANGLE_INCR = Math.PI / 8;

	@Override
	protected int getMaxDurationSeconds() {
		return Config.getMaxWormTurnDuration();
	}

	@Override
	public void forwardKeyPressed(String key) {
		if (key.equals("Left")) {
			moveWorm(Math.PI);
		}

		if (key.equals("Right")) {
			moveWorm(0);
		}

		if (key.equals("Up")) {
			moveWeapon(-1);
		}

		if (key.equals("Down")) {
			moveWeapon(1);
		}

		if (key.equals("Space")) {
			if(Helper.getCurrentWeapon() instanceof SuperGrenade && Helper.getActivePlayer().getSuperGrenadeAmmo() > 0) {
				Helper.getActivePlayer().setSuperGrenadeAmmo(Helper.getActivePlayer().getSuperGrenadeAmmo()-1);
			}
			Helper.getActivePlayer().setInventory(false);
			Helper.getCurrentWeapon().fire();
		}
		if (key.equals("W")) {
			Helper.getActivePlayer().changeWeapon();
			WormSoundPlayer mysound = new WormSoundPlayer();
			mysound.playSound("src/resources/sound/switchWeapon.wav");
			if(Helper.getCurrentWeapon() instanceof SuperGrenade && Helper.getActivePlayer().getSuperGrenadeAmmo() == 0) {
				Helper.getActivePlayer().changeWeapon();
				return;
			}
		}
		if (key.equals("I")) {
			if(Helper.getActivePlayer().isInventory()) {
				Helper.getActivePlayer().setInventory(false);
			}else {
				Helper.getActivePlayer().setInventory(true);
			}
		}
	}

	private void moveWeapon(int direction) {
		AbstractWeapon weapon = Helper.getCurrentWeapon();
		Worm worm = Helper.getActiveWorm();
		double angle = weapon.getAngle();

		weapon.incrementAngle(direction * Math.cos(worm.getDirection()) * WEAPON_ANGLE_INCR);
		if (Math.abs(weapon.getAngle() - worm.getDirection()) >= Math.PI / 2 + 1e-3) {
			weapon.setAngle(angle);
		}
	}

	private void moveWorm(double angle) {
		if (Helper.getPC().getFirstStandingOn(Helper.getActiveWorm()).isEmpty()) {
			return;
		}

		Helper.getCurrentWeapon().setAngle(angle);
		Worm worm = Helper.getActiveWorm();

		worm.setDirection(angle);
		worm.setUserMoving(true);
		worm.singleMove(Helper.getPC(), Math.cos(angle) * WORM_STEP_SPEED, 0.0);
	}

	@Override
	protected void drawMain(Graphics2D g, ImageObserver io) {
		Worm activeWorm = Helper.getActiveWorm();
		if (!activeWorm.isUserMoving()) {
			Helper.getCurrentWeapon().draw(g, io);

			drawWeaponDirectionLine(g, io);
		}
		activeWorm.setUserMoving(false);
	}

	private void drawWeaponDirectionLine(Graphics2D g, ImageObserver io) {
		double angle = Helper.getCurrentWeapon().getAngle();

		g.setColor(Helper.getActivePlayer().getColor());
		int x = (int) Helper.getWormX();
		int y = (int) Helper.getWormY();

		g.drawLine(
				x,
				y,
				x + (int) (WEAPON_LINE_LENGTH * Math.cos(angle)),
				y + (int) (WEAPON_LINE_LENGTH * Math.sin(angle)));
	}
}
