package com.denis.parser.yur.frontend.flyway;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.flywaydb.core.Flyway;

import com.denis.parser.yur.backend.config.HibernateConfig;

public class DbMigrator extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

		/*
		 * final ManagedDataSource dataSource = config.getDataSourceFactory().build(new
		 * MetricRegistry(), "flyway-service"); final Flyway flyway = new Flyway();
		 * flyway.setLocations(config.getFlywayFactory().getLocations().get(0));
		 * flyway.setDataSource(dataSource); flyway.repair(); // flyway.migrate();
		 * flyway.clean();
		 * 
		 * if (flyway.migrate() <= 0) { throw new RuntimeException("migration failed!");
		 * }
		 */

		// Create the Flyway instance
		Flyway flyway = new Flyway();

		flyway.setBaselineOnMigrate(true);
		flyway.setValidateOnMigrate(false);
		// Point it to the database

		flyway.setDataSource(HibernateConfig.getInstance().getDataSource());

		// Start the migration
		try {
			flyway.migrate();
		} catch (Exception e) {
			flyway.repair();
			flyway.migrate();
		}
	}
}
