package com.denis.parser.yur.frontend.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadFileController {
	private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

	@RequestMapping(value = "/upload/file", method = RequestMethod.GET)
	public String uploadGet() {
		return "redirect:/home";
	}

	@RequestMapping(value = "/upload/file", method = RequestMethod.POST)
	public String uploadPost(@RequestParam("sqlFile") MultipartFile sqlFile,
			@RequestParam("txtFile") MultipartFile txtFile, HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> uploadFilesAttribute = (Map<String, Map<String, String>>) request.getSession()
				.getAttribute("uploadFiles");

		if (uploadFilesAttribute == null) {
			uploadFilesAttribute = new HashMap<>();
		}

		Map<String, String> errors = new HashMap<>();

		Map<String, String> sqlFileInfo = checkAndUploadFileToTempFolder(sqlFile, "sql");
		Map<String, String> txtFileInfo = checkAndUploadFileToTempFolder(txtFile, "txt");

		if (sqlFileInfo.containsKey("error")) {
			errors.put("sql", sqlFileInfo.get("error"));
		}
		if (txtFileInfo.containsKey("error")) {
			errors.put("txt", txtFileInfo.get("error"));
		}
		if (errors.containsKey("sql") || errors.containsKey("txt")) {
			uploadFilesAttribute.put("errors", errors);
		} else {
			if (uploadFilesAttribute.containsKey("errors")) {
				uploadFilesAttribute.remove("errors");
			}
			uploadFilesAttribute.put("sql", sqlFileInfo);
			uploadFilesAttribute.put("txt", txtFileInfo);
		}
		request.getSession().setAttribute("uploadFiles", uploadFilesAttribute);

		return "forward:/parser/file";
	}

	private Map<String, String> checkAndUploadFileToTempFolder(MultipartFile file, String fileType) {

		Map<String, String> result = new HashMap<>();

		if (!file.isEmpty()) {
			String fileTypeFromFile = getTypeOfUploadFile(file);
			if (fileTypeFromFile.equals(fileType)) {
				String fileName = getFileNameOfUploadFile(file);
				Map<String, String> uploadFileToTempFolder = uploadFileToTempFolder(file, fileName, fileTypeFromFile);
				String uploadFileError = uploadFileToTempFolder.get("error");
				if (uploadFileError == null) {
					return uploadFileToTempFolder;
				} else {
					result.put("error", uploadFileError);
				}
			} else {
				result.put("error", "You input not " + fileType + " file!");
			}
		} else {
			result.put("error", fileType.replace(fileType.substring(0, 1), fileType.substring(0, 1).toUpperCase())
					+ " file is empty!");
		}
		return result;
	}

	private String getFileNameOfUploadFile(MultipartFile file) {
		String fullName = getFullNameOfUploadFile(file);
		String typeOfFile = getTypeOfUploadFile(file);

		logger.info("Return name of file " + file.getOriginalFilename());
		return fullName.replace("." + typeOfFile, "");

	}

	private Map<String, String> uploadFileToFolder(MultipartFile file, String fileName, String fileType) {
		// String rootPath = System.getProperty("catalina.home");
		// File dir = new File(rootPath + File.separator + "tmpFiles");

		// if (!dir.exists()) {
		// dir.mkdirs();
		// }

		// File uploadFile = new File(dir.getAbsolutePath() + File.separator + name);

		return null;
	}

	private Map<String, String> uploadFileToTempFolder(MultipartFile file, String fileName, String fileType) {

		Map<String, String> result = new HashMap<>();

		byte[] bytes = null;
		File uploadFile = null;
		try {
			bytes = file.getBytes();
		} catch (IOException e) {
			logger.error("Problem when try to get massive of bytes of file" + file.getOriginalFilename());
			result.put("error", "Problem when try to upload file to temp folder!");
			return result;
		}

		try {
			uploadFile = File.createTempFile(fileName + "_temp_", "." + fileType);
		} catch (IOException e) {
			logger.error("Problem when try to get massive of bytes of file" + file.getOriginalFilename());
			result.put("error", "Problem when try to upload file to temp folder!");
			return result;
		}

		BufferedOutputStream bout = null;
		try {
			bout = new BufferedOutputStream(new FileOutputStream(uploadFile));
		} catch (FileNotFoundException e) {
			logger.error("Problem when try to create BufferedOutputStream for file" + file.getOriginalFilename());
			result.put("error", "Problem when try to upload file to temp folder!");
			return result;
		}
		try {
			bout.write(bytes);
			bout.flush();
			bout.close();
		} catch (IOException e) {
			logger.error(
					"Problem when try to write/flush/close BufferedOutputStream for file" + file.getOriginalFilename());
			result.put("error", "Problem when try to upload file to temp folder!");
			return result;
		}

		result.put("fileName", fileName);
		result.put("fileType", fileType);
		result.put("fullFileName", fileName + "." + fileType);
		result.put("path", uploadFile.getAbsolutePath());

		return result;
	}

	private String getFullNameOfUploadFile(MultipartFile file) {
		String fullName;
		try {
			fullName = new String(file.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Problem when got fileName of file " + file.getOriginalFilename());
			return "";
		}
		return fullName;
	}

	private String getTypeOfUploadFile(MultipartFile file) {
		String fullName = getFullNameOfUploadFile(file);

		String[] nameMas = fullName.split("\\.");
		if (nameMas != null && nameMas.length > 1) {
			logger.info("Return type of file " + file.getOriginalFilename());
			return nameMas[nameMas.length - 1];
		}
		logger.info("File " + file.getOriginalFilename() + " didn't contain type!");
		return "";
	}
}
