package com.advdev.game.entities.skills;

import com.advdev.game.entities.Entity;
import com.advdev.game.items.Item;
import com.advdev.game.items.ItemContainer;
import com.advdev.game.objects.GameObject;

public class Firemaking extends Skill {
	public Firemaking(Entity entity) {
		this(entity, 0);
	}

	public Firemaking(Entity entity, double exp) {
		super(entity, "Firemaking", exp);
	}

	public void createFire(Item o, Item t) {
		ItemContainer inventory = this.entity.getInventory();
		if (!inventory.containsItems(o, t)) return;
		if (entity.getLocation().isOccupied()) return;
		//Check firemaking level depending on log type
		inventory.removeItem(o.getName().contains("Logs") ? o : t);
		GameObject campfire = new GameObject(2, 0);
		campfire.spawnTemporaryObject(new Item(2, 0, 1), 45, entity.getLocation());
	}
}
