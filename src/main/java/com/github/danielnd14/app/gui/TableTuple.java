package com.github.danielnd14.app.gui;

public final class TableTuple implements Comparable<TableTuple> {
	private final String col;
	private final String formula;
	private Integer colParsed;

	public TableTuple(String col, String formula) {
		this.col = col;
		this.formula = formula;
	}

	public String formula(int idxRow) {
		return formula.trim()
				.replaceFirst("=", "")
				.replaceAll("#", idxRow + "");
	}

	public Integer col() {
		if (colParsed == null)
			colParsed = Integer.parseInt(col.trim());
		return colParsed;
	}

	public boolean isValid() {
		return (col != null && formula != null) && !col.isBlank() && !formula.isBlank();
	}

	@Override
	public String toString() {
		return "TableTuple{" +
				"col='" + col + '\'' +
				", formula='" + formula + '\'' +
				'}';
	}

	@Override
	public int compareTo(TableTuple o) {
		if (isValid() && o.isValid())
			return col().compareTo(o.col());
		else return 1;
	}
}
