package com.advdev.display.interfaces.components;

import java.awt.*;

import com.advdev.game.maps.pieces.Region;

public class ProgressBar extends Component {
	private Color back, front;

	public ProgressBar(Rectangle bounds, Border border, Color back, Color front) {
		super(bounds, border);
		this.back = back;
		this.front = front;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(this.border.getColor(false));
		g.fill(this.bounds);
		g.setColor(this.back);
		g.fillRect(this.bounds.x + this.border.getSize(), this.bounds.y + this.border.getSize(),
				this.bounds.width - (this.border.getSize() * 2), this.bounds.height - (this.border.getSize() * 2));
		Region region = player.getLocation().getRegion();
		if (region == null) return;
		Rectangle r = new Rectangle(this.bounds.x + 2, this.bounds.y + 2,
				(int) ((this.bounds.width - (this.border.getSize() * 2)) * region.getCompletion()),
				this.bounds.height - (this.border.getSize() * 2));
		g.setColor(this.front);
		g.fill(r);
	}
}
