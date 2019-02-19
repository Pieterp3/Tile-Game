package com.advdev.game.objects;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import com.advdev.Initializable;
import com.advdev.Main;
import com.advdev.game.entities.player.Player;
import com.advdev.game.maps.pieces.Tile;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class ObjectHandler implements Initializable {
	private HashMap<Integer, ObjectDefinition[]> definitions = new HashMap<>();
	private HashMap<String, Integer> idsByName = new HashMap<>();
	private Player player;

	public GameObject createObjectByName(String string) {
		for (Integer s : definitions.keySet()) {
			for (ObjectDefinition d : definitions.get(s)) {
				if (d.getName().equals(string)) return new GameObject(d.getType(), d.getId());
			}
		}
		return null;
	}

	public ObjectDefinition getDefinition(int type, int id) {
		return definitions.get(type)[id];
	}

	public void init() {
		this.player = Main.getPlayer();
		List<String> defData = IOLoader.getTextFromFile("GameObjects/ObjectDefinitions");
		for (String line : defData) {
			String[] objDef = line.split("\t");
			int type = Integer
					.valueOf(objDef[0].contains("a") ? objDef[0].substring(0, objDef[0].length() - 2) : objDef[0]);
			int sections = -1;
			if (objDef[0].contains("a")) {
				sections = Integer.valueOf(objDef[0].substring(objDef[0].indexOf("a") + 1));
			}
			String[] sSizes = objDef[1].split(" ");
			int[] sizes = new int[sSizes.length];
			for (int i = 0; i < sizes.length; i++) {
				sizes[i] = Integer.valueOf(sSizes[i]);
			}
			String name = objDef[2];
			BufferedImage[] imgs = null;
			if (sizes[0] > 0) {
				imgs = IOLoader.splitImage("GameObjects/" + name, sizes[0], sizes[1]);
			}
			ObjectDefinition[] definitions = new ObjectDefinition[objDef.length - 3];
			for (int i = 3; i < objDef.length; i++) {
				String[] idDef = objDef[i].split("_");
				String idName = idDef[0];
				long animDelay = Long.valueOf(idDef[1]);
				String hoverText = idDef[2];
				boolean selfAnimated = Boolean.valueOf(idDef[3]);
				ObjectDefinition def = new ObjectDefinition(type, i - 3);
				def.setSelfAnimated(selfAnimated);
				def.setName(idName.equals(name) ? name : idName + " " + name);
				idsByName.put(def.getName(), i - 3);
				def.setDelay(animDelay);
				def.setHoverText(hoverText);
				if (sizes[0] == 0) {
					BufferedImage[] images = IOLoader.loadImagesFromFiles("GameObjects/Portals/" + idName, 1, 23);
					def.setImages(images);
				} else if (sections < 0) {
					def.setImages(imgs);
				} else {
					BufferedImage[] images = new BufferedImage[sections];
					Constants.partialArrayTransfer(imgs, images, (i - 3) * sections, ((i - 3) + 1) * sections);
					def.setImages(images);
				}
				definitions[i - 3] = def;
			}
			this.definitions.put(type, definitions);
		}
	}

	@Override
	public String getClassName() {
		return "Object Handler";
	}

	public void clickObject(Tile loc, GameObject obj) {
		
	}
}
