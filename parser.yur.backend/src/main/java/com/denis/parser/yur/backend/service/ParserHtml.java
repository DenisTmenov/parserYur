package com.denis.parser.yur.backend.service;

import java.util.List;

public interface ParserHtml {

	void setStart_URL(String start_URL);

	List<String> getProductURLs();

	void start();

}
