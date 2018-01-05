package com.denis.parser.yur.backend.service;

import java.util.List;
import java.util.Map;

public interface ParserFile {

	void setPathToSqlFile(String pathToSqlFile);

	void setPathToTxtFile(String pathToTxtFile);

	void setCategories(List<String> chooseCategories);

	Map<String, String> getAllCategoriesFromSqlFile(String pathToFile);

	void start();

}
