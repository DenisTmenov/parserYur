package com.denis.parser.yur.backend.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
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
			logger.info("Return DoorImage by id=" + id);
			return sessionFactory.getCurrentSession().get(DoorImage.class, Integer.valueOf(id));
		} catch (Exception ex) {
			logger.error(EXCEPTION_PREFIX + " getById method. Return null - ERROR");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DoorImage> getByDoorId(@NonNull int doorId) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery("FROM DoorImage WHERE doorId = :doorId");
		query.setParameter("doorId", doorId);

		List<DoorImage> resultList = (List<DoorImage>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return DoorImage by doorId=" + doorId);
			return resultList;
		} else {
			logger.info("DoorImage didn't found. Return null !");
			return null;
		}
	}

	@Override
	public boolean saveOrUpdate(DoorImage entity) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate productImage " + entity.getName());
			return true;
		} catch (Exception e) {
			logger.error(EXCEPTION_PREFIX + " saveOrUpdate method - ERROR");
		}

		logger.info("DoorImage " + entity.getName() + ". Saved or Updated.");
		return true;
	}

}
