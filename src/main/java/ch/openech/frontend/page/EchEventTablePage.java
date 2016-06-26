package ch.openech.frontend.page;

import java.time.LocalDateTime;

import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.TablePage.TablePageWithDetail;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

import ch.openech.frontend.page.EchEventTablePage.EchEvent;

public abstract class EchEventTablePage<T, DETAIL_PAGE extends Page> extends TablePageWithDetail<EchEvent<T>, DETAIL_PAGE> {

	private static final Object[] KEYS = new Object[]{EchEvent.$.version, EchEvent.$.time, EchEvent.$.description};
	
	public EchEventTablePage() {
		super(KEYS);
	}
	
	public static class EchEvent<T> {
		public static final EchEvent<?> $ = Keys.of(EchEvent.class);
		
		public String version;
		public LocalDateTime time;
		@Size(255)
		public String description;
		public T object;
	}

}
