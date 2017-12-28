package com.denis.parser.yur.backend.dao;

import java.util.List;

import com.denis.parser.yur.backend.dto.DoorImage;

public interface DoorImageDAO {

	DoorImage getById(int id);

	List<DoorImage> getByDoorId(int doorId);

	boolean saveOrUpdate(DoorImage entity);
}
