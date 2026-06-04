package org.betterliving.repository;

import java.util.List;

public interface Storable<T> {
	List<T> findAll();

	void save(T entity);

	void deleteById(int id);
}
