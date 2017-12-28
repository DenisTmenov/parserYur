package com.denis.parser.yur.backend.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import com.denis.parser.yur.backend.service.BackupInformation;
import com.denis.parser.yur.backend.service.ManagerFiles;
import com.denis.parser.yur.backend.utils.StringUtils;

@Service("backupInformation")
public class BackupInformationImpl implements BackupInformation {

	// private List<String> allOriginalLines;
	@Autowired
	private ManagerFiles managerFiles;

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Map> getAllProductsFromeSQLFile(String pathToFile) {

		Map<String, Map> result = new HashMap<>();

		managerFiles.setPath(pathToFile);
		List<String> originalLines = managerFiles.readFromeFile();
		// maps from file
		Map<Integer, Map<String, String>> ocCategoryMap = new HashMap<>();
		Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap = new HashMap<>();
		Map<Integer, Integer> ocCategoryToStoreMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocManufacturerMap = new HashMap<>();
		Map<Integer, Integer> ocManufacturerToStoreMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductMap = new HashMap<>();
		Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> ocProductAttributeMap = new HashMap<>();
		// Map< product_id , Map< attribute_id , Map< language_id , Map<String,
		// String>>>>
		Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductImageMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductRewardMap = new HashMap<>();
		Map<Integer, List<Integer>> ocProductToCategoryMap = new HashMap<>();
		Map<Integer, Integer> ocProductToStoreMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocUrlAliasMap = new HashMap<>();

		// maps for fast searching
		Map<String, Integer> categoryNameNum = new HashMap<>();
		Map<String, Map<String, Integer>> productNameNum = new HashMap<>();
		Map<String, Integer> last = new HashMap<>();

		for (String originalLine : originalLines) {
			fillMapsFromFile(originalLine, ocCategoryMap, ocCategoryDescriptionMap, ocCategoryToStoreMap,
					ocManufacturerMap, ocManufacturerToStoreMap, ocProductMap, ocProductAttributeMap,
					ocProductDescriptionMap, ocProductImageMap, ocProductRewardMap, ocProductToCategoryMap,
					ocProductToStoreMap, ocUrlAliasMap, last);
		}

		fillCategoryNameNum(categoryNameNum, ocCategoryMap, ocCategoryDescriptionMap);
		fillProductNameNum(productNameNum, ocCategoryMap, ocCategoryDescriptionMap, ocProductMap, ocProductAttributeMap,
				ocProductDescriptionMap, ocManufacturerMap, ocProductToCategoryMap);

		result.put("ocCategoryMap", ocCategoryMap);
		result.put("ocCategoryDescriptionMap", ocCategoryDescriptionMap);
		result.put("categoryNameNum", categoryNameNum);
		result.put("ocCategoryToStoreMap", ocCategoryToStoreMap);
		result.put("ocManufacturerMap", ocManufacturerMap);
		result.put("ocManufacturerToStoreMap", ocManufacturerToStoreMap);
		result.put("ocProductMap", ocProductMap);
		result.put("ocProductAttributeMap", ocProductAttributeMap);
		result.put("ocProductDescriptionMap", ocProductDescriptionMap);
		result.put("ocProductImageMap", ocProductImageMap);
		result.put("ocProductRewardMap", ocProductRewardMap);
		result.put("ocProductToCategoryMap", ocProductToCategoryMap);
		result.put("ocProductToStoreMap", ocProductToStoreMap);
		result.put("ocUrlAliasMap", ocUrlAliasMap);
		result.put("productNameNum", productNameNum);
		result.put("last", last);

		return result;
	}

	private void fillMapsFromFile(String originalLine, Map<Integer, Map<String, String>> ocCategoryMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap,
			Map<Integer, Integer> ocCategoryToStoreMap, Map<Integer, Map<String, String>> ocManufacturerMap,
			Map<Integer, Integer> ocManufacturerToStoreMap, Map<Integer, Map<String, String>> ocProductMap,
			Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> ocProductAttributeMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap,
			Map<Integer, Map<String, String>> ocProductImageMap, Map<Integer, Map<String, String>> ocProductRewardMap,
			Map<Integer, List<Integer>> ocProductToCategoryMap, Map<Integer, Integer> ocProductToStoreMap,
			Map<Integer, Map<String, String>> ocUrlAliasMap, Map<String, Integer> last) {

		Integer id = 0;
		String idString = "";
		switch (getContainsINSERT(originalLine)) {

		case "INSERT INTO `oc_category`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_category`", ocCategoryMap,
					"category_id");
			setToLast(id, "ocCategory", last);
			break;
		case "INSERT INTO `oc_category_description`":
			id = setInfoToMapIntegerMapIntegerMapStringString(originalLine, "INSERT INTO `oc_category_description`",
					ocCategoryDescriptionMap, "category_id", "language_id");
			setToLast(id, "ocCategoryDescription", last);
			break;
		case "INSERT INTO `oc_category_to_store`":
			id = setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_category_to_store`", ocCategoryToStoreMap,
					"category_id", "store_id");
			setToLast(id, "ocCategoryToStore", last);
			break;
		case "INSERT INTO `oc_manufacturer`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_manufacturer`", ocManufacturerMap,
					"manufacturer_id");
			setToLast(id, "ocManufacturer", last);
			break;
		case "INSERT INTO `oc_manufacturer_to_store`":
			id = setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_manufacturer_to_store`",
					ocManufacturerToStoreMap, "manufacturer_id", "store_id");
			setToLast(id, "ocManufacturerToStore", last);
			break;
		case "INSERT INTO `oc_product`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product`", ocProductMap,
					"product_id");
			setToLast(id, "ocProduct", last);
			break;
		case "INSERT INTO `oc_product_attribute`":
			id = setInfoToMapIntegerMapIntegerMapIntegerMapStringString(originalLine,
					"INSERT INTO `oc_product_attribute`", ocProductAttributeMap, "product_id", "language_id");
			setToLast(id, "ocProductAttribute", last);
			break;
		case "INSERT INTO `oc_product_image`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_image`", ocProductImageMap,
					"product_id");

			idString = ocProductImageMap.get(id).get("product_image_id");
			if (StringUtils.isNumeric(idString)) {
				id = Integer.valueOf(idString);
			}

			setToLast(id, "ocProductImage", last);
			break;
		case "INSERT INTO `oc_product_reward`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_reward`", ocProductRewardMap,
					"product_id");

			idString = ocProductRewardMap.get(id).get("product_reward_id");
			if (StringUtils.isNumeric(idString)) {
				id = Integer.valueOf(idString);
			}

			setToLast(id, "ocProductReward", last);
			break;
		case "INSERT INTO `oc_product_to_category`":
			id = setInfoToMapIntegerListInteger(originalLine, "INSERT INTO `oc_product_to_category`",
					ocProductToCategoryMap, "product_id", "category_id");
			setToLast(id, "ocProductToCategory", last);
			break;
		case "INSERT INTO `oc_product_to_store`":
			id = setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_product_to_store`", ocProductToStoreMap,
					"product_id", "store_id");
			setToLast(id, "ocProductToStore", last);
			break;
		case "INSERT INTO `oc_product_description`":
			id = setInfoToMapIntegerMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_description`",
					ocProductDescriptionMap, "product_id", "language_id");
			setToLast(id, "ocProductDescription", last);
			break;
		case "INSERT INTO `oc_url_alias`":
			id = setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_url_alias`", ocUrlAliasMap,
					"url_alias_id");
			setToLast(id, "ocUrlAlias", last);
			break;

		default:
			break;
		}
	}

	private void setToLast(Integer id, String nameKey, Map<String, Integer> last) {
		if (id != null) {
			Integer lastId = last.get(nameKey);

			if (lastId != null) {
				if (lastId < id) {
					last.put(nameKey, id);
				}
			} else {
				last.put(nameKey, id);
			}
		}
	}

	private boolean fillCategoryNameNum(Map<String, Integer> categoryNameNum,
			Map<Integer, Map<String, String>> ocCategoryMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap) {
		if (!ocCategoryMap.isEmpty() && !ocCategoryDescriptionMap.isEmpty()) {
			for (Entry<Integer, Map<Integer, Map<String, String>>> entry : ocCategoryDescriptionMap.entrySet()) {
				Integer key = entry.getKey();

				String categoryName = getCategoryName(ocCategoryDescriptionMap, key);

				String categoryFullName = "";

				List<Integer> parentList = getParentList(ocCategoryMap, ocCategoryDescriptionMap, key, null);

				if (parentList.size() != 0) {
					for (int i = parentList.size() - 1; i >= 0; i--) {

						categoryFullName += getCategoryName(ocCategoryDescriptionMap, parentList.get(i)) + " -> ";
					}
				}
				categoryNameNum.put(categoryFullName + categoryName, key);
			}
			return true;
		}
		return false;
	}

	private boolean fillProductNameNum(Map<String, Map<String, Integer>> productNameNum,
			Map<Integer, Map<String, String>> ocCategoryMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap,
			Map<Integer, Map<String, String>> ocProductMap,
			Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> ocProductAttributeMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap,
			Map<Integer, Map<String, String>> ocManufacturerMap, Map<Integer, List<Integer>> ocProductToCategoryMap) {

		for (Entry<Integer, Map<String, String>> entry : ocProductMap.entrySet()) {
			Map<String, Integer> productInfo = new HashMap<>();

			Integer productId = Integer.valueOf(entry.getValue().get("product_id"));

			Integer categoryId = 0;
			Integer manufacturerId = Integer.valueOf(entry.getValue().get("manufacturer_id"));
			List<Integer> categories = ocProductToCategoryMap.get(productId);
			if (categories.size() > 0) {
				Collections.sort(categories, Comparators.comparable());

				categoryId = categories.get(categories.size() - 1);
			}

			Map<Integer, Map<String, String>> categoryDescription = ocCategoryDescriptionMap.get(categoryId);

			String collection = "";

			if (categoryDescription != null) {
				categoryDescription.get(1).get("name");
				if (collection.isEmpty()) {
					collection = ocCategoryDescriptionMap.get(categoryId).get(2).get("name");
				}
			}

			String brand = ocManufacturerMap.get(manufacturerId).get("name");
			String name = ocProductDescriptionMap.get(productId).get(1).get("name");
			if (name.isEmpty()) {
				name = ocProductDescriptionMap.get(productId).get(2).get("name");
			}
			String priceString = entry.getValue().get("price");
			Double price = 0.0;
			if (StringUtils.isNumeric(priceString)) {
				price = Double.valueOf(priceString);
			}

			productInfo.put("productId", productId);
			productInfo.put("categoryId", categoryId);
			productInfo.put("manufacturerId", manufacturerId);

			StringBuffer sb = new StringBuffer();
			sb.append(brand).append(" -> ");
			sb.append(collection).append(" -> ");
			sb.append(name);

			String nameElement = sb.toString();

			productNameNum.put(nameElement, productInfo);
		}

		return false;
	}

	@Override
	public Map<String, Map> getAllProductsFromTxtFile(String pathToFile) {
		Map<String, Map> result = new HashMap<>();

		managerFiles.setPath(pathToFile);
		List<String> allOriginalLines = managerFiles.readFromeFile();

		Pattern pBrand = Pattern.compile("\\$\\{brand\\}.*\\$\\{\\\\brand\\}");
		Pattern pCollection = Pattern.compile("\\$\\{collection\\}.*\\$\\{\\\\collection\\}");
		Pattern pName = Pattern.compile("\\$\\{name\\}.*\\$\\{\\\\name\\}");
		Pattern pType = Pattern.compile("\\$\\{type\\}.*\\$\\{\\\\type\\}");
		Pattern pPolotno = Pattern.compile("\\$\\{polotno\\}.*\\$\\{\\\\polotno\\}");
		Pattern pKorobka = Pattern.compile("\\$\\{korobka\\}.*\\$\\{\\\\korobka\\}");
		Pattern pNalichnik = Pattern.compile("\\$\\{nalichnik\\}.*\\$\\{\\\\nalichnik\\}");
		Pattern pTotalPrice = Pattern.compile("\\$\\{total price\\}.*\\$\\{\\\\total price\\}");

		for (String line : allOriginalLines) {
			Map<String, String> element = new HashMap<>();

			Matcher mBrand = pBrand.matcher(line);
			Matcher mCollection = pCollection.matcher(line);
			Matcher mName = pName.matcher(line);
			Matcher mType = pType.matcher(line);
			Matcher mPolotno = pPolotno.matcher(line);
			Matcher mKorobka = pKorobka.matcher(line);
			Matcher mNalichnik = pNalichnik.matcher(line);
			Matcher mTotalPrice = pTotalPrice.matcher(line);

			if (mBrand.find()) {
				element.put("brand", mBrand.group().replace("${brand}", "").replace("${\\brand}", ""));
			}
			if (mCollection.find()) {
				element.put("collection",
						mCollection.group().replace("${collection}", "").replace("${\\collection}", ""));
			}
			if (mName.find()) {
				element.put("name", mName.group().replace("${name}", "").replace("${\\name}", ""));
			}
			if (mType.find()) {
				element.put("type", mType.group().replace("${type}", "").replace("${\\type}", ""));
			}
			if (mPolotno.find()) {
				element.put("polotno", mPolotno.group().replace("${polotno}", "").replace("${\\polotno}", ""));
			}
			if (mKorobka.find()) {
				element.put("korobka", mKorobka.group().replace("${korobka}", "").replace("${\\korobka}", ""));
			}
			if (mNalichnik.find()) {
				element.put("nalichnik", mNalichnik.group().replace("${nalichnik}", "").replace("${\\nalichnik}", ""));
			}
			if (mTotalPrice.find()) {
				element.put("totalPrice",
						mTotalPrice.group().replace("${total price}", "").replace("${\\total price}", ""));
			}

			StringBuffer sb = new StringBuffer();
			sb.append(element.get("brand")).append(" -> ");
			sb.append(element.get("collection")).append(" -> ");
			sb.append(element.get("name"));

			String nameElement = sb.toString();

			result.put(nameElement, element);
		}

		return result;
	}

	private String getContainsINSERT(String originalLine) {
		if (originalLine.contains("INSERT INTO `oc_category`")) {
			return "INSERT INTO `oc_category`";
		}
		if (originalLine.contains("INSERT INTO `oc_category_description`")) {
			return "INSERT INTO `oc_category_description`";
		}

		if (originalLine.contains("INSERT INTO `oc_category_to_store`")) {
			return "INSERT INTO `oc_category_to_store`";
		}

		if (originalLine.contains("INSERT INTO `oc_manufacturer`")) {
			return "INSERT INTO `oc_manufacturer`";
		}

		if (originalLine.contains("INSERT INTO `oc_manufacturer_to_store`")) {
			return "INSERT INTO `oc_manufacturer_to_store`";
		}

		if (originalLine.contains("INSERT INTO `oc_product`")) {
			return "INSERT INTO `oc_product`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_attribute`")) {
			return "INSERT INTO `oc_product_attribute`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_description`")) {
			return "INSERT INTO `oc_product_description`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_image`")) {
			return "INSERT INTO `oc_product_image`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_reward`")) {
			return "INSERT INTO `oc_product_reward`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_to_category`")) {
			return "INSERT INTO `oc_product_to_category`";
		}

		if (originalLine.contains("INSERT INTO `oc_product_to_store`")) {
			return "INSERT INTO `oc_product_to_store`";
		}

		if (originalLine.contains("INSERT INTO `oc_url_alias`")) {
			return "INSERT INTO `oc_url_alias`";
		}

		return "";
	}

	private Integer setInfoToMapIntegerMapStringString(String originalLine, String search,
			Map<Integer, Map<String, String>> result, String nameKeyMain) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (info.containsKey(nameKeyMain)) {
				Integer id = Integer.valueOf(info.get(nameKeyMain));
				result.put(id, info);
				return id;
			}
		}
		return null;
	}

	private Integer setInfoToMapIntegerMapIntegerMapStringString(String originalLine, String search,
			Map<Integer, Map<Integer, Map<String, String>>> result, String nameKeyMain, String nameKeySecondary) {

		Map<String, String> info = getInfoFromLine(originalLine, search);

		if (info != null) {
			if (info.containsKey(nameKeySecondary) && info.containsKey(nameKeyMain)) {
				Integer categoryId = Integer.valueOf(info.get(nameKeyMain));

				if (!result.containsKey(categoryId)) {
					result.put(categoryId, new HashMap<>());
				}

				if (StringUtils.isNumeric(info.get(nameKeySecondary))) {
					result.get(categoryId).put(Integer.valueOf(info.get(nameKeySecondary)), info);

					return categoryId;
				}

			}
		}
		return null;
	}

	private Integer setInfoToMapIntegerMapIntegerMapIntegerMapStringString(String originalLine, String search,
			Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> result, String nameKeyMain,
			String nameKeySecondary) {

		Map<String, String> info = getInfoFromLine(originalLine, search);

		if (info != null) {
			Integer attribute_id = 0;
			Integer product_id = 0;
			Integer language_id = 0;

			if (StringUtils.isNumeric(info.get("attribute_id"))) {
				attribute_id = Integer.valueOf(info.get("attribute_id"));
			}
			if (StringUtils.isNumeric(info.get("product_id"))) {
				product_id = Integer.valueOf(info.get("product_id"));
			}
			if (StringUtils.isNumeric(info.get("language_id"))) {
				language_id = Integer.valueOf(info.get("language_id"));
			}

			if (product_id > 0 && attribute_id > 0 && language_id > 0) {
				Map<Integer, Map<Integer, Map<String, String>>> map1 = result.get(product_id);
				if (map1 == null) {
					map1 = new HashMap<>();
				}
				Map<Integer, Map<String, String>> map2 = map1.get(attribute_id);
				if (map2 == null) {
					map2 = new HashMap<>();
					map1.put(attribute_id, map2);
				}

				map2.put(language_id, info);

				result.put(product_id, map1);

				return product_id;
			}

		}
		return null;
	}

	private Integer setInfoToMapIntegerListInteger(String originalLine, String search,
			Map<Integer, List<Integer>> result, String nameKeyMain, String nameKeySecondary) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (StringUtils.isNumeric(info.get(nameKeyMain)) && StringUtils.isNumeric(info.get(nameKeySecondary))) {
				Integer keyMain = Integer.valueOf(info.get(nameKeyMain));
				Integer keySecondary = Integer.valueOf(info.get(nameKeySecondary));

				if (result.containsKey(keyMain)) {
					result.get(keyMain).add(keySecondary);
				} else {
					result.put(keyMain, new ArrayList<Integer>());
					result.get(keyMain).add(keySecondary);
				}
				return keyMain;
			}
		}

		return null;
	}

	private Integer setInfoToMapIntegerInteger(String originalLine, String search, Map<Integer, Integer> result,
			String nameKeyMain, String nameKeySecondary) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (StringUtils.isNumeric(info.get(nameKeyMain)) && StringUtils.isNumeric(info.get(nameKeySecondary))) {
				Integer keyMain = Integer.valueOf(info.get(nameKeyMain));
				Integer keySecondary = Integer.valueOf(info.get(nameKeySecondary));

				result.put(keyMain, keySecondary);

				return keyMain;
			}
		}
		return null;
	}

	private String getCategoryName(Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap,
			Integer key) {

		String categoryName = ocCategoryDescriptionMap.get(key).get(1).get("name");
		if (categoryName.isEmpty()) {
			categoryName = ocCategoryDescriptionMap.get(key).get(2).get("name");
		}
		return categoryName;
	}

	private List<Integer> getParentList(Map<Integer, Map<String, String>> ocCategoryMap,
			Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap, Integer key,
			List<Integer> parentList) {

		if (parentList == null) {
			parentList = new ArrayList<>();
		}

		Integer parentId = Integer.valueOf(ocCategoryMap.get(key).get("parent_id"));
		if (parentId == 0 || !ocCategoryDescriptionMap.containsKey(parentId)) {
			return parentList;

		}
		parentList.add(parentId);

		return getParentList(ocCategoryMap, ocCategoryDescriptionMap, parentId, parentList);
	}

	private Map<String, String> getInfoFromLine(String originalLine, String search) {
		Map<String, String> result = new HashMap<>();

		String[] splitVALUES = originalLine.split("\\) VALUES \\(");
		if (splitVALUES.length >= 2) {
			splitVALUES[0] = splitVALUES[0].replace(search + " (", "");
			splitVALUES[1] = splitVALUES[1].replace(");", "");
			///
			String[] splitLEFT = splitVALUES[0].split("`, `");
			String[] splitRIGHT = splitVALUES[1].split("', '");

			if (splitLEFT.length == splitRIGHT.length) {
				for (int i = 0; i < splitLEFT.length; i++) {
					result.put(splitLEFT[i].replaceAll("`", ""), splitRIGHT[i].replaceAll("'", ""));
				}
				return result;
			}
		}

		return null;
	}

	@Override
	public boolean updateInformation(Map<String, String> queries, Map<String, Map> allProductsFromeSQLFile) {

		if (updateInformation(queries, allProductsFromeSQLFile, "ocProduct", "INSERT INTO `oc_product`", "ocProductMap",
				"product_id", null) &&

				updateInformation(queries, allProductsFromeSQLFile, "ocProductImage", "INSERT INTO `oc_product_image`",
						"ocProductImageMap", "product_id", null)
				&&

				updateInformation(queries, allProductsFromeSQLFile, "ocProductToStore",
						"INSERT INTO `oc_product_to_store`", "ocProductToStoreMap", "product_id", "store_id")
				&&

				updateInformationDescription(queries, allProductsFromeSQLFile, "ocProductDescription",
						"INSERT INTO `oc_product_description`", "ocProductDescriptionMap", "product_id")
				&&

				updateInformationCategory(queries, allProductsFromeSQLFile, "ocProductToCategory",
						"INSERT INTO `oc_product_to_category`", "ocProductToCategoryMap", "product_id", "category_id")
				&&

				updateInformation(queries, allProductsFromeSQLFile, "ocProductReward",
						"INSERT INTO `oc_product_reward`", "ocProductRewardMap", "product_id", null)
				&&

				updateInformation(queries, allProductsFromeSQLFile, "ocUrlAlias", "INSERT INTO `oc_url_alias`",
						"ocUrlAliasMap", "url_alias_id", null)) {
			return true;
		}

		return false;

	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	private boolean updateInformationCategory(Map<String, String> queries, Map allProductsFromeSQLFile,
			String nameQuery, String search, String nameSQL, String mainKey, String secondaryKey) {
		String query = queries.get(nameQuery);
		if (query != null) {
			Map<String, String> infoFromLine = getInfoFromLine(query, search);
			Integer product_id = Integer.valueOf(infoFromLine.get(mainKey));

			Map<Integer, List<Integer>> ocProductToCategoryMap = (Map<Integer, List<Integer>>) allProductsFromeSQLFile
					.get(nameSQL);
			List<Integer> list = ocProductToCategoryMap.get(product_id);
			if (list == null) {
				list = new ArrayList<>();
			}

			String string = infoFromLine.get(secondaryKey);
			String[] stringMas = string.split(", ");

			for (String str : stringMas) {
				list.add(Integer.valueOf(str));
			}

			ocProductToCategoryMap.put(product_id, list);
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean updateInformation(Map<String, String> queries, Map<String, Map> allProductsFromeSQLFile,
			String nameQuery, String search, String nameSQL, String mainKey, String secondaryKey) {

		String query = queries.get(nameQuery);
		if (query != null) {
			Map<String, String> infoFromLine = getInfoFromLine(query, search);
			if (secondaryKey == null) {
				allProductsFromeSQLFile.get(nameSQL).put(Integer.valueOf(infoFromLine.get(mainKey)), infoFromLine);
			} else {

				String string = infoFromLine.get(secondaryKey);
				String[] stringMas = string.split(", ");
				if (stringMas != null) {
					for (String str : stringMas) {
						allProductsFromeSQLFile.get(nameSQL).put(Integer.valueOf(infoFromLine.get(mainKey)),
								Integer.valueOf(str));
					}
				} else {

					allProductsFromeSQLFile.get(nameSQL).put(Integer.valueOf(infoFromLine.get(mainKey)),
							Integer.valueOf(string));
				}
			}
			return true;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	public boolean updateInformationDescription(Map<String, String> queries,
			@SuppressWarnings("rawtypes") Map<String, Map> allProductsFromeSQLFile, String nameQuery, String search,
			String nameSQL, String mainKey) {

		String query = queries.get(nameQuery);
		if (query != null) {

			String[] queriesMas = query.split("\n");

			Map<Integer, Map<String, String>> enter = new HashMap<>();
			Integer idNewProduct = 0;
			for (String line : queriesMas) {
				Map<String, String> infoFromLine = getInfoFromLine(line, search);
				enter.put(Integer.valueOf(infoFromLine.get("language_id")), infoFromLine);
				if (idNewProduct == 0) {
					idNewProduct = Integer.valueOf(infoFromLine.get("product_id"));
				}
			}
			allProductsFromeSQLFile.get(nameSQL).put(idNewProduct, enter);
			return true;
		}
		return false;
	}

	@Override
	public boolean saveModifySQLFile(String pathToSQLFile, Map<String, Map> allProductsFromeSQLFile) {

		managerFiles.setPath(pathToSQLFile);
		List<String> originalLines = managerFiles.readFromeFile();

		List<String> modifyLines = getModifyLines(originalLines, allProductsFromeSQLFile);

		if (managerFiles.saveLinesToFile(modifyLines, null)) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private List<String> getModifyLines(List<String> originalLines,
			@SuppressWarnings("rawtypes") Map<String, Map> allProductsFromeSQLFile) {
		List<String> result = new ArrayList<>();

		boolean oc_category = false;
		boolean oc_category_description = false;
		boolean oc_category_to_store = false;
		boolean oc_manufacturer = false;
		boolean oc_manufacturer_to_store = false;
		boolean oc_product = false;
		boolean oc_product_attribute = false;
		boolean oc_product_image = false;
		boolean oc_product_reward = false;
		boolean oc_product_to_category = false;
		boolean oc_product_to_store = false;
		boolean oc_product_description = false;
		boolean oc_url_alias = false;

		for (String line : originalLines) {

			if (line.contains("INSERT INTO `oc_category`")) {
				if (oc_category == false) {
					oc_category = true;
					Map<Integer, Map<String, String>> ocCategoryMap = allProductsFromeSQLFile.get("ocCategoryMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocCategoryMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append(
								"INSERT INTO `oc_category` (`category_id`, `image`, `parent_id`, `top`, `column`, `sort_order`, `status`, `date_added`, `date_modified`) VALUES ('");
						sb.append(ocCategoryMap.get(num).get("category_id")).append("', '");
						sb.append(ocCategoryMap.get(num).get("image")).append("', '");
						sb.append(ocCategoryMap.get(num).get("parent_id")).append("', '");
						sb.append(ocCategoryMap.get(num).get("top")).append("', '");
						sb.append(ocCategoryMap.get(num).get("column")).append("', '");
						sb.append(ocCategoryMap.get(num).get("sort_order")).append("', '");
						sb.append(ocCategoryMap.get(num).get("status")).append("', '");
						sb.append(ocCategoryMap.get(num).get("date_added")).append("', '");
						sb.append(ocCategoryMap.get(num).get("date_modified")).append("');");

						result.add(sb.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_category_description`")) {
				if (oc_category_description == false) {
					oc_category_description = true;
					Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap = allProductsFromeSQLFile
							.get("ocCategoryDescriptionMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<Integer, Map<String, String>>> entry : ocCategoryDescriptionMap
							.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb1 = new StringBuffer();
						sb1.append(
								"INSERT INTO `oc_category_description` (`category_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("category_id")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("language_id")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("name")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("description")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("meta_description")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("meta_keyword")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("u_title")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("u_h1")).append("', '");
						sb1.append(ocCategoryDescriptionMap.get(num).get(1).get("u_h2")).append("');");

						StringBuffer sb2 = new StringBuffer();
						sb2.append(
								"INSERT INTO `oc_category_description` (`category_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("category_id")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("language_id")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("name")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("description")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("meta_description")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("meta_keyword")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("u_title")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("u_h1")).append("', '");
						sb2.append(ocCategoryDescriptionMap.get(num).get(2).get("u_h2")).append("');");

						result.add(sb1.toString());
						result.add(sb2.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_category_to_store`")) {
				if (oc_category_to_store == false) {
					oc_category_to_store = true;
					Map<Integer, Integer> ocCategoryToStoreMap = allProductsFromeSQLFile.get("ocCategoryToStoreMap");

					for (Entry<Integer, Integer> num : ocCategoryToStoreMap.entrySet()) {

						StringBuffer sb = new StringBuffer();
						sb.append("INSERT INTO `oc_category_to_store` (`category_id`, `store_id`) VALUES ('");
						sb.append(num.getKey()).append("', '");
						sb.append(num.getValue()).append("');");

						result.add(sb.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_manufacturer`")) {
				if (oc_manufacturer == false) {
					oc_manufacturer = true;
					Map<Integer, Map<String, String>> ocManufacturerMap = allProductsFromeSQLFile
							.get("ocManufacturerMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocManufacturerMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append(
								"INSERT INTO `oc_manufacturer` (`manufacturer_id`, `name`, `image`, `sort_order`) VALUES ('");
						sb.append(ocManufacturerMap.get(num).get("manufacturer_id")).append("', '");
						sb.append(ocManufacturerMap.get(num).get("name")).append("', '");
						sb.append(ocManufacturerMap.get(num).get("image")).append("', '");
						sb.append(ocManufacturerMap.get(num).get("sort_order")).append("');");

						result.add(sb.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_manufacturer_to_store`")) {
				if (oc_manufacturer_to_store == false) {
					oc_manufacturer_to_store = true;
					Map<Integer, Integer> ocManufacturerToStoreMap = allProductsFromeSQLFile
							.get("ocManufacturerToStoreMap");

					for (Entry<Integer, Integer> num : ocManufacturerToStoreMap.entrySet()) {

						StringBuffer sb = new StringBuffer();
						sb.append("INSERT INTO `oc_category_to_store` (`manufacturer_id`, `store_id`) VALUES ('");
						sb.append(num.getKey()).append("', '");
						sb.append(num.getValue()).append("');");

						result.add(sb.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product`")) {
				if (oc_product == false) {
					oc_product = true;
					Map<Integer, Map<String, String>> ocProductMap = allProductsFromeSQLFile.get("ocProductMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocProductMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append(
								"INSERT INTO `oc_product` (`product_id`, `model`, `sku`, `upc`, `ean`, `jan`, `isbn`, `mpn`, `location`, `quantity`, `stock_status_id`, `image`, "
										+ "`manufacturer_id`, `shipping`, `price`, `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, `length`, `width`, `height`, "
										+ "`length_class_id`, `subtract`, `minimum`, `sort_order`, `status`, `date_added`, `date_modified`, `viewed`) VALUES ('");
						sb.append(ocProductMap.get(num).get("product_id")).append("', '");
						sb.append(ocProductMap.get(num).get("model")).append("', '");
						sb.append(ocProductMap.get(num).get("sku")).append("', '");
						sb.append(ocProductMap.get(num).get("upc")).append("', '");
						sb.append(ocProductMap.get(num).get("ean")).append("', '");
						sb.append(ocProductMap.get(num).get("jan")).append("', '");
						sb.append(ocProductMap.get(num).get("isbn")).append("', '");
						sb.append(ocProductMap.get(num).get("mpn")).append("', '");
						sb.append(ocProductMap.get(num).get("location")).append("', '");
						sb.append(ocProductMap.get(num).get("quantity")).append("', '");
						sb.append(ocProductMap.get(num).get("stock_status_id")).append("', '");
						sb.append(ocProductMap.get(num).get("image")).append("', '");
						sb.append(ocProductMap.get(num).get("manufacturer_id")).append("', '");
						sb.append(ocProductMap.get(num).get("shipping")).append("', '");
						sb.append(ocProductMap.get(num).get("price")).append("', '");
						sb.append(ocProductMap.get(num).get("points")).append("', '");
						sb.append(ocProductMap.get(num).get("tax_class_id")).append("', '");
						sb.append(ocProductMap.get(num).get("date_available")).append("', '");
						sb.append(ocProductMap.get(num).get("weight")).append("', '");
						sb.append(ocProductMap.get(num).get("weight_class_id")).append("', '");
						sb.append(ocProductMap.get(num).get("length")).append("', '");
						sb.append(ocProductMap.get(num).get("width")).append("', '");
						sb.append(ocProductMap.get(num).get("height")).append("', '");
						sb.append(ocProductMap.get(num).get("length_class_id")).append("', '");
						sb.append(ocProductMap.get(num).get("subtract")).append("', '");
						sb.append(ocProductMap.get(num).get("minimum")).append("', '");
						sb.append(ocProductMap.get(num).get("sort_order")).append("', '");
						sb.append(ocProductMap.get(num).get("status")).append("', '");
						sb.append(ocProductMap.get(num).get("date_added")).append("', '");
						sb.append(ocProductMap.get(num).get("date_modified")).append("', '");
						sb.append(ocProductMap.get(num).get("viewed")).append("');");

						result.add(sb.toString());
						continue;
					}
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_attribute`")) {
				if (oc_product_attribute == false) {
					oc_product_attribute = true;
					Map<Integer, Map<Integer, Map<Integer, Map<String, String>>>> ocProductAttributeMap = allProductsFromeSQLFile
							.get("ocProductAttributeMap");

					for (Entry<Integer, Map<Integer, Map<Integer, Map<String, String>>>> entry1 : ocProductAttributeMap
							.entrySet()) {
						Integer product_id = entry1.getKey();
						for (Entry<Integer, Map<Integer, Map<String, String>>> entry2 : entry1.getValue().entrySet()) {
							Integer attribute_id = entry2.getKey();
							for (Entry<Integer, Map<String, String>> entry3 : entry2.getValue().entrySet()) {
								Integer language_id = entry3.getKey();
								String text = entry3.getValue().get("text");
								StringBuffer sb1 = new StringBuffer();
								sb1.append(
										"INSERT INTO `oc_product_attribute` (`product_id`, `attribute_id`, `language_id`, `text`) VALUES ('");
								sb1.append(product_id).append("', '");
								sb1.append(attribute_id).append("', '");
								sb1.append(language_id).append("', '");
								sb1.append(text).append("');");

								result.add(sb1.toString());
							}

						}

					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_image`")) {
				if (oc_product_image == false) {
					oc_product_image = true;
					Map<Integer, Map<String, String>> ocProductImageMap = allProductsFromeSQLFile
							.get("ocProductImageMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocProductImageMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append(
								"INSERT INTO `oc_product_image` (`product_image_id`, `product_id`, `image`, `sort_order`) VALUES ('");
						sb.append(ocProductImageMap.get(num).get("product_image_id")).append("', '");
						sb.append(ocProductImageMap.get(num).get("product_id")).append("', '");
						sb.append(ocProductImageMap.get(num).get("image")).append("', '");
						sb.append(ocProductImageMap.get(num).get("sort_order")).append("');");

						result.add(sb.toString());
					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_reward`")) {
				if (oc_product_reward == false) {
					oc_product_reward = true;
					Map<Integer, Map<String, String>> ocProductRewardMap = allProductsFromeSQLFile
							.get("ocProductRewardMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocProductRewardMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append(
								"INSERT INTO `oc_product_reward` (`product_reward_id`, `product_id`, `customer_group_id`, `points`) VALUES ('");
						sb.append(ocProductRewardMap.get(num).get("product_image_id")).append("', '");
						sb.append(ocProductRewardMap.get(num).get("product_id")).append("', '");
						sb.append(ocProductRewardMap.get(num).get("customer_group_id")).append("', '");
						sb.append(ocProductRewardMap.get(num).get("points")).append("');");

						result.add(sb.toString());
					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_to_category`")) {
				if (oc_product_to_category == false) {
					oc_product_to_category = true;
					Map<Integer, List<Integer>> ocProductToCategoryMap = allProductsFromeSQLFile
							.get("ocProductToCategoryMap");

					for (Entry<Integer, List<Integer>> num : ocProductToCategoryMap.entrySet()) {

						List<Integer> list = num.getValue();

						for (Integer category : list) {

							StringBuffer sb = new StringBuffer();
							sb.append("INSERT INTO `oc_product_to_category` (`product_id`, `category_id`) VALUES ('");
							sb.append(num.getKey()).append("', '");
							sb.append(category).append("');");

							result.add(sb.toString());
						}
					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_to_store`")) {
				if (oc_product_to_store == false) {
					oc_product_to_store = true;
					Map<Integer, Integer> ocProductToStoreMap = allProductsFromeSQLFile.get("ocProductToStoreMap");

					for (Entry<Integer, Integer> num : ocProductToStoreMap.entrySet()) {

						StringBuffer sb = new StringBuffer();
						sb.append("INSERT INTO `oc_product_to_store` (`product_id`, `store_id`) VALUES ('");
						sb.append(num.getKey()).append("', '");
						sb.append(num.getValue()).append("');");

						result.add(sb.toString());
					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_product_description`")) {
				if (oc_product_description == false) {
					oc_product_description = true;
					Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap = allProductsFromeSQLFile
							.get("ocProductDescriptionMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<Integer, Map<String, String>>> entry : ocProductDescriptionMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb1 = new StringBuffer();
						sb1.append(
								"INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("product_id")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("language_id")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("name")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("description")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("meta_description")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("meta_keyword")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("tag")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("u_title")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("u_h1")).append("', '");
						sb1.append(ocProductDescriptionMap.get(num).get(1).get("u_h2")).append("');");

						StringBuffer sb2 = new StringBuffer();
						sb2.append(
								"INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("product_id")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("language_id")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("name")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("description")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("meta_description")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("meta_keyword")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("tag")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("u_title")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("u_h1")).append("', '");
						sb2.append(ocProductDescriptionMap.get(num).get(2).get("u_h2")).append("');");

						result.add(sb1.toString());
						result.add(sb2.toString());
					}
					continue;
				} else {
					continue;
				}
			} else if (line.contains("INSERT INTO `oc_url_alias`")) {
				if (oc_url_alias == false) {
					oc_url_alias = true;
					Map<Integer, Map<String, String>> ocUrlAliasMap = allProductsFromeSQLFile.get("ocUrlAliasMap");
					List<Integer> numbers = new ArrayList<>();
					for (Entry<Integer, Map<String, String>> entry : ocUrlAliasMap.entrySet()) {
						numbers.add(entry.getKey());
					}
					Collections.sort(numbers);
					for (Integer num : numbers) {

						StringBuffer sb = new StringBuffer();
						sb.append("INSERT INTO `oc_url_alias` (`url_alias_id`, `query`, `keyword`) VALUES ('");
						sb.append(ocUrlAliasMap.get(num).get("url_alias_id")).append("', '");
						sb.append(ocUrlAliasMap.get(num).get("query")).append("', '");
						sb.append(ocUrlAliasMap.get(num).get("keyword")).append("');");

						result.add(sb.toString());
					}
					continue;
				} else {
					continue;
				}
			} else {
				result.add(line);
			}

		}

		return result;
	}

}
