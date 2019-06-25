package ch.openech.frontend.ech0008;

import static ch.ech.ech0072.CountryInformation.$;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;
import org.minimalj.frontend.page.SimpleTableEditorPage;
import org.minimalj.repository.query.By;

import ch.ech.ech0072.CountryInformation;

public class CountryTablePage extends SimpleTableEditorPage<CountryInformation> {

	@Override
	protected Object[] getColumns() {
		return new Object[] { $.id, $.iso2Id, $.iso3Id, $.getShortName() };
	}

	@Override
	protected Form<CountryInformation> createForm(boolean editable, boolean newObject) {
		Form<CountryInformation> form = new Form<>(editable, 4);
		form.line(!newObject ? new TextFormElement($.id) : $.id, $.unId);
		form.line($.iso2Id, $.iso3Id);
		form.line($.shortNameDe, $.shortNameFr, $.shortNameIt, $.shortNameEn);
		form.line($.officialNameDe, $.officialNameFr, $.officialNameIt, $.continent);
		form.line($.region, $.state, $.areaState);
		form.line($.unMember, $.unEntryDate);
		form.line($.recognizedCh, $.recognizedDate);
		form.line($.remarkDe);
		form.line($.remarkFr);
		form.line($.remarkIt);
		form.line($.entryValid, $.dateOfChange);
		return form;
//		Form<CountryInformation> form = new Form<>(editable, 2);
//		form.line(!newObject ? new TextFormElement($.id) : $.id, $.unId);
//		form.line($.iso2Id, $.iso3Id);
//		form.line($.shortNameDe, $.shortNameFr);
//		form.line($.shortNameIt, $.shortNameEn);
//		form.line($.officialNameDe, $.officialNameFr);
//		form.line($.officialNameIt, $.continent);
//		form.line($.region, $.state);
//		form.line($.areaState);
//		form.line($.unMember, $.unEntryDate);
//		form.line($.recognizedCh, $.recognizedDate);
//		form.line($.remarkDe, $.remarkFr);
//		form.line($.remarkIt);
//		form.line($.entryValid, $.dateOfChange);
//		return form;
	}
	
	@Override
	protected List<CountryInformation> load() {
		return Backend.find(CountryInformation.class, By.ALL);
	}
	
}
