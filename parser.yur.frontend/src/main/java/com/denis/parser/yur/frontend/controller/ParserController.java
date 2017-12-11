package com.denis.parser.yur.frontend.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.denis.parser.yur.backend.service.HtmlParser;

@Controller
public class ParserController {

	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	@Autowired
	private HtmlParser htmlParser;

	@RequestMapping(value = "/parser", method = RequestMethod.GET)
	public ModelAndView parsePage() {

		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Parser");
		mv.addObject("userClickParser", true);

		logger.info("Inside PageController index method - INFO");
		logger.debug("Inside PageController index method - DEBUG");

		return mv;
	}

	@RequestMapping(value = "/parser", method = RequestMethod.POST)
	public String parseNtImpactDrill(@RequestParam Map<String, String> allRequestParams) {

		for (Entry<String, String> iterable_element : allRequestParams.entrySet()) {

			if (iterable_element.getKey().equals("yurkas-seriia-sevilja")
					&& iterable_element.getValue().equals("yurkas-seriia-sevilja")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/sevilja/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("profildoors-seriia-portas")
					&& iterable_element.getValue().equals("profildoors-seriia-portas")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/porta/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("profildoors-seriia-x-klassika")
					&& iterable_element.getValue().equals("profildoors-seriia-x-klassika")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-x-klassika/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("profildoors-seriia-x-modern")
					&& iterable_element.getValue().equals("profildoors-seriia-x-modern")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-x-modern/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("profildoors-seriia-u-modern")
					&& iterable_element.getValue().equals("profildoors-seriia-u-modern")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-u-modern/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("profildoors-seriia-u-klassika")
					&& iterable_element.getValue().equals("profildoors-seriia-u-klassika")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-u-klassika/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("profildoors-seriia-z")
					&& iterable_element.getValue().equals("profildoors-seriia-z")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-z/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("profildoors-seriia-e")
					&& iterable_element.getValue().equals("profildoors-seriia-e")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/seriia-e/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("yurkas-seriia-kasaporte")
					&& iterable_element.getValue().equals("yurkas-seriia-kasaporte")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/kasaporte/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("yurkas-seriia-siti")
					&& iterable_element.getValue().equals("yurkas-seriia-siti")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/siti/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("odincovo-seriia-turin")
					&& iterable_element.getValue().equals("odincovo-seriia-turin")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/serija-turin/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("ladora-seriia-jego")
					&& iterable_element.getValue().equals("ladora-seriia-jego")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D1%8D%D0%BA%D0%BE%D1%88%D0%BF%D0%BE%D0%BD/serija-jego/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			/////////////////////

			if (iterable_element.getKey().equals("high-gloss") && iterable_element.getValue().equals("high-gloss")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/high-gloss/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("doors-massive")
					&& iterable_element.getValue().equals("doors-massive")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D0%B8%D0%B7_%D0%BC%D0%B0%D1%81%D1%81%D0%B8%D0%B2%D0%B0/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("doors-shponirovanie")
					&& iterable_element.getValue().equals("doors-shponirovanie")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-shponirovannye/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("doors-emal-okrashenie")
					&& iterable_element.getValue().equals("doors-emal-okrashenie")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-emal/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-3d") && iterable_element.getValue().equals("doors-3d")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/3d-dveri/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-mdf") && iterable_element.getValue().equals("doors-mdf")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-mdf/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-pvh") && iterable_element.getValue().equals("doors-pvh")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/%D0%B4%D0%B2%D0%B5%D1%80%D0%B8_%D0%BF%D0%B2%D1%85/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-garmoshka")
					&& iterable_element.getValue().equals("doors-garmoshka")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-garmoshka/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-steklianye")
					&& iterable_element.getValue().equals("doors-steklianye")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/steklianye-dveri/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (iterable_element.getKey().equals("doors-skladnye")
					&& iterable_element.getValue().equals("doors-skladnye")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/skladnye-dveri/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (iterable_element.getKey().equals("doors-nevidimki")
					&& iterable_element.getValue().equals("doors-nevidimki")) {
				htmlParser.setStart_URL(
						"http://yurkas.by/shop/%D0%9C%D0%B5%D0%B6%D0%BA%D0%BE%D0%BC%D0%BD%D0%B0%D1%82%D0%BD%D1%8B%D0%B5_%D0%B4%D0%B2%D0%B5%D1%80%D0%B8/dveri-nevidimki/");
				try {
					htmlParser.start();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		}

		return "redirect:/parser";

	}

}
