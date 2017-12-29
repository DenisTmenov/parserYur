package com.denis.parser.yur.backend.dao;

import java.util.List;

import com.denis.parser.yur.backend.dto.Door;

public interface DoorDAO {

	List<Door> loadAllDoors();

	Door findDoorById(int id);

	Door findDoorByURL(String url);

	boolean saveOrUpdateDoor(Door entity);

	Integer getDoorId(Door entity);

	List<Door> findDoorsByCollection(String collection);

	Door findDoorByBrandCollectionName(String brand, String collection, String name);

	List<String> getAllURLs();

}
