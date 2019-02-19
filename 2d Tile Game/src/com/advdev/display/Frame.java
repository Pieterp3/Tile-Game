package com.advdev.display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JFrame;

import com.advdev.Main;
import com.advdev.display.interfaces.handlers.InterfaceManager;

public class Frame extends JFrame implements MouseListener, MouseMotionListener, KeyListener {
	private Screen screen;
	public static Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();

	public Frame() {
		super("2D Game");
		setSize(frameSize);
		setResizable(false);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setUndecorated(true);
		setLayout(null);
		setLocationRelativeTo(null);
		this.screen = new Screen(frameSize.width, frameSize.height);
		add(this.screen);
		this.screen.setBounds(0, 0, frameSize.width, frameSize.height);
		setVisible(true);
	}

	public Screen getScreen() {
		return this.screen;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			Main.getPlayer().executeAction();
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Main.getInterfaceManager().openInterface(InterfaceManager.OptionsScreen);
		}
		if (e.getKeyCode() == KeyEvent.VK_P) {
			Main.getShopHandler().openShop(1);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.screen.click(arg0.getPoint());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.screen.setMousePosition(e.getPoint());
	}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	private static final long serialVersionUID = -6739420367153626387L;
}
