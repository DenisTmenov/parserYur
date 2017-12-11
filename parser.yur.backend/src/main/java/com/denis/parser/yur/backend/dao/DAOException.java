package com.denis.parser.yur.backend.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.denis.parser.yur.backend.dao.impl.DoorDAOImpl;

public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DoorDAOImpl.class);

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

	public DAOException(String message) {
		logger.error("Error");
	}

}
