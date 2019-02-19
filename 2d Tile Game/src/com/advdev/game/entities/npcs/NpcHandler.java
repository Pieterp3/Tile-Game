package com.advdev.game.entities.npcs;

import java.util.*;

import com.advdev.Initializable;
import com.advdev.Main;
import com.advdev.game.entities.Entity;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class NpcHandler implements Initializable {
	private HashMap<Integer, NpcDefinition> definitions = new HashMap<>();
	private List<Integer> shopNpcs = new ArrayList<>();

	public void addShopNpc(int id) {
		shopNpcs.add(id);
	}

	public void clickNpc(Entity entity, Npc npc) {
		if (Constants.debug) {
			System.out.println(entity.getName() + " clicked " + npc.getName());
		}
		if (isShopNpc(npc.getId())) {
			System.out.println("Open Shop: " + npc.getId());
			Main.getShopHandler().openShop(npc.getId());
			return;
		}
	}

	public NpcDefinition getDefinition(int id) {
		return definitions.get(id);
	}

	public void init() {
		List<String> lines = IOLoader.getTextFromFile("Npcs/NpcDefinitions");
		for (String line : lines) {
			String[] defData = line.split("\t");
			int id = Integer.valueOf(defData[0]);
			String name = defData[1];
			String options = defData[2];
			int width = Integer.valueOf(defData[3]);
			int height = Integer.valueOf(defData[4]);
			NpcDefinition definition = new NpcDefinition(name, id, options, width, height);
			definitions.put(id, definition);
		}
	}

	private boolean isShopNpc(int id) {
		for (int i : shopNpcs) {
			if (i == id) return true;
		}
		return false;
	}

	@Override
	public String getClassName() {
		return "Npc Handler";
	}
}
