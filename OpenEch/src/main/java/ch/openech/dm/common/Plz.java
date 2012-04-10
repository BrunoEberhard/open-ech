package ch.openech.dm.common;

import java.io.Serializable;

import ch.openech.mj.util.StringUtils;


public class Plz implements Comparable<Plz>, Serializable, Cloneable {

	public int onrp;
	public int typ;
	public int postleitzahl;
	public int zusatzziffern;
	public String ortsbezeichnung;
	public String kanton;
	
	
	@Override
	protected Plz clone() {
		Plz clone = new Plz();
		clone.onrp = this.onrp;
		clone.typ = this.typ;
		clone.postleitzahl = this.postleitzahl;
		clone.zusatzziffern = this.zusatzziffern;
		clone.ortsbezeichnung = this.ortsbezeichnung;
		clone.kanton = this.kanton;
		return clone;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(postleitzahl);
		if (zusatzziffern != 0) {
			s.append(' ');
			s.append(StringUtils.padLeft("" + zusatzziffern, 2, '0'));
		}
		return s.toString();
	}
	
	public String toStringReadable() {
		StringBuilder s = new StringBuilder();
		s.append("Plz = " +  postleitzahl);
		s.append(", Zusatzziffern = " +  zusatzziffern);
		s.append(", ONRP = " +  onrp);
		return s.toString();
	}
	
	public String toStringId() {
		StringBuilder s = new StringBuilder();
		s.append(StringUtils.padLeft("" + postleitzahl, 4, '0'));
		s.append(StringUtils.padLeft("" + zusatzziffern, 2, '0'));
		s.append(StringUtils.padLeft("" + onrp, 5, '0'));
		return s.toString();
	}
	
	@Override
	public int compareTo(Plz o) {
		return order().compareTo(o.order());
	}

	private Integer order() {
		return postleitzahl * 100 + zusatzziffern;
	}

}
