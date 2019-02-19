package com.advdev.display.interfaces.handlers;

import com.advdev.Main;
import com.advdev.display.Screen;
import com.advdev.display.interfaces.components.Button;
import com.advdev.game.entities.player.Player;
import com.advdev.misc.Constants;

public class ButtonHandler {
	public static boolean clickButton(Screen screen, Button button) {
		if (Constants.debug) System.out.println("Clicked Button: " + button.getId());
		InterfaceManager manager = Main.getInterfaceManager();
		Player player = Main.getPlayer();
		if (button.getId() >= 1000 && button.getId() < 1000 + player.getInventory().getMaxItemCount()) {
			Main.getItemHandler().clickItem(button.getId() - 1000);
			return true;
		}
		switch (button.getId()) {
			case -1:
				manager.closeInterface();
				break;
			case 0:// Swap to inventory interface
			case 1:// Swap to options interface
				manager.openInterface(button.getId());
				break;
			case 20://Toggle fog
				Constants.fog = !Constants.fog;
				break;
			case 21://Toggle RegionView
				Constants.displayRegions = !Constants.displayRegions;
				break;
			case 22://Quit game
				System.exit(0);
				break;
		}
		return false;
	}
}
