package ch.ech.ech0196;

import java.util.List;
import java.math.BigDecimal;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:24.432260500")
public class ListOfExpenses {
	public static final ListOfExpenses $ = Keys.of(ListOfExpenses.class);

	public Object id;
	public List<Expense> expense;
	@NotEmpty
	@Size(3)
	public String currency;
	@NotEmpty
	public BigDecimal totalExpenses;
	@NotEmpty
	public BigDecimal totalExpensesDeductible;
	@NotEmpty
	public BigDecimal totalExpensesDeductibleCanton;
}