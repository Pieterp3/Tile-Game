package com.advdev.display.interfaces;

import java.awt.*;

import com.advdev.display.Screen;
import com.advdev.display.interfaces.components.Border;
import com.advdev.display.interfaces.components.Component;
import com.advdev.display.interfaces.components.Text;

public class GameInterface extends Component implements Clickable {
	private boolean visible;
	private Color background;

	public GameInterface(Rectangle bounds, Border border, Color background) {
		super(bounds, border);
		this.background = background;
	}

	public GameInterface(Rectangle bounds, Color background) {
		this(bounds, null, background);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean moveMouse(Screen screen, Point loc) {
		for (Component c : components) {
			if (c instanceof Clickable) {
				if (((Clickable) c).moveMouse(screen, loc)) { return true; }
			}
		}
		return bounds.contains(loc);
	}

	@Override
	public boolean click(Screen screen, Point loc) {
		for (Component c : components) {
			if (c instanceof Clickable) {
				((Clickable) c).click(screen, loc);
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(background);
		g.fill(bounds);
		border.draw(g, bounds, false);
		for (Component c : components) {
			c.draw(g);
		}
	}

	public Text getTextComponent(int id) {
		for (Component c : components) {
			if (c instanceof Text) {
				if (((Text) c).getId() == id) { return ((Text) c); }
			}
		}
		return null;
	}

	public void clearComponents() {
		components.clear();
	}
}
