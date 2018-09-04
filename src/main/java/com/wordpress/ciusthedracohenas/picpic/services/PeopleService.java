package com.wordpress.ciusthedracohenas.picpic.services;

import java.util.List;

import com.wordpress.ciusthedracohenas.picpic.exception.PeopleNotFoundException;
import com.wordpress.ciusthedracohenas.picpic.models.Debt;

public interface PeopleService {
	public List<String> getPeople();
	public void updatePeople();
	public Debt getDebt(String username) throws PeopleNotFoundException;
}
