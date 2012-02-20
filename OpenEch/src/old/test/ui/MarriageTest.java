package ch.openech.test.ui;

import org.junit.runner.RunWith;

@RunWith(SwingRunner.class)
public class MarriageTest extends AbstractPageTest {

	public static final String DATA1_OFFICIAL_NAME = "Test1OfficialName";
	public static final String DATA1_FIRST_NAME = "Test1FirstName";
	public static final String DATA1_DATE_OF_BIRTH = "01.12.1942";
	public static final String DATA1_DATE_OF_ARRIVAL = "31.08.2010";

	public static final String DATA2_OFFICIAL_NAME = "Test2OfficialName";
	public static final String DATA2_FIRST_NAME = "Test2FirstName";
	public static final String DATA2_DATE_OF_BIRTH = "01.10.1943";
	public static final String DATA2_DATE_OF_ARRIVAL = "31.08.2010";
	
//	@Test
//	public void init1() throws InterruptedException, InvocationTargetException {
//		menu("Zuzug");
//		fillData1();
//		save();
//		
//		menu("Zuzug");
//		fillData2();
//		save();
//		
//		doClickButton("Eheschliessung");
//		JInternalFrame marriageFrame = findFrame(getViewComponent(), "Eheschliessung");
//		setText(marriageFrame, StringConstants.DATE_OF_MARITAL_STATUS, "01.09.2010");
//		
//		Component partner2 = findComponent(marriageFrame, "partner2");
//		clickContextMenu(partner2, "Person suchen");
//		
//		PersonSearchFrame personSearchFrame = (PersonSearchFrame)findFrame(getViewComponent(), "Personensuche");
//		setText(personSearchFrame, PersonSearchFrame.SEARCH_FIELD, DATA1_OFFICIAL_NAME);
//		doClickButton("Suchen");
//		personSearchFrame.selectedAndSave(0);
//		
//		doClickButton("Speichern");
//	}
//
//	
//	@Test
//	public void checkArrivalDate() throws InterruptedException, InvocationTargetException {
//		Assert.assertEquals(DATA1_DATE_OF_ARRIVAL, getText(PERSON.arrivalDate));
//	}
//
//	private void fillData1() {
//		NavigationFrame view = getView();
//		EditorPage editorPage = (EditorPage) view.getVisiblePage();
//		FormComponent formComponent = (FormComponent) editorPage.getPanel();
//		
//		setText(PERSON_IDENTIFICATION.officialName , DATA1_OFFICIAL_NAME);
//		setText(PERSON_IDENTIFICATION.firstName , DATA1_FIRST_NAME);
//		setText(PERSON_IDENTIFICATION.dateOfBirth , DATA1_DATE_OF_BIRTH);
//		setText(PERSON.arrivalDate , DATA1_DATE_OF_ARRIVAL);
//		
//		doClickButton("Heimatort hinzufügen");
//		setText(PLACE_OF_ORIGIN.originName , "Amden");
//		setText(PLACE_OF_ORIGIN.canton , "SG");
//		doClickButton("Übernehmen");
//		
//		doClickButton("Wohnadresse bearbeiten");
//		JInternalFrame dwellingAddressFrame = findFrame(getViewComponent(), "Wohnadresse");
//		setText(dwellingAddressFrame, DwellingAddress.DWELLING_ADDRESS.householdID , "12345");
//
//		Component mailAddress = findComponent(getViewComponent(), "mailAddress");
//		clickContextMenu(mailAddress, "Adresse Editieren");
//		JInternalFrame addressFrame = findFrame(getViewComponent(), "Adresse");
//		setText(addressFrame, Address.ADDRESS.street , "TestStrasse");
//		 doClickButton(addressFrame, "Übernehmen");
//
//		 doClickButton("Übernehmen");
//	}
//	
//	
//	private void fillData2() {
//		EditorPage editorPage = (EditorPage) view.getVisiblePage();
//		FormComponent formComponent = (FormComponent) editorPage.getPanel();
//
//		setText(PERSON_IDENTIFICATION.officialName , DATA2_OFFICIAL_NAME);
//		setText(PERSON_IDENTIFICATION.firstName , DATA2_FIRST_NAME);
//		setText(PERSON_IDENTIFICATION.dateOfBirth , DATA2_DATE_OF_BIRTH);
//		setText(PERSON.arrivalDate , DATA2_DATE_OF_ARRIVAL);
//		setCombo(formComponent, PERSON_IDENTIFICATION.sex, 1);
//		
//		doClickButton("Heimatort hinzufügen");
//		setText(PLACE_OF_ORIGIN.originName , "Amden");
//		setText(PLACE_OF_ORIGIN.canton , "SG");
//		doClickButton("Übernehmen");
//		
//		doClickButton("Wohnadresse bearbeiten");
//		JInternalFrame dwellingAddressFrame = findFrame(getViewComponent(), "Wohnadresse");
//		setText(dwellingAddressFrame, DwellingAddress.DWELLING_ADDRESS.householdID , "12345");
//
//		Component mailAddress = findComponent(getViewComponent(), "mailAddress");
//		clickContextMenu(mailAddress, "Adresse Editieren");
//		JInternalFrame addressFrame = findFrame(getViewComponent(), "Adresse");
//		setText(addressFrame, Address.ADDRESS.street , "TestStrasse");
//		 doClickButton(addressFrame, "Übernehmen");
//
//		 doClickButton("Übernehmen");
//	}
	
}
