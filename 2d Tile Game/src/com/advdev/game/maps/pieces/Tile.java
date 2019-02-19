package com.advdev.game.maps.pieces;

import java.awt.image.BufferedImage;

import com.advdev.Main;
import com.advdev.game.entities.npcs.Npc;
import com.advdev.game.maps.Zoneable;
import com.advdev.game.objects.GameObject;

public class Tile {
	private double opacity;
	private Region region;
	private Npc topLevelNpc;
	private GameObject topLevelObject;
	private int type, x, y;
	private Zone zone;
	// Pathfinding below
	private double movementCost;
	private double finalCost;
	private int h;
	private Tile parent;

	public Tile(int type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.opacity = 1;
	}

	public void clearObject() {
		if (this.zone.hasObject()) {
			this.zone = null;
		}
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public void setMovementCost(double movementCost) {
		this.movementCost = movementCost;
	}

	public double getFinalCost() {
		return finalCost;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public void calculateHeuristic(Tile finalNode) {
		this.h = Math.abs(finalNode.x - x) + Math.abs(finalNode.y - y);
	}

	private void calculateFinalCost() {
		double finalCost = getMovementCost() + getH();
		setFinalCost(finalCost);
	}

	public void setFinalCost(double f) {
		this.finalCost = f;
	}

	public void setNodeData(Tile currentNode, double cost) {
		double gCost = currentNode.getMovementCost() + cost;
		setParent(currentNode);
		setMovementCost(gCost);
		calculateFinalCost();
	}

	public boolean checkBetterPath(Tile currentNode, double cost) {
		double gCost = currentNode.getMovementCost() + cost;
		if (gCost < getMovementCost()) {
			setNodeData(currentNode, cost);
			return true;
		}
		return false;
	}

	public double getMovementCost() {
		return movementCost;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Tile)) { return false; }
		Tile t = (Tile) object;
		return t.x == x && t.y == y;
	}

	public void clearTopLevelObject() {
		this.topLevelObject = null;
	}

	/**
	 *
	 * @return True if nothing is on tile, tile contains a portal, or zone is
	 *         complete
	 */
	public boolean completed() {
		if (this.zone == null) return true;
		return this.zone.containsPortal() ? true
				: this.zone.hasNpc() ? this.zone.getNpc().isDead() : this.zone.getObject().harvested();
	}

	public void createZone(Zoneable z, double rarity) {
		this.zone = new Zone(z, rarity);
	}

	public void createZoneWithMapPortal() {
		this.zone = new Zone();
	}

	public BufferedImage getImage() {
		return Main.getMapHandler().getMap().getImage(this.x, this.y);
	}

	public double getOpacity() {
		return this.opacity;
	}

	public Region getRegion() {
		return this.region;
	}

	public Npc getTopLevelNpc() {
		return this.topLevelNpc;
	}

	public GameObject getTopLevelObject() {
		return this.topLevelObject;
	}

	public int getType() {
		return this.type;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Zone getZone() {
		return this.zone;
	}

	public boolean hasZone() {
		return this.zone != null;
	}

	public boolean isOccupied() {
		return (this.zone != null) || (this.topLevelObject != null) || (this.topLevelNpc != null);
	}

	public void process() {
		if (this.zone != null) {
			this.zone.process();
		}
		if (this.topLevelObject != null) {
			this.topLevelObject.process();
		}
		if (this.topLevelNpc != null) {
			this.topLevelNpc.process();
		}
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setTopLevelGameObject(GameObject object) {
		this.topLevelObject = object;
	}

	public void setTopLevelNpc(Npc npc) {
		this.topLevelNpc = npc;
	}

	public void setType(int genVal) {
		this.type = genVal;
	}

	@Override
	public String toString() {
		return "Tile: " + this.x + ", " + this.y;
	}

	public BufferedImage getOccupantImage() {
		BufferedImage image = null;
		if (zone != null) {
			image = zone.getEnterance().getImage();
		} else if (getTopLevelObject() != null) {
			image = getTopLevelObject().getImage();
		} else if (getTopLevelNpc() != null) {
			image = getTopLevelNpc().getImage();
		}
		return image;
	}
}
