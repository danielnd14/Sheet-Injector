package com.github.danielnd14.app;

import com.github.danielnd14.app.gui.SheetIntejectorGUI;

import javax.swing.*;
import java.util.stream.Stream;

public final class SheetInjector {
	public SheetInjector() {
		throw new RuntimeException("is the main class");
	}

	public static void main(String[] args) {
		setLookAndFeel();
		SheetIntejectorGUI
				.getInstance()
				.setVisible(true);
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			final var gtk = "GTK+";
			final var hasgtk = Stream.of(UIManager.getInstalledLookAndFeels())
					.anyMatch(lf -> lf.getName().equalsIgnoreCase(gtk));
			final var look = hasgtk ? gtk : "Nimbus";
			for (var info : UIManager.getInstalledLookAndFeels()) {
				if (look.equals(info.getName())) {
					try {
						UIManager.setLookAndFeel(info.getClassName());
					} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException classNotFoundException) {
						classNotFoundException.printStackTrace();
					}
					break;
				}
			}
		}
	}
}
