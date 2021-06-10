package com.github.danielnd14.app.repository;

import java.awt.*;

public final class ColorRepository {
	private static ColorRepository instance;
	private final Color purpleAccent;
	private final Color greenAccent;
	private final Color redAccent;
	private final Color orangeAccent;

	private ColorRepository() {
		if (instance != null) throw new RuntimeException("Single must be a singleton {" + this.getClass() + "}");
		this.purpleAccent = new Color(138, 44, 194);
		this.greenAccent = new Color(73, 215, 0);
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

	public Color getPurpleAccent() {
		return purpleAccent;
	}

	public Color getOrangeAccent() {
		return orangeAccent;
	}
}
