package org.code.core.id;

import java.util.UUID;

/**
 * UUID
 * @author bbaiggey
 *
 */
public final class SimpleUuidGenerator implements IdGenerator<String> {

	/**
	 * 
	 */
	@Override
	public String getNextId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
     * 
     */
    public static void main(String args[]) {
    	SimpleUuidGenerator g = new SimpleUuidGenerator();
    	for(int i = 0; i < 100; i++) {
    		System.out.println(g.getNextId());
    	}
    }
}