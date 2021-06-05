package com.github.danielnd14.app.dto;

import com.github.danielnd14.app.repository.ValidationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TableDTO {
	private final String col;
	private final String formula;
	public static String NOTHING_TO_DO = "NOTHING-TODO";
	private static ValidationRepository validationRepository;
	private final String rows;
	private List<Integer> cols;

	public TableDTO(String col, String formula, String rows) {
		this.col = col;
		this.formula = formula;
		this.rows = rows;
		if (validationRepository == null) validationRepository = ValidationRepository.instance();
	}

	public boolean isSELFFormula() {
		return formula.equalsIgnoreCase("SELF") || formula.equalsIgnoreCase("=SELF");
	}

	public String formula(int actualRow) {

		var formulaAux = formula.replaceFirst("=", "");

		if (Pattern.compile("^\\d+$").matcher(rows).find() && Integer.parseInt(rows) != actualRow) {//singleNumber ex."91"
			return NOTHING_TO_DO;
		} else if (Pattern.compile("^\\d+:\\d+$").matcher(rows).find()) {//rangeClosed ex."0:100"
			var bounds = rows.split(":");
			var boundA = Integer.min(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
			var boundB = Integer.max(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
			if (actualRow < boundA || actualRow > boundB)
				return NOTHING_TO_DO;
			return formulaAux.replaceAll("#", actualRow + "");
		} else if (Pattern.compile("^\\[[\\d,]+]$").matcher(rows).find()) {//lista arbitraria de numeros ex. [1,2,3,89]
			var numbers = rows.replaceAll("\\[|]|(,])", "").split(",");
			var isNotPresent = Arrays.stream(numbers).noneMatch(s -> s.equalsIgnoreCase(actualRow + ""));
			if (isNotPresent) return NOTHING_TO_DO;
		} else if (Pattern.compile("^ALL-\\[[\\d,]+]$").matcher(rows).find()) {//ALL less any row ex."ALL-[1,34,67]"
			var matcher = Pattern.compile("\\[[\\d,]+]$").matcher(rows);
			if (matcher.find()) {
				var numbers = matcher.group().replaceAll("\\[|]|(,])", "").split(",");
				var isPresent = Arrays.stream(numbers).anyMatch(s -> s.equalsIgnoreCase(actualRow + ""));
				if (isPresent) return NOTHING_TO_DO;
			} else return NOTHING_TO_DO;
		} else if (Pattern.compile("^ALL-\\[[\\d]+:[\\d]+]$").matcher(rows).find()) {//ALL less any row "ALL-[0:78]"
			var matcher = Pattern.compile("\\[[\\d]+:[\\d]+]$").matcher(rows);
			if (matcher.find()) {
				var bounds = matcher.group().replaceAll("[\\[\\]]", "").split(":");
				var boundA = Integer.min(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
				var boundB = Integer.max(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
				if (actualRow >= boundA && actualRow <= boundB) return NOTHING_TO_DO;
			} else return NOTHING_TO_DO;
		}
		return formulaAux.replaceAll("#", actualRow + "");
	}

	public List<Integer> cols() {
		if (cols == null) {
			if (Pattern.compile("^\\d+$").matcher(col).find()) {//singleNumber ex."91"
				cols = List.of(Integer.parseInt(col));
			} else if (Pattern.compile("^\\d+:\\d+$").matcher(col).find()) {//rangeClosed ex."0:100"
				var bounds = col.split(":");
				var boundA = Integer.min(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
				var boundB = Integer.max(Integer.parseInt(bounds[0]), Integer.parseInt(bounds[1]));
				cols = IntStream.rangeClosed(boundA, boundB).boxed()
						.collect(Collectors.toUnmodifiableList());
			} else if (Pattern.compile("^\\[[\\d,]+]$").matcher(col).find()) {//lista arbitraria de numeros ex. [1,2,3,89]
				var numbers = col.replaceAll("\\[|]|(,])", "").split(",");
				cols = Arrays.stream(numbers).mapToInt(Integer::parseInt)
						.boxed().sorted().collect(Collectors.toUnmodifiableList());
			}
		}
		return cols;

	}

	public boolean isValid() {
		return (col != null && formula != null && rows != null) &&
				validationRepository.getColumnValidator().valid(col) &&
				!formula.isBlank() &&
				validationRepository.getRowValidator().valid(rows);
	}

	@Override
	public String toString() {
		return "TableTuple{" +
				"col='" + col + '\'' +
				", formula='" + formula + '\'' +
				", row='" + rows + '\'' +
				'}';
	}
}
