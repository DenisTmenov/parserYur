package com.denis.parser.yur.backend.service.htmlinfo;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HrefsFromPage implements Runnable {
	private List<String> URLs = new ArrayList<>();
	private Document doc;

	public HrefsFromPage(Document doc) {
		this.doc = doc;
	}

	public List<String> getURLs() {
		return URLs;
	}

	@Override
	public void run() {
		Elements elementsByClass = doc.getElementsByClass("page");
		if (!elementsByClass.isEmpty()) {
			for (Element element : elementsByClass) {

				Elements elementsByTag = element.getElementsByTag("a");
				if (!elementsByTag.isEmpty()) {
					URLs.add(elementsByTag.first().attr("abs:href"));
				}
			}
		}

	}

}
