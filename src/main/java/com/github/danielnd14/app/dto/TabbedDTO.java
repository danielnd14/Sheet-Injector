package com.github.danielnd14.app.dto;

import java.util.List;

public final class TabbedDTO {
	private final String tabTitle;
	private final List<TableDTO> tupleList;

	public TabbedDTO(String tabTitle, List<TableDTO> tupleList) {
		this.tabTitle = tabTitle;
		this.tupleList = tupleList;
	}

	public String getTabTitle() {
		return tabTitle;
	}

	public List<TableDTO> getTupleList() {
		return tupleList;
	}

	public boolean isValid() {
		return tabTitle != null && tupleList != null &&
				!tabTitle.equalsIgnoreCase("Sem nome") &&
				!tabTitle.isBlank() &&
				!tupleList.isEmpty();
	}

	public boolean hasSelfFormula() {
		return tupleList.stream().anyMatch(TableDTO::isSELFFormula);
	}

	@Override
	public String toString() {
		return "TabbedDTO{" +
				"tabTitle='" + tabTitle + '\'' +
				", tupleList=" + tupleList +
				'}';
	}
}
