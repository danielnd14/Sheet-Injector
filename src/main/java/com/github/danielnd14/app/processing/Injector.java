package com.github.danielnd14.app.processing;

import com.github.danielnd14.app.gui.TableTuple;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class Injector {

	public Injector() {
		throw new RuntimeException("This is a utility class");
	}

	public static void inject(final String sheetName, final List<TableTuple> tableTuples, final File sheetFile) throws IOException, FormulaParseException, IllegalStateException {
		final var fileStream = new FileInputStream(sheetFile);
		final var wb = new XSSFWorkbook(fileStream);
		final var sheet = wb.getSheet(sheetName);

		if (sheet == null) {
			wb.close();
			throw new IOException("Aba inexistente!");
		}

		final var ev = wb.getCreationHelper().createFormulaEvaluator();
		final var lastRow = sheet.getLastRowNum();

		for (var idxRow = 1; idxRow <= lastRow; idxRow++) {
			final Row row = sheet.getRow(idxRow);
			for (final var tt : tableTuples) {
				final Cell cell = row.getCell(tt.col());
				cell.setCellFormula(tt.formula(idxRow + 1));
				ev.evaluateFormulaCell(cell);
			}
		}
		fileStream.close();
		writeFile(wb, sheetFile);
	}


	private static void writeFile(final Workbook wb, final File file) throws IOException {
		final var dir = file.getParentFile();
		if (!dir.exists()) Files.createDirectory(dir.toPath());
		if (!file.exists()) Files.createFile(file.toPath());
		final var fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
		wb.close();
	}
}
