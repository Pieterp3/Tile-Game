package com.advdev.display.interfaces;

import java.awt.Point;

import com.advdev.display.Screen;

public interface Clickable {
	public boolean moveMouse(Screen screen, Point loc);
	boolean click(Screen screen, Point loc);
	
}
