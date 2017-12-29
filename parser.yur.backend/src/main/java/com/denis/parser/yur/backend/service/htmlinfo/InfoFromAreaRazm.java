package com.denis.parser.yur.backend.service.htmlinfo;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class InfoFromAreaRazm implements Runnable {

	private Document doc;

	private String doorSize = "";

	public InfoFromAreaRazm(Document doc) {
		this.doc = doc;
	}

	public String getSize() {
		return doorSize;
	}

	@Override
	public void run() {
		Elements elementsRazm = doc.getElementsByClass("razm");
		if (!elementsRazm.isEmpty()) {

			if (!elementsRazm.get(0).getElementsByClass("tt2").isEmpty()) {
				doorSize = elementsRazm.get(0).getElementsByClass("tt2").text().replace("Размеры:", "");
			}
		}

	}

}
