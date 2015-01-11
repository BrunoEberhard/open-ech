package ch.openech.util;

import java.io.Serializable;

import org.minimalj.util.StringUtils;


public class Plz implements Comparable<Plz>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public int onrp;
	public int typ;
	public int postleitzahl;
	public int zusatzziffern;
	public String ortsbezeichnung;
	public String kanton;

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

	@Override
	public int hashCode() {
		return onrp;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plz other = (Plz) obj;
		return other.onrp == onrp;
	}

}
