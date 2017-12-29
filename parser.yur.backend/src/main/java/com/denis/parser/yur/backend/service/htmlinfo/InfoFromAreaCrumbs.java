package com.denis.parser.yur.backend.service.htmlinfo;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class InfoFromAreaCrumbs implements Runnable {
	private Document doc;

	private String doorName = "";
	private String doorCollection = "";

	public InfoFromAreaCrumbs(Document doc) {
		this.doc = doc;
	}

	public String getName() {
		return doorName;
	}

	public String getCollection() {
		return doorCollection;
	}

	@Override
	public void run() {
		Elements elementsCrumbs = doc.getElementsByClass("crumbs");
		if (!elementsCrumbs.isEmpty()) {

			Elements elementsByTag = elementsCrumbs.first().getElementsByTag("li");
			if (!elementsByTag.isEmpty()) {

				doorName = elementsByTag.last().text();
				doorCollection = elementsByTag.get(elementsByTag.size() - 3).text();
			}
		}

	}
}
