package com.denis.parser.yur.backend.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.denis.parser.yur.backend.dao.DoorDAO;
import com.denis.parser.yur.backend.dao.DoorImageDAO;
import com.denis.parser.yur.backend.dto.Door;
import com.denis.parser.yur.backend.dto.DoorImage;
import com.denis.parser.yur.backend.service.ParserHtml;
import com.denis.parser.yur.backend.service.ServiceException;
import com.denis.parser.yur.backend.service.htmlinfo.HrefsFromPage;
import com.denis.parser.yur.backend.service.htmlinfo.InfoFromAreaCrumbs;
import com.denis.parser.yur.backend.service.htmlinfo.InfoFromAreaRazm;
import com.denis.parser.yur.backend.service.htmlinfo.InfoFromAreaShopImg;
import com.denis.parser.yur.backend.service.htmlinfo.InfoFromAreaShopItemSmoleContent;
import com.denis.parser.yur.backend.utils.StringUtils;

@Service("parserHtml")
public class ParserHtmlImpl implements ParserHtml {

	private static final Logger logger = LoggerFactory.getLogger(ParserHtmlImpl.class);

	private final String ERROR = "(HtmlParser) Problem ";

	private String start_URL = "";

	private List<String> productURLs = new ArrayList<>();

	@Autowired
	private DoorDAO doorDAO;

	@Autowired
	private DoorImageDAO doorImageDAO;

	@Override
	public List<String> getProductURLs() {
		return productURLs;
	}

	@Override
	public void start(String start_URL) {
		this.start_URL = start_URL;

		fillProductURLs();

		if (productURLs != null && productURLs.size() > 0) {

			for (String productURL : productURLs) {
				Document doc = null;
				try {
					doc = Jsoup.connect(productURL).get();
				} catch (IOException e) {
					logger.error("Unsuccessful connect by URL " + productURL, e);
					try {
						Thread.sleep(5000);
						try {
							doc = Jsoup.connect(productURL).get();
						} catch (IOException e1) {
							logger.error("Unsuccessful connect by URL " + productURL);
							continue;
						}
					} catch (InterruptedException e2) {
						logger.error("Problem with thread sleep.");
						continue;
					}
				}

				Door door = getProductInfoFromPage(doc, productURL);
				logger.info("Information about door " + door.getName() + " received.");

				saveDoorOnDB(door);
			}
		}
	}

	private void fillProductURLs() {
		List<String> URLsFromDAO = doorDAO.getAllURLs();

		List<String> URLsFromSite = getURLs();
		if (URLsFromDAO != null && !URLsFromDAO.isEmpty()) {
			for (String url : URLsFromSite) {
				if (URLsFromDAO.contains(url)) {
					continue;
				}
				productURLs.add(url);
			}
			productURLs.addAll(URLsFromDAO);
		} else {
			productURLs.addAll(URLsFromSite);
		}

	}

	private void saveDoorOnDB(Door door) {

		doorDAO.saveOrUpdateDoor(door);
		if (!door.getDoorImages().isEmpty()) {

			int doorId = doorDAO.getDoorId(door);
			if (doorId != 0) {

				for (DoorImage doorImage : door.getDoorImages()) {
					doorImage.setDoorId(doorId);
					doorImageDAO.saveOrUpdate(doorImage);
				}
			}
		}
	}

	private List<String> getURLs() {

		List<String> result = new ArrayList<>();
		Map<Integer, Document> urlDocMap = new HashMap<>();
		Document doc = null;
		int countPages = getCountPages(urlDocMap);

		Map<Integer, HrefsFromPage> mapHrefsFromPage = new HashMap<>();
		Map<Integer, Thread> mapThread = new HashMap<>();

		for (int i = 1; i <= countPages; i++) {

			if (urlDocMap.containsKey(i)) {
				doc = urlDocMap.get(i);
			} else {
				try {
					doc = Jsoup.connect(start_URL + "/page-" + i + "/").get();
				} catch (IOException e) {
					System.out.println("Problem with get connection by url!!!");
					return result;
				}
				urlDocMap.put(i, doc); // add Document (all information) about URL
			}

			if (doc != null) {
				HrefsFromPage hrefsFromPage = new HrefsFromPage(doc);
				Thread thread = new Thread(hrefsFromPage);
				thread.start();

				mapHrefsFromPage.put(i, hrefsFromPage);
				mapThread.put(i, thread);
			}
		}

		if (!mapHrefsFromPage.isEmpty() && !mapThread.isEmpty() && mapHrefsFromPage.size() == mapThread.size()) {
			for (int i = 1; i <= countPages; i++) {
				while (mapThread.get(i).isAlive()) {
				}
				result.addAll(mapHrefsFromPage.get(i).getURLs());
				logger.info("Added " + mapHrefsFromPage.get(i).getURLs().size() + "URLs.");
			}
		}

		logger.info("All " + result.size() + "URLs received.");
		return result;
	}

	private int getCountPages(Map<Integer, Document> urlDocMap) {
		int lastNumber = 0;
		boolean detector = true;
		Document doc = null;
		do {
			try {
				if (lastNumber == 0) {
					doc = Jsoup.connect(start_URL).get();
					urlDocMap.put(1, doc); // add Document (all information) about URL
				} else {
					doc = Jsoup.connect(start_URL + "/page-" + lastNumber + "/").get();
					urlDocMap.put(lastNumber, doc);
				}

			} catch (IOException e) {

				throw new ServiceException(ERROR + "while getting connection! (getCountPages)", e);
			}

			Elements allElementsFieldPageNumber = doc.getElementsByClass("pagination-button");
			int docNumber = 0;

			if (!allElementsFieldPageNumber.isEmpty()) {
				int i = 0;
				if (allElementsFieldPageNumber.size() > 2) {
					i = allElementsFieldPageNumber.size() - 2;
				}
				for (; i < allElementsFieldPageNumber.size(); i++) {

					Elements elementsByTag = allElementsFieldPageNumber.get(i).getElementsByTag("a");
					if (elementsByTag.size() > 0) {
						for (Element element : elementsByTag) {
							if (StringUtils.isNumeric(element.text())) {
								Integer pageNumber = Integer.valueOf(element.text());
								if (docNumber < pageNumber) {
									docNumber = pageNumber;
								}
							}
						}
					}
				}
			}

			if (docNumber <= lastNumber) {
				detector = false;
			} else {
				if (lastNumber < docNumber) {
					lastNumber = docNumber;
				}
			}

		} while (detector);

		if (lastNumber == 0) {
			lastNumber++;
		}

		return lastNumber;
	}

	private Door getProductInfoFromPage(Document doc, String productURL) {

		Door result = new Door();

		InfoFromAreaShopItemSmoleContent info1 = new InfoFromAreaShopItemSmoleContent(doc);
		Thread thread1 = new Thread(info1);
		thread1.start();

		InfoFromAreaRazm info2 = new InfoFromAreaRazm(doc);
		Thread thread2 = new Thread(info2);
		thread2.start();

		InfoFromAreaCrumbs info3 = new InfoFromAreaCrumbs(doc);
		Thread thread3 = new Thread(info3);
		thread3.start();

		InfoFromAreaShopImg info4 = new InfoFromAreaShopImg(doc);
		Thread thread4 = new Thread(info4);
		thread4.start();

		while (thread1.isAlive()) {
		}

		while (thread2.isAlive()) {
		}

		while (thread3.isAlive()) {
		}

		while (thread4.isAlive()) {
		}

		result.setBrand(info1.getBrand());
		result.setCoating(info1.getCoating());
		result.setColor(info1.getColor());
		result.setConstruction(info1.getConstruction());
		result.setMaterial(info1.getMaterial());
		result.setType(info1.getType());

		result.setSize(info2.getSize());

		result.setName(info3.getName());
		result.setCollection(info3.getCollection());

		info4.renameImages(result.getName());
		result.setDoorImages(info4.getDoorImages());

		result.setUrl(productURL);

		return result;
	}

}
