package com.advdev.display.interfaces;

import java.awt.Color;

public interface Hoverable {
	public String[] getHoverText();

	public Color[] getHoverColors();

	public boolean isHovered();

	public void setHovered(boolean hovered);

	public void setHoverData(Color[] colors, String... hoverText);
}
