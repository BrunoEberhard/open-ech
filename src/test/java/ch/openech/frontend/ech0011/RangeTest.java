package ch.openech.frontend.ech0011;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import ch.ech.ech0046.DateRange;
import ch.openech.OpenEchTest;

public class RangeTest extends OpenEchTest {

	@Test
	public void testRange() {
		DateRange range = RangeUtil.parseDateRange("1.1.2010 - 1.2.2011");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertEquals(LocalDate.of(2011, 2, 1), range.dateTo);

		range = RangeUtil.parseDateRange("(1.1.2010 - 1.2.2011)");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertEquals(LocalDate.of(2011, 2, 1), range.dateTo);

		range = RangeUtil.parseDateRange("(1.1.2010-1.2.2011)");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertEquals(LocalDate.of(2011, 2, 1), range.dateTo);
	}

	@Test
	public void testRangeFrom() {
		DateRange range = RangeUtil.parseDateRange("1.1.2010 - ");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("1.1.2010-");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("ab 1.1.2010");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("Ab 1.1.2010");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("ab1.1.2010");
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateFrom);
		Assert.assertNull(range.dateTo);
	}

	@Test
	public void testRangeTo() {
		DateRange range = RangeUtil.parseDateRange(" - 1.1.2010");
		Assert.assertNull(range.dateFrom);
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateTo);

		range = RangeUtil.parseDateRange("-1.1.2010");
		Assert.assertNull(range.dateFrom);
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateTo);

		range = RangeUtil.parseDateRange("bis 1.1.2010");
		Assert.assertNull(range.dateFrom);
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateTo);

		range = RangeUtil.parseDateRange("Bis 1.1.2010");
		Assert.assertNull(range.dateFrom);
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateTo);

		range = RangeUtil.parseDateRange("bis1.1.2010");
		Assert.assertNull(range.dateFrom);
		Assert.assertEquals(LocalDate.of(2010, 1, 1), range.dateTo);
	}

	@Test
	public void testRangeEmpty() {
		DateRange range = RangeUtil.parseDateRange("");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("-");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("()");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("(-)");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("ab");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);

		range = RangeUtil.parseDateRange("bis");
		Assert.assertNull(range.dateFrom);
		Assert.assertNull(range.dateTo);
	}
}
