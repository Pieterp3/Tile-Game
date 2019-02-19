package com.advdev.game.entities.npcs;

import java.awt.image.BufferedImage;

import com.advdev.Main;
import com.advdev.game.entities.Entity;
import com.advdev.game.maps.pieces.Tile;

public class Npc extends Entity {
	private int animIndex;
	private long animTimerPassed;
	private boolean dead;
	private NpcDefinition definition;
	private long lastProcess = System.currentTimeMillis();

	public Npc(int id, Tile location) {
		super(id, location, Main.getNpcHandler().getDefinition(id).getName());
		this.definition = Main.getNpcHandler().getDefinition(id);
	}

	public int getAnimIndex() {
		return this.animIndex;
	}

	public NpcDefinition getDefinition() {
		return this.definition;
	}

	@Override
	public BufferedImage getImage() {
		return this.definition.getSpriteSheet()[this.animIndex];
	}

	public boolean isDead() {
		return this.dead;
	}

	public void kill() {
		this.dead = true;
	}

	@Override
	public void process() {
		this.animTimerPassed += (System.currentTimeMillis() - this.lastProcess);
		if (this.animTimerPassed >= 300) {
			this.animTimerPassed = 0;
			this.animIndex += 1;
			if (this.animIndex == this.definition.getAnimIndexCap()) {
				this.animIndex = 0;
			}
		}
		this.lastProcess = System.currentTimeMillis();
	}
}
