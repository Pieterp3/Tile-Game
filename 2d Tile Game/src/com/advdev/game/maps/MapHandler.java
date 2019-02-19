package com.advdev.game.maps;

import java.util.*;

import com.advdev.Initializable;
import com.advdev.display.Frame;
import com.advdev.game.maps.pieces.Map;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class MapHandler implements Initializable {
	private HashMap<String, TileSet> tileSets = new HashMap<>();
	private Map map;

	@Override
	public void init() {
		List<String> tileDefs = IOLoader.getTextFromFile("Tiles/TileDefinitions");
		for (String line : tileDefs) {
			String[] a = line.split("\t");
			String img = a[0];
			int w = Integer.valueOf(a[1]);
			int h = Integer.valueOf(a[2]);
			String[] d = a[3].split(" ");
			int[] def = new int[d.length];
			for (int i = 0; i < d.length; i++) {
				def[i] = Integer.valueOf(d[i]);
			}
			String[] l = a[4].split(" ");
			int[] land = new int[l.length];
			for (int i = 0; i < d.length; i++) {
				land[i] = Integer.valueOf(l[i]);
			}
			boolean deepWater = Boolean.valueOf(a[5]);
			String[] bTiles = a[6].split("\t");
			int[] blockedTiles = new int[bTiles.length];
			for (int i = 0; i < bTiles.length; i++) {
				blockedTiles[i] = Integer.valueOf(bTiles[i]);
			}
			TileSet set = new TileSet(img, IOLoader.splitImage("Tiles/" + img, w, h), def, land, deepWater, blockedTiles);
			this.tileSets.put(img, set);
		}
		createMap();
	}
	
	private void createMap() {
		int tileWidth = calcMaxTileWidth();
		int tileHeight = calcMaxTileHeight();
		map = new Map(false, 0, tileWidth, tileHeight);
	}
	
	public Map getMap() {
		return map;
	}

	private static int calcMaxTileHeight() {
		int d = (Frame.frameSize.height / Constants.tileSize);
		int max = 0;
		for (int i = 20; i < 200; i++) {
			if (d % i == 0) {
				max = i;
			}
		}
		return max;
	}

	private static int calcMaxTileWidth() {
		int d = (Frame.frameSize.width / Constants.tileSize);
		int max = 0;
		for (int i = 20; i < 200; i++) {
			if (d % i == 0) {
				max = i;
			}
		}
		return max;
	}
	
	public TileSet getRandomTileSet() {
		List<String> keys = new ArrayList<>(tileSets.keySet());
		return tileSets.get(keys.get(Constants.getRandomInt(keys.size())));
	}

	@Override
	public String getClassName() {
		return "Map Handler";
	}
}
