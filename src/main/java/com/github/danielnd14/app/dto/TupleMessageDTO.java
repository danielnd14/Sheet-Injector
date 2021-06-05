package com.github.danielnd14.app.dto;

public final class TupleMessageDTO {
	private final String message;
	private final String item;

	public TupleMessageDTO(String message, String item) {
		this.message = message;
		this.item = item;
	}

	public String message() {
		if (message == null) return "null";
		return message.trim();
	}

	public String item() {
		if (item == null) return "null";
		return item.trim();
	}

	public String[] toArray() {
		return new String[]{message(), item()};
	}
}
