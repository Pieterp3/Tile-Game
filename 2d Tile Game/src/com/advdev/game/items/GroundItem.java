package com.advdev.game.items;

public class GroundItem {
	private long created;
	private Item item;
	private long timeInWorld;

	public GroundItem(Item item) {
		this(item, defaultTime);
	}

	public GroundItem(Item item, long timeInWorld) {
		this.item = item;
		this.created = System.currentTimeMillis();
		this.timeInWorld = timeInWorld;
	}

	public boolean expired() {
		return (System.currentTimeMillis() - this.created) > this.timeInWorld;
	}

	public Item getItem() {
		return this.item;
	}

	private static final long defaultTime = 180000;// 3 minutes
}
