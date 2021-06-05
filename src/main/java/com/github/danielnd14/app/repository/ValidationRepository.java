package com.github.danielnd14.app.repository;

import com.github.danielnd14.app.validation.ValidatorByRegex;

public final class ValidationRepository {

	private static ValidationRepository instance;
	private final ValidatorByRegex rowValidator;
	private final ValidatorByRegex columnValidator;

	private ValidationRepository() {
		if (instance != null) throw new RuntimeException("Singleton must be a singleton {" + this.getClass() + "}");
		columnValidator = new ValidatorByRegex("^\\d+$|(^\\d+:\\d+$)|(^\\[[\\d,]+\\]$)");
		rowValidator = new ValidatorByRegex("(^\\d+$)|(^[\\d]+:[\\d]+)|(^ALL$)|(^ALL\\-\\[[\\d,]+\\]$)|(^ALL\\-\\[[\\d]+:[\\d]+\\]$)|(^\\[[\\d,]+\\]$)");
	}

	public synchronized static ValidationRepository instance() {
		if (instance == null) instance = new ValidationRepository();
		return instance;
	}

	public ValidatorByRegex getRowValidator() {
		return rowValidator;
	}

	public ValidatorByRegex getColumnValidator() {
		return columnValidator;
	}
}
