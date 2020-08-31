package ch.ech.ech0020.v3;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2020-06-11T15:12:23.806311100")
public class InfostarPerson {
	public static final InfostarPerson $ = Keys.of(InfostarPerson.class);

	public Object id;
	@NotEmpty
	public ch.ech.ech0044.PersonIdentification personIdentification;
	public final NameInfo nameInfo = new NameInfo();
	public static class BirthInfo {
		public static final BirthInfo $ = Keys.of(BirthInfo.class);

		public final ch.ech.ech0011.BirthData birthData = new ch.ech.ech0011.BirthData();
		public ch.ech.ech0021.BirthAddonData birthAddonData;
	}
	public final BirthInfo birthInfo = new BirthInfo();
	public final MaritalInfo maritalInfo = new MaritalInfo();
	public final ch.ech.ech0011.NationalityData nationalityData = new ch.ech.ech0011.NationalityData();
	public List<PlaceOfOriginInfo> placeOfOriginInfo;
	public ch.ech.ech0011.DeathData deathData;
}