package com.github.danielnd14.app.processing;

import com.github.danielnd14.app.gui.TableTuple;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public final class Injector {

	public Injector() {
		throw new RuntimeException("This is a utility class");
	}

	public static void inject(final String sheetName, final List<TableTuple> tableTuples, final File sheetFile) throws Exception {
		final var fileStream = new FileInputStream(sheetFile);
		try (var wb = new XSSFWorkbook(fileStream)) {
			final var sheet = wb.getSheet(sheetName);
			final var ev = wb.getCreationHelper().createFormulaEvaluator();
			final var lastRow = sheet.getLastRowNum();
			Cell cell;
			Row row;
			for (var idxRow = 1; idxRow <= lastRow; idxRow++) {
				row = sheet.getRow(idxRow);
				for (var tt : tableTuples) {
					cell = row.getCell(tt.col());
					cell.setCellFormula(tt.formula(idxRow + 1));
					ev.evaluateFormulaCell(cell);
				}
			}
			fileStream.close();
			writeFile(wb, sheetFile);
		}
	}

	@SneakyThrows
	private static void writeFile(final Workbook wb, final File file) {
		final var dir = file.getParentFile();
		if (!dir.exists()) Files.createDirectory(dir.toPath());
		if (!file.exists()) Files.createFile(file.toPath());
		@Cleanup OutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		wb.close();
	}
}
