package com.advdev.display.interfaces.components;

import java.awt.*;

public class Line extends Component {
	private Color color;
	private int thickness;

	public Line(Rectangle points, Color color, int thickness) {
		super(points, null);
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g) {
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(thickness));
		g.setColor(this.color);
		g.drawLine(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
		g.setStroke(s);
	}
}
