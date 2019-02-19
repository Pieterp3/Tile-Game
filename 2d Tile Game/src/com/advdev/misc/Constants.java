package com.advdev.misc;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;

public class Constants {

	public static final int tileSize = 30;
	public static final boolean debug = true;
	public static boolean debugIO = false;
	private static final DecimalFormat df = new DecimalFormat("#.00%");
	public static boolean displayRegions = false;
	public static boolean fog = false;
	private static Random mapGen = new Random(83116);

	public static Object chooseRandom(Iterator<?> it, int size) {
		int index = getRandomInt(size);
		Object at = null;
		for (int i = -1; i < index; i++) {
			at = it.next();
		}
		return at;
	}

	public static String formatPerc(double d) {
		return df.format(d);
	}

	public static double getRandomDouble() {
		return mapGen.nextDouble();
	}

	public static int getRandomInt(int cap) {
		return mapGen.nextInt(cap);
	}

	public static int getRandomInt(int seed, int cap) {
		mapGen.setSeed(seed);
		int r = mapGen.nextInt(cap);
		mapGen.setSeed(83116);
		return r;
	}

	public static void partialArrayTransfer(Object[] from, Object[] to, int start, int comp) {
		for (int i = start; i < comp; i++) {
			to[i - start] = from[i];
		}
	}
}
