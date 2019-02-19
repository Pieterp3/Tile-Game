package com.advdev.display;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.List;

import javax.swing.JPanel;

import com.advdev.Main;
import com.advdev.display.interfaces.GameInterface;
import com.advdev.display.interfaces.components.Border;
import com.advdev.display.interfaces.components.Component;
import com.advdev.game.entities.player.Player;
import com.advdev.game.items.*;
import com.advdev.game.maps.pieces.*;
import com.advdev.misc.Constants;
import com.advdev.misc.IOLoader;

public class Screen extends JPanel {
	private int framesDrawn, fps;
	private String[] hoverText;
	private Color[] hoverColors;
	private Color[][] defaultHoverColors = new Color[][] {
			{
					Color.white, Color.yellow
			}, {
					Color.white, Color.yellow, Color.cyan
			}, {
					Color.white, Color.yellow, Color.white, Color.cyan
			}
	};
	private GameInterface displayText;
	private boolean displayTextbox;
	private long lastFps;
	private Tile lastHoveredTile;
	private Map map;
	private Player player;
	private Point mouse;
	private int screenWidth, screenHeight;

	public Screen(int width, int height) {
		screenWidth = width;
		screenHeight = height;
		Rectangle displayBounds = new Rectangle(10, screenHeight - 220, screenWidth - 20, 210);
		displayText = new GameInterface(displayBounds, new Border(5, Color.black), Color.lightGray);
		this.map = Main.getMapHandler().getMap();
		this.player = Main.getPlayer();
	}

	public void setDisplayText(Component... components) {
		displayText.clearComponents();
		displayTextbox = components != null;
		if (displayTextbox) {
			for (Component c : components) {
				displayText.addComponent(c);
			}
		}
	}
	
	public void clearHoverText() {
		hoverText = null;
		hoverColors = null;
	}

	public void click(Point p) {
		if (Main.getInterfaceManager().click(p, this)) { return; }
		// Handle clicking map tiles
		int x = p.x / Constants.tileSize;
		int y = p.y / Constants.tileSize;
		Tile t = this.map.getTile(x, y);
		player.setMovementPath(null);
		List<Tile> path = map.getPathFinder().findPath(player.getLocation(), t);
		player.setMovementPath(path);
		clickMapTile(t);
	}

	private void clickMapTile(Tile t) {
		this.lastHoveredTile = null;
		resetMousePosition();
		Item selected = this.player.getSelectedItem();
		Zone z = t.getZone();
		if (z != null) {
			if (selected != null) {
				z.useItemOn(selected);
			} else {
				z.click();
			}
		}
	}

	private String getBuiltHoverText() {
		String ret = "";
		for (String s : hoverText) {
			ret = (ret + s + " ");
		}
		return ret.substring(0, ret.length() - 1);
	}

	private void drawHoverText(Graphics2D g) {
		if (hoverText == null) { return; }
		int x = 8;
		int width = g.getFontMetrics().stringWidth(getBuiltHoverText());
		g.setColor(Color.black);
		g.fillRect(5, 5, width + 8, 20);
		g.setColor(Color.gray);
		g.fillRect(7, 7, width + 4, 16);
		for (int i = 0; i < hoverText.length; i++) {
			String text = hoverText[i];
			width = g.getFontMetrics().stringWidth(text);
			g.setColor(hoverColors[i]);
			g.drawString(text, x, 20);
			x += width + 3;
		}
	}

	private void drawMap(Graphics2D g) {
		int size = Constants.tileSize;
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				Tile tile = map.getTile(x, y);
				int drawX = (x * Constants.tileSize);
				int drawY = (y * Constants.tileSize);
				// Draw tiles base image
				g.drawImage(tile.getImage(), drawX, drawY, size, size, null);
				if (tile.isOccupied()) {
					g.drawImage(tile.getOccupantImage(), drawX, drawY, size, size, null);
				}
				if (Constants.fog) {
					Composite comp = g.getComposite();
					AlphaComposite opa = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) tile.getOpacity());
					g.setComposite(opa);
					g.drawImage(fog, drawX, drawY, size, size, null);
					g.setComposite(comp);
				}
				if (Constants.displayRegions) {// Display Region Shading
					Composite comp = g.getComposite();
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
					if (tile.getRegion() != null) {
						g.setColor(player.ownsRegion(tile.getRegion()) ? Color.white
								: regionShades[tile.getRegion().getId() % regionShades.length]);
						g.fillRect(drawX, drawY, size, size);
					}
					g.setComposite(comp);
				}
				if (player.getPath() != null) {
					List<Tile> path = player.getPath();
					g.setColor(Color.yellow);
					Composite comp = g.getComposite();
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
					if (path.contains(tile)) {
						g.fillOval(drawX + 10, drawY + 10, size - 20, size - 20);
					}
					g.setComposite(comp);
				}
			}
		}
		String str = this.map.getOwner() == null ? "Unconquered Map" : this.map.getOwner().getName() + "'s Map";
		int width = g.getFontMetrics().stringWidth(str);
		Rectangle r = new Rectangle(8, 30, width + 4, 16);
		g.setColor(Color.black);
		g.fill(r);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
		g.setColor(Color.black);
		g.drawString(str, r.x + 2, r.y + 12);
		Tile loc = player.getLocation();
		g.drawImage(this.player.getImage(), (loc.getX() * size) + this.player.getXOffset(),
				(loc.getY() * size) + this.player.getYOffset(), size, size, null);
	}

	public Tile getClickedTile(int clickX, int clickY) {
		int x = (clickX / Constants.tileSize);
		int y = (clickY / Constants.tileSize);
		return this.map.getTile(x, y);
	}

	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		Graphics2D g = (Graphics2D) g2;
		g.setColor(Color.black);
		g.fillRect(0, 0, screenWidth, screenHeight);
		drawMap(g);
		drawHoverText(g);
		Main.getInterfaceManager().drawInterfaces(g);
		if (displayTextbox) {
			displayText.draw(g);
		}
		String str = "Time Remaining: " + this.map.getTimeRemaining();
		int width = g.getFontMetrics().stringWidth(str);
		g.setColor(Color.yellow);
		g.fillRect(Frame.WIDTH - width - 16, Frame.HEIGHT - 32, width + 14, 24);
		g.setColor(Color.gray);
		g.fillRect(Frame.WIDTH - width - 14, Frame.HEIGHT - 30, width + 10, 20);
		g.setColor(Color.white);
		g.drawString(str, Frame.WIDTH - width - 5, Frame.HEIGHT - 15);
		str = "Fps: " + this.fps;
		width = g.getFontMetrics().stringWidth(str);
		g.drawString(str, Frame.WIDTH - width - 5, Frame.HEIGHT - 37);
		this.framesDrawn += 1;
		if ((System.currentTimeMillis() - this.lastFps) >= 1000) {
			this.fps = this.framesDrawn;
			this.framesDrawn = 0;
			this.lastFps = System.currentTimeMillis();
		}
	}

	public void resetMousePosition() {
		setMousePosition(mouse);
	}

	public void setHoverText(Color[] colors, String... hoverText) {
		if (hoverText == null) {
			this.hoverText = null;
			this.hoverColors = null;
			return;
		}
		this.hoverText = hoverText;
		if (colors == null) {
			for (Color[] cA : defaultHoverColors) {
				if (cA.length == hoverText.length) {
					hoverColors = cA;
					return;
				}
			}
			setHoverColorsToDefault();
		} else if (colors.length == hoverText.length) {
			hoverColors = colors;
		} else if (colors.length > hoverText.length) {
			hoverColors = new Color[hoverText.length];
			for (int i = 0; i < hoverColors.length; i++) {
				hoverColors[i] = colors[i];
			}
		} else {
			hoverColors = new Color[hoverText.length];
			for (int i = 0; i < colors.length; i++) {
				hoverColors[i] = colors[i];
			}
			for (int i = colors.length; i < hoverColors.length; i++) {
				hoverColors[i] = defaultHoverColors[defaultHoverColors.length - 1][(i - colors.length)
						% defaultHoverColors.length];
			}
		}
	}

	private void setHoverColorsToDefault() {
		hoverColors = new Color[hoverText.length];
		for (int i = 0; i < hoverText.length; i++) {
			hoverColors[i] = defaultHoverColors[defaultHoverColors.length - 1][i % defaultHoverColors.length];
		}
	}

	public void setMousePosition(Point mouse) {
		this.mouse = mouse;
		if (Main.getInterfaceManager().setMousePosition(mouse, this)) return;
		if (Main.getShopHandler().shopOpen()) {
			Main.getShopHandler().moveMouse(mouse);
			return;
		}
		Tile t = this.map.getTile(mouse.x / Constants.tileSize, mouse.y / Constants.tileSize);
		if (t == null) return;
		checkHoverTile(t);
	}

	private void checkHoverTile(Tile t) {
		if (t != this.lastHoveredTile) {
			Item selected = this.player.getSelectedItem();
			String[] hoverText = null;
			Color[] hoverColors = null;
			if (t.hasZone()) {
				Zone z = t.getZone();
				if (selected != null) {
					hoverText = new String[] {
							"Use", selected.getName(), "on", z.getContentType()
					};
				} else if (z.containsPortal()) {
					hoverText = new String[] {
							"Teleport to", "New Map"
					};
				} else if ((!z.isComplete() && (z.getOwner() == null)) || (z.getOwner() == this.player)) {
					hoverText = new String[] {
							"Enter", z.getContentType()
					};
				} else if (z.getOwner() == this.player) {
					hoverText = new String[] {
							"Collect from", z.getContentType(),
							"(" + z.getIdleProfits() + "/" + z.getMinToCollect() + ")"
					};
				} else {
					hoverColors = new Color[] {
							Color.white, Color.green, Color.yellow
					};
					hoverText = new String[] {
							"View", z.getOwner().getName() + "'s", z.getContentType()
					};
				}
			} else if (t.getTopLevelNpc() != null) {
				hoverText = selected != null ? new String[] {
						"Use", selected.getName(), "on", t.getTopLevelNpc().getName()
				} : new String[] {
						t.getTopLevelNpc().getDefinition().getOptions(), t.getTopLevelNpc().getName()
				};
			} else if (t.getTopLevelObject() != null) {
				hoverText = selected != null ? new String[] {
						"Use" + selected.getName() + "on", t.getTopLevelObject().getName()
				} : new String[] {
						t.getTopLevelObject().getHoverText(), t.getTopLevelObject().getName()
				};
			} else {
				clearHoverText();
			}
			this.lastHoveredTile = t;
			setHoverText(hoverColors, hoverText);
		}
	}

	private static BufferedImage fog = IOLoader.loadImage("Clouds");
	private static Color[] regionShades = {
			Color.red, Color.yellow, Color.BLUE, Color.green, Color.magenta, Color.orange
	};
	private static final long serialVersionUID = -472497304108417272L;
}