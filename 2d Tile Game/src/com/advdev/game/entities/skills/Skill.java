package com.advdev.game.entities.skills;

import com.advdev.game.entities.Entity;

public abstract class Skill {
	protected Entity entity;
	private double exp;
	private String name;

	public Skill(Entity entity, String name) {
		this(entity, name, 0);
	}

	public Skill(Entity entity, String name, double exp) {
		this.name = name;
		this.exp = exp;
		this.entity = entity;
	}

	public void addExp(double exp) {
		this.exp += exp;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public double getExp() {
		return this.exp;
	}

	public String getName() {
		return this.name;
	}
}
