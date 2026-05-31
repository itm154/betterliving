package org.betterliving.model.user;

public class Teacher extends User {

	public Teacher(String name, int id) {
		super(name, id);
	}

	public void addQuestion() {
		System.out.println("Question added");
	}

	public void editQuestion() {
		System.out.println("Question edited");
	}

	@Override
	public String toString() {
		return "Teacher : " + name + ", ID : " + id;
	}
}
