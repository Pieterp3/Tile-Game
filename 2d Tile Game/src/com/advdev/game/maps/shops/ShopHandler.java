package com.advdev.game.maps.shops;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import com.advdev.Initializable;
import com.advdev.Main;
import com.advdev.display.interfaces.handlers.InterfaceManager;
import com.advdev.game.items.Item;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class ShopHandler implements Initializable {
	private Shop openShop = null;
	private int[] purchaseQuantities = new int[] {
			1, 5, 10, 25, 50, 100, 250
	};
	private int purchaseQuantityIndex = 0;
	private HashMap<Integer, Shop> shops = new HashMap<>();

	public Shop getOpenShop() {
		return openShop;
	}

	public int getPurchaseQuantity() {
		return purchaseQuantities[purchaseQuantityIndex];
	}

	@Override
	public void init() {
		List<String> defaultShops = IOLoader.getTextFromFile("Shops");
		for (String line : defaultShops) {
			String[] args = line.split("\t");
			String name = args[0];
			int coins = Integer.valueOf(args[1]);
			int shopId = Integer.valueOf(args[2]);
			Main.getNpcHandler().addShopNpc(shopId);
			Item[] items = new Item[args.length - 3];
			for (int i = 3; i < args.length; i++) {
				int type = Integer.valueOf(args[i].split(" ")[0]);
				int id = Integer.valueOf(args[i].split(" ")[1]);
				long quantity = Long.valueOf(args[i].split(" ")[2]);
				Item item = new Item(type, id, quantity);
				items[i - 3] = item;
			}
			Shop shop = new Shop(name, items, coins);
			shops.put(shopId, shop);
		}
	}

	public void moveMouse(Point mouse) {}

	public void openShop(int id) {
		openShop = shops.get(id);
		Main.getInterfaceManager().openInterface(InterfaceManager.ShopScreen);
		if (Constants.debug) System.out.println("Open Shop: " + id + " (" + openShop + ")");
	}

	public void setPurchaseQuantity(int index) {
		purchaseQuantityIndex = index;
	}

	public boolean shopOpen() {
		return openShop != null;
	}

	@Override
	public String getClassName() {
		return "Shop Handler";
	}
}
