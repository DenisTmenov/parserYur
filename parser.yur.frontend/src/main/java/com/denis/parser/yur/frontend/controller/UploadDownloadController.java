package com.denis.parser.yur.frontend.controller;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UploadDownloadController implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	@RequestMapping(value = "/parser/UploadDownloadFileServlet", method = RequestMethod.POST)
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		String rootPath = System.getProperty("catalina.home");
		ServletContext ctx = servletContextEvent.getServletContext();
		String relativePath = ctx.getInitParameter("tempfile.dir");
		File file = new File(rootPath + File.separator + relativePath);
		if (!file.exists())
			file.mkdirs();
		System.out.println("File Directory created to be used for storing files");
		ctx.setAttribute("FILES_DIR_FILE", file);
		ctx.setAttribute("FILES_DIR", rootPath + File.separator + relativePath);
	}

}
