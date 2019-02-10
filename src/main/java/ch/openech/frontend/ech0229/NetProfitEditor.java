package ch.openech.frontend.ech0229;

import org.minimalj.frontend.editor.Editor.NewObjectEditor;
import org.minimalj.frontend.form.Form;

import ch.ech.ech0229.NetProfit;

public class NetProfitEditor extends NewObjectEditor<NetProfit> {

	@Override
	protected Form<NetProfit> createForm() {
		Form<NetProfit> form = new Form<NetProfit>(6) {
			protected int getColumnWidthPercentage() {
				return 50;
			}
		};
		form.line(Form.GROW_FIRST_ELEMENT, "Laber die Fasel die Quatsch langer Text", NetProfit.$.taxableIncome.federalTax,
				NetProfit.$.taxableIncome.cantonalTax);
		return form;
	}

	@Override
	protected NetProfit save(NetProfit object) {
		return object;
	}

}
