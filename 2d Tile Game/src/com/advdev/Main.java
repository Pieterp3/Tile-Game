package com.advdev;

import com.advdev.display.Frame;
import com.advdev.display.interfaces.handlers.DialogueHandler;
import com.advdev.display.interfaces.handlers.InterfaceManager;
import com.advdev.engine.Engine;
import com.advdev.game.entities.npcs.NpcHandler;
import com.advdev.game.entities.player.Player;
import com.advdev.game.items.ItemHandler;
import com.advdev.game.maps.MapHandler;
import com.advdev.game.maps.shops.ShopHandler;
import com.advdev.game.objects.ObjectHandler;

public class Main {
	private static Engine engine;
	private static Frame frame;
	private static Player player;
	private static Initializable[] initialClasses = new Initializable[] {
			new MapHandler(), new ItemHandler(), new ObjectHandler(), new ShopHandler(), new NpcHandler(),
			new InterfaceManager()
	};
	
	public static Frame getFrame() {
		return frame;
	}
	
	public static MapHandler getMapHandler() {
		return (MapHandler) initialClasses[0];
	}

	public static ItemHandler getItemHandler() {
		return (ItemHandler) initialClasses[1];
	}

	public static ObjectHandler getObjectHandler() {
		return (ObjectHandler) initialClasses[2];
	}

	public static ShopHandler getShopHandler() {
		return (ShopHandler) initialClasses[3];
	}

	public static NpcHandler getNpcHandler() {
		return (NpcHandler) initialClasses[4];
	}

	public static InterfaceManager getInterfaceManager() {
		return (InterfaceManager) initialClasses[5];
	}

	public static Player getPlayer() {
		return player;
	}

	public static void main(String[] args) {
		long mainStart = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		player = new Player(0, null, "Default Player");
		System.out.println("Player initialized in " + (System.currentTimeMillis() - start) + " ms");
		for (Initializable init : initialClasses) {
			start = System.currentTimeMillis();
			init.init();
			System.out.println(init.getClassName() + " initialized in " + (System.currentTimeMillis() - start) + " ms");
		}
		getMapHandler().getMap().populateMap();
		getItemHandler().giveAllItems(player);
		player.createHomeMap();
		player.setMap(getMapHandler().getMap());
		start = System.currentTimeMillis();
		frame = new Frame();
		DialogueHandler.init();
		System.out.println("Frame initialized in " + (System.currentTimeMillis() - start) + " ms");
		start = System.currentTimeMillis();
		engine = new Engine(frame.getScreen());
		engine.start();
		System.out.println("Engine initialized in " + (System.currentTimeMillis() - start) + " ms");
		start = System.currentTimeMillis();
		System.out.println("Game initialized in " + (System.currentTimeMillis() - mainStart) + " ms");
	}
}
