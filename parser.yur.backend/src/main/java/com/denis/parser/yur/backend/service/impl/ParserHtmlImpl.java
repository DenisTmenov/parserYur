package com.denis.parser.yur.backend.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.denis.parser.yur.backend.dao.DoorDAO;
import com.denis.parser.yur.backend.dao.DoorImageDAO;
import com.denis.parser.yur.backend.dto.Door;
import com.denis.parser.yur.backend.dto.DoorImage;
import com.denis.parser.yur.backend.service.ParserHtml;
import com.denis.parser.yur.backend.service.ServiceException;
import com.denis.parser.yur.backend.utils.StringUtils;

@Service("parserHtml")
public class ParserHtmlImpl implements ParserHtml {

	private final String ERROR = "(HtmlParser) Problem ";

	private String start_URL = "";

	private List<String> productURLs;

	@Autowired
	private DoorDAO doorDAO;

	@Autowired
	private DoorImageDAO doorImageDAO;

	@Override
	public void setStart_URL(String start_URL) {
		this.start_URL = start_URL;
	}

	@Override
	public List<String> getProductURLs() {
		return productURLs;
	}

	@Override
	public void start() {

		saveDoorOnDB(getInfoFromPages());
	}

	private void saveDoorOnDB(List<Door> list) {

		for (Door door : list) {

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
	}

	private List<Door> getInfoFromPages() {
		Document doc = null;
		List<Door> result = new ArrayList<>();

		productURLs = getProductURLs(doc);
		if (productURLs.size() > 0) {

			for (String productURL : productURLs) {

				try {
					doc = Jsoup.connect(productURL).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Door door = getProductInfoFromPage(doc, productURL);

				result.add(door);
			}
		}

		return result;

	}

	private List<String> getProductURLs(Document doc) {
		List<String> result = new ArrayList<>();
		Map<Integer, Document> urlDocMap = new HashMap<>();
		int countPages = getCountPages(doc, urlDocMap);

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
				Elements elementsByClass = doc.getElementsByClass("page");
				if (elementsByClass.size() > 0) {
					for (Element element : elementsByClass) {

						Elements elementsByTag = element.getElementsByTag("a");
						if (elementsByTag.size() > 0) {

							result.add(elementsByTag.first().attr("abs:href"));
						}
					}
				}
			}

		}

		return result;
	}

	private int getCountPages(Document doc, Map<Integer, Document> urlDocMap) {
		int lastNumber = 0;
		boolean detector = true;

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

			if (allElementsFieldPageNumber != null) {
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

		result = getInfoFromAreaShopItemSmoleContent(doc, result);
		result = getInfoFromAreaRazm(doc, result);
		result = getInfoFromAreaCrumbs(doc, result);
		result = getDoorImages(doc, result);

		result.setUrl(productURL);

		return result;
	}

	private Door getInfoFromAreaShopItemSmoleContent(Document doc, Door result) {

		Elements elementsShopItemSmoleContent = doc.getElementsByClass("shop-item-smole-content");
		if (!elementsShopItemSmoleContent.isEmpty()) {
			Elements elementsP = elementsShopItemSmoleContent.first().getElementsByTag("p");
			if (!elementsP.isEmpty()) {
				for (Element elementP : elementsP) {
					Elements elementsByTag2 = elementP.getElementsByTag("strong");
					if (!elementsByTag2.isEmpty()) {

						if (elementsByTag2.text().contains("Бренд:")) {
							result.setBrand(getTextFromElementByTag(elementP, "span"));
							continue;
						}

						if (elementsByTag2.text().contains("Материал:")) {
							result.setMaterial(getTextFromElementByTag(elementP, "p").replace("Материал: ", ""));
							continue;
						}

						if (elementsByTag2.text().contains("Покрытие:")) {
							result.setCoating(getTextFromElementByTag(elementP, "p").replace("Покрытие: ", ""));
							continue;
						}

						if (elementsByTag2.text().contains("Конструкция полотна:")) {
							result.setConstruction(
									getTextFromElementByTag(elementP, "p").replace("Конструкция полотна: ", ""));
							continue;
						}

						if (elementsByTag2.text().contains("Цвет:")) {
							result.setColor(getTextFromElementByTag(elementP, "p").replace("Цвет: ", ""));
							continue;
						}

						if (elementsByTag2.text().contains("Тип двери:")) {
							result.setTipe(getTextFromElementByTag(elementP, "p").replace("Тип двери: ", ""));
							continue;
						}
					}
				}
			}
		}

		return result;
	}

	private Door getInfoFromAreaRazm(Document doc, Door result) {
		Elements elementsRazm = doc.getElementsByClass("razm");
		if (!elementsRazm.isEmpty()) {

			if (!elementsRazm.get(0).getElementsByClass("tt2").isEmpty()) {
				result.setSize(elementsRazm.get(0).getElementsByClass("tt2").text().replace("Размеры:", ""));
			}
		}
		return result;
	}

	private Door getInfoFromAreaCrumbs(Document doc, Door result) {
		Elements elementsCrumbs = doc.getElementsByClass("crumbs");
		if (!elementsCrumbs.isEmpty()) {

			Elements elementsByTag = elementsCrumbs.first().getElementsByTag("li");
			if (!elementsByTag.isEmpty()) {

				result.setName(elementsByTag.last().text());
				result.setCollection(elementsByTag.get(elementsByTag.size() - 3).text());
			}
		}
		return result;
	}

	private Door getDoorImages(Document doc, Door result) {
		List<DoorImage> doorImages = new ArrayList<>();

		Elements elementsShopImg = doc.getElementsByClass("shop_img");
		if (!elementsShopImg.isEmpty()) {

			Elements selectA = elementsShopImg.select("a");
			if (!selectA.isEmpty()) {

				int countImage = 1;
				for (Element element : selectA) {

					DoorImage doorImage = new DoorImage();

					String href = element.attr("abs:href");

					doorImage.setImage(getImage(href));
					doorImage.setName(result.getName().replaceAll(" ", "_") + "_" + countImage);

					if (href.contains(".")) {
						String[] split = href.split("\\.");
						doorImage.setType(split[split.length - 1]);
					}

					countImage++;
					doorImages.add(doorImage);
				}

			}
		}

		result.setDoorImages(doorImages);

		return result;
	}

	private String getTextFromElementByTag(Element element, String tagName) {

		String result = "";

		Elements elements = element.getElementsByTag(tagName);
		if (!elements.isEmpty()) {
			result = elements.first().text();
		}

		return result;
	}

	private byte[] getImage(String imgURL) {

		URL url = null;
		try {
			url = new URL(imgURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in = null;
		try {
			in = new BufferedInputStream(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		try {
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] response = out.toByteArray();

		return response;

	}

}
