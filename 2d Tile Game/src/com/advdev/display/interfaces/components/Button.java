package com.advdev.display.interfaces.components;

import java.awt.*;

import com.advdev.display.Screen;
import com.advdev.display.interfaces.*;
import com.advdev.display.interfaces.handlers.ButtonHandler;

public class Button extends Component implements Hoverable, Clickable {
	private int id;
	private boolean hovered;
	private Drawable drawable;
	private Color background, hoverBackground;
	private String[] hoverText;
	private Color[] hoverColors;
	private Rectangle drawableBounds;

	private Button(Rectangle bounds, Border border, int id) {
		super(bounds, border);
		this.id = id;
	}

	public Button(Rectangle bounds, Border border, Drawable drawable, int id) {
		this(bounds, border, id);
		this.drawable = drawable;
		if (drawable == null) {
			background = Color.darkGray;
			hoverBackground = Color.lightGray;
		}
	}

	public Button(Rectangle bounds, Border border, Rectangle drawableBounds, Drawable drawable, int id) {
		this(bounds, border, drawable, id);
		this.drawableBounds = drawableBounds;
	}

	public Button(Rectangle bounds, Border border, Color background, Color hoverBackground, int id) {
		this(bounds, border, id);
		this.background = background;
		this.hoverBackground = hoverBackground;
	}

	public int getId() {
		return id;
	}

	@Override
	public String[] getHoverText() {
		return hoverText;
	}

	@Override
	public boolean isHovered() {
		return hovered;
	}

	@Override
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(hovered ? background : hoverBackground);
		g.fill(bounds);
		if (drawable != null) {
			Rectangle b = drawableBounds == null ? bounds : drawableBounds;
			g.drawImage(drawable.getImage(), b.x + border.getSize(), b.y + border.getSize(),
					b.width - (2 * border.getSize()), b.height - (2 * border.getSize()), null);
		}
		for (Component c : components) {
			c.draw(g);
		}
		if (border != null) {
			border.draw(g, bounds, hovered);
		}
	}

	@Override
	public Color[] getHoverColors() {
		return hoverColors;
	}

	@Override
	public void setHoverData(Color[] colors, String... hoverText) {
		this.hoverColors = colors;
		this.hoverText = hoverText;
	}

	@Override
	public boolean click(Screen screen, Point loc) {
		if (bounds.contains(loc)) {
			ButtonHandler.clickButton(screen, this);
		}
		return bounds.contains(loc);
	}

	@Override
	public boolean moveMouse(Screen screen, Point loc) {
		setHovered(bounds.contains(loc));
		if (drawable instanceof Hoverable) {
			((Hoverable) drawable).setHovered(hovered);
		}
		if (hovered) {
			screen.setHoverText(hoverColors, hoverText);
		}
		return hovered;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
}
