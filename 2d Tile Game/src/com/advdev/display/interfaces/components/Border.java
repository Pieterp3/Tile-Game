package com.advdev.display.interfaces.components;

import java.awt.*;

public class Border {
	private Color normal, hovered;
	private int size;

	public Border(int size, Color color) {
		this(size, color, color);
	}

	public Border(int size, Color normalColor, Color hoverColor) {
		this.size = size;
		this.normal = normalColor;
		this.hovered = hoverColor;
	}

	public Color getColor(boolean hover) {
		return hover ? this.hovered : this.normal;
	}

	public int getSize() {
		return this.size;
	}

	public void draw(Graphics2D g, Rectangle bounds, boolean hovered) {
		g.setColor(hovered ? this.hovered : normal);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(size));
		g.draw(bounds);
		g.setStroke(s);
	}

	public void setColor(Color normal) {
		this.normal = normal;
	}
}
