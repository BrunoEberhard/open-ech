package ch.ech.ech0147.t0;

import java.util.List;
import org.minimalj.model.annotation.NotEmpty;
import javax.annotation.Generated;
import org.minimalj.model.Keys;

@Generated(value="org.minimalj.metamodel.generator.ClassGenerator", date = "2019-09-24T07:31:03.222")
public class Folder {
	public static final Folder $ = Keys.of(Folder.class);

	public Object id;
	@NotEmpty
	public FolderTitles folderTitles;
	public Documents documents;
	public List<ApplicationCustom> applicationCustom;
}