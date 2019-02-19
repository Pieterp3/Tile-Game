package com.advdev.game.maps.pieces;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.*;

import com.advdev.Main;
import com.advdev.game.entities.Entity;
import com.advdev.game.entities.npcs.Npc;
import com.advdev.game.maps.SimplexNoise;
import com.advdev.game.maps.TileSet;
import com.advdev.game.maps.pathfinding.PathFinder;
import com.advdev.game.objects.GameObject;
import com.advdev.misc.Constants;

public class Map {
	private long creationTime;
	private boolean home;
	private long LIFETIME;
	private Entity owner;
	private List<Region> regions = new ArrayList<>();
	private List<Region> bodiesOfWater = new ArrayList<>();
	private long seed;
	private Tile[][] tiles;
	private TileSet tileSet;
	private int tileWidth, tileHeight;
	private static final int[][] movementDirections = new int[][] {
			{
					-1, -1
			}, {
					0, -1
			}, {
					1, -1
			}, {
					-1, 0
			}, {
					-1, 1
			}, {
					0, 1
			}, {
					1, 1
			}, {
					1, 0
			}
	};
	private PathFinder pathFinder;

	public Map(boolean home, long seed, int tileWidth, int tileHeight) {
		long start = System.currentTimeMillis();
		this.seed = home ? seed : (mapCounter += 1);
		this.home = home;
		this.tileHeight = tileHeight;
		this.tileWidth = tileWidth;
		resetMap(true, Main.getMapHandler().getRandomTileSet());
		pathFinder = new PathFinder(this);
		if (Constants.debug) {
			System.out.println("Map Created in " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	private void addNpcs() {
		for (Region r : regions) {
			int npcs = (r.getTiles().size() / 15) + 1;
			for (int j = 0; j < npcs; j++) {
				Tile t = r.getRandomTile();
				while (t.isOccupied()) {
					t = r.getRandomTile();
				}
				Npc npc = new Npc(0, t);
				t.createZone(npc, r.getDensity());
			}
			Tile t = getRandomTile();
			while ((t == null) || t.isOccupied() || !this.tileSet.isLand(t)) {
				t = getRandomTile();
			}
			Npc mystic = new Npc(1, t);
			t.setTopLevelNpc(mystic);
		}
	}

	private void addObjects() {
		for (Region r : regions) {
			int density = r.getDensity() * 2;
			int objects = r.getTileCount() / density;
			for (int j = 0; j < objects; j++) {
				if (Constants.getRandomDouble() > .15) {
					r.getRandomTile().createZone(Main.getObjectHandler().createObjectByName("Normal Tree"),
							r.getDensity());
				} else {
					r.getRandomTile().createZone(Main.getObjectHandler().createObjectByName("Tin Rock"),
							r.getDensity());
				}
			}
			for (int i = 0; i < 3; i++) {
				GameObject boat = new GameObject(5, 0);
				Tile tile = r.getWaterNearEdge();
				tile.setTopLevelGameObject(boat);
			}
		}
		for (Region r : bodiesOfWater) {
			GameObject boat = new GameObject(5, 0);
			Tile tile = r.getRandomEdgeTile();
			tile.setTopLevelGameObject(boat);
		}
	}

	public void createRegions() {
		long start = System.currentTimeMillis();
		boolean[][] filled = new boolean[tileWidth][tileHeight];
		int regionId = 1;
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				Tile tile = tiles[x][y];
				if (!filled[tile.getX()][tile.getY()]) {
					Queue<Point> queue = new LinkedList<>();
					queue.add(new Point(tile.getX(), tile.getY()));
					Region region = new Region(this, regionId, Constants.getRandomInt(5) + 1,
							this.tileSet.isLand(tile));
					while (!queue.isEmpty()) {
						Point p = queue.remove();
						if ((p.x >= 0) && ((p.x < tileWidth) && (p.y >= 0) && (p.y < tileHeight))) {
							if (!filled[p.x][p.y] && (this.tileSet.isLand(getTile(p.x, p.y)) == region.isLand())) {
								filled[p.x][p.y] = true;
								region.addTile(getTile(p.x, p.y));
								queue.add(new Point(p.x + 1, p.y));
								queue.add(new Point(p.x - 1, p.y));
								queue.add(new Point(p.x, p.y + 1));
								queue.add(new Point(p.x, p.y - 1));
							}
						}
					}
					if (region.size() > 6) {
						if (region.isLand()) {
							regions.add(region);
						} else {
							bodiesOfWater.add(region);
						}
						regionId += 1;
					}
				}
			}
		}
		if (Constants.debug) {
			System.out.println("Identified Regions in " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public BufferedImage getImage(int x, int y) {
		return this.tileSet.getImage(getTile(x, y).getType());
	}

	public Tile getNextTile(int dir, Tile tile) {
		return getTile(tile.getX() + movementDirections[dir][0], tile.getY() + movementDirections[dir][1]);
	}

	/**
	 * Returns the direction between two adjacent tiles
	 * 
	 * @param tile
	 *            Tile starting at
	 * @param tile2
	 *            Tile moving to
	 * @return -1 if tiles are not connected
	 */
	public int getDirection(Tile tile, Tile tile2) {
		int xM = tile.getX() - tile2.getX();
		int yM = tile.getY() - tile2.getY();
		for (int i = 0; i < movementDirections.length; i++) {
			if (xM == movementDirections[i][0] && yM == movementDirections[i][1]) { return i; }
		}
		return -1;
	}

	public Entity getOwner() {
		return this.owner;
	}

	private Tile getRandomTile() {
		int y = Constants.getRandomInt(this.tiles.length);
		return this.tiles[y][Constants.getRandomInt(this.tiles[y].length)];
	}

	public Region getRegion(int region) {
		for (Region r : regions) {
			if (r.getId() == region) { return r; }
		}
		return null;
	}

	public int getRegionCount() {
		return this.regions.size();
	}

	public Tile getTile(int x, int y) {
		if (x >= tiles.length || x < 0 || y >= tiles[x].length || y < 0) { return null; }
		return tiles[x][y];
	}

	public TileSet getTileSet() {
		return this.tileSet;
	}

	public String getTimeRemaining() {
		return Long.toString(this.LIFETIME - ((System.currentTimeMillis() - this.creationTime) / 1000));
	}

	protected void initDefaultMap() {
		this.tiles = new Tile[tileWidth][tileHeight];
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				this.tiles[x][y] = new Tile(0, x, y);
			}
		}
		int[] defaultTypes = this.tileSet.getDefaultTiles();
		// Use 2D Perlin noise to create a default Map
		float[][] genValues = SimplexNoise.generateSimplexNoise(tileWidth, tileHeight, this.seed);
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				Tile t = tiles[x][y];
				int type = defaultTypes[(int) (genValues[x][y] * defaultTypes.length) % defaultTypes.length];
				while (!this.tileSet.isLand(type) && (tileSizeIndex < 2)) {
					type = defaultTypes[((int) (genValues[x][y] * defaultTypes.length) % defaultTypes.length) + 1];
				}
				t.setType(type);
			}
		}
	}

	public void process() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				tiles[x][y].process();
			}
		}
		Main.getPlayer().process();
		Main.getInterfaceManager().updateOpenInterface();
		if ((System.currentTimeMillis() - this.creationTime) >= (this.LIFETIME * 1000)) {
			resetMap(false, Main.getMapHandler().getRandomTileSet());
		}
	}

	private void resetMap(boolean initial, TileSet tileSet) {
		this.creationTime = System.currentTimeMillis();
		this.tileSet = tileSet;
		initDefaultMap();
		createRegions();
		if (!this.home && !initial) {
			populateMap();
		} else if (!initial) {
			setPortalLocation();
		}
		setRegionsForTiles();
		setPlayerPosition();
		int seconds = 300;
		this.LIFETIME = seconds + (Constants.getRandomInt(seconds));
	}

	public void populateMap() {
		addObjects();
		addNpcs();
		setPortalLocation();
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	private void setPlayerPosition() {
		Region r = regions.get(0);
		Tile t = r.getTiles().get(Constants.getRandomInt(r.getTiles().size()));
		while(tileSet.isWater(t)) {
			t = r.getTiles().get(Constants.getRandomInt(r.getTiles().size()));
		}
		Main.getPlayer().setLocation(this, t);
	}

	private void setPortalLocation() {
		Region r = regions.get(Constants.getRandomInt(regions.size()));
		Tile t = r.getTiles().get(Constants.getRandomInt(r.getTiles().size()));
		t.createZoneWithMapPortal();
	}

	private void setRegionsForTiles() {
		for (Region r : regions) {
			for (Tile t : r.getTiles()) {
				t.setRegion(r);
			}
			r.setTotals();
		}
		for (Region r : regions) {
			for (Tile t : r.getTiles()) {
				t.setRegion(r);
			}
		}
	}

	public int getHeight() {
		return tileHeight;
	}

	public int getWidth() {
		return tileWidth;
	}

	private static int mapCounter = 0;
	private static int tileSizeIndex = 6;

	private List<Entity> getOwners() {
		List<Entity> owners = new ArrayList<>();
		for (Region r : regions) {
			Entity owner = r.getOwner();
			if (!owners.contains(owner)) {
				owners.add(owner);
			}
		}
		return owners;
	}

	public void checkRegions() {
		List<Entity> owners = getOwners();
		int[] regionCount = new int[owners.size()];
		for (Region r : regions) {
			Entity owner = r.getOwner();
			regionCount[owners.indexOf(owner)] += 1;
		}
		int totalZones = regions.size();
		for (int i = 0; i < regionCount.length; i++) {
			if (regionCount[i] > (totalZones * .5)) {
				if (owners.get(i) != null) {
					setOwner(owners.get(i));
					return;
				}
			}
		}
	}
}
