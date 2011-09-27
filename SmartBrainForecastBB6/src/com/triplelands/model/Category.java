package com.triplelands.model;

import java.util.Vector;

public class Category {
	private int id;
	private String name;
	private Vector signals;
	
	public Category(int id, String name, Vector signals) {
		this.id = id;
		this.name = name;
		this.signals = signals;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public Vector getSignals() {
		return signals;
	}
	
}
