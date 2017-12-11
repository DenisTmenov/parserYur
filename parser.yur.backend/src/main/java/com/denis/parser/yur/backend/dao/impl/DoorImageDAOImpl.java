package com.denis.parser.yur.backend.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.denis.parser.yur.backend.dao.DoorImageDAO;
import com.denis.parser.yur.backend.dto.DoorImage;

@Repository("doorImageDAO")
@Transactional
public class DoorImageDAOImpl implements DoorImageDAO {

	private final String EXCEPTION_PREFIX = "Problem in DoorImageDAOImpl in";

	private static final Logger logger = LoggerFactory.getLogger(DoorImageDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public DoorImage getById(@NonNull int id) {
		try {
			return sessionFactory.getCurrentSession().get(DoorImage.class, Integer.valueOf(id));
		} catch (Exception ex) {
			System.out.println(EXCEPTION_PREFIX + " getById method.");
			return null;
		}
	}

	@Override
	public boolean saveOrUpdate(DoorImage entity) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate productImage - INFO");
			return true;
		} catch (Exception e) {
			Class<? extends Exception> class1 = e.getClass();
			System.out.println(class1.toString());
		}
		return true;
	}

}
