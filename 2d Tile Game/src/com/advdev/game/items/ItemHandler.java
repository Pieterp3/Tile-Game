package com.advdev.game.items;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import com.advdev.Initializable;
import com.advdev.Main;
import com.advdev.game.entities.npcs.Npc;
import com.advdev.game.entities.player.Player;
import com.advdev.game.entities.skills.Firemaking;
import com.advdev.game.objects.GameObject;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class ItemHandler implements Initializable {
	private HashMap<Integer, ItemDefinition[]> itemDefinitions = new HashMap<>();
	private HashMap<Integer, BufferedImage[]> itemImages = new HashMap<>();
	private HashMap<String, Integer> typeIdsByName = new HashMap<>();
	private Player player;
	private ItemContainer playerInventory;

	public void clickItem(int slot) {
		Item selected = player.getSelectedItem();
		if (selected == null) {
			player.setSelectedItem(playerInventory.getItem(slot));
		} else if (selected == playerInventory.getItem(slot)) {
			player.setSelectedItem(null);
		} else {
			itemOnItem(playerInventory.getItem(slot), selected);
		}
		Main.getFrame().getScreen().resetMousePosition();
	}

	public ItemDefinition getDefinition(int type, int id) {
		return itemDefinitions.get(type)[id];
	}

	public BufferedImage getImage(int type, int id) {
		return itemImages.get(type)[id];
	}

	public BufferedImage getImage(Item item) {
		return getImage(item.getType(), item.getId());
	}

	public int getType(String type) {
		return typeIdsByName.get(type);
	}

	public void giveAllItems(Player player) {
		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < itemDefinitions.size(); i++) {// for each type of object
				for (int j = 0; j < itemDefinitions.get(i).length; j++) {// for each object of that type
					player.getInventory().addItem(new Item(i, j, 1));
				}
			}
		}
	}

	@Override
	public void init() {
		player = Main.getPlayer();
		playerInventory = player.getInventory();
		List<String> definitionData = IOLoader.getTextFromFile("Items/ItemDefinitions");
		for (String line : definitionData) {
			String[] data = line.split("\t");
			int objectType = Integer.valueOf(data[0]);
			ItemDefinition[] definitions = new ItemDefinition[data.length - 2];
			String imageName = data[1];
			typeIdsByName.put(imageName, objectType);
			BufferedImage[] loadedObjectImages = IOLoader.splitImage("items/" + imageName, 64, 64);
			itemImages.put(objectType, loadedObjectImages);
			for (int i = 2; i < data.length; i++) {
				String[] defData = data[i].split("_");
				String name = defData[0];
				String hoverText = defData[1];
				int slot = Integer.valueOf(defData[2]);
				boolean stacks = Boolean.valueOf(defData[3]);
				int price = Integer.valueOf(defData[4]);
				String description = defData[5];
				ItemDefinition definition = new ItemDefinition(name, hoverText, slot, stacks, price, description);
				definitions[i - 2] = definition;
			}
			itemDefinitions.put(objectType, definitions);
		}
	}

	private boolean ioi(int check1, int check2, Item i, Item i2) {
		return ((check1 == i.getType()) && (check2 == i2.getType()))
				|| ((check2 == i.getType()) && (check1 == i2.getType()));
	}

	public void itemOnItem(Item item, Item selected) {
		if (Constants.debug) {
			System.out.println(selected.getName() + " on " + item.getName());
		}
		if (ioi(0, 1, item, selected)) {
			((Firemaking) Main.getPlayer().getSkill("Firemaking")).createFire(item, selected);
		}
		Main.getPlayer().setSelectedItem(null);
	}

	public void itemOnNpc(Npc npc, Item item) {
		if (Constants.debug) {
			System.out.println("Used " + item.getName() + " on " + npc.getName());
		}
	}

	public void itemOnObject(GameObject obj, Item item) {
		if (Constants.debug) {
			System.out.println("Used " + item.getName() + " on " + obj.getName());
		}
	}

	@Override
	public String getClassName() {
		return "Item Handler";
	}
}
