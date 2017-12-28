package com.denis.parser.yur.backend.dao;

import java.util.List;

import com.denis.parser.yur.backend.dto.Door;

public interface DoorDAO {

	List<Door> loadAllInfo();

	Door getById(int id);

	Door getByURL(String url);

	boolean saveOrUpdate(Door entity);

	Integer getId(Door entity);

	List<Door> getByCollection(String collection);

	Door getByBrandCollectionName(String brand, String collection, String name);
}
