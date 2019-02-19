package com.advdev.game.maps.pieces;

import java.util.ArrayList;
import java.util.List;

import com.advdev.game.entities.Entity;
import com.advdev.misc.Constants;

public class Region {
	private double[] completions;
	private int density;
	private int id;
	private Entity owner;
	private List<Tile> tiles;
	private double[] totals;
	private Map map;
	private boolean isLand;
	private boolean hasNearbyBoat;

	public Region(Map map, int id, int density, boolean isLand) {
		this.id = id;
		this.map = map;
		this.isLand = isLand;
		this.totals = new double[2];
		this.completions = new double[2];
		this.density = density;
		this.tiles = new ArrayList<>();
	}

	public void setHasNearbyBoat(boolean nearbyBoat) {
		this.hasNearbyBoat = nearbyBoat;
	}

	public boolean hasNearbyBoat() {
		return hasNearbyBoat;
	}

	public void addTile(Tile t) {
		this.tiles.add(t);
	}

	public boolean isLand() {
		return isLand;
	}

	public void checkZones() {
		List<Entity> owners = getOwners();
		int[] zoneCount = new int[owners.size()];
		for (Tile t : this.tiles) {
			if (t.getZone() != null) {
				zoneCount[owners.indexOf(t.getZone().getOwner())] += 1;
			}
		}
		int totalZones = getTotalZones();
		for (int i = 0; i < zoneCount.length; i++) {
			if (zoneCount[i] > (totalZones * .7)) {
				if (owners.get(i) != null) {
					setOwner(owners.get(i));
					map.checkRegions();
					return;
				}
			}
		}
	}

	public double getCompletion() {
		double total = this.totals[0] + this.totals[1];
		return (this.completions[0] + this.completions[1]) / total;
	}

	public int getDensity() {
		return this.density;
	}

	public int getHarvestedObjects() {
		setCompletion();
		return (int) this.completions[0];
	}

	public int getId() {
		return this.id;
	}

	public int getKilledNpcs() {
		setCompletion();
		return (int) this.completions[1];
	}

	public Entity getOwner() {
		return this.owner;
	}

	private List<Entity> getOwners() {
		List<Entity> owners = new ArrayList<>();
		for (Tile t : this.tiles) {
			if (t.hasZone()) {
				Entity owner = t.getZone().getOwner();
				if (!owners.contains(owner)) {
					owners.add(owner);
				}
			}
		}
		return owners;
	}

	public Tile getRandomTile() {
		return this.tiles.get(Constants.getRandomInt(this.tiles.size()));
	}

	public List<Tile> getTiles() {
		return this.tiles;
	}

	public int getTotalNpcs() {
		return (int) this.totals[1];
	}

	public int getTotalObjects() {
		return (int) this.totals[0];
	}

	private int getTotalZones() {
		int ct = 0;
		for (Tile t : this.tiles) {
			ct += t.hasZone() ? 1 : 0;
		}
		return ct;
	}

	public void setCompletion() {
		this.completions[0] = this.completions[1] = 0;
		for (Tile t : this.tiles) {
			if (t.hasZone()) {
				Zone z = t.getZone();
				if (z.isComplete()) {
					this.completions[z.getNpc() != null ? 1 : 0] += 1;
				}
			}
		}
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	public void setTotals() {
		for (Tile t : this.tiles) {
			if (t.hasZone()) {
				Zone z = t.getZone();
				this.totals[z.getNpc() != null ? 1 : 0] += 1;
			}
		}
	}

	public int size() {
		return this.tiles.size();
	}

	private boolean isEdgeTile(Tile tile) {
		for (int j = 1; j < 8; j += 2) {
			Tile next = map.getNextTile(j, tile);
			if (next == null) continue;
			if (map.getTileSet().isLand(next) != isLand) { return true; }
		}
		return false;
	}

	public Tile getWaterNearEdge() {
		Tile tile = map.getNextTile(1, tiles.get(Constants.getRandomInt(getTileCount())));
		while (tile == null || tile.isOccupied() || map.getTileSet().isLand(tile) || !isEdgeTile(tile)) {
			for (int j = 1; j < 8; j += 2) {
				tile = map.getNextTile(j, tiles.get(Constants.getRandomInt(getTileCount())));
			}
		}
		return tile;
	}

	public Tile getRandomEdgeTile() {
		Tile tile = tiles.get(Constants.getRandomInt(getTileCount()));
		while (tile.isOccupied() || !isEdgeTile(tile)) {
			tile = tiles.get(Constants.getRandomInt(getTileCount()));
		}
		return tile;
	}

	public int getTileCount() {
		return tiles.size();
	}
}
