package com.denis.parser.yur.frontend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.denis.parser.yur.backend.service.ParserFile;

@Controller
public class CategoriesController {
	private static final Logger logger = LoggerFactory.getLogger(CategoriesController.class);

	@Autowired
	private ParserFile parserFile;

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public String categoriesGet() {
		return "redirect:/home";
	}

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public String categoriesPost(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {

		String chooseCategories = allRequestParams.get("choose-categories");
		String chooseEmpty = allRequestParams.get("choose-empty");
		String chooseBack = allRequestParams.get("choose-back");

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> uploadFilesAttribute = (Map<String, Map<String, String>>) request.getSession()
				.getAttribute("uploadFiles");

		if (chooseCategories == null && chooseEmpty == null && chooseBack == null) {

			@SuppressWarnings("unchecked")
			Map<String, Map<String, String>> categoriesAttribute = (Map<String, Map<String, String>>) request
					.getSession().getAttribute("categories");

			if (uploadFilesAttribute == null) {
				return "forward:/upload/file";
			}

			if (categoriesAttribute == null) {
				categoriesAttribute = new HashMap<>();
			}

			request.getSession().setAttribute("categories",
					parserFile.getAllCategoriesFromSqlFile(uploadFilesAttribute.get("sql").get("path")));
			return "redirect:/parser/file";
		}

		List<String> categories = new ArrayList<>();

		if (chooseCategories != null) {

			for (Entry<String, String> entry : allRequestParams.entrySet()) {
				if (entry.getKey().contains("check")) {
					categories.add(entry.getValue());
				}
			}
			request.getSession().setAttribute("chooseCategories", categories);

		}

		if (chooseEmpty != null) {
			request.getSession().setAttribute("chooseCategories", new ArrayList<>());
		}

		if (chooseBack != null) {

			request.getSession().removeAttribute("uploadFiles");

			request.getSession().removeAttribute("categories"); /// !!!!!!!!!!!!

			return "redirect:/parser/file";
		}

		return "forward:/parser/file";
	}
}