package ch.openech.model.estate;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

/**
 * Anweisung: Basiskomponente zur Abbildung von Bearbeitungsanweisungen an den
 * Empfänger muss an dieser Stelle definiert werden, da diese im Nachrichtentyp
 * eCH-0147T2 um Dossiers, Dokumente und Adressen erweitert wird
 * (Verschachtelung)
 * 
 * 147T2
 * 
 *
 */
public class Directive {

	public static final Directive $ = Keys.of(Directive.class);
	
	public Object id;

	// UUID: Universally Unique Identifier der Anweisung. Referenz des Objekts, nicht der Nachricht.
	@NotEmpty @Size(36)
	public String uuid;

	// eCH-0039:directiveInstruction
	public DirectiveInstruction instruction;
	// eCH-0039:priorityType
	public Priority priority;
	
	// eCH-0039:titlesType
	// Titel: Benennung von Tätigkeit und Gegenstand des Geschäftsvorfalls.
	// public Titles titles;
	
	// Bearbeitungsfrist: Tag, an dem die Aktivität erledigt sein soll.
	public LocalDate deadline;
	
	// Leistungsidentifikation: Identifikation der Leistung gemäss eCH-0070 Leistungsinventar eGov CH.
	@Size(255) // Grösse in eCH unbekannt
	public String serviceId;
	
	/*
	// eCH-0039:commentsType
	public Comments comments;
	
	// eCH-0147T0:dossiersType
	public Dossiers dossiers;
	
	// eCH-0147T0:documentsType
	public Documents documents;
	
	// eCH-0147T0:addressesType
	public addresses addresses;

	// eCH-0147T0:applicationCustomType
	public List<applicationCustom> applicationCustom;
	*/
	
}


