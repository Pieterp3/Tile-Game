package com.advdev.game.maps.shops;

import com.advdev.game.items.Item;

public class Shop {
	private int coins;
	private Item[] items;
	private String name;
	public static final int itemCap = 64;

	public Shop(String name, Item[] defaultItems, int coins) {
		this.name = name;
		this.items = new Item[250];
		setItems(defaultItems);
		this.coins = coins;
	}

	public int getCoins() {
		return this.coins;
	}

	public Item[] getItems() {
		return this.items;
	}

	public String getName() {
		return this.name;
	}

	public void setItems(Item[] items) {
		this.items = new Item[itemCap];
		for (int i = 0; i < itemCap; i++) {
			if (i == items.length) break;
			this.items[i] = items[i];
		}
	}

	public Item getItem(int i) {
		return items[i];
	}
}
