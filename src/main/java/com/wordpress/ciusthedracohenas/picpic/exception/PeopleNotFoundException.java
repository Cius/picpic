package com.wordpress.ciusthedracohenas.picpic.exception;

public class PeopleNotFoundException extends Exception {

	private static final long serialVersionUID = -6563454764602929674L;

	public PeopleNotFoundException() {
		super("People not found.");
	}
}
