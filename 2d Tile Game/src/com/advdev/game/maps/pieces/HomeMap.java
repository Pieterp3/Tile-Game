package com.advdev.game.maps.pieces;

import com.advdev.game.entities.Entity;

public class HomeMap extends Map {
	private Entity owner;

	public HomeMap(Entity owner) {
		super(true, owner.getId() + 2500000, 20, 20);
		this.owner = owner;
	}
	
	public Entity getOwner() {
		return owner;
	}
}
