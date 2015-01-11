package ch.openech.frontend.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.edit.form.Form;

import ch.openech.frontend.XmlEditor;
import ch.openech.frontend.tpn.MoveDirection;
import ch.openech.frontend.tpn.TpnMoveForm;
import ch.openech.model.contact.Contact;
import ch.openech.model.contact.ContactEntry;
import ch.openech.model.tpn.ThirdPartyMove;
import ch.openech.xml.read.StaxEch0046;
import ch.openech.xml.write.EchSchema;
import ch.openech.xml.write.WriterEch0046;
import ch.openech.xml.write.WriterEch0112;
import ch.openech.xml.write.WriterElement;

public class TpnMoveEditor extends XmlEditor<ThirdPartyMove> {
	private final MoveDirection direction;

	public TpnMoveEditor(String direction) {
		this(MoveDirection.valueOf(direction));
	}
		
	public TpnMoveEditor(MoveDirection direction) {
		super(EchSchema.getNamespaceContext(112, "1.0"));
		this.direction = direction;
	}
	
	private boolean isDirectionIn() {
		return direction == MoveDirection.IN;
	}
	
	protected String getResourceBase() {
		return isDirectionIn() ? "TpnMoveIn" : "TpnMoveOut";
	}

	protected String getResourceBaseName() {
		return isDirectionIn() ? "TpnMoveIn" : "TpnMoveOut";
	}

	@Override
	public Object save(ThirdPartyMove tpm) {
		String xml;
		try {
			WriterEch0112 writer = echSchema.getWriterEch0112();
			xml = isDirectionIn() ? writer.moveIn(tpm) : writer.moveIn(tpm);
			saveContractor(tpm.contractor);
			// MoveIn result = StaxEch0108.process(xml);
			// new MoveInDAO(AbstractDAO.getCommonConnection()).insertMoveIn(1, 0, result);
			return SAVE_SUCCESSFUL;
		} catch (Exception e) {
			e.printStackTrace();
			return SAVE_FAILED;
		}
	}

	@Override
	public List<String> getXml(ThirdPartyMove tpm) throws Exception {
		List<String> xmlList = new ArrayList<String>();
		if (isDirectionIn()) {
			xmlList.add(new WriterEch0112(echSchema).moveIn(tpm));
		} else {
			xmlList.add(new WriterEch0112(echSchema).moveOut(tpm));
		}
		return xmlList;
	}

	private File getDefaultContractorFileName() {
		String userHome = System.getProperty("user.home");
		File file = new File (userHome + File.separator + "Open-Ech_DefaultContractor.xml");
		return file;
	}
	
	private Contact loadLastContractor() {
		File file = getDefaultContractorFileName();
		try {
			if (file.isFile() && file.canRead()) {
				return new StaxEch0046().read(new FileInputStream(file));
			}
		} catch (Exception x) {
			System.out.println("loadLastContractor konnte nicht von File lesen: " + file);
		}
		return null;
	}
	
	private void saveContractor(List<ContactEntry> contractor) throws Exception {
		File file = getDefaultContractorFileName();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			
			WriterEch0046 writer = new WriterEch0046(EchSchema.getNamespaceContext(46, "2.0"));
			WriterElement writerElement = writer.delivery(outputStreamWriter);
			writer.contact(writerElement, contractor);
			writer.result();
			
			fileOutputStream.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	@Override
	protected Form<ThirdPartyMove> createForm() {
		return new TpnMoveForm(direction);
	}

	@Override
	protected ThirdPartyMove newInstance() {
		ThirdPartyMove thirdPartyMove = new ThirdPartyMove();
		thirdPartyMove.contractor.addAll(loadLastContractor().entries);

//		private void setPresets() {
//			for (String key : getStringKeys()) {
//				String setting = AbstractApplication.preferences().get(key, null);
//				if (setting != null) {
//					set(key, setting);
//				}
//			}
//			if (residenceField != null) {
//				// TODO das sollte das Formpanel von sich aus können
//				MunicipalityIdentification municipalityIdentification = new MunicipalityIdentification();
//				for (String key : municipalityIdentification.keySet()) {
//					String setting = AbstractApplication.preferences().get(key, null);
//					if (setting != null) {
//						municipalityIdentification.set(key, setting);
//					}
//				}
//				Residence residence = new Residence();
//				residence.reportingMunicipality = municipalityIdentification;
//				residenceField.setObject(residence);
//			}
//		}
		
		return thirdPartyMove;
	}

}
