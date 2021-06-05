package com.github.danielnd14.app.gui;

import java.awt.*;

public final class ColorRepository {
	private static ColorRepository instance;
	private final Color blueAccent = new Color(0, 64, 146);
	private final Color greenAccent = new Color(34, 114, 0);
	private final Color redAccent = new Color(138, 35, 35);

	private ColorRepository() {
		if (instance != null) throw new RuntimeException("Single must be a singleton {" + this.getClass() + "}");
	}

	public static synchronized ColorRepository instance() {
		if (instance == null) instance = new ColorRepository();
		return instance;
	}

	public Color getRedAccent() {
		return redAccent;
	}

	public Color getGreenAccent() {
		return greenAccent;
	}

	public Color getBlueAccent() {
		return blueAccent;
	}
}
