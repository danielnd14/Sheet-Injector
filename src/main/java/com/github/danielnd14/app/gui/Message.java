package com.github.danielnd14.app.gui;

public final class Message {
	private final String message;
	private final String item;

	public Message(String message, String item) {
		this.message = message;
		this.item = item;
	}

	public String message() {
		return message.trim();
	}

	public String item() {
		return item.trim();
	}

	public String[] toArray() {
		return new String[]{message(), item()};
	}
}
