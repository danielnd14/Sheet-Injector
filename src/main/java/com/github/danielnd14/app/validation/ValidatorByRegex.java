package com.github.danielnd14.app.validation;

import java.util.regex.Pattern;

public final class ValidatorByRegex implements Validation<String> {
	private final String regexToValid;

	public ValidatorByRegex(final String regexToValid) {
		this.regexToValid = regexToValid;
	}

	@Override
	public boolean valid(final String toValid) {
		if (toValid.isBlank())
			return false;
		var matcher = Pattern.compile(regexToValid).matcher(toValid);
		if (matcher.find())
			return !matcher.group().isBlank();
		return false;
	}
}
