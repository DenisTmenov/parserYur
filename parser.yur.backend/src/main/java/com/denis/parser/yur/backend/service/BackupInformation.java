package com.denis.parser.yur.backend.service;

import java.util.Map;

public interface BackupInformation {

	Map<String, Map> getAllProductsFromeSQLFile(String pathToFile);

	Map<String, Map> getAllProductsFromTxtFile(String pathToFile);

	boolean updateInformation(Map<String, String> queries, Map<String, Map> allProductsFromeSQLFile);

	boolean saveModifySQLFile(String pathToSQLFile, Map<String, Map> allProductsFromeSQLFile);
}
