package quaternary.halogen.aura.type;

public class AuraTypes {
	public static final AuraTypeNormal NORMAL = new AuraTypeNormal();
	
	//TODO: some real implementation
	public static AuraType fromString(String name) {
		if(name.equals(NORMAL.getName())) return NORMAL;
		return null;
	}
}
