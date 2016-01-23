package grid;

import java.util.ArrayList;

/**
 * Utilities class with singletons methods
 * @author Paride Casulli
 * @author Lorenzo La Banca
 */
public class Utils {
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
}
