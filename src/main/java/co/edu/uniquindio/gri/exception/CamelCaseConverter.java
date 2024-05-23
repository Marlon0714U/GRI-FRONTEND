package co.edu.uniquindio.gri.exception;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.el.stream.Stream;

import com.google.common.base.CaseFormat;
//import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import co.edu.uniquindio.gri.model.orcid.LastModifiedDate;

public class CamelCaseConverter {

	public static void main(String[] args) {
		// Ruta de la carpeta que contiene los archivos Java a editar
		String folderPath = "D:\\Escritorio\\TB\\GRI\\src\\main\\java\\co\\edu\\uniquindio\\gri\\model\\orcid";
		try {
			Files.walk(Paths.get(folderPath)).filter(Files::isRegularFile).forEach(file -> {
				try {
					editFile(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void editFile(Path file) throws IOException {
		System.out.println(file);
		String fileName = file.getFileName().toString();
		if (fileName.endsWith(".java")) {
			StringBuilder content = new StringBuilder();
			try (BufferedReader reader = Files.newBufferedReader(file)) {
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(processLine(line)).append("\n");
				}
			}

			try (BufferedWriter writer = Files.newBufferedWriter(file)) {
				writer.write(content.toString());
			}
		}
	}

	private static String processLine(String line) {

		String regex = "\\b(\\w+(?:-\\w+)*)\\b(?=\\W*$)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);

		if (matcher.find() && line.contains("-")) {
			String resultado = "@JsonProperty(\"".concat(matcher.group(1)).concat("\")\n");
			return resultado.concat(convertToCamelCase(line));
		}

		return line;
	}

	private static String convertToCamelCase(String input) {
		StringBuilder camelCase = new StringBuilder();
		boolean toUpperCase = false;

		for (char c : input.toCharArray()) {
			if (c == '-') {
				toUpperCase = true;
			} else {
				if (toUpperCase) {
					camelCase.append(Character.toUpperCase(c));
					toUpperCase = false;
				} else {
					camelCase.append(c);
				}
			}
		}

		return camelCase.toString();
	}
}