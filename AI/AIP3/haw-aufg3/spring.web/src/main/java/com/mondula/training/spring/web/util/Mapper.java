package com.mondula.training.spring.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * this is a simple class to map from entities to a map with different keys e.g. 
 * for JSON serialization (allows to hide internals)
 * Usage:
 * Mapper<User> mapper = new Mapper<User>() // Java 8 convenience!
 *     			.map("name", User::getUsername)
 *     			.map("id", User::getId)
 *     			.map("label", u -> "Label: "+u.getUsername());
 * result = mapper.apply(userService.getUsers());
 * 
 * @author mondula
 *
 * @param <T> the source type to map from
 */
public class Mapper<T> {
	private class Mapping {
		private final String name;
		private final Function<T, Object> map;
		public Mapping(String name, Function<T, Object> map) {
			this.name 	= name;
			this.map 	= map;
		}
	}
	private final List<Mapping> mappings = new ArrayList<Mapping>();

	public Mapper<T> map(String name, Function<T,Object> map) {
		mappings.add(new Mapping(name,map));
		return this;
	}

	public List<Map<String,? extends Object>> apply(List<T> sources) {
		List<Map<String,? extends Object>> result = new ArrayList<Map<String,? extends Object>>();
		for(T src: sources) result.add(apply(src));
		return result;
	}

	public Map<String,? extends Object> apply(T source) {
		Map<String, Object> target = new HashMap<String, Object>();
		for(Mapping m: mappings) {
			target.put(m.name, m.map.apply(source));
		}
		return target;
	}

}
