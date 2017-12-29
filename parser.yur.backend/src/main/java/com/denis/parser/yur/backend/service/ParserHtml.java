package com.denis.parser.yur.backend.service;

import java.util.List;

public interface ParserHtml {

	List<String> getProductURLs();

	void start(String start_URL);

}
