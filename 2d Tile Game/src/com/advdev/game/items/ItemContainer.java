package com.advdev.game.items;

public class ItemContainer {
	private Item[] items;

	public ItemContainer(int size) {
		this.items = new Item[size];
	}

	public void addItem(Item item) {
		int slot = getSlotForItem(item);
		if (slot == -1) return;//Drop the item on the ground, not enough space
		if (this.items[slot] != null) {
			this.items[slot].addQty(item.getQty());
		} else {
			this.items[slot] = new Item(item);
		}
	}

	public boolean containsItem(Item item) {
		for (Item i2 : this.items) {
			if (i2 == null) {
				continue;
			}
			if ((i2.getId() == item.getId()) && (i2.getType() == item.getType()) && (i2.getQty() >= item.getQty()))
				return true;
		}
		return false;
	}

	public boolean containsItems(Item... items) {
		for (int i = 0; i < items.length; i++) {
			if (!containsItem(items[i])) return false;
		}
		return true;
	}

	public Item getItem(int slot) {
		return this.items[slot];
	}

	public Item[] getItems() {
		return this.items;
	}

	private int getSlotForItem(Item item) {
		int firstNull = -1;
		for (int i = 0; i < this.items.length; i++) {
			if ((firstNull == -1) && (this.items[i] == null)) {
				firstNull = i;
			} else if (this.items[i] != null) {
				if ((this.items[i].getType() == item.getType()) && (this.items[i].getId() == item.getId())
						&& item.isStackable())
					return i;
			}
		}
		return firstNull;
	}

	public boolean hasItem(Item item) {
		for (Item i : this.items) {
			if (i == null) {
				continue;
			}
			if ((i.getType() == item.getType()) && (i.getId() == item.getId())) return (i.getQty() >= item.getQty());
		}
		return false;
	}

	public void removeItem(int slot, long qty) {
		if (this.items[slot] == null) return;
		if (this.items[slot].getQty() > qty) {
			this.items[slot].removeQty(qty);
		} else {
			this.items[slot] = null;
		}
	}

	public void removeItem(Item item) {
		long qty = item.getQty();
		for (int j = 0; j < this.items.length; j++) {
			Item i = this.items[j];
			if ((i == null) || (i.getId() != item.getId()) || (i.getType() != item.getType())) {
				continue;
			}
			if (i.getQty() > qty) {
				i.removeQty(qty);
				break;
			} else if (i.getQty() == qty) {
				this.items[j] = null;
				break;
			} else {
				qty -= this.items[j].getQty();
				this.items[j] = null;
			}
		}
	}

	public int getItemCount() {
		int ct = 0;
		for (Item i : items) {
			if (i != null) { ct += 1; }
		}
		return ct;
	}
	
	public int getMaxItemCount() {
		return items.length;
	}
}
