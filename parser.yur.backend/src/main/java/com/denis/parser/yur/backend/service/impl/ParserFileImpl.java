package com.denis.parser.yur.backend.service.impl;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.denis.parser.yur.backend.dao.DoorDAO;
import com.denis.parser.yur.backend.dao.DoorImageDAO;
import com.denis.parser.yur.backend.dao.DoorPriceDAO;
import com.denis.parser.yur.backend.dto.Door;
import com.denis.parser.yur.backend.dto.DoorImage;
import com.denis.parser.yur.backend.dto.DoorPrice;
import com.denis.parser.yur.backend.service.BackupInformation;
import com.denis.parser.yur.backend.service.ParserFile;
import com.denis.parser.yur.backend.service.Translit;
import com.denis.parser.yur.backend.utils.StringUtils;

@Service("parserFile")
public class ParserFileImpl implements ParserFile {

	@Autowired
	private BackupInformation backupInformation;
	@Autowired
	private DoorDAO doorDAO;
	@Autowired
	private DoorPriceDAO doorPriceDAO;
	@Autowired
	private DoorImageDAO doorImageDAO;

	private String pathToSqlFile;
	private String pathToTxtFile;
	private List<String> chooseCategories;
	private String pathFolder;

	@Override
	public void setPathToSqlFile(String pathToSqlFile) {
		this.pathToSqlFile = pathToSqlFile;
	}

	@Override
	public void setPathToTxtFile(String pathToTxtFile) {
		this.pathToTxtFile = pathToTxtFile;
	}

	@Override
	public void setCategories(List<String> chooseCategories) {
		this.chooseCategories = chooseCategories;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start() {

		Map<String, Map> allProductsFromeSQLFile = backupInformation.getAllProductsFromeSQLFile(pathToSqlFile);
		Map<String, Map> allProductsFromeTxtFiles = new HashMap<>();
		Map<String, List<Door>> allProductsFromeDB = new HashMap<>();

		Map<String, Map> allProducts = backupInformation.getAllProductsFromTxtFile(pathToTxtFile);
		if (allProducts != null) {
			allProductsFromeTxtFiles.putAll(allProducts);
		}

		List<Door> newDoors = new ArrayList<>();

		for (Entry<String, Map> entry : allProductsFromeTxtFiles.entrySet()) {

			Map<String, String> productInfo = entry.getValue();

			if (productInSQLFile(productInfo, allProductsFromeSQLFile)) {
				System.out.println("Product in SQL file!!!!!");
				updatePriceInSQL(productInfo, allProductsFromeSQLFile, chooseCategories);
				updatePriceInDB(productInfo, allProductsFromeDB);

			} else if (productInDB(productInfo, allProductsFromeDB)) {
				System.out.println("Product in DB!!!!!");
				updatePriceInDB(productInfo, allProductsFromeDB);
				Door door = getDoorFromDB(productInfo, allProductsFromeDB);
				setProductToAllProductsFromeSQLFile(door, allProductsFromeSQLFile, chooseCategories);
				newDoors.add(door);
			} else {
				System.out.println("It is new product!!!!!");
			}
		}

		Path path = backupInformation.saveModifySQLFile(pathToSqlFile, allProductsFromeSQLFile);

		backupInformation.saveDoorsImagesToTempFolder(newDoors, allProductsFromeSQLFile, path);

	}

	private boolean setProductToAllProductsFromeSQLFile(Door door, Map<String, Map> allProductsFromeSQLFile,
			List<String> categories) {

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");

		Integer doorId = last.get("ocProduct") + 1;
		door.setId(doorId);
		last.put("ocProduct", doorId);

		if (updateOcProductToCategoryMap(door, allProductsFromeSQLFile, categories) &&

				updateOcProductAttributeMap(door, allProductsFromeSQLFile) &&

				updateOcProductDescriptionMap(door, allProductsFromeSQLFile) &&

				updateOcProductMap(door, allProductsFromeSQLFile) &&

				updateOcProductRewardMap(door, allProductsFromeSQLFile) &&

				updateOcProductToStoreMap(door, allProductsFromeSQLFile) &&

				updateOcProductImageMap(door, allProductsFromeSQLFile) &&

				updateOcUrlAliasMap(door, allProductsFromeSQLFile)) {
			return true;
		}

		if (false) {
			Map<String, String> preparationRequests = preparationRequests(door, allProductsFromeSQLFile); /// !!!!!
																											/// готовые
																											/// запросы
																											/// на
																											/// добавление
																											/// новых
																											/// дверей
			if (setNewDoorInformationToMaps(preparationRequests, allProductsFromeSQLFile)) {
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean updateOcUrlAliasMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocUrlAliasMap = allProductsFromeSQLFile.get("ocUrlAliasMap");

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer urlAliasId = last.get("ocUrlAlias") + 1;

		Integer doorId = door.getId();
		String keyword = Translit.transliterate(door.getName());

		Map<String, String> map1 = new HashMap<>();
		map1.put("query", "category_id=" + doorId);
		map1.put("keyword", keyword);
		map1.put("url_alias_id", urlAliasId.toString());

		ocUrlAliasMap.put(urlAliasId, map1);
		allProductsFromeSQLFile.put("ocUrlAliasMap", ocUrlAliasMap);

		last.put("ocUrlAlias", urlAliasId);

		return true;
	}

	private boolean updateOcProductImageMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocProductImageMap = allProductsFromeSQLFile.get("ocProductImageMap");

		@SuppressWarnings("unchecked")
		Map<Integer, List<String>> productImage = allProductsFromeSQLFile.get("productImage");
		if (productImage == null) {
			productImage = new HashMap<>();
		}

		Integer productId = door.getId();

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer productImageId = last.get("ocProductImage");

		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocProductMap = allProductsFromeSQLFile.get("ocProductMap");
		String image = ocProductMap.get(productId).get("image");

		Map<String, String> map1 = new HashMap<>();
		map1.put("image", image);
		map1.put("product_id", productId.toString());
		map1.put("sort_order", "0");
		map1.put("product_image_id", productImageId.toString());

		ocProductImageMap.put(productId, map1);
		allProductsFromeSQLFile.put("ocProductImageMap", ocProductImageMap);

		if (!productImage.containsKey(productId)) {
			productImage.put(productId, new ArrayList<>());
		}

		productImage.get(productId).add(image);

		allProductsFromeSQLFile.put("productImage", productImage);

		return true;
	}

	private boolean updateOcProductToStoreMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Integer> ocProductToStoreMap = allProductsFromeSQLFile.get("ocProductToStoreMap");
		Integer doorId = door.getId();
		ocProductToStoreMap.put(doorId, 0);
		allProductsFromeSQLFile.put("ocProductToStoreMap", ocProductToStoreMap);

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer productToStore = last.get("ocProductToStore") + 1;
		last.put("ocProductToStore", productToStore);

		return true;

	}

	private boolean updateOcProductRewardMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocProductRewardMap = allProductsFromeSQLFile.get("ocProductRewardMap");
		Integer doorId = door.getId();
		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");

		Integer productRewardId = last.get("ocProductReward") + 1;

		Map<String, String> map1 = new HashMap<>();
		map1.put("product_id", doorId.toString());
		map1.put("product_reward_id", productRewardId.toString());
		map1.put("customer_group_id", "1");
		map1.put("points", "0");

		ocProductRewardMap.put(productRewardId, map1);

		last.put("ocProductReward", productRewardId);

		return true;
	}

	private boolean updateOcProductMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocProductMap = allProductsFromeSQLFile.get("ocProductMap");

		Integer doorId = door.getId();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat hours = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-dd");
		String brand = door.getBrand();
		Double price = door.getDoorPrice().getTotalPrice();
		String dateAvailable = date.format(cal.getTime());
		String dateAdded = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String dateModified = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String model = door.getColor();

		String manufacturerId = "";
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocManufacturerMap = allProductsFromeSQLFile.get("ocManufacturerMap");
		if (ocManufacturerMap != null) {
			for (Entry<Integer, Map<String, String>> entry : ocManufacturerMap.entrySet()) {
				Map<String, String> entryValue = entry.getValue();
				if (entryValue != null) {
					if (entryValue.get("name").equals(brand)) {
						manufacturerId = entryValue.get("manufacturer_id");
						break;
					}
				}
			}
		}

		String status = "0";
		if (price > 0) {
			status = "1";
		}

		String image = "";

		List<String> pathForDoorImages = getPathForDoorImages(door);
		if (pathForDoorImages != null && !pathForDoorImages.isEmpty()) {
			image = getPathForDoorImages(door).get(0);
		}

		Map<String, String> map1 = new HashMap<>();

		map1.put("length_class_id", "1");
		map1.put("isbn", "");
		map1.put("points", "0");
		map1.put("ean", "");
		map1.put("shipping", "1");
		map1.put("jan", "");
		map1.put("price", price.toString());
		map1.put("product_id", doorId.toString());
		map1.put("viewed", "191");
		map1.put("model", model);
		map1.put("stock_status_id", "5");
		map1.put("sku", "");
		map1.put("sort_order", "1");
		map1.put("height", "0.00000000");
		map1.put("manufacturer_id", manufacturerId);
		map1.put("quantity", "100");
		map1.put("subtract", "1");
		map1.put("tax_class_id", "0");
		map1.put("length", "0.00000000");
		map1.put("upc", "");
		map1.put("weight", "0.00000000");
		map1.put("mpn", "");
		map1.put("date_added", dateAdded);
		map1.put("date_modified", dateModified);
		map1.put("width", "0.00000000");
		map1.put("weight_class_id", "1");
		map1.put("location", "");
		map1.put("date_available", dateAvailable);
		map1.put("minimum", "1");
		map1.put("status", status);

		map1.put("image", image);

		ocProductMap.put(doorId, map1);
		allProductsFromeSQLFile.put("ocProductMap", ocProductMap);

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer productImage = last.get("ocProductImage") + 1;
		last.put("ocProductImage", productImage);
		last.put("ocProduct", doorId);

		return true;
	}

	private boolean updateOcProductDescriptionMap(Door door, Map<String, Map> allProductsFromeSQLFile) {

		@SuppressWarnings("unchecked")
		Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap = allProductsFromeSQLFile
				.get("ocProductDescriptionMap");

		Integer doorId = door.getId();
		String name = door.getName();
		String description = "&lt;p&gt;&lt;strong&gt;Цена включает в себя&lt;/strong&gt;: дверное полотно, коробка без порога, наличник с двух сторон.&lt;br /&gt;\\r\\n&lt;strong&gt;Дополнительная комплектация: &lt;/strong&gt;фурнитура, доборы.&lt;/p&gt;\\r\\n";

		Map<Integer, Map<String, String>> map1 = new HashMap<>();
		Map<String, String> map2 = new HashMap<>();
		map2.put("meta_description", "");
		map2.put("u_h2", "");
		map2.put("u_h1", "");
		map2.put("product_id", doorId.toString());
		map2.put("u_title", "");
		map2.put("name", name);
		map2.put("description", description);
		map2.put("language_id", description);
		map2.put("tag", "");

		for (Integer i = 1; i <= 2; i++) {
			map2.put("language_id", i.toString());
			map1.put(i, map2);
		}
		ocProductDescriptionMap.put(doorId, map1);
		allProductsFromeSQLFile.put("ocProductDescriptionMap", ocProductDescriptionMap);

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer productDescription = last.get("ocProductDescription") + 1;
		last.put("ocProductDescription", productDescription);

		return true;
	}

	private boolean updateOcProductAttributeMap(Door door, Map<String, Map> allProductsFromeSQLFile) {
		@SuppressWarnings("unchecked")
		Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> ocProductAttributeMap = allProductsFromeSQLFile
				.get("ocProductAttributeMap");

		Integer doorId = door.getId();

		String construction = door.getConstruction(); // 4
		String material = door.getMaterial(); // 5
		String coating = door.getCoating(); // 6
		String type = door.getType(); // 7
		String size = door.getSize().replaceAll("х", "x");// 8

		switch (type) {
		case "остекленная с рисунком":
			type = "Стекло с рисунком";
			break;
		case "глухая":
			type = "Без стекла";
			break;
		case "остекленная":
			type = "Матовое";
			break;
		}

		if (!size.isEmpty()) {
			String[] allSize = size.split(" ");
			// 200/60, 70, 80, 90 см, толщина: 36 мм
			// 600x2000х40 700x2000х40 800x2000х40 900x2000х40
			String height = "";
			String thickness = "";
			String width = "";
			StringBuffer sbSize = new StringBuffer();
			for (int i = 0; i < allSize.length; i++) {
				String[] splitSize = allSize[i].split("x");
				if (splitSize.length == 3) {
					if (height.equals("")) {
						height = splitSize[1];
						if (height.contains("000")) {
							height = height.replace("000", "00");
						}
						sbSize.append(height).append("/");
					}
					if (thickness.equals("")) {
						thickness = splitSize[2];
					}
					width = splitSize[0];
					if (width.contains("00")) {
						width = width.replace("00", "0");
					}
				}
				sbSize.append(width);
				if (i < allSize.length - 1) {
					sbSize.append(", ");
				}
			}

			sbSize.append(" см, толщина: ").append(thickness).append(" мм");
			size = sbSize.toString();
		}

		Map<Integer, Map<Integer, Map<String, String>>> map3 = new HashMap<>();

		for (Integer i = 4; i <= 8; i++) {
			Map<String, String> map1 = new HashMap<>();
			Map<Integer, Map<String, String>> map2 = new HashMap<>();
			map1.put("attribute_id", i.toString());
			map1.put("product_id", doorId.toString());
			for (Integer j = 1; j <= 2; j++) {
				map1.put("language_id", j.toString());
				// косяк!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				switch (i) {
				case 4:
					map1.put("text", construction); // Особенности
					break;
				case 5:
					map1.put("text", material); // Материал
					break;
				case 6:
					map1.put("text", coating); // Отделка
					break;
				case 7:
					map1.put("text", type); // Стекло
					break;
				case 8:
					map1.put("text", size); // Размеры
					break;
				}
				map2.put(j, map1);
			}
			map3.put(i, map2);
		}

		ocProductAttributeMap.put(doorId, map3);
		allProductsFromeSQLFile.put("ocProductAttributeMap", ocProductAttributeMap);

		@SuppressWarnings("unchecked")
		Map<String, Integer> last = allProductsFromeSQLFile.get("last");
		Integer productAttribute = last.get("ocProductAttribute") + 1;
		last.put("ocProductAttribute", productAttribute);

		return true;

	}

	@SuppressWarnings("rawtypes")
	private boolean updateOcProductToCategoryMap(Door door, Map<String, Map> allProductsFromeSQLFile,
			List<String> categories) {
		int doorId = door.getId();

		@SuppressWarnings("unchecked")
		Map<Integer, List<Integer>> ocProductToCategoryMap = allProductsFromeSQLFile.get("ocProductToCategoryMap");

		if (categories.isEmpty()) {
			ocProductToCategoryMap.put(doorId, Arrays.asList(0));
		} else {
			List<Integer> list = new ArrayList<>();
			for (String category : categories) {
				if (StringUtils.isNumeric(category)) {
					list.add(Integer.valueOf(category));
				}
			}
			ocProductToCategoryMap.put(doorId, list);

			@SuppressWarnings("unchecked")
			Map<String, Integer> last = allProductsFromeSQLFile.get("last");
			Integer ocProductToCategory = last.get("ocProductToCategory") + 1;
			last.put("ocProductToCategory", ocProductToCategory);

			return true;
		}
		return false;
	}

	private Door getDoorFromDB(Map<String, String> productInfo, Map<String, List<Door>> allProductsFromeDB) {
		String name = productInfo.get("name");
		String collection = productInfo.get("collection");
		String brand = productInfo.get("brand");

		List<Door> doors = allProductsFromeDB.get(collection);

		for (Door door : doors) {
			if (door.getBrand().equals(brand) && door.getName().equals(name)) {
				return door;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private boolean updatePriceInSQL(Map<String, String> productInfo, Map<String, Map> allProductsFromeSQLFile,
			List<String> chooseCategories) {

		String price = productInfo.get("totalPrice");

		if (price != null && StringUtils.isNumeric(price)) {

			String nameProductInfo = productInfo.get("name");
			String brandProductInfo = productInfo.get("brand");

			String keyForCompare = getKeyForCompare(nameProductInfo, brandProductInfo);

			if (allProductsFromeSQLFile.get("productNameNum").containsKey(keyForCompare)) {
				@SuppressWarnings("unchecked")
				Map<String, Integer> mapFromProductNameNum = (Map<String, Integer>) allProductsFromeSQLFile
						.get("productNameNum").get(keyForCompare);
				Integer productId = mapFromProductNameNum.get("productId");

				@SuppressWarnings("unchecked")
				Map<String, String> mapFromOcProductMap = (Map<String, String>) allProductsFromeSQLFile
						.get("ocProductMap").get(productId);

				mapFromOcProductMap.put("price", price);

				if (!chooseCategories.isEmpty()) {
					@SuppressWarnings("unchecked")
					Map<String, String> mapFromOcProductToCategoryMap = (Map<String, String>) allProductsFromeSQLFile
							.get("ocProductToCategoryMap").get(productId);

				}

				return true;
			}
		}
		return false;
	}

	private boolean updatePriceInDB(Map<String, String> productInfo, Map<String, List<Door>> allProductsFromeDB) {

		String price = productInfo.get("totalPrice");

		if (price != null && StringUtils.isNumeric(price)) {

			String name = productInfo.get("name");
			String collection = productInfo.get("collection");
			String brand = productInfo.get("brand");

			List<Door> doors = allProductsFromeDB.get(collection);
			if (doors == null) {
				doors = getDoorCollectionFromDB(productInfo);
				allProductsFromeDB.put(collection, doors);
			}
			if (doors != null) {
				Door door = null;

				Integer doorInDoors = 0;
				for (int i = 0; i < doors.size(); i++) {
					if (doors.get(i).getBrand().equals(brand) && doors.get(i).getCollection().equals(collection)
							&& doors.get(i).getName().equals(name)) {
						door = doors.get(i);
						doorInDoors = i;
						break;
					}
				}

				if (door != null) {

					Integer doorId = door.getId();
					DoorPrice doorPrice = door.getDoorPrice();

					if (doorPrice == null) {
						doorPrice = new DoorPrice();
						doorPrice.setDoorId(doorId);
					}

					String polotno = productInfo.get("polotno");
					String korobka = productInfo.get("korobka");
					String nalichnik = productInfo.get("nalichnik");
					String totalPrice = productInfo.get("totalPrice");

					if (StringUtils.isNumeric(polotno) && StringUtils.isNumeric(korobka)
							&& StringUtils.isNumeric(nalichnik) && StringUtils.isNumeric(totalPrice)) {

						double leaf = Double.valueOf(polotno);// полотно
						double frame = Double.valueOf(korobka);// коробка
						double clypeus = Double.valueOf(nalichnik);// наличники
						double totalPriceDouble = Double.valueOf(totalPrice);

						doorPrice.setLeaf(leaf);
						doorPrice.setFrame(frame);
						doorPrice.setClypeus(clypeus);
						doorPrice.setTotalPrice(totalPriceDouble);

						if (doorPriceDAO.saveOrUpdate(doorPrice)) {
							door.setDoorPrice(doorPrice);
							doors.add(doorInDoors, door);
							allProductsFromeDB.put(collection, doors);
							return true;
						}
					}
				}

			}
		}

		return false;
	}

	private boolean productInSQLFile(Map<String, String> productInfo, Map<String, Map> allProductsFromeSQLFile) {
		String nameProductInfo = productInfo.get("name");
		String brandProductInfo = productInfo.get("brand");

		String keyForCompare = getKeyForCompare(nameProductInfo, brandProductInfo);

		if (allProductsFromeSQLFile.get("productNameNum").get(keyForCompare) != null) {
			return true;
		}

		return false;
	}

	private boolean productInDB(Map<String, String> productInfo, Map<String, List<Door>> allProductsFromeDB) {

		String name = productInfo.get("name");
		String brand = productInfo.get("brand");
		String collection = productInfo.get("collection");

		if (allProductsFromeDB == null || allProductsFromeDB.isEmpty() || !allProductsFromeDB.containsKey(collection)) {

			allProductsFromeDB.put(collection, getDoorCollectionFromDB(productInfo));

		}

		List<Door> doorCollection = allProductsFromeDB.get(collection);
		if (doorCollection == null || doorCollection.isEmpty()) {
			return false;
		}

		for (Door door : doorCollection) {
			if (door.getBrand().equals(brand) && door.getCollection().equals(collection)
					&& door.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	private List<Door> getDoorCollectionFromDB(Map<String, String> productInfo) {
		String collection = productInfo.get("collection");
		List<Door> result = doorDAO.findDoorsByCollection(collection);
		if (result != null) {
			for (Door door : result) {
				String doorName = door.getName();
				String doorBrand = door.getBrand();
				Integer doorId = doorDAO.findDoorByBrandCollectionName(doorBrand, collection, doorName).getId();
				List<DoorImage> doorImages = doorImageDAO.getByDoorId(doorId);
				DoorPrice doorPrice = doorPriceDAO.getByDoorId(doorId);
				door.setDoorImages(doorImages);
				door.setDoorPrice(doorPrice);
			}
		}
		return result;
	}

	private String getKeyForCompare(String name, String brand) {

		StringBuffer sb = new StringBuffer();
		sb.append(brand).append(" -> ");
		sb.append(name);

		return sb.toString();
	}

	private Map<String, String> preparationRequests(Door door, Map<String, Map> allProductsFromeSQLFile) {

		Map<String, String> result = new HashMap<>();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat hours = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-dd");
		@SuppressWarnings("unchecked")
		Map<String, Integer> lastMap = allProductsFromeSQLFile.get("last");
		if (lastMap != null) {
			int productId = lastMap.get("ocProduct") + 1;
			int imageId = lastMap.get("ocProductImage") + 1;
			int productRewardId = lastMap.get("ocProductReward") + 1;
			int urlAliasId = lastMap.get("ocUrlAlias") + 1;

			String queryOcProduct = getQueryOcProduct(door, productId, cal, hours, date, allProductsFromeSQLFile);
			String queryOcProductDescription = getQueryOcProductDescription(door, productId);
			String queryOcProductImage = getQueryOcProductImage(door, productId, imageId);
			String queryOcProductReward = getQueryOcProductReward(productId, productRewardId);
			String queryOcProductToCategory = getQueryOcProductToCategory(door, productId);
			String queryOcProductToStore = getQueryOcProductToStore(productId);
			String queryOcUrlAlias = getQueryOcUrlAlias(door, productId, urlAliasId);
			String queryOcProductAttribute = getQueryOcProductAttribute(door, productId);

			result.put("ocProduct", queryOcProduct);
			result.put("ocProductDescription", queryOcProductDescription);
			result.put("ocProductImage", queryOcProductImage);
			result.put("ocProductReward", queryOcProductReward);
			result.put("ocProductToCategory", queryOcProductToCategory);
			result.put("ocProductToStore", queryOcProductToStore);
			result.put("ocUrlAlias", queryOcUrlAlias);
			result.put("ocProductAttribute", queryOcProductAttribute);

			lastMap.put("ocProduct", productId);
			lastMap.put("ocProductImage", imageId);
			lastMap.put("ocProductReward", productRewardId);
			lastMap.put("ocUrlAlias", urlAliasId);
		}
		return result;

	}

	private boolean setNewDoorInformationToMaps(Map<String, String> queries,
			@SuppressWarnings("rawtypes") Map<String, Map> allProductsFromeSQLFile) {
		if (backupInformation.updateInformation(queries, allProductsFromeSQLFile)) {
			return true;
		}
		return false;

	}

	private List<String> getPathForDoorImages(Door door) {

		List<String> result = new ArrayList<>();

		String collection = door.getCollection();
		String brand = door.getBrand();

		List<DoorImage> doorImages = door.getDoorImages();

		if (doorImages != null) {

			for (DoorImage doorImage : doorImages) {

				StringBuilder sb = new StringBuilder();
				sb.append("data/doors/").append(Translit.transliterate(brand).toLowerCase()).append("/");
				sb.append(Translit.transliterate(collection).toLowerCase()).append("/");
				sb.append(Translit.transliterate(doorImage.getName()).toLowerCase()).append(".");
				sb.append(doorImage.getType());

				result.add(sb.toString());
			}
		}

		return result;
	}

	private String getQueryOcProduct(Door door, int product_id, Calendar cal, SimpleDateFormat hours,
			SimpleDateFormat date, Map<String, Map> allProductsFromeSQLFile) {

		String brand = door.getBrand();
		double price = door.getDoorPrice().getTotalPrice();
		String date_available = date.format(cal.getTime());
		String date_added = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String date_modified = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String model = door.getColor();

		String manufacturer_id = "";
		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, String>> ocManufacturerMap = allProductsFromeSQLFile.get("ocManufacturerMap");
		if (ocManufacturerMap != null) {
			for (Entry<Integer, Map<String, String>> entry : ocManufacturerMap.entrySet()) {
				Map<String, String> entryValue = entry.getValue();
				if (entryValue != null) {
					if (entryValue.get("name").equals(brand)) {
						manufacturer_id = entryValue.get("manufacturer_id");
						break;
					}
				}
			}
		}

		String status = "0";
		if (price > 0) {
			status = "1";
		}

		String image = "";

		List<String> pathForDoorImages = getPathForDoorImages(door);
		if (pathForDoorImages != null && !pathForDoorImages.isEmpty()) {
			image = getPathForDoorImages(door).get(0);
		}

		StringBuilder sb = new StringBuilder();
		sb.append(
				"INSERT INTO `oc_product` (`product_id`, `model`, `sku`, `upc`, `ean`, `jan`, `isbn`, `mpn`, `location`, `quantity`, `stock_status_id`, `image`, `manufacturer_id`, `shipping`, `price`, `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, `length`, `width`, `height`,`length_class_id`, `subtract`, `minimum`, `sort_order`, `status`,`date_added`, `date_modified`, `viewed`) VALUES ('");
		sb.append(product_id).append("', '");
		sb.append(model).append("', '', '', '', '', '', '', '', '100','5', '");
		sb.append(image).append("', '");
		sb.append(manufacturer_id).append("', '1', '");
		sb.append(price).append("', '0', '0', '");
		sb.append(date_available)
				.append("', '0.00000000', '1', '0.00000000', '0.00000000', '0.00000000', '1', '1','1', '1', '");
		sb.append(status).append("', '");
		sb.append(date_added).append("', '");
		sb.append(date_modified).append("', '169');");

		return sb.toString();
	}

	private String getQueryOcProductDescription(Door door, int product_id) {
		String name = door.getName();
		String description = ""; ////////////

		StringBuilder sbUSA = new StringBuilder();
		sbUSA.append(
				"INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
		sbUSA.append(product_id).append("', '1', '");
		sbUSA.append(name).append("', '', '', '', '', '', '', '');");

		StringBuilder sbRUS = new StringBuilder();
		sbRUS.append(
				"INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
		sbRUS.append(product_id).append("', '2', '");
		sbRUS.append(name).append("', '");
		sbRUS.append(description).append("', '', '', '', '', '', '');");

		return sbUSA.toString() + "\n" + sbRUS.toString();
	}

	private String getQueryOcProductImage(Door door, int product_id, int img_id) {
		String image = "";

		List<String> pathForDoorImages = getPathForDoorImages(door);
		if (pathForDoorImages != null && !pathForDoorImages.isEmpty()) {
			image = getPathForDoorImages(door).get(0);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `oc_product_image` (`product_image_id`, `product_id`, `image`, `sort_order`) VALUES ('");
		sb.append(img_id).append("', '");
		sb.append(product_id).append("', '");
		sb.append(image).append("', '0');");

		return sb.toString();
	}

	private String getQueryOcProductReward(int product_id, int product_reward_id) {

		StringBuilder sb = new StringBuilder();
		sb.append(
				"INSERT INTO `oc_product_reward` (`product_reward_id`, `product_id`, `customer_group_id`, `points`) VALUES ('");
		sb.append(product_reward_id).append("', '");
		sb.append(product_id).append("', '1', '0');");

		return sb.toString();
	}

	private String getQueryOcProductToCategory(Door door, int product_id) {
		/////// придумать как закидывать продукт в нужную категорию
		String category_id = "0";
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO `oc_product_to_category` (`product_id`, `category_id`) VALUES ('");
		sb.append(product_id).append("', '");
		sb.append(category_id).append("');");

		return sb.toString();

	}

	private String getQueryOcProductToStore(int product_id) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `oc_product_to_store` (`product_id`, `store_id`) VALUES ('");
		sb.append(product_id).append("', '0');");

		return sb.toString();
	}

	private String getQueryOcUrlAlias(Door door, int product_id, int url_id) {
		StringBuilder sb = new StringBuilder();
		String keyword = Translit.transliterate(door.getName());

		sb.append("INSERT INTO `oc_url_alias` (`url_alias_id`, `query`, `keyword`) VALUES ('");
		sb.append(url_id).append("', 'product_id=");
		sb.append(product_id).append("', '");
		sb.append(keyword).append("');");

		return sb.toString();
	}

	private String getQueryOcProductAttribute(Door door, int product_id) {

		String construction = door.getConstruction();
		String material = door.getMaterial();
		String coating = door.getCoating();
		String type = door.getType();
		String size = door.getSize().replaceAll("х", "x");

		// остекленная с рисунком
		switch (type) {
		case "остекленная с рисунком":
			type = "Стекло с рисунком";
			break;
		case "глухая":
			type = "Без стекла";
			break;
		case "остекленная":
			type = "Матовое";
			break;
		}

		if (!size.isEmpty()) {
			String[] allSize = size.split(" ");
			// 200/60, 70, 80, 90 см, толщина: 36 мм
			// 600x2000х40 700x2000х40 800x2000х40 900x2000х40
			String height = "";
			String thickness = "";
			String width = "";
			StringBuffer sbSize = new StringBuffer();
			for (int i = 0; i < allSize.length; i++) {
				String[] splitSize = allSize[i].split("x");
				if (splitSize.length == 3) {
					if (height.equals("")) {
						height = splitSize[1];
						if (height.contains("000")) {
							height = height.replace("000", "00");
						}
						sbSize.append(height).append("/");
					}
					if (thickness.equals("")) {
						thickness = splitSize[2];
					}
					width = splitSize[0];
					if (width.contains("00")) {
						width = width.replace("00", "0");
					}
				}
				sbSize.append(width);
				if (i < allSize.length - 1) {
					sbSize.append(", ");
				}
			}

			sbSize.append(" см, толщина: ").append(thickness).append(" мм");
			size = sbSize.toString();
		}

		String startLine = "INSERT INTO `oc_product_attribute` (`product_id`, `attribute_id`, `language_id`, `text`) VALUES ('";
		String attribute_id = "";
		String language_id = "";

		StringBuilder sb = new StringBuilder();

		for (int i = 4; i <= 8; i++) {
			attribute_id = String.valueOf(i);
			for (int j = 1; j <= 2; j++) {
				language_id = String.valueOf(j);
				sb.append(startLine);
				sb.append(product_id).append("', '");
				sb.append(attribute_id).append("', '");
				sb.append(language_id).append("', '");

				switch (i) {
				case 4:
					sb.append(construction); // Особенности
					break;
				case 5:
					sb.append(material); // Материал
					break;
				case 6:
					sb.append(coating); // Отделка
					break;
				case 7:
					sb.append(type); // Стекло
					break;
				case 8:
					sb.append(size); // Размеры
					break;
				}

				sb.append("');");
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	@Override
	public Map<String, String> getAllCategoriesFromSqlFile(String pathToFile) {
		return backupInformation.getAllCategoriesFromeSQLFile(pathToFile);
	}

}
