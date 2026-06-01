package org.betterliving.repository;

import java.util.List;

public interface Storeable<T> {
	List<T> findAll();

	void save(T entity);

	void deleteById(int id);
}
