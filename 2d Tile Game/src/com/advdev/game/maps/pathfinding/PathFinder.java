package com.advdev.game.maps.pathfinding;

import java.util.List;

import com.advdev.game.maps.pieces.Map;
import com.advdev.game.maps.pieces.Tile;

/**
 * Pathfinding driver
 *
 * @author Pieter Barone
 */
public class PathFinder {
	private Map map;

	public PathFinder(Map map) {
		this.map = map;
	}

	public List<Tile> findPath(Tile start, Tile end) {
		AStar aStar = new AStar(map, start, end);
		List<Tile> path = aStar.findPath();
		return path;
	}
}