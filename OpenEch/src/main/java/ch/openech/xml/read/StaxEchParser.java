package ch.openech.xml.read;


public interface StaxEchParser {

	public void process(String xml);
	
	public String getLastInsertedId();
}
