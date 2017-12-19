package com.denis.parser.yur.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.denis.parser.yur.backend.dto.Door;
import com.denis.parser.yur.backend.utils.StringUtils;

public class BackupInformation {
	private String pathToOriginalFile;
	private List<String> allOriginalLines;

	public BackupInformation(String pathToOriginalFile) {
		this.pathToOriginalFile = pathToOriginalFile;
	}

	public void getAllProductsFromeOriginalFile() {

		if (allOriginalLines == null) {
			readFromeFile();
		}

		Map<String, Door> allDoors = new HashMap<>();

		Map<Integer, Map<String, String>> ocCategoryMap = new HashMap<>();
		Map<Integer, Map<Integer, Map<String, String>>> ocCategoryDescriptionMap = new HashMap<>();
		Map<String, Integer> categoryNameNum = new HashMap<>();
		Map<Integer, Integer> ocCategoryToStoreMap = new HashMap<>();
		Map<String, Map<String, String>> ocManufacturerMap = new HashMap<>();
		Map<Integer, Integer> ocManufacturerToStoreMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductMap = new HashMap<>();
		Map<Integer, Map<Integer, Map<String, String>>> ocProductAttributeMap = new HashMap<>();
		Map<Integer, Map<Integer, Map<String, String>>> ocProductDescriptionMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductImageMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocProductRewardMap = new HashMap<>();
		Map<Integer, List<Integer>> ocProductToCategoryMap = new HashMap<>();
		Map<Integer, Integer> ocProductToStoreMap = new HashMap<>();
		Map<Integer, Map<String, String>> ocUrlAliasMap = new HashMap<>();

		for (String originalLine : allOriginalLines) {

			switch (getContainsINSERT(originalLine)) {
			case "INSERT INTO `oc_category`":
				setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_category`", ocCategoryMap,
						"category_id");
				break;
			case "INSERT INTO `oc_category_description`":
				setInfoToMapIntegerMapIntegerMapStringString(originalLine, "INSERT INTO `oc_category_description`",
						ocCategoryDescriptionMap, "category_id", "language_id");
				break;
			case "INSERT INTO `oc_category_to_store`":
				setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_category_to_store`", ocCategoryToStoreMap,
						"category_id", "store_id");
				break;
			case "INSERT INTO `oc_manufacturer`":
				setInfoToMapStringMapStringString(originalLine, "INSERT INTO `oc_manufacturer`", ocManufacturerMap,
						"name");
				break;
			case "INSERT INTO `oc_manufacturer_to_store`":
				setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_manufacturer_to_store`",
						ocManufacturerToStoreMap, "manufacturer_id", "store_id");
				break;
			case "INSERT INTO `oc_product`":
				setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product`", ocProductMap,
						"product_id");
				break;
			case "INSERT INTO `oc_product_attribute`":
				setInfoToMapIntegerMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_attribute`",
						ocProductAttributeMap, "product_id", "language_id");
				break;
			case "INSERT INTO `oc_product_image`":
				setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_image`", ocProductImageMap,
						"product_id");
				break;
			case "INSERT INTO `oc_product_reward`":
				setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_reward`", ocProductRewardMap,
						"product_id");
				break;
			case "INSERT INTO `oc_product_to_category`":
				setInfoToMapIntegerListInteger(originalLine, "INSERT INTO `oc_product_to_category`",
						ocProductToCategoryMap, "product_id", "category_id");
				break;
			case "INSERT INTO `oc_product_to_store`":
				setInfoToMapIntegerInteger(originalLine, "INSERT INTO `oc_product_to_store`", ocProductToStoreMap,
						"product_id", "store_id");
				break;
			case "INSERT INTO `oc_product_description`":
				setInfoToMapIntegerMapIntegerMapStringString(originalLine, "INSERT INTO `oc_product_description`",
						ocProductDescriptionMap, "product_id", "language_id");
				break;
			case "INSERT INTO `oc_url_alias`":
				setInfoToMapIntegerMapStringString(originalLine, "INSERT INTO `oc_url_alias`", ocUrlAliasMap,
						"url_alias_id");
				break;

			default:
				break;
			}
		}

		if (!ocCategoryMap.isEmpty() && !ocCategoryDescriptionMap.isEmpty()) {
			for (Entry<Integer, Map<Integer, Map<String, String>>> entry : ocCategoryDescriptionMap.entrySet()) {
				Integer key = entry.getKey();

				String categoryName = getCategoryName(ocCategoryDescriptionMap, key);

				String categoryFullName = "";

				List<Integer> parentList = getParentList(ocCategoryMap, ocCategoryDescriptionMap, key, null);

				if (parentList.size() != 0) {
					for (int i = parentList.size() - 1; i >= 0; i--) {

						categoryFullName += getCategoryName(ocCategoryDescriptionMap, parentList.get(i)) + " > ";
					}
				}

				categoryNameNum.put(categoryFullName + categoryName, key);

			}

		}

		System.out.println("end");
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

	private boolean setInfoToMapIntegerMapStringString(String originalLine, String search,
			Map<Integer, Map<String, String>> result, String nameKeyMain) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (info.containsKey(nameKeyMain)) {
				result.put(Integer.valueOf(info.get(nameKeyMain)), info);
				return true;
			}
		}
		return false;
	}

	private boolean setInfoToMapIntegerMapIntegerMapStringString(String originalLine, String search,
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

					return true;
				}

			}
		}
		return false;
	}

	private boolean setInfoToMapStringMapStringString(String originalLine, String search,
			Map<String, Map<String, String>> result, String nameKeyMain) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (info.containsKey(nameKeyMain)) {
				result.put(info.get(nameKeyMain), info);
				return true;
			}
		}

		return false;
	}

	private boolean setInfoToMapIntegerListInteger(String originalLine, String search,
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
				return true;
			}
		}

		return false;
	}

	private boolean setInfoToMapIntegerInteger(String originalLine, String search, Map<Integer, Integer> result,
			String nameKeyMain, String nameKeySecondary) {

		Map<String, String> info = getInfoFromLine(originalLine, search);
		if (info != null) {
			if (StringUtils.isNumeric(info.get(nameKeyMain)) && StringUtils.isNumeric(info.get(nameKeySecondary))) {
				Integer keyMain = Integer.valueOf(info.get(nameKeyMain));
				Integer keySecondary = Integer.valueOf(info.get(nameKeySecondary));

				result.put(keyMain, keySecondary);

				return true;
			}
		}
		return false;
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

	private void readFromeFile() {
		try {
			allOriginalLines = Files.readAllLines(Paths.get(pathToOriginalFile));
		} catch (IOException e) {
			new ServiceException("ORIGINAL file not read!", e);
		}
	}
}
