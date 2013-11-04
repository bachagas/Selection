package garbagecollection;

public class CollectGarbage {
	public static void collectGarbage() {
		Runtime rt = Runtime.getRuntime();
		long isFree = rt.freeMemory();
		long wasFree;
		do {
			wasFree = isFree;
			rt.runFinalization();
			rt.gc();
			isFree = rt.freeMemory();
		}while (isFree > wasFree);
	}
}
