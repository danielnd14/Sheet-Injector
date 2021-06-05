package com.github.danielnd14.app.validation;

public interface Validation<T> {
	boolean valid(T toValid);
}
