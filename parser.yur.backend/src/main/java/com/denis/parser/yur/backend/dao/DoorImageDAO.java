package com.denis.parser.yur.backend.dao;

import com.denis.parser.yur.backend.dto.DoorImage;

public interface DoorImageDAO {

	DoorImage getById(int id);

	boolean saveOrUpdate(DoorImage entity);
}
