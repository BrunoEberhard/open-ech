package  ch.openech.model;

public enum EchSchema0020 implements EchSchemaVersion {

	_1_0, _1_1, _2_0, _2_1, _2_2, _2_3;
	
	private int version;
	private int minorVersion;
	
	private EchSchema0020() {
		String name = name();
		int pos = name.indexOf("_", 1);
		if (pos > 0) {
			version = Integer.parseInt(name.substring(1, pos));
			minorVersion = Integer.parseInt(name.substring(pos+1));
		}
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public int getMinorVersion() {
		return minorVersion;
	}

	@Override
	public int getSchemaNumber() {
		return 20;
	}
	
}
