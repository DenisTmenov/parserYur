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

import com.denis.parser.yur.backend.dao.DoorDAO;
import com.denis.parser.yur.backend.dto.Door;

@Repository("doorDAO")
@Transactional
public class DoorDAOImpl implements DoorDAO {

	private final String EXCEPTION_PREFIX = "Problem in DoorDAOImpl in";

	private static final Logger logger = LoggerFactory.getLogger(DoorDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public List<Door> loadAllDoors() {
		logger.info("Return List<Door> - INFO");
		return sessionFactory.getCurrentSession().createQuery("FROM Door", Door.class).getResultList();
	}

	@Override
	public Door findDoorById(@NonNull int id) {
		try {
			logger.info("Return Door - INFO");
			return sessionFactory.getCurrentSession().get(Door.class, Integer.valueOf(id));
		} catch (Exception ex) {
			logger.error(EXCEPTION_PREFIX + " getById method. Return null - ERROR");
			return null;
		}
	}

	@Override
	public Door findDoorByURL(@NonNull String url) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery("FROM Door WHERE url = :url");
		query.setParameter("url", url);
		List<?> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return Door - INFO");
			return (Door) resultList.get(0);
		} else {
			logger.info("Door didn't found. Return null - INFO");
			return null;
		}
	}

	@Override
	public boolean saveOrUpdateDoor(@NonNull Door entity) {

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate product - INFO");
			return true;
		} catch (Exception e) {
			logger.error(EXCEPTION_PREFIX + " saveOrUpdate method - ERROR");
		}

		logger.info("SaveOrUpdate done - INFO");
		return true;
	}

	@Override
	public Integer getDoorId(@NonNull Door entity) {

		Query<?> query = sessionFactory.getCurrentSession().createQuery(
				"from Door WHERE name = :name and brand = :brand and collection = :collection and url = :url");
		query.setParameter("name", entity.getName());
		query.setParameter("brand", entity.getBrand());
		query.setParameter("collection", entity.getCollection());
		query.setParameter("url", entity.getUrl());

		@SuppressWarnings("unchecked")
		List<Door> resultList = (List<Door>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return id - INFO");
			return resultList.get(0).getId();
		} else {
			logger.info("Return 0 - INFO");
			return 0;
		}
	}

	@Override
	public List<Door> findDoorsByCollection(@NonNull String collection) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery("FROM Door WHERE collection = :collection");

		query.setParameter("collection", collection);

		@SuppressWarnings("unchecked")
		List<Door> resultList = (List<Door>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return List<Door> - INFO");
			return resultList;
		} else {
			logger.info("Doors didn't found by collection. Return null - INFO");
			return null;
		}
	}

	@Override
	public Door findDoorByBrandCollectionName(@NonNull String brand, @NonNull String collection, @NonNull String name) {
		Query<?> query = sessionFactory.getCurrentSession()
				.createQuery("from Door WHERE name = :name and brand = :brand and collection = :collection");
		query.setParameter("name", name);
		query.setParameter("brand", brand);
		query.setParameter("collection", collection);

		@SuppressWarnings("unchecked")
		List<Door> resultList = (List<Door>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return List<Door> - INFO");
			return resultList.get(0);
		} else {
			logger.info("Doors didn't found by brand + collection + name. Return null - INFO");
			return null;
		}
	}

}
