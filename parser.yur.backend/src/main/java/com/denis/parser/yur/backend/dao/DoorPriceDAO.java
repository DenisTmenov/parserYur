package com.denis.parser.yur.backend.dao;

import com.denis.parser.yur.backend.dto.DoorPrice;

public interface DoorPriceDAO {
	DoorPrice getById(int id);

	DoorPrice getByDoorId(int doorId);

	boolean saveOrUpdate(DoorPrice entity);

}
