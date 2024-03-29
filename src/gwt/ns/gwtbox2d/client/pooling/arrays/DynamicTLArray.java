package gwt.ns.gwtbox2d.client.pooling.arrays;

import gwt.ns.gwtbox2d.client.pooling.notThreadLocal;

import java.util.HashMap;


public abstract class DynamicTLArray<I> {
	
	//XXX change for gwt
	private static class TLHashMap<K, V> extends notThreadLocal<HashMap<K, V>>{
		protected HashMap<K, V> initialValue(){
			return new HashMap<K, V>();
		}
	}
//	private static class TLHashMap<K, V> extends ThreadLocal<HashMap<K, V>>{
//		protected HashMap<K, V> initialValue(){
//			return new HashMap<K, V>();
//		}
//	}
	
	private final TLHashMap<Integer, I[]> tlMap = new TLHashMap<Integer, I[]>();
	
	public I[] get( int argLength){
		assert(argLength > 0);
		
		HashMap<Integer, I[]> map = tlMap.get();
		
		if(!map.containsKey(argLength)){
			map.put(argLength, getInitializedArray(argLength));
		}
		
		assert(map.get(argLength).length == argLength) : "Array not built of correct length";
		return map.get(argLength);
	}
	
	public void recycle( I[] argArray){
		
	}
	
	protected abstract I[] getInitializedArray(int argLength);
}
