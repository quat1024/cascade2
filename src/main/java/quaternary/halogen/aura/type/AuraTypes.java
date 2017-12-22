package quaternary.halogen.aura.type;

import com.google.common.collect.HashBiMap;

public class AuraTypes {
	public static final AuraType NORMAL = new AuraTypeNormal();
	
	public static final HashBiMap<AuraType, String> typeNameMap = HashBiMap.create();
	
	public static AuraType fromName(String name) {
		return typeNameMap.inverse().get(name);
	}
	
	//How to java enum tutorial
	public static AuraType[] values() {
		return (AuraType[]) typeNameMap.keySet().toArray();
	}
	
	static {
		typeNameMap.put(NORMAL, NORMAL.getName());
	}
}
