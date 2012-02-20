package ch.openech.client.ewk;

import java.util.List;

public interface XmlResult<T> {

	public List<String> getXml(T object) throws Exception;
}
