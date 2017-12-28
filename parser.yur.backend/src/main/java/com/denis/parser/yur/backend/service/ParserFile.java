package com.denis.parser.yur.backend.service;

import java.util.List;

public interface ParserFile {

	void setPathToOriginalFile(String pathToOriginalFile);

	void setPathToTxtFiles(List<String> pathToTxtFiles);

	void start();

}
