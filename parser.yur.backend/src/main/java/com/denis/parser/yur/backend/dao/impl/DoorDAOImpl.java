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
	public List<Door> loadAllInfo() {
		return sessionFactory.getCurrentSession().createQuery("FROM Door", Door.class).getResultList();
	}

	@Override
	public Door getById(@NonNull int id) {
		try {
			return sessionFactory.getCurrentSession().get(Door.class, Integer.valueOf(id));
		} catch (Exception ex) {
			System.out.println(EXCEPTION_PREFIX + " getById method.");
			return null;
		}
	}

	@Override
	public Door getByURL(String url) {
		Query<?> query = sessionFactory.getCurrentSession().createQuery("FROM Door WHERE url = '" + url + "'");
		if (query != null) {

			List<?> resultList = query.getResultList();

			return (Door) resultList.get(0);
		} else {

			return null;
		}
	}

	@Override
	public boolean saveOrUpdate(Door entity) {

		try {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		} catch (ConstraintViolationException e) {
			sessionFactory.getCurrentSession().clear();
			logger.info("Dublicate product - INFO");
			return true;
		} catch (Exception e) {
			Class<? extends Exception> class1 = e.getClass();
			System.out.println(class1.toString());
		}
		return true;
	}

	@Override
	public Integer getId(Door entity) {

		Query<?> query = sessionFactory.getCurrentSession().createQuery(
				"from Door where name = :name and brand = :brand and collection = :collection and url = :url");
		query.setParameter("name", entity.getName());
		query.setParameter("brand", entity.getBrand());
		query.setParameter("collection", entity.getCollection());
		query.setParameter("url", entity.getUrl());

		List<Door> resultList = (List<Door>) query.getResultList();

		if (!resultList.isEmpty()) {

			return resultList.get(0).getId();
		} else {

			return 0;
		}
	}

}
