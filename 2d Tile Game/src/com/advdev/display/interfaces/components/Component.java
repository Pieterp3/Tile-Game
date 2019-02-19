package com.advdev.display.interfaces.components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.advdev.Main;
import com.advdev.game.entities.player.Player;

public abstract class Component {
	protected Border border;
	protected Rectangle bounds;
	protected List<Component> components;
	protected Player player;

	public Component(Rectangle bounds, Border border) {
		this.bounds = bounds;
		this.border = border;
		this.components = new ArrayList<>();
		this.player = Main.getPlayer();
	}

	public abstract void draw(Graphics2D g);
	
	public void addComponent(Component c) {
		this.components.add(c);
	}

	public Border getBorder() {
		return this.border;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public List<Component> getComponents() {
		return this.components;
	}
}
