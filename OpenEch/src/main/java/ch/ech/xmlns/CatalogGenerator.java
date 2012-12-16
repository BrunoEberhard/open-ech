package ch.ech.xmlns;

import java.io.File;
import java.io.FilenameFilter;

public class CatalogGenerator {

	// workspace/.metadata/.plugins/org.eclipse.wst.xml.core/usser_catalog.xml
	
	public static void main(String[] args) {
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String name) {
				return name.startsWith("eCH-") && name.endsWith(".xsd");
			}
		};
		
		for (File file : new File(".").listFiles(filter)) {
			// eCH-0006-2-0.xsd
			// 0123456789012345
			//  <uri name="http://www.ech.ch/xmlns/eCH-0020/1" uri="platform:/resource/OpenEch/src/main/xml/ch/ech/xmlns/eCH-0020-1-0.xsd"/>

			String name = file.getName();
			
			System.out.print("  <uri name=\"http://www.ech.ch/xmlns/");
			System.out.print(name.substring(0, 8));
			System.out.print("/");
			System.out.print(name.substring(9, 10));
			System.out.print("\" uri=\"platform:/resource/OpenEch/src/main/xml/ch/ech/xmlns/");
			System.out.print(name);
			System.out.println("\"/>");
		}
		
		for (File file : new File(".").listFiles(filter)) {
			// eCH-0006-2-0.xsd
			// 0123456789012345
			//  <system systemId="http://www.ech.ch/xmlns/eCH-0006/2/eCH-0006-2-0.xsd" uri="platform:/resource/OpenEch/src/main/xml/ch/ech/xmlns/eCH-0006-2-0.xsd"/>

			String name = file.getName();
			
			System.out.print("  <system systemId=\"http://www.ech.ch/xmlns/");
			System.out.print(name.substring(0, 8));
			System.out.print("/");
			System.out.print(name.substring(9, 10));
			System.out.print("/");
			System.out.print(name);
			System.out.print("\" uri=\"platform:/resource/OpenEch/src/main/xml/ch/ech/xmlns/");
			System.out.print(name);
			System.out.println("\"/>");
		}

	}

}
