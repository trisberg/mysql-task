package com.springdeveloper.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.transaction.annotation.Transactional;

public class SelectTask implements CommandLineRunner {
	private final Log logger = LogFactory.getLog(TaskConfiguration.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {
		dbInit();
		newData();
	}

	private void newData() {
		try {
			jdbcTemplate.query("SELECT id,created,message FROM data WHERE processed IS NULL", new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					long id = rs.getLong("id");
					Timestamp ts = rs.getTimestamp("created");
					String m = rs.getString("message");
					processMessage(id, ts, m);
				}
			});
		} catch (DataAccessException e) {
			logger.info("Unable to access data: " + e.getMessage());
		}
	}

	@Transactional
	private void processMessage(long id, Timestamp ts, String message) {
		logger.info("Created: " + ts.toString() + " Message: " + message);
		jdbcTemplate.update("UPDATE data SET processed = 'Y' where id = ?", id);
	}

	private void dbInit() {
		Object dbType = null;
		Object dbVersion = null;
		try {
			dbType = JdbcUtils.extractDatabaseMetaData(jdbcTemplate.getDataSource(), "getDatabaseProductName");
			dbVersion = JdbcUtils.extractDatabaseMetaData(jdbcTemplate.getDataSource(), "getDatabaseProductVersion");
		} catch (MetaDataAccessException e) {
			logger.info("Unable to access database: " + e.getMessage());
			return;
		}
		logger.info("Database product is " + dbType + " " + dbVersion);
		String userQuery = "SELECT USER() FROM (VALUES(0))";
		if (dbType.toString().startsWith("MySQL")) {
			userQuery = "SELECT USER()";
		}
		String user = jdbcTemplate.queryForObject(userQuery, String.class);
		logger.info("Running as " + user);
	}

}
