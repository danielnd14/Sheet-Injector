package com.github.danielnd14.app.validation;

public final class Validator {

	private static Validator instance;
	private final ValidatorByRegex rowValidator;
	private final ValidatorByRegex columnValidator;

	private Validator() {
		if (instance != null) throw new RuntimeException("Singleton must be a singleton {" + this.getClass() + "}");
		columnValidator = new ValidatorByRegex("^\\d+$|(^\\d+:\\d+$)");
		rowValidator = new ValidatorByRegex("(^\\d+$)|(^[\\d]+:[\\d]+)|(^ALL$)|(^ALL\\-\\[[\\d,]+\\]$)|(^ALL\\-\\[[\\d]+:[\\d]+\\]$)");
	}

	public synchronized static Validator instance() {
		if (instance == null) instance = new Validator();
		return instance;
	}

	public ValidatorByRegex getRowValidator() {
		return rowValidator;
	}

	public ValidatorByRegex getColumnValidator() {
		return columnValidator;
	}
}
