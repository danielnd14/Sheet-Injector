package com.github.danielnd14.app.gui;

public final class Message {
	private final String message;
	private final String item;

	public Message(String message, String item) {
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
