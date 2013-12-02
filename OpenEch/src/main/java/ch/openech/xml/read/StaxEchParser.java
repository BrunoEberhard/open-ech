package ch.openech.xml.read;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;


public interface StaxEchParser {

	public void process(String xml) throws XmlPullParserException, IOException;
	
	public String getLastInsertedId();
}
