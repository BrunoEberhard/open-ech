package ch.openech.frontend.ewk;

import java.util.List;

// TODO XmlResult sollte ursprünglich wohl mal mit XmlPreview verwendet werden. Momentan wird das nicht mehr unterstützt. Entweder wieder aktivieren oder rückbauen
public interface XmlResult<T> {

	public List<String> getXml(T object) throws Exception;
}
