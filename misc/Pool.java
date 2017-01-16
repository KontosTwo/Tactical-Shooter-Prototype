package com.mygdx.misc;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.mygdx.misc.Pool.Poolable;;
/**
 * Standard pool implementation
 * . Mitigate memory fragmentation by storing created objects in a primitive array
 * . Avoid object creation by reusing already-created objects
 *  
 * @author Vincent Li
 */
public class Pool <E extends Poolable>{
	public interface Poolable{
		/**
		 * Resets all of this object's fields. This is the alternative
		 * to creating a new object.
		 */
		public void resetAttributes();
	}
	
	private final ArrayDeque<E> stored;
	private final Set<E> inUse;
	
	public Pool(int estimatedSize){
		stored = new ArrayDeque<E>(estimatedSize);
		inUse = new HashSet<E>();
	}
	
}
