package com.advdev.game.objects;

import java.awt.image.BufferedImage;

public class ObjectDefinition {
	private String hoverText;
	private BufferedImage[] images;
	private long msDelay;
	private String name;
	private int type, id;
	private boolean selfAnimated;

	public ObjectDefinition(int type, int id) {
		this.type = type;
		this.id = id;
	}

	public void setSelfAnimated(boolean selfAnimated) {
		this.selfAnimated = selfAnimated;
	}
	
	public boolean isSelfAnimated() {
		return selfAnimated;
	}
	
	public long getDelay() {
		return this.msDelay;
	}

	public String getHoverText() {
		return this.hoverText;
	}

	public int getId() {
		return this.id;
	}

	public BufferedImage getImage(int index) {
		return this.images[index];
	}

	public int getImageCount() {
		return this.images.length;
	}

	public String getName() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}

	public void setDelay(long delay) {
		this.msDelay = delay;
	}

	public void setHoverText(String hoverText) {
		this.hoverText = hoverText;
	}

	public void setImages(BufferedImage[] images) {
		this.images = images;
	}

	public void setName(String name) {
		this.name = name;
	}
}
