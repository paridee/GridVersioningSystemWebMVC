package grid;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import grid.entities.GridElement;

/**
 * Utilities class with singletons methods
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */
public class Utils {
	
	private static final Logger logger	=	LoggerFactory.getLogger(Utils.class);
	
	/**
	 * Given 2 arraylists adds the non-duplicate elements in the first one
	 * @param one first list (will be also the result of merge operation)
	 * @param two second list to be merged
	 */
	@SuppressWarnings("unchecked")
	public static void mergeLists(@SuppressWarnings("rawtypes") ArrayList one, @SuppressWarnings("rawtypes") ArrayList two){
		for(int i=0;i<two.size();i++){
			if(!one.contains(two.get(i))){
				one.add(two.get(i));
			}
		}
	}
	
	/**
	 * Converts hashmap of <String,GridElement> to an hashmap <String,Object>
	 * @param map input map
	 * @return output map
	 */
	public static HashMap<String,Object> convertHashMap(HashMap<String,GridElement> map){
		HashMap<String,Object> retMap	=	new HashMap<String,Object>();
		java.util.Iterator<String> anIterator				=	map.keySet().iterator();
		while(anIterator.hasNext()){
			String key	=	anIterator.next();
			retMap.put(key, map.get(key));
		}
		return retMap;
	}
}
