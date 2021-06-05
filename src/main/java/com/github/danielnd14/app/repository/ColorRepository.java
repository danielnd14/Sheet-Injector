package com.github.danielnd14.app.repository;

import java.awt.*;

public final class ColorRepository {
	private static ColorRepository instance;
	private final Color blueAccent;
	private final Color greenAccent;
	private final Color redAccent;
	private final Color orangeAccent;

	private ColorRepository() {
		if (instance != null) throw new RuntimeException("Single must be a singleton {" + this.getClass() + "}");
		this.blueAccent = new Color(62, 133, 159);
		this.greenAccent = new Color(73, 155, 84);
		this.redAccent = new Color(198, 84, 80);
		this.orangeAccent = new Color(247, 136, 50);
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

	public Color getOrangeAccent() {
		return orangeAccent;
	}
}
