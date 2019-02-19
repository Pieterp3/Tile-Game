package com.advdev.game.items;

import java.awt.image.BufferedImage;

import com.advdev.Main;
import com.advdev.display.interfaces.Drawable;

public class Item implements Drawable {
	private ItemDefinition definition;
	private int id, type;
	private long qty;

	public Item(int type, int id, long qty) {
		this.id = id;
		this.type = type;
		this.qty = qty;
		this.definition = Main.getItemHandler().getDefinition(type, id);
	}

	public Item(Item item) {
		this(item.getType(), item.getId(), item.getQty());
	}

	public Item(String type, int id, long qty) {
		this(Main.getItemHandler().getType(type), id, qty);
	}

	public void addQty(long qty) {
		this.qty += qty;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public BufferedImage getImage() {
		return Main.getItemHandler().getImage(this.type, this.id);
	}

	public String getName() {
		return this.definition.getName();
	}

	public long getPrice() {
		return this.definition.getPrice();
	}

	public long getQty() {
		return this.qty;
	}

	public int getSlot() {
		return this.definition.getSlot();
	}

	public int getType() {
		return this.type;
	}

	public String getUses() {
		return this.definition.getHoverText();
	}

	public boolean isStackable() {
		return this.definition.stacks();
	}

	public void removeQty(long qty) {
		this.qty -= qty;
	}
}
