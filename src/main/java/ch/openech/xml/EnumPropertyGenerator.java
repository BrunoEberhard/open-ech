package ch.openech.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

import org.minimalj.metamodel.generator.ClassGenerator;
import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjModel;

public class EnumPropertyGenerator {

	private final File file;
	private final Properties properties = new Properties();

	public EnumPropertyGenerator(String path) {
		file = new File(path);
		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file)) {
				properties.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			file.delete();
		}
	}

	public void generate(MjModel model) {
		generate(model.entities);
	}

	public void generate(Collection<? extends MjEntity> entities) {
		entities.stream().filter(MjEntity::isEnumeration).forEach(this::generateEntity);

		try (FileOutputStream fos = new FileOutputStream(file)) {
			TreeSet<String> keys = new TreeSet<>();
			Enumeration<Object> e = properties.keys();
			while (e.hasMoreElements()) {
				keys.add((String) e.nextElement());
			}
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "8859_1"))) {
				String lastPrefix = "";
				for (String k : keys) {
					String prefix = k.substring(0, k.indexOf('.'));
					if (!prefix.equals(lastPrefix)) {
						bw.newLine();
						lastPrefix = prefix;
					}
					String val = saveConvert(properties.getProperty(k), false);
					bw.write(k + " = " + val);
					bw.newLine();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateEntity(MjEntity entity) {
		entity.values.forEach(value -> {
			String key = entity.getClassName() + "." + ClassGenerator.toEnum(value);
			if (!properties.containsKey(key)) {
				properties.put(key, "TODO");
			}
		});
	}

	// from Properties
	private String saveConvert(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if (((aChar < 0x0020) || (aChar > 0x007e))) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(Integer.toHexString((aChar >> 12) & 0xF));
					outBuffer.append(Integer.toHexString((aChar >> 8) & 0xF));
					outBuffer.append(Integer.toHexString((aChar >> 4) & 0xF));
					outBuffer.append(Integer.toHexString(aChar & 0xF));
				}
			}
		}
		return outBuffer.toString();
	}
}
