package  ch.openech.model;

public enum EchSchema0148  implements EchSchemaVersion {

	_1_0;
	
	private int version;
	private int minorVersion;
	
	private EchSchema0148() {
		String name = name();
		int pos = name.indexOf("_", 1);
		if (pos > 0) {
			version = Integer.parseInt(name.substring(1, pos));
			minorVersion = Integer.parseInt(name.substring(pos+1));
		}
	}

	@Override
	public int getSchemaNumber() {
		return 148;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getMinorVersion() {
		return minorVersion;
	}
	
}
