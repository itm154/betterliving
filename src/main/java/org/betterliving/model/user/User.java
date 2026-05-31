package org.betterliving.model.user;

import org.betterliving.model.Identifiable;

public abstract class User implements Identifiable {

	protected String name;
	protected int id;

	public User(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
}
