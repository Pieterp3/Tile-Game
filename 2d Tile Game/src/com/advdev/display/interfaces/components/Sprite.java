package com.advdev.display.interfaces.components;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.advdev.display.interfaces.*;

public class Sprite extends Component implements Hoverable, Drawable {
	private BufferedImage[] imgs;
	private boolean hovered;
	private int imgIndex;
	private long nextAnim;
	private int animDelay = 100;

	public Sprite(Rectangle bounds, Border border, BufferedImage[] bufferedImages) {
		super(bounds, border);
		this.imgs = bufferedImages;
		imgIndex = 0;
		nextAnim = System.currentTimeMillis() + animDelay;
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(), bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	@Override
	public String[] getHoverText() {
		return null;
	}

	@Override
	public Color[] getHoverColors() {
		return null;
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
	public void setHoverData(Color[] colors, String... hoverText) {
		throw new UnsupportedOperationException("Hover text not yet supported in sprites");
	}

	public void process() {
		if (nextAnim < System.currentTimeMillis()) {
			imgIndex += 1;
			if (imgIndex == imgs.length) {
				imgIndex = 0;
			}
			nextAnim = System.currentTimeMillis() + animDelay;
		}
	}

	@Override
	public BufferedImage getImage() {
		return imgs.length == 2 ? (hovered ? imgs[1] : imgs[0]) : imgs[imgIndex];
	}
}
