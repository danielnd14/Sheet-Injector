package com.github.danielnd14.app.processing;

import lombok.SneakyThrows;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

public final class RecursiveLister implements XLSXList {
	@SneakyThrows
	@Override
	public List<Path> list() {
		File rootDir;
		final var chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int i = chooser.showOpenDialog(null);
		if (i == JFileChooser.APPROVE_OPTION)
			rootDir = chooser.getSelectedFile();
		else return List.of();
		return new SheetFileSearch(rootDir.toPath()).list();
	}

	private static class SheetFileSearch extends SimpleFileVisitor<Path> implements XLSXList {
		private final List<Path> sheets;
		private final Path root;

		public SheetFileSearch(Path root) {
			this.root = root;
			this.sheets = new ArrayList<>();
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
			if (file.toFile().getName().endsWith(".xlsx")) sheets.add(file);
			return CONTINUE;
		}

		@Override
		public List<Path> list() throws IOException {
			Files.walkFileTree(root, this);
			return sheets;
		}
	}
}