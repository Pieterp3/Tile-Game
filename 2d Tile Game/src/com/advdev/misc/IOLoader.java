package com.advdev.misc;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

public class IOLoader {
	private static HashMap<String, BufferedImage> images = new HashMap<>();
	private static HashMap<String, List<String>> text = new HashMap<>();

	public static List<String> getTextFromFile(String file) {
		if (text.containsKey(file)) return text.get(file);
		List<String> lines = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new FileReader(new File("res/" + file + ".txt")))) {
			String line;
			while ((line = in.readLine()) != null) {
				while (line.contains("\t\t")) {
					line = line.replaceAll("\t\t", "\t");
				}
				lines.add(line);
			}
			text.put(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public static BufferedImage loadImage(String name) {
		if (images.containsKey(name)) return images.get(name);
		try {
			File imgFile = new File("res/" + name + ".png");
			if (Constants.debug && Constants.debugIO) {
				System.out.println(imgFile.getAbsolutePath());
			}
			BufferedImage img = ImageIO.read(imgFile);
			images.put(name, img);
			return img;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage[] loadImagesFromFiles(String prePortal, int start, int stop) {
		BufferedImage[] images = new BufferedImage[stop];
		for (int i = start; i <= stop; i++) {
			BufferedImage img = loadImage(prePortal + i);
			images[i - start] = img;
		}
		return images;
	}

	public static BufferedImage[] splitImage(String name, int width, int height) {
		if (!images.containsKey(name)) {
			loadImage(name);
		}
		BufferedImage img = images.get(name);
		BufferedImage[] imgs = new BufferedImage[(img.getWidth() / width) * (img.getHeight() / height)];
		int imgsInLine = img.getWidth() / width;
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			imgs[i].getGraphics().drawImage(img, 0, 0, width, height, (i % imgsInLine) * width,
					(i / imgsInLine) * height, ((i % imgsInLine) + 1) * width, (height * (i / imgsInLine)) + height,
					null);
		}
		return imgs;
	}
}
