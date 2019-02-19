package com.advdev.display.interfaces.components;

import java.awt.*;

public class Text extends Component {
	private int alignment;
	private Color color;
	private String displayText;
	private int id;

	public Text(Rectangle bounds, String displayText, Border border, int alignment, Color color, int id) {
		super(bounds, border);
		this.displayText = displayText;
		this.alignment = alignment;
		this.color = color;
		this.id = id;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(this.color);
		if (this.alignment == CENTER) {
			int width = g.getFontMetrics().stringWidth(this.displayText);
			g.drawString(this.displayText, (this.bounds.x + (this.bounds.width / 2)) - (width / 2), this.bounds.y);
		} else if (this.alignment == LEFT) {
			g.drawString(this.displayText, this.bounds.x, this.bounds.y);
		} else if (this.alignment == RIGHT) {
			int width = g.getFontMetrics().stringWidth(this.displayText);
			g.drawString(this.displayText, (this.bounds.x - 5 - width) + this.bounds.width, this.bounds.y);
		}
	}

	public String getText() {
		return this.displayText;
	}

	public void setText(String displayText) {
		this.displayText = displayText;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return this.displayText;
	}

	public static final int CENTER = 1;
	public static final int LEFT = 0;
	public static final int RIGHT = 2;
}
