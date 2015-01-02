package com.sanjay900.nmsUtil.v1_8_R1;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_8_R1.EntityTypes;

public class CustomEntityType {
	@SuppressWarnings("rawtypes")
	public void registerCustomEntity(Class oclass, String s, int i) throws Exception {
		putInPrivateStaticMap("c", s, oclass);
		putInPrivateStaticMap("d", oclass, s);
		putInPrivateStaticMap("e", Integer.valueOf(i), oclass);
		putInPrivateStaticMap("f", oclass, Integer.valueOf(i));
		putInPrivateStaticMap("g", s, Integer.valueOf(i));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void putInPrivateStaticMap(String string, Object key, Object value) {
			Field f;
			try {
				f = EntityTypes.class.getDeclaredField(string);
				f.setAccessible(true);
				Map m = (Map) f.get(null);
				m.put(key, value);
				f.set(null, m);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
	
	}
}

