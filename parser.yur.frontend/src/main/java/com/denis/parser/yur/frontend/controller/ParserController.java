package com.denis.parser.yur.frontend.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.denis.parser.yur.backend.service.ParserFile;
import com.denis.parser.yur.backend.service.ParserHtml;

@Controller
public class ParserController {

	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	@Autowired
	private ParserHtml parserHtml;

	@Autowired
	private ParserFile parserFile;

	@RequestMapping(value = "/parser", method = RequestMethod.GET)
	public ModelAndView parserGet() {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Parser");
		mv.addObject("userClickParser", true);

		logger.info("Inside PageController index method - INFO");

		return mv;
	}

	@RequestMapping(value = "/parser", method = RequestMethod.POST)
	public String parserPost(@RequestParam Map<String, String> allRequestParams) {

		for (Entry<String, String> ie : allRequestParams.entrySet()) {

			if (ie.getKey().equals("htmlParserStart") && !ie.getKey().isEmpty()) {
				logger.info("Inside PageController index method - INFO");

				return "redirect:/parser/html";
			}

			if (ie.getKey().equals("fileParserStart") && !ie.getKey().isEmpty()) {
				logger.info("Inside PageController index method - INFO");

				return "redirect:/parser/file";
			}

		}

		logger.info("Inside PageController index method - INFO");

		return "redirect:/parser";
	}

	@RequestMapping(value = "/parser/html", method = RequestMethod.GET)
	public ModelAndView parseHtmlGet() {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Parser Html");
		mv.addObject("userClickParserHtml", true);

		logger.info("Inside PageController index method - INFO");

		return mv;

	}

	@RequestMapping(value = "/parser/html", method = RequestMethod.POST)
	public String parseHtmlPost(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {

		for (Entry<String, String> iterable_element : allRequestParams.entrySet()) {

			request.getSession().setAttribute("parametersForParserHTML",
					checkParametersForParserHTML(iterable_element));
		}

		return "redirect:/parser/html";

	}

	@RequestMapping(value = "/parser/file", method = RequestMethod.GET)
	public ModelAndView parseFileGet() {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Parser File");
		mv.addObject("userClickParserFile", true);

		logger.info("Inside PageController index method - INFO");

		return mv;

	}

	@RequestMapping(value = "/parser/file", method = RequestMethod.POST)
	public String parseFilePost(@RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> uploadFilesAttribute = (Map<String, Map<String, String>>) request.getSession()
				.getAttribute("uploadFiles");
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> categoriesAttribute = (Map<String, Map<String, String>>) request.getSession()
				.getAttribute("categories");

		if (uploadFilesAttribute == null) {
			return "forward:/upload/file";
		}

		if (uploadFilesAttribute.containsKey("sql") && uploadFilesAttribute.containsKey("txt")) {

			if (categoriesAttribute == null) {
				return "forward:/categories";

			}

			@SuppressWarnings({ "unchecked" })
			List<String> chooseCategories = (List<String>) request.getSession().getAttribute("chooseCategories");
			if (chooseCategories != null) {
				parserFile.setPathToSqlFile(uploadFilesAttribute.get("sql").get("path"));
				parserFile.setPathToTxtFile(uploadFilesAttribute.get("txt").get("path"));
				parserFile.setCategories(chooseCategories);
				parserFile.start();

				request.getSession().removeAttribute("uploadFiles");

				request.getSession().removeAttribute("categories");

				return "redirect:/home";
			}

		}

		return "redirect:/parser/file";

	}

	private Map<String, List<String>> checkParametersForParserHTML(Entry<String, String> iterable_element) {

		Map<String, List<String>> parameters = new HashMap<>();

		if (iterable_element.getKey().equals("html-test") && iterable_element.getValue().equals("html-test")) {

			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-z/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-yurkas-mezhkomnatnye-dveri")
				&& iterable_element.getValue().equals("html-yurkas-mezhkomnatnye-dveri")) {

			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-yurkas-vhodnye-dveri")
				&& iterable_element.getValue().equals("html-yurkas-vhodnye-dveri")) {
			runHtmlParsing("http://yurkas.by/shop/vhodnye-dveri/", iterable_element.getValue(), parameters);

		}

		return parameters;
	}

	private void runHtmlParsing(String URL, String keyForParameters, Map<String, List<String>> parameters) {

		parserHtml.start(URL);

	}

}
