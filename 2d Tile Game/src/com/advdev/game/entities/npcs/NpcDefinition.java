package com.advdev.game.entities.npcs;

import java.awt.image.BufferedImage;

import com.advdev.misc.IOLoader;

public class NpcDefinition {
	private int id;
	private String name;
	private String options;
	private BufferedImage[] spriteSheet;

	public NpcDefinition(String name, int id, String options, int width, int height) {
		setName(name);
		this.id = id;
		this.options = options;
		this.spriteSheet = IOLoader.splitImage("Npcs/" + name.replaceAll(" ", ""), width, height);
	}

	public int getAnimIndexCap() {
		return this.spriteSheet.length;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getOptions() {
		return this.options;
	}

	public BufferedImage[] getSpriteSheet() {
		return this.spriteSheet;
	}

	public void setName(String name) {
		this.name = name;
	}
}
