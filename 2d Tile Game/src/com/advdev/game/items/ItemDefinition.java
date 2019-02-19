package com.advdev.game.items;

public class ItemDefinition {
	private String description;
	private String hoverText;
	private String name;
	private long price;
	private int slot;
	private boolean stacks;

	public ItemDefinition(String name, String hoverText, int slot, boolean stacks, long price, String description) {
		this.name = name;
		this.description = description;
		this.hoverText = hoverText;
		this.slot = slot;
		this.price = price;
	}

	public String getDescription() {
		return this.description;
	}

	public String getHoverText() {
		return this.hoverText;
	}

	public String getName() {
		return this.name;
	}

	public long getPrice() {
		return this.price;
	}

	public int getSlot() {
		return this.slot;
	}

	public boolean stacks() {
		return this.stacks;
	}
}
