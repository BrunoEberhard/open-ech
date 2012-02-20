package ch.openech.dm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.util.StringUtils;

public class StringToConstantConverter2 {

	private List<String> values = new ArrayList<String>();
	private List<String> constants = new ArrayList<String>();
	
	public StringToConstantConverter2() {
		List<String> names = readNames();
		for (String value : names) {
			values.add("\"" + value + "\"");
			constants.add(StringUtils.toConstant(value));
		}
	}
	
	private List<String> readNames() {
		List<String> names = new ArrayList<String>();
		XmlConstants constants = new XmlConstants();
		for (Field field: XmlConstants.class.getFields()) {
			if (field.getType() == String.class) {
				try {
					String value = (String)field.get(constants);
					names.add(value);
					System.out.println(value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
			}
		}
		return names;
	}

	private String findReplace(String line) {
		for (int i = 0; i<values.size(); i++) {
			String value = values.get(i);
			int pos = line.indexOf(value);
			while (pos > -1) {
				line = line.substring(0, pos) + constants.get(i) + line.substring(pos + value.length());
				pos = line.indexOf(value);
			}
		}
		return line;
	}
	
	private void convert(File file) throws IOException {
		if (!file.getName().endsWith(".java")) {
			System.out.println("Skipping: " + file.getName());
			return;
		} else if (file.getName().endsWith("StringConstants.java")) {
			System.out.println("Skipping: " + file.getName());
			return;
		}
		System.out.println("Search/Replace: " + file.getName());
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		List<String> strings = new ArrayList<String>();
		while (bufferedReader.ready()) {
			String line = bufferedReader.readLine();
			line = findReplace(line);
			strings.add(line);
		}
		bufferedReader.close();
		fileReader.close();

		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		for (String string : strings) {
			bufferedWriter.write(string + "\n");
		}
		
		bufferedWriter.close();
		fileWriter.close();
		
	}
	
	
	private void convertDirectory(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				convertDirectory(file);
			} else {
				convert(file);
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		File source = new File(".");
		new StringToConstantConverter2().convertDirectory(source);
	}

}
