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
		logger.info("Return List<Door> !");
		return sessionFactory.getCurrentSession().createQuery("FROM Door", Door.class).getResultList();
	}

	@Override
	public Door findDoorById(@NonNull int id) {
		try {
			logger.info("Return Door by id=" + id);
			return sessionFactory.getCurrentSession().get(Door.class, Integer.valueOf(id));
		} catch (Exception ex) {
			logger.error(EXCEPTION_PREFIX + " getById method. Return null!");
			return null;
		}
	}

	@Override
	public Door findDoorByURL(@NonNull String url) {
		@SuppressWarnings("unchecked")
		Query<Door> query = sessionFactory.getCurrentSession().createQuery("FROM Door WHERE url = :url");
		query.setParameter("url", url);
		List<Door> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			logger.info("Return Door " + resultList.get(0).getName());
			return resultList.get(0);
		} else {
			logger.info("Door didn't found by URL - " + url + ". Return null - INFO");
			return null;
		}
	}

	@Override
	public boolean saveOrUpdateDoor(@NonNull Door entity) {

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate Door " + entity.getName());
			return true;
		} catch (Exception e) {
			logger.error(EXCEPTION_PREFIX + " saveOrUpdate method - ERROR");
		}

		logger.info("Door " + entity.getName() + ". Saved or Updated.");
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
			logger.info("Return id by Door " + entity.getName());
			return resultList.get(0).getId();
		} else {
			logger.info("Door " + entity.getName() + " didn't found. Return 0!");
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
			logger.info("Return List<Door> by collection " + collection);
			return resultList;
		} else {
			logger.info("Doors didn't found by collection " + collection + ". Return null !");
			return null;
		}
	}

	@Override
	public Door findDoorByBrandCollectionName(@NonNull String brand, @NonNull String collection, @NonNull String name) {
		Query<?> query = sessionFactory.getCurrentSession()
				.createQuery("FROM Door WHERE name = :name and brand = :brand and collection = :collection");
		query.setParameter("name", name);
		query.setParameter("brand", brand);
		query.setParameter("collection", collection);

		@SuppressWarnings("unchecked")
		List<Door> resultList = (List<Door>) query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("Return List<Door> by Brand ").append(brand);
			sb.append(", Collection").append(collection);
			sb.append(", Name ").append(name);
			logger.info(sb.toString());
			return resultList.get(0);
		} else {
			logger.info("Doors didn't found by brand + collection + name. Return null !");
			return null;
		}
	}

	@Override
	public List<String> getAllURLs() {
		@SuppressWarnings("unchecked")
		Query<String> query = sessionFactory.getCurrentSession().createQuery("SELECT url FROM Door");
		logger.info("Return all URLs!");
		return query.getResultList();
	}

}
