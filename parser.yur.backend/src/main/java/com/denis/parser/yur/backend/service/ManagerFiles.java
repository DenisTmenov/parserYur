package com.denis.parser.yur.backend.service;

import java.util.List;

public interface ManagerFiles {

	List<String> readFromeFile();

	boolean saveLinesToFile(List<String> lines, String pathToFileAndFileName);

	boolean saveImageToFile(byte[] image, String pathToFileAndFileName);

	void setPath(String path);
}
