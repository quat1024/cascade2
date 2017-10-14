package quaternary.halogen.util;

public class Utils {
	//Copy of the one from MathHelper, but not clientside only (WHY IS IT SIDEONLY???)
	public static int fastFloor(double blah) {
		return (int) (blah + 1024.0D) - 1024;
	}
	
	public static int min(int... numbers) {
		int i=Integer.MAX_VALUE;
		for(int j : numbers) {
			if(j < i) i = j;
		}
		return i;
	}
}
