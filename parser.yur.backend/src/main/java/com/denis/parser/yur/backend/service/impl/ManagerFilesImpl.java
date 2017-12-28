package com.denis.parser.yur.backend.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.springframework.stereotype.Service;

import com.denis.parser.yur.backend.service.ManagerFiles;
import com.denis.parser.yur.backend.service.ServiceException;

@Service("managerFiles")
public class ManagerFilesImpl implements ManagerFiles {
	private String pathToOriginalFileAndFileName;

	@Override
	public void setPath(String path) {
		this.pathToOriginalFileAndFileName = path;
	}

	@Override
	public List<String> readFromeFile() {
		List<String> allOriginalLines = null;
		try {
			allOriginalLines = Files.readAllLines(Paths.get(pathToOriginalFileAndFileName));
		} catch (IOException e) {
			new ServiceException("ORIGINAL file not read!", e);
		}
		return allOriginalLines;
	}

	@Override
	public boolean saveLinesToFile(List<String> lines, String pathToFileAndFileName) {

		String newFileName = "";

		if (pathToFileAndFileName == null || pathToFileAndFileName.isEmpty()
				|| pathToFileAndFileName.trim().contains(" ")) {
			String[] masName = pathToOriginalFileAndFileName.split("\\.");
			newFileName = masName[0] + "_modify." + masName[1];
		} else {
			newFileName = pathToFileAndFileName;
		}

		try {
			if (!Files.exists(Paths.get(newFileName))) {
				Files.write(Paths.get(newFileName), "".getBytes(), StandardOpenOption.CREATE_NEW);
			} else {
				Files.write(Paths.get(newFileName), "".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
			}

			for (String line : lines) {
				Files.write(Paths.get(newFileName), (line + "\n").getBytes(), StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean saveImageToFile(byte[] image, String pathToFileAndFileName) {

		try {
			Files.write(Paths.get(pathToFileAndFileName), image, StandardOpenOption.CREATE);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

}
