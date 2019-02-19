package com.advdev.game.maps.pathfinding;

import java.util.*;

import com.advdev.game.maps.TileSet;
import com.advdev.game.maps.pieces.Map;
import com.advdev.game.maps.pieces.Tile;

public class AStar {
	private Tile[][] searchArea;
	private PriorityQueue<Tile> openList;
	private Set<Tile> closedSet;
	private Map map;
	private Tile end, start;
	private TileSet set;

	public AStar(Map map, Tile initialNode, Tile finalNode) {
		this.map = map;
		this.set = map.getTileSet();
		this.start = initialNode;
		this.end = finalNode;
		this.searchArea = new Tile[map.getWidth()][map.getHeight()];
		this.openList = new PriorityQueue<>(new Comparator<Tile>() {
			@Override
			public int compare(Tile tile, Tile tile2) {
				return Double.compare(tile.getFinalCost(), tile2.getFinalCost());
			}
		});
		setSearchArea();
		this.closedSet = new HashSet<>();
	}

	private void setSearchArea() {
		for (int i = 0; i < searchArea.length; i++) {
			for (int j = 0; j < searchArea[i].length; j++) {
				Tile tile = map.getTile(i, j);
				tile.calculateHeuristic(end);
				this.searchArea[i][j] = tile;
				tile.setParent(null);
			}
		}
	}

	public List<Tile> findPath() {
		openList.add(start);
		while (openList.size() != 0) {
			Tile current = openList.poll();
			closedSet.add(current);
			if (current.equals(end)) {
				return convertToPath(current);
			} else {
				addAdjacentNodes(current);
			}
		}
		return null;
	}

	private List<Tile> convertToPath(Tile current) {
		List<Tile> path = new ArrayList<>();
		path.add(current);
		Tile parent;
		while ((parent = current.getParent()) != null) {
			path.add(0, parent);
			current = parent;
		}
		return path;
	}

	private void addAdjacentNodes(Tile current) {
		for (int x = -1; x < 2; x++) {
			int nx = current.getX() + x;
			if (nx < 0 || nx >= map.getWidth()) continue;
			for (int y = -1; y < 2; y++) {
				int ny = current.getY() + y;
				if (ny < 0 || ny >= map.getHeight()) continue;
				Tile next = searchArea[nx][ny];
				if ((x != 0 && y != 0)) {
					if (set.isWater(current, next)) {
						checkNode(current, next, set.getCost(current) * 1.5);
					}
				} else if (!(set.isLand(current) && set.isWater(next))) {
					checkNode(current, next, set.getCost(current));
				} else if (!set.isWater(current) && set.isWater(next)) {
					if (next.getTopLevelObject() != null) {
						if (next.getTopLevelObject().getType() == 5) {
							checkNode(current, next, set.getCost(current) * 5);
						}
					}
				}
			}
		}
	}

	private void checkNode(Tile current, Tile next, double cost) {
		if (!set.isBlocked(next) && !closedSet.contains(next) && (!(set.isWater(current)
				&& next.getTopLevelObject() != null && next.getTopLevelObject().getType() == 5))) {
			if (!openList.contains(next)) {
				next.setNodeData(current, cost);
				openList.add(next);
			} else {
				boolean changed = next.checkBetterPath(current, cost);
				if (changed) {
					openList.remove(next);
					openList.add(next);
				}
			}
		}
	}
}