package com.advdev.engine;

import com.advdev.Main;
import com.advdev.display.Screen;

public class Engine {
	private Thread logic;
	private Thread paint;

	public Engine(Screen screen) {
		this.logic = createLogicThread();
		this.paint = createPaintThread(screen);
	}

	private Thread createLogicThread() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Main.getMapHandler().getMap().process();
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private Thread createPaintThread(Screen screen) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						screen.repaint();
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void start() {
		this.logic.start();
		this.paint.start();
	}
}
