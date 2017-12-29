package com.denis.parser.yur.backend.service.htmlinfo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.denis.parser.yur.backend.dto.DoorImage;

public class InfoFromAreaShopImg implements Runnable {
	private Document doc;
	private String name = "standartName";

	private List<DoorImage> doorImages = new ArrayList<>();

	public InfoFromAreaShopImg(Document doc) {
		this.doc = doc;
	}

	public List<DoorImage> getDoorImages() {
		return doorImages;
	}

	@Override
	public void run() {

		Elements elementsShopImg = doc.getElementsByClass("shop_img");
		if (!elementsShopImg.isEmpty()) {

			Elements selectA = elementsShopImg.select("a");
			if (!selectA.isEmpty()) {
				int countImage = 1;
				Set<String> hrefImages = new HashSet<>();
				for (Element element : selectA) {
					hrefImages.add(element.attr("abs:href"));
				}

				for (String href : hrefImages) {
					DoorImage doorImage = new DoorImage();
					doorImage.setImage(getImage(href));
					doorImage.setName(name.replaceAll(" ", "_") + "_" + countImage);

					if (href.contains(".")) {
						String[] split = href.split("\\.");
						doorImage.setType(split[split.length - 1]);
					}

					countImage++;
					doorImages.add(doorImage);
				}
			}
		}
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

	public void renameImages(String newName) {
		for (DoorImage doorImage : doorImages) {
			doorImage.setName(doorImage.getName().replaceAll(name, newName));
		}
	}

}
