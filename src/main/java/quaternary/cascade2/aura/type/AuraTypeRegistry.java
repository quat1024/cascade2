package quaternary.cascade2.aura.type;

import java.util.HashMap;

//This is not an IForgeRegistry but LOL FUCK YOU.
//I'll make it one if I can figure them out.
//There's no resources for them anywhere :v
public class AuraTypeRegistry {
	static HashMap<String, AuraType> typeMap = new HashMap<>();
	
	public static void registerType(AuraType type) {
		typeMap.put(type.getName(), type);
	}
	
	//this is why I'm using a hashmap, basically
	public static AuraType fromString(String name) {
		return typeMap.get(name);
	}
	
	public static AuraType[] allTypes() {
		return (AuraType[]) typeMap.values().toArray();
	}
}
