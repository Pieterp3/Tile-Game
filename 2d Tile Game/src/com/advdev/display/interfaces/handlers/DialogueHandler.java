package com.advdev.display.interfaces.handlers;

import java.awt.Color;
import java.awt.Rectangle;

import com.advdev.Main;
import com.advdev.display.Frame;
import com.advdev.display.Screen;
import com.advdev.display.interfaces.components.Button;
import com.advdev.display.interfaces.components.Text;

public class DialogueHandler {
	private static Screen screen;
	private static int dialogueWidth;
	private static int dialogueHeight = 200;
	
	private static Rectangle twoOptionsChoice1;
	private static Rectangle twoOptionsChoice2;

	public static void init() {
		screen = Main.getFrame().getScreen();
		dialogueWidth = Frame.frameSize.width - 40;
		twoOptionsChoice1 = new Rectangle(20, Frame.frameSize.height - 160, dialogueWidth, 20);
		twoOptionsChoice2 = new Rectangle(20, Frame.frameSize.height - 120, dialogueWidth, 20);
	}
	
	public static void displayTwoOptions(String option1, String option2) {
		Button choiceOne = new Button(twoOptionsChoice1, null, Color.lightGray, Color.lightGray, 4596);
		Text choiceOneText = new Text(twoOptionsChoice1, option1, null, Text.CENTER, Color.black, 10);
		choiceOne.addComponent(choiceOneText);
		Button choiceTwo = new Button(twoOptionsChoice2, null, Color.lightGray, Color.lightGray, 4597);
		Text choiceTwoText = new Text(twoOptionsChoice2, option2, null, Text.CENTER, Color.black, 10);
		choiceTwo.addComponent(choiceTwoText);
		screen.setDisplayText(choiceOne, choiceTwo);
	}

}
