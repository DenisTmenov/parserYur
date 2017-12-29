package com.denis.parser.yur.backend.service.impl;

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

	private String pathToSQLFile;
	private List<String> pathToTxtFiles = Arrays.asList(new String[] { "d:\\TEST\\50. Севилья.txt" }); // !!!!!!!!!s

	private List<String> modifyLines = new ArrayList<>(); // не используется!!!!!!!!!!!!

	List<String> resultQueryOcProduct = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcProductDescription = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcProductImage = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcProductReward = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcProductToCategory = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcProductToStore = new ArrayList<>();// не используется!!!!!!!!!!!!
	List<String> resultQueryOcUrlAlias = new ArrayList<>();// не используется!!!!!!!!!!!!

	@Override
	public void setPathToOriginalFile(String pathToOriginalFile) {
		this.pathToSQLFile = pathToOriginalFile;
	}

	@Override
	public void setPathToTxtFiles(List<String> pathToTxtFiles) {
		this.pathToTxtFiles = pathToTxtFiles;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start() {

		Map<String, Map> allProductsFromeSQLFile = backupInformation.getAllProductsFromeSQLFile(pathToSQLFile);
		Map<String, Map> allProductsFromeTxtFiles = new HashMap<>();
		Map<String, List<Door>> allProductsFromeDB = new HashMap<>();

		for (String pathToTxtFile : pathToTxtFiles) {
			Map<String, Map> allProducts = backupInformation.getAllProductsFromTxtFile(pathToTxtFile);
			if (allProducts != null) {
				allProductsFromeTxtFiles.putAll(allProducts);
			}
		}

		for (Entry<String, Map> entry : allProductsFromeTxtFiles.entrySet()) {

			Map<String, String> productInfo = entry.getValue();

			if (productInSQLFile(productInfo, allProductsFromeSQLFile)) {
				System.out.println("Product in SQL file!!!!!");
				updatePriceInSQL(productInfo, allProductsFromeSQLFile);
				updatePriceInDB(productInfo, allProductsFromeDB);

			} else if (productInDB(productInfo, allProductsFromeDB)) {
				System.out.println("Product in DB!!!!!");
				updatePriceInDB(productInfo, allProductsFromeDB);
				Door door = getDoorFromDB(productInfo, allProductsFromeDB);
				setProductToSQLFile(door, allProductsFromeSQLFile);

			} else {
				System.out.println("It is new product!!!!!");
			}
		}

		backupInformation.saveModifySQLFile(pathToSQLFile, allProductsFromeSQLFile);

	}

	private boolean setProductToSQLFile(Door door, Map<String, Map> allProductsFromeSQLFile) {
		Map<String, String> preparationRequests = preparationRequests(door, allProductsFromeSQLFile); /// !!!!! готовые
																										/// запросы на
																										/// добавление
																										/// новых дверей
		if (setNewDoorInformationToMaps(preparationRequests, allProductsFromeSQLFile)) {
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
	private boolean updatePriceInSQL(Map<String, String> productInfo, Map<String, Map> allProductsFromeSQLFile) {

		String price = productInfo.get("totalPrice");

		if (price != null && StringUtils.isNumeric(price)) {

			String nameProductInfo = productInfo.get("name");
			String collectionProductInfo = productInfo.get("collection");
			String brandProductInfo = productInfo.get("brand");

			String keyForCompare = getKeyForCompare(nameProductInfo, collectionProductInfo, brandProductInfo);

			if (allProductsFromeSQLFile.get("productNameNum").containsKey(keyForCompare)) {
				@SuppressWarnings("unchecked")
				Map<String, Integer> mapFromProductNameNum = (Map<String, Integer>) allProductsFromeSQLFile
						.get("productNameNum").get(keyForCompare);
				Integer productId = mapFromProductNameNum.get("productId");

				@SuppressWarnings("unchecked")
				Map<String, String> mapFromOcProductMap = (Map<String, String>) allProductsFromeSQLFile
						.get("ocProductMap").get(productId);

				mapFromOcProductMap.put("price", price);
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
		String collectionProductInfo = productInfo.get("collection");
		String brandProductInfo = productInfo.get("brand");

		String keyForCompare = getKeyForCompare(nameProductInfo, collectionProductInfo, brandProductInfo);

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

	private String getKeyForCompare(String name, String collection, String brand) {

		StringBuffer sb = new StringBuffer();
		sb.append(brand).append(" -> ");
		sb.append(collection).append(" -> ");
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

			result.put("ocProduct", queryOcProduct);
			result.put("ocProductDescription", queryOcProductDescription);
			result.put("ocProductImage", queryOcProductImage);
			result.put("ocProductReward", queryOcProductReward);
			result.put("ocProductToCategory", queryOcProductToCategory);
			result.put("ocProductToStore", queryOcProductToStore);
			result.put("ocUrlAlias", queryOcUrlAlias);

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

		StringBuilder sb = new StringBuilder();

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

}
