package com.advdev.game.entities;

import com.advdev.display.interfaces.Drawable;
import com.advdev.game.entities.skills.Skill;
import com.advdev.game.entities.skills.SkillHandler;
import com.advdev.game.items.ItemContainer;
import com.advdev.game.maps.Zoneable;
import com.advdev.game.maps.pieces.HomeMap;
import com.advdev.game.maps.pieces.Tile;

public abstract class Entity implements Zoneable, Drawable {
	private long coins;
	private int id;
	private ItemContainer inventory;
	private String name;
	private SkillHandler skillHandler;
	protected Tile location;
	private HomeMap home;

	public Entity(int id, Tile location, String name) {
		this.name = name;
		this.id = id;
		this.location = location;
		this.skillHandler = new SkillHandler(this);
		this.inventory = new ItemContainer(50);
	}
	
	public void createHomeMap() {
		home  = new HomeMap(this);
	}
	
	public HomeMap getHomeMap() {
		return home;
	}

	public void addCoins(int toAdd) {
		this.coins += toAdd;
	}

	public long getCoins() {
		return this.coins;
	}

	public int getId() {
		return this.id;
	}

	public ItemContainer getInventory() {
		return this.inventory;
	}

	public String getName() {
		return this.name;
	}

	public Skill getSkill(String name) {
		return this.skillHandler.getSkill(name);
	}

	public Tile getLocation() {
		return location;
	}

	public abstract void process();

	public void setLocation(Tile location) {
		this.location = location;
	}

	public void takeCoins(int toTake) {
		this.coins -= toTake;
	}
}
