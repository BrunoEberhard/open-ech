package ch.openech.test.ui;

import org.junit.runner.RunWith;

@RunWith(SwingRunner.class)
public class MoveInPageTest extends AbstractPageTest {

	public static final String DATA_OFFICIAL_NAME = "Test1OfficialName";
	public static final String DATA_FIRST_NAME = "Test1FirstName";
	public static final String DATA_DATE_OF_BIRTH = "01.02.1942";
	public static final String DATA_ARRIVAL_DATE = "02.08.2010";

//	@BeforeClass
//	public static void createPerson() throws InterruptedException, InvocationTargetException {
//		startupEwkWindow();
//		gotoMovinPage();
//		fillData();
//		save();
//	}
//
//	@AfterClass
//	public static void shutDown() throws InterruptedException, InvocationTargetException {
//		closeWindow();
//	}
//
//	@Test
//	public void checkOfficialName() throws InterruptedException, InvocationTargetException {
//		Assert.assertEquals(DATA_OFFICIAL_NAME, getText(PERSON_IDENTIFICATION.officialName));
//	}
//
//	@Test
//	public void checkFirstName() throws InterruptedException, InvocationTargetException {
//		Assert.assertEquals(DATA_FIRST_NAME, getText(PERSON_IDENTIFICATION.firstName));
//	}
//
//	@Test
//	public void checkDateOfBirth() throws InterruptedException, InvocationTargetException {
//		Assert.assertEquals(DATA_DATE_OF_BIRTH, getText(PERSON_IDENTIFICATION.dateOfBirth));
//	}
//
//	@Test
//	public void checkArrivalDate() throws InterruptedException, InvocationTargetException {
//		Assert.assertEquals(DATA_ARRIVAL_DATE, getText(PERSON.arrivalDate));
//	}
//
//	
//	private static void gotoMovinPage() {
//		doClickButton("Zuzug");
//	}
//	
//	private static void fillData() {
//		NavigationFrame view = getView();
//		MoveInEditor moveInPage = (MoveInEditor) view.getVisiblePage();
//
//		setText(moveInPage, PERSON_IDENTIFICATION.officialName, DATA_OFFICIAL_NAME);
//		setText(moveInPage, PERSON_IDENTIFICATION.firstName, DATA_FIRST_NAME);
//		setText(moveInPage, PERSON_IDENTIFICATION.dateOfBirth, DATA_DATE_OF_BIRTH);
//		setText(moveInPage, PERSON.arrivalDate, DATA_ARRIVAL_DATE);
//		
//		doClickButton("Heimatort hinzufügen");
//		setText(moveInPage, PLACE_OF_ORIGIN.originName, "Amden");
//		setText(moveInPage, PLACE_OF_ORIGIN.canton, "SG");
//		doClickButton("Übernehmen");
//		
//		doClickButton("Wohnadresse bearbeiten");
//		JInternalFrame dwellingAddressFrame = findFrame(getViewComponent(), "Wohnadresse");
//		setText(dwellingAddressFrame, DwellingAddress.DWELLING_ADDRESS.householdID, "12345");
//
//		Component mailAddress = findComponent(getViewComponent(), "mailAddress");
//		clickContextMenu(mailAddress, "Adresse Editieren");
//		JInternalFrame addressFrame = findFrame(getViewComponent(), "Adresse");
//		setText(addressFrame, Address.ADDRESS.street, "TestStrasse");
//		 doClickButton(addressFrame, "Übernehmen");
//
//		 doClickButton("Übernehmen");
//	}
	
}
