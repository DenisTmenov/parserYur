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
		logger.debug("Inside PageController index method - DEBUG");

		return mv;
	}

	@RequestMapping(value = "/parser", method = RequestMethod.POST)
	public String parserPost(@RequestParam Map<String, String> allRequestParams) {

		for (Entry<String, String> ie : allRequestParams.entrySet()) {

			if (ie.getKey().equals("htmlParserStart") && !ie.getKey().isEmpty()) {
				logger.info("Inside PageController index method - INFO");
				logger.debug("Inside PageController index method - DEBUG");

				return "redirect:/parser/html";
			}

			if (ie.getKey().equals("fileParserStart") && !ie.getKey().isEmpty()) {
				logger.info("Inside PageController index method - INFO");
				logger.debug("Inside PageController index method - DEBUG");

				return "redirect:/parser/file";
			}

		}

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		return "redirect:/parser";
	}

	@RequestMapping(value = "/parser/html", method = RequestMethod.GET)
	public ModelAndView parseHtmlGet() {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Parser Html");
		mv.addObject("userClickParserHtml", true);

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

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
		logger.debug("Inside PageController index method - DEBUG");

		return mv;

	}

	@RequestMapping(value = "/parser/file", method = RequestMethod.POST)
	public String parseFilePost(@RequestParam Map<String, String> allRequestParams) {

		// for (Entry<String, String> iterable_element : allRequestParams.entrySet()) {
		//
		// checkParametersForParserFile(iterable_element);
		// }

		parserFile.setPathToOriginalFile("D:/TEST/2017-12-15_10-01-53_backup.sql");
		parserFile.start();

		return "redirect:/parser/file";

	}

	private Map<String, List<String>> checkParametersForParserHTML(Entry<String, String> iterable_element) {

		Map<String, List<String>> parameters = new HashMap<>();

		if (iterable_element.getKey().equals("html-yurkas-seriia-sevilja")
				&& iterable_element.getValue().equals("html-yurkas-seriia-sevilja")) {

			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/sevilja/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-profildoors-seriia-portas")
				&& iterable_element.getValue().equals("html-profildoors-seriia-portas")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/porta/",
					iterable_element.getValue(), parameters);

		}
		if (iterable_element.getKey().equals("html-profildoors-seriia-x-klassika")
				&& iterable_element.getValue().equals("html-profildoors-seriia-x-klassika")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-x-klassika/",
					iterable_element.getValue(), parameters);

		}
		if (iterable_element.getKey().equals("html-profildoors-seriia-x-modern")
				&& iterable_element.getValue().equals("html-profildoors-seriia-x-modern")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-x-modern/",
					iterable_element.getValue(), parameters);

		}
		if (iterable_element.getKey().equals("html-profildoors-seriia-u-modern")
				&& iterable_element.getValue().equals("html-profildoors-seriia-u-modern")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-u-modern/",
					iterable_element.getValue(), parameters);

		}
		if (iterable_element.getKey().equals("html-profildoors-seriia-u-klassika")
				&& iterable_element.getValue().equals("html-profildoors-seriia-u-klassika")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-u-klassika/",
					iterable_element.getValue(), parameters);

		}

		if (iterable_element.getKey().equals("html-profildoors-seriia-z")
				&& iterable_element.getValue().equals("html-profildoors-seriia-z")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-z/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-profildoors-seriia-e")
				&& iterable_element.getValue().equals("html-profildoors-seriia-e")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-e/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-yurkas-seriia-kasaporte")
				&& iterable_element.getValue().equals("html-yurkas-seriia-kasaporte")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/kasaporte/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-yurkas-seriia-siti")
				&& iterable_element.getValue().equals("html-yurkas-seriia-siti")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/siti/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-odincovo-seriia-turin")
				&& iterable_element.getValue().equals("html-odincovo-seriia-turin")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/serija-turin/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-ladora-seriia-jego")
				&& iterable_element.getValue().equals("html-ladora-seriia-jego")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/serija-jego/",
					iterable_element.getValue(), parameters);

		}

		if (iterable_element.getKey().equals("html-high-gloss")
				&& iterable_element.getValue().equals("html-high-gloss")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/high-gloss/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-doors-massive")
				&& iterable_element.getValue().equals("html-doors-massive")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D0%B8%D0%B7_%D0%BC%D0%B0%D1%81%D1%81%D0%B8%D0%B2%D0%B0/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-doors-shponirovanie")
				&& iterable_element.getValue().equals("html-doors-shponirovanie")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-shponirovannye/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-doors-emal-okrashenie")
				&& iterable_element.getValue().equals("html-doors-emal-okrashenie")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-emal/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-3d") && iterable_element.getValue().equals("html-doors-3d")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/3d-dveri/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-mdf")
				&& iterable_element.getValue().equals("html-doors-mdf")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-mdf/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-pvh")
				&& iterable_element.getValue().equals("html-doors-pvh")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D0%BF%D0%B2%D1%85/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-garmoshka")
				&& iterable_element.getValue().equals("html-doors-garmoshka")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-garmoshka/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-steklianye")
				&& iterable_element.getValue().equals("html-doors-steklianye")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/steklianye-dveri/",
					iterable_element.getValue(), parameters);
		}

		if (iterable_element.getKey().equals("html-doors-skladnye")
				&& iterable_element.getValue().equals("html-doors-skladnye")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/skladnye-dveri/",
					iterable_element.getValue(), parameters);
		}
		if (iterable_element.getKey().equals("html-doors-nevidimki")
				&& iterable_element.getValue().equals("html-doors-nevidimki")) {
			runHtmlParsing(
					"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-nevidimki/",
					iterable_element.getValue(), parameters);
		}

		return parameters;
	}

	private void runHtmlParsing(String URL, String keyForParameters, Map<String, List<String>> parameters) {
		parserHtml.setStart_URL(URL);
		parserHtml.start();
		setURLsToMapParameters(keyForParameters, parameters);

	}

	private void setURLsToMapParameters(String key, Map<String, List<String>> parameters) {
		List<String> productURLs = parserHtml.getProductURLs();
		if (!productURLs.isEmpty()) {
			parameters.put(key, productURLs);
		}

	}

	private void checkParametersForParserFile(Entry<String, String> iterable_element) {
		if (iterable_element.getKey().equals("file-yurkas-seriia-sevilja")
				&& iterable_element.getValue().equals("file-yurkas-seriia-sevilja")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}
		if (iterable_element.getKey().equals("file-profildoors-seriia-portas")
				&& iterable_element.getValue().equals("file-profildoors-seriia-portas")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}
		if (iterable_element.getKey().equals("file-profildoors-seriia-x-klassika")
				&& iterable_element.getValue().equals("file-profildoors-seriia-x-klassika")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}
		if (iterable_element.getKey().equals("file-profildoors-seriia-x-modern")
				&& iterable_element.getValue().equals("file-profildoors-seriia-x-modern")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
		if (iterable_element.getKey().equals("file-profildoors-seriia-u-modern")
				&& iterable_element.getValue().equals("file-profildoors-seriia-u-modern")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}
		if (iterable_element.getKey().equals("file-profildoors-seriia-u-klassika")
				&& iterable_element.getValue().equals("file-profildoors-seriia-u-klassika")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}

		if (iterable_element.getKey().equals("file-profildoors-seriia-z")
				&& iterable_element.getValue().equals("file-profildoors-seriia-z")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-profildoors-seriia-e")
				&& iterable_element.getValue().equals("file-profildoors-seriia-e")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-yurkas-seriia-kasaporte")
				&& iterable_element.getValue().equals("file-yurkas-seriia-kasaporte")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-yurkas-seriia-siti")
				&& iterable_element.getValue().equals("file-yurkas-seriia-siti")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-odincovo-seriia-turin")
				&& iterable_element.getValue().equals("file-odincovo-seriia-turin")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-ladora-seriia-jego")
				&& iterable_element.getValue().equals("file-ladora-seriia-jego")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();

		}

		if (iterable_element.getKey().equals("file-high-gloss")
				&& iterable_element.getValue().equals("file-high-gloss")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
		if (iterable_element.getKey().equals("file-doors-massive")
				&& iterable_element.getValue().equals("file-doors-massive")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
		if (iterable_element.getKey().equals("file-doors-shponirovanie")
				&& iterable_element.getValue().equals("file-doors-shponirovanie")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
		if (iterable_element.getKey().equals("file-doors-emal-okrashenie")
				&& iterable_element.getValue().equals("file-doors-emal-okrashenie")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-3d") && iterable_element.getValue().equals("file-doors-3d")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-mdf")
				&& iterable_element.getValue().equals("file-doors-mdf")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-pvh")
				&& iterable_element.getValue().equals("file-doors-pvh")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-garmoshka")
				&& iterable_element.getValue().equals("file-doors-garmoshka")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-steklianye")
				&& iterable_element.getValue().equals("file-doors-steklianye")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}

		if (iterable_element.getKey().equals("file-doors-skladnye")
				&& iterable_element.getValue().equals("file-doors-skladnye")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
		if (iterable_element.getKey().equals("file-doors-nevidimki")
				&& iterable_element.getValue().equals("file-doors-nevidimki")) {
			parserFile.setPathToOriginalFile("");

			parserFile.start();
		}
	}
}
