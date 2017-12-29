package com.denis.parser.yur.backend.service.htmlinfo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InfoFromAreaShopItemSmoleContent implements Runnable {

	private Document doc;

	private String doorBrand = "";
	private String doorMaterial = "";
	private String doorCoating = "";
	private String doorConstruction = "";
	private String doorColor = "";
	private String doorType = "";

	public InfoFromAreaShopItemSmoleContent(Document doc) {
		this.doc = doc;
	}

	public String getBrand() {
		return doorBrand;
	}

	public String getMaterial() {
		return doorMaterial;
	}

	public String getCoating() {
		return doorCoating;
	}

	public String getConstruction() {
		return doorConstruction;
	}

	public String getColor() {
		return doorColor;
	}

	public String getType() {
		return doorType;
	}

	@Override
	public void run() {
		Elements elementsShopItemSmoleContent = doc.getElementsByClass("shop-item-smole-content");
		if (!elementsShopItemSmoleContent.isEmpty()) {
			Elements elementsP = elementsShopItemSmoleContent.first().getElementsByTag("p");
			if (!elementsP.isEmpty()) {
				for (Element elementP : elementsP) {
					Elements elementsByTag2 = elementP.getElementsByTag("strong");
					if (!elementsByTag2.isEmpty()) {

						if (elementsByTag2.text().contains("Бренд:")) {
							doorBrand = getTextFromElementByTag(elementP, "span");
							continue;
						}

						if (elementsByTag2.text().contains("Материал:")) {
							doorMaterial = getTextFromElementByTag(elementP, "p").replace("Материал: ", "");
							continue;
						}

						if (elementsByTag2.text().contains("Покрытие:")) {
							doorCoating = getTextFromElementByTag(elementP, "p").replace("Покрытие: ", "");
							continue;
						}

						if (elementsByTag2.text().contains("Конструкция полотна:")) {
							doorConstruction = getTextFromElementByTag(elementP, "p").replace("Конструкция полотна: ",
									"");
							continue;
						}

						if (elementsByTag2.text().contains("Цвет:")) {
							doorColor = getTextFromElementByTag(elementP, "p").replace("Цвет: ", "");
							continue;
						}

						if (elementsByTag2.text().contains("Тип двери:")) {
							doorType = getTextFromElementByTag(elementP, "p").replace("Тип двери: ", "");
							continue;
						}
					}
				}
			}
		}
	}

	private String getTextFromElementByTag(Element element, String tagName) {

		String result = "";

		Elements elements = element.getElementsByTag(tagName);
		if (!elements.isEmpty()) {
			result = elements.first().text();
		}

		return result;
	}

}
