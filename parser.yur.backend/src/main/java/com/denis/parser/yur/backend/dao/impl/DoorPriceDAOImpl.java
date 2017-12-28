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

import com.denis.parser.yur.backend.dao.DoorPriceDAO;
import com.denis.parser.yur.backend.dto.DoorPrice;

@Repository("doorPriceDAO")
@Transactional
public class DoorPriceDAOImpl implements DoorPriceDAO {

	private final String EXCEPTION_PREFIX = "Problem in DoorPriceDAOImpl in";

	private static final Logger logger = LoggerFactory.getLogger(DoorPriceDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public DoorPrice getById(@NonNull int id) {
		try {
			logger.info("Return DoorPrice - INFO");
			return sessionFactory.getCurrentSession().get(DoorPrice.class, Integer.valueOf(id));
		} catch (Exception ex) {
			logger.error(EXCEPTION_PREFIX + " getById method. Return null - ERROR");
			return null;
		}
	}

	@Override
	public DoorPrice getByDoorId(@NonNull int doorId) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery("FROM DoorPrice WHERE doorId = :doorId");
		query.setParameter("doorId", doorId);
		@SuppressWarnings("unchecked")
		List<DoorPrice> resultList = (List<DoorPrice>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return DoorPrice - INFO");
			return resultList.get(0);
		} else {
			logger.info("DoorPrice didn't found. Return null - INFO");
			return null;
		}
	}

	@Override
	public boolean saveOrUpdate(DoorPrice entity) {
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate productImage - INFO");
			return true;
		} catch (Exception e) {
			logger.error(EXCEPTION_PREFIX + " saveOrUpdate method - ERROR");
		}
		logger.info("SaveOrUpdate done - INFO");
		return true;
	}

}
