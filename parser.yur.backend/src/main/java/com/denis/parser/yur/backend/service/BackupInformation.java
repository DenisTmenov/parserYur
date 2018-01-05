package com.denis.parser.yur.backend.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.denis.parser.yur.backend.dto.Door;

public interface BackupInformation {

	@SuppressWarnings("rawtypes")
	Map<String, Map> getAllProductsFromeSQLFile(String pathToFile);

	@SuppressWarnings("rawtypes")
	Map<String, Map> getAllProductsFromTxtFile(String pathToFile);

	Map<String, String> getAllCategoriesFromeSQLFile(String pathToSqlFile);

	boolean updateInformation(Map<String, String> queries, Map<String, Map> allProductsFromeSQLFile);

	Path saveModifySQLFile(String pathToSQLFile, Map<String, Map> allProductsFromeSQLFile);

	boolean saveDoorsImagesToTempFolder(List<Door> newDoors, Map<String, Map> allProductsFromeSQLFile, Path path);

}
