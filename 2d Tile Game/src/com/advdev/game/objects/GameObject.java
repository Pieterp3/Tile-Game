package com.advdev.game.objects;

import java.awt.image.BufferedImage;

import com.advdev.Main;
import com.advdev.display.interfaces.Drawable;
import com.advdev.game.items.Item;
import com.advdev.game.maps.Zoneable;
import com.advdev.game.maps.pieces.Tile;

public class GameObject implements Zoneable, Drawable {
	private long created, transformTime;
	private ObjectDefinition definition;
	private boolean harvested;
	private long last = System.currentTimeMillis();
	private double lifeTime;
	private int objectId, objectType, imageId = 0;
	private Item replaceWith;
	// Temporary Object variables
	private Tile spawnedAt;
	private boolean temporary, spawned, transformed;

	public GameObject(GameObject o) {
		this(o.objectType, o.objectId);
	}

	public GameObject(int objectType, int objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.definition = Main.getObjectHandler().getDefinition(objectType, objectId);
	}

	private void despawn() {
		this.spawnedAt.clearTopLevelObject();
	}

	public String getHoverText() {
		if (this.temporary && this.transformed) return "Pick up";
		return this.definition.getHoverText();
	}

	public int getId() {
		return this.objectId;
	}

	@Override
	public BufferedImage getImage() {
		if (this.temporary && this.transformed) return Main.getItemHandler().getImage(this.replaceWith);
		return this.definition.getImage(this.imageId);
	}

	public String getName() {
		return temporary && transformed ? replaceWith.getName() : definition.getName();
	}

	public int getType() {
		return this.objectType;
	}

	public void harvest() {
		this.harvested = true;
	}

	public boolean harvested() {
		return this.harvested;
	}

	public void process() {
		if (definition.isSelfAnimated() && ((System.currentTimeMillis() - this.last) > this.definition.getDelay())) {
			this.last = System.currentTimeMillis();
			this.imageId += 1;
			if (this.imageId == this.definition.getImageCount()) {
				this.imageId = 0;
			}
		}
		if (this.temporary && this.spawned) {
			if ((System.currentTimeMillis() - this.created) >= (this.lifeTime * 1000)) {
				this.transformed = true;
				this.transformTime = System.currentTimeMillis();
			}
			if (this.transformed) {
				if ((System.currentTimeMillis() - this.transformTime) >= 180000) {
					despawn();
				}
			}
		}
	}

	public void spawnTemporaryObject(Item item, double lifeTime, Tile spawnAt) {
		this.temporary = true;
		this.replaceWith = item;
		this.lifeTime = lifeTime;
		this.spawnedAt = spawnAt;
		spawnAt.setTopLevelGameObject(this);
		this.created = System.currentTimeMillis();
		this.spawned = true;
	}
}
