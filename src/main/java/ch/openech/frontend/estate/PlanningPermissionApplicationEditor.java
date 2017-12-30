package ch.openech.frontend.estate;

import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;

import ch.openech.frontend.XmlEditor;
import ch.openech.model.estate.PlanningPermissionApplication;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0211;
import ch.openech.xml.write.WriterElement;

//public class PlanningPermissionApplicationEditor extends NewObjectEditor<PlanningPermissionApplication> {
public class PlanningPermissionApplicationEditor extends XmlEditor<PlanningPermissionApplication, PlanningPermissionApplication> {
	
	public PlanningPermissionApplicationEditor() {
		super(EchSchema.getNamespaceContext(211, "1.0"));
	}

	@Override
	protected Form<PlanningPermissionApplication> createForm() {
		return new PlanningPermissionApplicationForm(true);
	}

	@Override
	protected PlanningPermissionApplication save(PlanningPermissionApplication application) {
		return Backend.save(application);
	}

	@Override
	public List<String> getXml(PlanningPermissionApplication object) throws Exception {
		WriterEch0211 writer = echSchema.getWriterEch0211();
		WriterElement writerElement = writer.delivery();
		writer.planningPermissionApplication(writerElement, object);
		return Collections.singletonList(writer.result());
	}

	@Override
	protected PlanningPermissionApplication createObject() {
		return new PlanningPermissionApplication();
	}

}
