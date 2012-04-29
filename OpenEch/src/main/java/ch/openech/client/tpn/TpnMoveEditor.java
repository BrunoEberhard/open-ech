package ch.openech.client.tpn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ch.openech.client.ewk.XmlEditor;
import ch.openech.dm.contact.Contact;
import ch.openech.dm.tpn.ThirdPartyMove;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.xml.read.StaxEch0046;
import ch.openech.xml.write.EchNamespaceContext;
import ch.openech.xml.write.WriterEch0046;
import ch.openech.xml.write.WriterEch0112;
import ch.openech.xml.write.WriterElement;

public class TpnMoveEditor extends XmlEditor<ThirdPartyMove> {
	private final MoveDirection direction;

	public TpnMoveEditor(String direction) {
		this(MoveDirection.valueOf(direction));
	}
		
	public TpnMoveEditor(MoveDirection direction) {
		super(EchNamespaceContext.getNamespaceContext(112, "1.0"));
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
	public boolean save(ThirdPartyMove tpm) {
		String xml;
		try {
			WriterEch0112 writer = new WriterEch0112(getEchNamespaceContext());
			xml = isDirectionIn() ? writer.moveIn(tpm) : writer.moveIn(tpm);
			saveContractor(tpm.contractor);
			// MoveIn result = StaxEch0108.process(xml);
			// new MoveInDAO(AbstractDAO.getCommonConnection()).insertMoveIn(1, 0, result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> getXml(ThirdPartyMove tpm) throws Exception {
		List<String> xmlList = new ArrayList<String>();
		if (isDirectionIn()) {
			xmlList.add(new WriterEch0112(getEchNamespaceContext()).moveIn(tpm));
		} else {
			xmlList.add(new WriterEch0112(getEchNamespaceContext()).moveOut(tpm));
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
	
	private void saveContractor(Contact contractor) throws Exception {
		File file = getDefaultContractorFileName();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			
			WriterEch0046 writer = new WriterEch0046(EchNamespaceContext.getNamespaceContext(46, "2.0"));
			WriterElement writerElement = writer.delivery(outputStreamWriter);
			writer.contact(writerElement, contractor);
			writer.result();
			
			fileOutputStream.close();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	@Override
	protected IForm<ThirdPartyMove> createForm() {
		return new TpnMoveForm(direction);
	}

	@Override
	protected ThirdPartyMove newInstance() {
		ThirdPartyMove thirdPartyMove = new ThirdPartyMove();
		thirdPartyMove.contractor = loadLastContractor();

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

	@Override
	public void validate(ThirdPartyMove object, List<ValidationMessage> resultList) {
		// nothing to do
	}
	
}
