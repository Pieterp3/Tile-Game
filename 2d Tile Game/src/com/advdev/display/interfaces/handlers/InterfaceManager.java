package com.advdev.display.interfaces.handlers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.advdev.Initializable;
import com.advdev.Main;
import com.advdev.display.Frame;
import com.advdev.display.Screen;
import com.advdev.display.interfaces.GameInterface;
import com.advdev.display.interfaces.components.*;
import com.advdev.display.interfaces.components.Button;
import com.advdev.display.interfaces.components.Component;
import com.advdev.game.entities.player.Player;
import com.advdev.game.items.Item;
import com.advdev.game.items.ItemContainer;
import com.advdev.game.maps.shops.Shop;
import com.advdev.misc.IOLoader;

public class InterfaceManager implements Initializable {
	private List<GameInterface> interfaces = new ArrayList<>();
	private GameInterface openInterface;
	private Player player;
	private static Dimension screenSize = Frame.frameSize;
	private static int midScreenX = screenSize.width / 2, midScreenY = screenSize.height / 2;
	private static BufferedImage[] xButtonImgs = IOLoader.splitImage("Buttons/X", 64, 64);
	private static BufferedImage[] inventorySwapButtonImgs = IOLoader.splitImage("Buttons/Inventory", 64, 64);
	private static BufferedImage[] optionsSwapButtonImgs = IOLoader.splitImage("Buttons/Options", 64, 64);
	private static BufferedImage[] coins = IOLoader.splitImage("Coins", 100, 100);
	private static Border defaultBorder = new Border(2, Color.black, Color.darkGray);
	private static Rectangle defaultBounds = new Rectangle(midScreenX - 300, midScreenY - 175, 600, 350);

	public void init() {
		player = Main.getPlayer();
		createInventory();
		createOptions();
		createShopInterface();
	}

	private void createOptions() {
		GameInterface optionsInterface = new GameInterface(defaultBounds, defaultBorder, Color.gray);
		// Create Options interface components
		// 'x' Button
		Rectangle xButtonBounds = new Rectangle(defaultBounds.x + defaultBounds.width - 20, defaultBounds.y, 20, 20);
		Sprite xButtonSprite = new Sprite(xButtonBounds, defaultBorder, xButtonImgs);
		Button xButton = new Button(xButtonBounds, defaultBorder, xButtonSprite, -1);
		optionsInterface.addComponent(xButton);
		// Title
		Rectangle titleBounds = new Rectangle(defaultBounds.x, defaultBounds.y + 25, defaultBounds.width, 20);
		String titleText = "Options";
		Text optionsTitle = new Text(titleBounds, titleText, null, Text.CENTER, Color.WHITE, -1);
		optionsInterface.addComponent(optionsTitle);
		// Inventory Swap-to
		Rectangle inventorySwapBounds = new Rectangle(defaultBounds.x + defaultBounds.width - 40, defaultBounds.y, 20,
				20);
		Sprite inventorySwapSprite = new Sprite(inventorySwapBounds, defaultBorder, inventorySwapButtonImgs);
		Button inventorySwap = new Button(inventorySwapBounds, defaultBorder, inventorySwapSprite, 0);
		optionsInterface.addComponent(inventorySwap);
		String[] optionsButtons = {
				"Toggle Fog", "Toggle Region View", "Quit Game"
		};
		for (int i = 0; i < optionsButtons.length; i++) {
			Rectangle buttonBounds = new Rectangle(defaultBounds.x + 20, defaultBounds.y + 50 + (i * 33), 150, 30);
			Border itemHolderBorder = new Border(2, Color.black, Color.yellow);
			Button button = new Button(buttonBounds, itemHolderBorder, Color.DARK_GRAY, Color.LIGHT_GRAY, 20 + i);
			Rectangle textBounds = new Rectangle(buttonBounds.x, buttonBounds.y + 20, buttonBounds.width,
					buttonBounds.height);
			Text buttonText = new Text(textBounds, optionsButtons[i], null, Text.CENTER, Color.white, -1);
			button.addComponent(buttonText);
			optionsInterface.addComponent(button);
		}
		interfaces.add(OptionsScreen, optionsInterface);
	}

	private void createShopInterface() {
		GameInterface shopInterface = new GameInterface(defaultBounds, defaultBorder, Color.GRAY);
		// Create shop screen
		// 'X' Button
		Rectangle xButtonBounds = new Rectangle(defaultBounds.x + defaultBounds.width - 20, defaultBounds.y, 20, 20);
		Sprite xButtonSprite = new Sprite(xButtonBounds, defaultBorder, xButtonImgs);
		Button xButton = new Button(xButtonBounds, defaultBorder, xButtonSprite, -1);
		shopInterface.addComponent(xButton);
		// Title
		Rectangle titleBounds = new Rectangle(defaultBounds.x, defaultBounds.y + 25, defaultBounds.width, 20);
		String titleText = "Shop Name";
		Text shopTitle = new Text(titleBounds, titleText, null, Text.CENTER, Color.WHITE, 1);
		shopInterface.addComponent(shopTitle);
		// Item Display
		int itemHolderX = defaultBounds.x + 20;
		int itemHolderXReset = itemHolderX;
		int itemHolderY = defaultBounds.y + 40;
		int itemHolderSize = 32;
		for (int i = 0; i < Shop.itemCap; i++) {
			Rectangle itemHolderBounds = new Rectangle(itemHolderX, itemHolderY, itemHolderSize, itemHolderSize);
			Border itemHolderBorder = new Border(2, Color.black, Color.yellow);
			Button itemHolder = new Button(itemHolderBounds, itemHolderBorder, null, 1500 + i);
			itemHolderX += 35;
			if ((i + 1) % 8 == 0) {
				itemHolderX = itemHolderXReset;
				itemHolderY += 35;
			}
			shopInterface.addComponent(itemHolder);
		}
		// Coins sprite
		Border coinBorder = new Border(2, Color.WHITE);
		Rectangle coinBounds = new Rectangle(defaultBounds.x + 5, defaultBounds.y + defaultBounds.height - 23, 20, 20);
		Sprite coinsSprite = new Sprite(coinBounds, coinBorder, coins);
		shopInterface.addComponent(coinsSprite);
		// End inventory
		interfaces.add(ShopScreen, shopInterface);
	}

	private void updateShop() {
		Shop shop = Main.getShopHandler().getOpenShop();
		for (Component c : openInterface.getComponents()) {
			if (c instanceof Button) {
				if (((Button) c).getId() > 1500) {
					Item item = shop.getItem(((Button) c).getId() - 1500);
					((Button) c).setDrawable(item);
					if (item != null) {
						String[] hoverText = {
								"Purchase", "1", item.getName()
						};
						((Button) c).setHoverData(null, hoverText);
					}
				}
			} else if (c instanceof Sprite) {
				((Sprite) c).process();
			} else if (c instanceof Text) {
				Text t = openInterface.getTextComponent(1);
				t.setText(shop.getName());
			}
		}
	}

	private void createInventory() {
		GameInterface inventory = new GameInterface(defaultBounds, defaultBorder, Color.GRAY);
		// Create inventory
		// 'X' Button
		Rectangle xButtonBounds = new Rectangle(defaultBounds.x + defaultBounds.width - 20, defaultBounds.y, 20, 20);
		Sprite xButtonSprite = new Sprite(xButtonBounds, defaultBorder, xButtonImgs);
		Button xButton = new Button(xButtonBounds, defaultBorder, xButtonSprite, -1);
		inventory.addComponent(xButton);
		// Title
		Rectangle titleBounds = new Rectangle(defaultBounds.x, defaultBounds.y + 25, 370, 20);
		String titleText = "Inventory";
		Text inventoryTitle = new Text(titleBounds, titleText, null, Text.CENTER, Color.WHITE, -1);
		inventory.addComponent(inventoryTitle);
		// Options Swap-to
		Rectangle optionsSwapBounds = new Rectangle(defaultBounds.x + defaultBounds.width - 40, defaultBounds.y, 20,
				20);
		Sprite optionsSwapSprite = new Sprite(optionsSwapBounds, defaultBorder, optionsSwapButtonImgs);
		Button optionsSwap = new Button(optionsSwapBounds, defaultBorder, optionsSwapSprite, 1);
		inventory.addComponent(optionsSwap);
		// Item Display
		ItemContainer items = player.getInventory();
		int itemHolderX = defaultBounds.x + 20;
		int itemHolderXReset = itemHolderX;
		int itemHolderY = defaultBounds.y + 40;
		int itemHolderSize = 32;
		for (int i = 0; i < items.getMaxItemCount(); i++) {
			Rectangle itemHolderBounds = new Rectangle(itemHolderX, itemHolderY, itemHolderSize, itemHolderSize);
			Border itemHolderBorder = new Border(2, Color.black, Color.yellow);
			Item item = items.getItem(i);
			Button itemHolder = new Button(itemHolderBounds, itemHolderBorder, item, 1000 + i);
			itemHolderX += 35;
			if ((i + 1) % 10 == 0) {
				itemHolderX = itemHolderXReset;
				itemHolderY += 35;
			}
			inventory.addComponent(itemHolder);
		}
		// End inventory
		interfaces.add(InventoryScreen, inventory);
	}

	private void updateInventory() {
		Item selected = player.getSelectedItem();
		for (Component c : openInterface.getComponents()) {
			if (c instanceof Button) {
				if (((Button) c).getId() > 1000) {
					Item item = player.getInventory().getItem(((Button) c).getId() - 1000);
					((Button) c).setDrawable(item);
					if (item != null) {
						String[] hoverText;
						if (selected == null) {
							hoverText = new String[] {
									"Use", item.getName()
							};
						} else {
							hoverText = new String[] {
									"Use", selected.getName(), "on", item.getName()
							};
						}
						((Button) c).setHoverData(null, hoverText);
					}
				}
			}
		}
	}

	public void updateOpenInterface() {
		switch (interfaces.indexOf(openInterface)) {
			case 0:
				updateInventory();
				break;
			case 2:
				updateShop();
				break;
		}
	}

	public boolean click(Point p, Screen screen) {
		for (GameInterface i : interfaces) {
			if (i.isVisible()) { return i.click(screen, p); }
		}
		return false;
	}

	public boolean setMousePosition(Point p, Screen screen) {
		for (GameInterface i : interfaces) {
			if (i.isVisible()) { return i.moveMouse(screen, p); }
		}
		return false;
	}

	public void closeInterface() {
		if (openInterface != null) {
			openInterface.setVisible(false);
		}
		openInterface = null;
	}

	public void drawInterfaces(Graphics2D g) {
		if (openInterface != null) {
			openInterface.draw(g);
		}
	}

	@Override
	public String getClassName() {
		return "Interface Manager";
	}

	public static final int InventoryScreen = 0;
	public static final int OptionsScreen = 1;
	public static final int ShopScreen = 2;

	public void openInterface(int interfaceId) {
		int tmp = openInterface == null ? -1 : interfaces.indexOf(openInterface);
		closeInterface();
		if (tmp != interfaceId) {
			openInterface = interfaces.get(interfaceId);
			openInterface.setVisible(true);
		}
	}
}
