package com.advdev.game.entities.player;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.advdev.Main;
import com.advdev.game.entities.Entity;
import com.advdev.game.items.Item;
import com.advdev.game.maps.TileSet;
import com.advdev.game.maps.pieces.*;
import com.advdev.game.objects.GameObject;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class Player extends Entity {
	// Movement
	private int faceDirection, animIndex;
	private boolean moving;
	private GameObject riding;
	private List<Tile> movementPath;
	private int stepIndex = 1;
	private final double newTileMovementDelay = 495;
	private double movementsInAnim = 9;
	private int movementDelay = 55;
	private int increment;
	private long nextMovement;
	private Zone clicked;
	// Other
	private List<Integer> maps;
	private BufferedImage[] playerImages;
	private List<Region> regions;
	private Item selectedItem;
	private double xOff, yOff;
	private Map map;

	public Player(int id, Tile location, String name) {
		super(id, location, name);
		this.playerImages = IOLoader.splitImage("Player", 64, 64);
		this.regions = new ArrayList<>();
		this.maps = new ArrayList<>();
		setMovementIncrement();
	}

	public void setMovementPath(List<Tile> path) {
		xOff = yOff = 0;
		animIndex = 0;
		if (path != null) {
			clicked = path.get(path.size() - 1).getZone();
		}
		this.movementPath = path;
		stepIndex = 1;
		moving = path != null;
	}

	public List<Tile> getPath() {
		return movementPath;
	}

	public void addRegion(Region r, int mapId) {
		this.regions.add(r);
		this.maps.add(mapId);
	}

	public void executeAction() {
		Tile t = location;
		if (t == null) return;
		if (t.isOccupied()) {
			Zone z = t.getZone();
			if (z != null) {
				if (z.isComplete()) {
					z.collect(this);
				} else {
					z.enterZone(this, t.getRegion());
				}
			} else if (t.getTopLevelNpc() != null) {
				Main.getNpcHandler().clickNpc(this, t.getTopLevelNpc());
			} else if (t.getTopLevelObject() != null) {
				Main.getObjectHandler().clickObject(t, t.getTopLevelObject());
			}
		}
	}

	private BufferedImage getRidingImage() {
		return riding.getImage();
	}

	@Override
	public BufferedImage getImage() {
		if (riding != null) { return getRidingImage(); }
		return this.playerImages[(int) ((faceDirection * this.movementsInAnim) + this.animIndex)];
	}

	public Item getSelectedItem() {
		return this.selectedItem;
	}

	public int getXOffset() {
		return (int) this.xOff;
	}

	public int getYOffset() {
		return (int) this.yOff;
	}

	public boolean ownsRegion(Region r) {
		if (r == null) return false;
		for (Tile t : r.getTiles()) {
			if (!t.completed()) return false;
		}
		return true;
	}

	private void updateFaceDirection(Tile next) {
		if (next.getX() != location.getX()) {
			faceDirection = next.getX() > location.getX() ? 3 : 1;
			return;
		}
		faceDirection = next.getY() > location.getY() ? 0 : 2;
	}

	private void moveAlongPath(Tile next) {
		if (System.currentTimeMillis() > nextMovement) {
			updateFaceDirection(next);
			if (next.getX() != location.getX()) {
				xOff += next.getX() > location.getX() ? increment : -increment;
			}
			if (next.getY() != location.getY()) {
				yOff += next.getY() > location.getY() ? increment : -increment;
			}
			animIndex += 1;
			if (animIndex == movementsInAnim) {
				animIndex = 0;
				xOff = yOff = 0;
				stepIndex += 1;
				setLocation(next);
				TileSet tileSet = map.getTileSet();
				if (tileSet.isWater(location) && riding == null) {
					riding = location.getTopLevelObject();
					location.setTopLevelGameObject(null);
				}
				if (stepIndex == movementPath.size()) {
					setMovementPath(null);
					if (clicked != null) {
						executeAction();
					}
					return;
				}
				Tile next2 = movementPath.get(stepIndex);
				if (tileSet.isWater(location) && tileSet.isLand(next2)) {
					location.setTopLevelGameObject(riding);
					riding = null;
				}
			}
			nextMovement = System.currentTimeMillis() + movementDelay;
		}
	}

	@Override
	public void process() {
		if (map == null) { return; }
		if (moving) {
			moveAlongPath(movementPath.get(stepIndex));
		}
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setLocation(Map map, Tile location) {
		super.setLocation(location);
		location.setOpacity(0);
		for (int i = 0; i < 8; i++) {
			Tile t2 = map.getNextTile(i, location);
			if (t2 != null) {
				t2.setOpacity(t2.getOpacity() / 1.5);
				if (t2.getOpacity() < .05) {
					t2.setOpacity(0);
				}
			}
		}
	}

	public void setSelectedItem(Item item) {
		this.selectedItem = item;
	}

	public void setMovementIncrement() {
		this.increment = (int) ((this.movementDelay / this.newTileMovementDelay) * Constants.tileSize);
	}

	public boolean inBoat() {
		return riding != null && riding.getType() == 5;
	}
}
