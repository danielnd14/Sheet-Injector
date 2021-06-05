package com.github.danielnd14.app.processing;

import com.github.danielnd14.app.dto.TabbedDTO;
import com.github.danielnd14.app.dto.TableDTO;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public final class Injector {

	public Injector() {
		throw new RuntimeException("This is a utility class");
	}

	/**
	 * primeiro é executao as formulas do tipo self para que corra o risco de inserir uma formula que dependa de uma
	 * celula que deveria ser selfValue
	 */
	public static void inject(final List<TabbedDTO> tabbedDTOList, final File sheetFile) throws IOException, FormulaParseException, IllegalStateException, NumberFormatException {
		final var selfFormulaList = tabbedDTOList.stream().filter(TabbedDTO::hasSelfFormula)
				.collect(Collectors.toUnmodifiableList());
		final var fileStream = new FileInputStream(sheetFile);
		final var wb = new XSSFWorkbook(fileStream);

		//aplicando selfValue onde precisa
		selfFormulaList.forEach(tabbedDTO -> {
			var sheet = wb.getSheet(tabbedDTO.getTabTitle());
			if (sheet == null) {
				return;
			}
			var lastRow = sheet.getLastRowNum();
			for (var idxRow = 0; idxRow <= lastRow; idxRow++) {
				final Row row = sheet.getRow(idxRow);
				updateSelfValue(tabbedDTO, row);
			}
		});

		final var ev = wb.getCreationHelper().createFormulaEvaluator();
		//injetando formulas
		tabbedDTOList.forEach(tabbedDTO -> {
			var sheet = wb.getSheet(tabbedDTO.getTabTitle());
			if (sheet == null) return;
			var lastRow = sheet.getLastRowNum();
			for (var idxRow = 0; idxRow <= lastRow; idxRow++) {
				final Row row = sheet.getRow(idxRow);
				insertFormulaValue(tabbedDTO, row, ev);
			}
		});
		fileStream.close();
		writeFile(wb, sheetFile);
	}

	private static void updateSelfValue(TabbedDTO tabbedDTO, Row row) {
		for (final var tt : tabbedDTO.getTupleList()) {
			for (Integer idxCol : tt.cols()) {
				final Cell cell = row.getCell(idxCol);
				if (cell == null) continue;
				var formula = tt.formula(row.getRowNum());
				if (formula.equalsIgnoreCase("self") && !formula.equalsIgnoreCase(TableDTO.NOTHING_TO_DO)) {
					try {
						cell.setCellFormula(cell.getNumericCellValue() + "");
					} catch (RuntimeException e) {
						cell.setCellFormula("\"" + cell.getStringCellValue() + "\"");
					}
				}
			}
		}
	}

	private static void insertFormulaValue(TabbedDTO tabbedDTO, Row row, XSSFFormulaEvaluator ev) {
		for (final var tt : tabbedDTO.getTupleList()) {
			for (var idxCol : tt.cols()) {
				final var cell = row.getCell(idxCol);
				var formula = tt.formula(row.getRowNum() + 1);
				if (!formula.equalsIgnoreCase("self") && !formula.equalsIgnoreCase(TableDTO.NOTHING_TO_DO)) {
					cell.setCellFormula(formula);
					ev.evaluateFormulaCell(cell);
				}
			}
		}
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