package quaternary.halogen.block;

public enum EnumHaloBlockType {
	FULLCUBE(true, false, 255),
	NONFULL_SOLID(false, false, 0),
	ETHEREAL(false, true, 0);
	
	/** Is this type an opaque full cube? (like stone) */
	public final boolean OPAQUE_FULL_CUBE;
	
	/** Can entities walk through this block? (like a torch) */
	public final boolean PASSABLE;
	
	/** How much light does this block block? (think of leaves or water) */
	public final int OPACITY;
	
	EnumHaloBlockType(boolean opaque, boolean passable, int opacity) {
		OPAQUE_FULL_CUBE = opaque;
		PASSABLE = passable;
		OPACITY = opacity;
	}
}
