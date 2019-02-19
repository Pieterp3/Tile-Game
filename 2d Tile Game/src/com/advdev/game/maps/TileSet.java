package com.advdev.game.maps;

import java.awt.image.BufferedImage;

import com.advdev.game.maps.pieces.Tile;

public class TileSet {
	private boolean deepWater;
	private int[] defaultTiles, landTypes;
	private BufferedImage[] images;
	private String type;
	private int[] blockedTiles;

	public TileSet(String img, BufferedImage[] splitImage, int[] defaultTiles, int[] landTypes, boolean deepWater,
			int[] blockedTiles) {
		this.images = splitImage;
		this.blockedTiles = blockedTiles;
		this.defaultTiles = defaultTiles;
		this.type = img;
		this.deepWater = deepWater;
		this.landTypes = landTypes;
	}

	public int[] getBlockedTiles() {
		return blockedTiles;
	}

	public int getDefaultTileCount() {
		return this.defaultTiles.length;
	}

	public int[] getDefaultTiles() {
		return this.defaultTiles;
	}

	public BufferedImage getImage(int type) {
		return this.images[type];
	}

	public int getTileTypeCount() {
		return this.images.length;
	}

	public String getType() {
		return this.type;
	}

	public boolean hasDeepWater() {
		return this.deepWater;
	}

	public boolean isLand(int type) {
		for (int t : this.landTypes) {
			if (t == type) return true;
		}
		return false;
	}

	public boolean isBlocked(Tile t) {
		for (Integer i : blockedTiles) {
			if (i == t.getType()) { return true; }
		}
		return false;
	}

	public boolean isLand(Tile tile) {
		if (tile == null) return false;
		for (int t : this.landTypes) {
			if (t == tile.getType()) return true;
		}
		return false;
	}

	public boolean isWater(Tile... check) {
		for (Tile t : check) {
			if (isLand(t)) { return false; }
		}
		return true;
	}

	public double getCost(Tile tile) {
		if (tile.getTopLevelObject() != null) {
			if (tile.getTopLevelObject().getType() == 5) { return 1; }
		}
		if (isLand(tile)) { return 3; }
		return 2;
	}
}
