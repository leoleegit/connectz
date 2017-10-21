package com.connectz.util.json;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.connectz.util.ReflectUtils;
import com.connectz.util.StringUtils;

public abstract class JsonObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(JsonObject.class);
	private String json;

	public JsonObject(String json) {
		this.setJson(json);
		if (json != null)
			json2Object(json, this);
	}

	/**
	 * obj1 merge to obj2
	 * 
	 * @param obj1
	 * @param obj2
	 * @return obj2
	 */
	public static JSONObject merge(JSONObject obj1, JSONObject obj2) {
		if (obj2 == null)
			return obj1;
		java.util.Iterator<?> it = obj2.keys();
		if(it!=null){
			for(;it.hasNext();){
				String name  = (String) it.next();
				Object value;
				try {
					value = obj2.getString(name);
					if (value != null
							&& (!(value instanceof String) || StringUtils
									.notNull((String) value))) {
						obj1.put(name, value);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logger.warn(e);
				}
				
			}
		}
		return obj1;
	}

	@SuppressWarnings("unchecked")
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		Class<?> type = null;
		sb.append("{");
		try {
			for (Class<?> clazz = getClass(); clazz != Object.class; clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();

				for (Field f : fs) {
					if (Modifier.isStatic(f.getModifiers()))
						continue;
					Object value = null;
					String name = f.getName();
					if (!"json".equals(name) && !"encode".equals(name)
							&& !"decode".equals(name)) {
						type = f.getType();
						Method m = ReflectUtils.getMethod(getClass(),
								new StringBuilder().append("get").append(name)
										.toString());
						if (m == null)
							m = ReflectUtils.getMethod(
									getClass(),
									new StringBuilder().append("is")
											.append(name).toString());
						if (m != null) {
							if (String.class == type) {
								value = (String) m.invoke(this, new Object[0]);
								if (StringUtils.notNull((String) value)) {
									value = encode((String) value);
								}
								if (StringUtils.notNull((String) value)) {
									value = "\"" + value + "\"";
								}
							} else if ((Integer.class == type)
									|| (Integer.TYPE == type)) {
								value = (Integer) m.invoke(this, new Object[0]);
							} else if ((Boolean.class == type)
									|| (Boolean.TYPE == type)) {
								value = (Boolean) m.invoke(this, new Object[0]);
							} else if ((Long.class == type)
									|| (Long.TYPE == type)) {
								value = (Long) m.invoke(this, new Object[0]);
							} else if ((Float.class == type)
									|| (Float.TYPE == type)) {
								value = (Float) m.invoke(this, new Object[0]);
							} else if (Timestamp.class == type) {
				            	Object o1 = m.invoke(this, new Object[0]);
				                value = o1!=null?String.valueOf(((Timestamp)o1).getTime()):null;
				              } else if (JsonObject.class.isAssignableFrom(type)) {
								Object o1 = m.invoke(this, new Object[0]);
								JsonObject jsonObj = o1 != null ? (JsonObject) o1
										: null;
								value = jsonObj != null ? jsonObj.toJson()
										: null;
							} else if (Collection.class.isAssignableFrom(type)) {
								List<Object> jsonObj = (List<Object>) m.invoke(
										this, new Object[0]);
								value = toJsonArray(jsonObj);
							} else if(JSONObject.class.isAssignableFrom(type)){
								Object o1 = m.invoke(this, new Object[0]);
								JSONObject jsonObj = o1 != null ? (JSONObject) o1
										: null;
								value = jsonObj != null ? jsonObj.toString()
										: null;
							}else {
								Object o1 = m.invoke(this, new Object[0]);
								Method m1 = o1 != null ? ReflectUtils
										.getMethod(o1.getClass(), "toJson",new Class<?>[0])
										: null;
								if (m1 != null) {
									try {
										value = m1.invoke(o1, new Object[0]);
									} catch (Exception e) {
										logger.warn(e);
									}
								} else
									value = toJson(name, o1);
							}
						}
						if (value != null
								&& (!(value instanceof String) || StringUtils
										.notNull((String) value))) {
							sb.append("\"").append(name).append("\"")
									.append(":").append(value).append(",");
						}
					}
				}
			}
		} catch (Throwable t) {
			logger.error("", t);
		}

		if (sb.lastIndexOf(",") == sb.length() - 1)
			sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public Object json2Object(String jsonStr, Object o) {
		JSONObject json = getJSONObject(jsonStr);
		if (json != null) {
			for (Class<?> clazz = o.getClass(); clazz != Object.class; clazz = clazz
					.getSuperclass()) {
				Field[] fs = clazz.getDeclaredFields();
				Class<?> type = null;
				for (Field f : fs) {
					String name = f.getName();
					Method m = ReflectUtils
							.getMethod(o.getClass(), new StringBuilder()
									.append("set").append(name).toString(),
									new Class<?>[] { f.getType() });
					if (m == null)
						m = ReflectUtils.getMethod(o.getClass(),
								new StringBuilder().append("is").append(name)
										.toString());
					if ((m != null) && (json.has(name))) {
						try {
							String value = json.getString(name);
							if (StringUtils.notNull(value)) {
								type = f.getType();
								Object args = null;
								if (String.class == type) {
									value = decode(value);
									args = value;
								} else if ((Integer.class == type)
										|| (Integer.TYPE == type))
									args = StringUtils.isNumeric(value) ? Integer
											.valueOf(value) : null;
								else if ((Boolean.class == type)
										|| (Boolean.TYPE == type))
									args = Boolean.valueOf(Boolean
											.parseBoolean(value));
								else if ((Long.class == type)
										|| (Long.TYPE == type))
									args = StringUtils.isNumeric(value) ? Long
											.valueOf(value) : null;
								else if ((Float.class == type)
										|| (Float.TYPE == type))
									args = Float.valueOf(value);
								else if (Timestamp.class == type)
					                args = StringUtils.isNumeric(value)?new Timestamp(Long.valueOf(value)):null;
								else if (JsonObject.class
										.isAssignableFrom(type)) {
									try {
										if (ReflectUtils
												.hasConstructor(
														type,
														new Class<?>[] { String.class }))
											args = ReflectUtils.newInstance(
													type,
													new Object[] { value });
									} catch (SecurityException e) {
										logger.warn(e);
										continue;
									} catch (IllegalArgumentException e) {
										logger.warn(e);
										continue;
									}
								} else if (Collection.class
										.isAssignableFrom(type)) {
									args = value;
									setListObject(name, args);
									continue;
								}else if(JSONObject.class.isAssignableFrom(type)){
									args = new JSONObject(value);
								}else {
									args = value;
									setObject(name, args);
									continue;
								}

								try {
									if (args != null)
										m.invoke(o, new Object[] { args });
								} catch (Exception e) {
									logger.warn(e);
								}
							}
						} catch (JSONException e) {
							logger.error("", e);
						}
					}
				}
			}
		}
		return o;
	}

	public abstract String toJson(String name, Object o1);

	public abstract String encode(String str);

	public abstract String decode(String str);

	public abstract void setListObject(String name, Object args);

	public abstract void setObject(String name, Object args);

	public static JSONObject getJSONObject(String str) {
		if (str == null || (str != null && str.trim().length() == 0)) {
			return null;
		}
		if (!str.startsWith("{")) {
			logger.warn(" A JSONObject text must begin with '{' at character 1 :\n"
					+ str);
			return null;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(str);
		} catch (JSONException e) {
			logger.warn(str, e);
			return null;
		}
		return jsonObject;
	}

	public static String lineToJson(Map<String, String> keyMaps,
			String[] commands) {
		JSONObject jsonObject = null;
		jsonObject = new JSONObject();
		String name = null;
		for (String s : commands) {
			if (keyMaps.containsKey(s)) {
				name = keyMaps.get(s);
				continue;
			}
			if (name == null)
				continue;
			try {
				if (jsonObject.has(s)) {
					jsonObject.append(name, s);
				} else {
					jsonObject.accumulate(name, s);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.warn("", e);
			}
		}
		return jsonObject.toString();
	}

	public static String toJsonArray(List<?> list) {
		if (list == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		if (list != null)
			for (Iterator<?> i$ = list.iterator(); i$.hasNext();) {
				Object o = i$.next();
				if (o instanceof JsonObject)
					sb.append(((JsonObject) o).toJson()).append(",");
				else {
					Method m = ReflectUtils.getMethod(o.getClass(), "toJson",
							new Class<?>[0]);
					if (m != null) {
						try {
							sb.append(m.invoke(o, new Object[0])).append(",");
						} catch (Exception e) {
							logger.warn(e);
						}
					} else
						sb.append(o.toString()).append(",");
				}
			}

		if (sb.lastIndexOf(",") == sb.length() - 1)
			sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	public static JSONArray getJSONArray(String str) {
		if (str == null || (str != null && str.trim().length() == 0)) {
			return null;
		}
		if (!str.startsWith("[")) {
			logger.warn(" A JSONArray text must begin with '[' at character 1 :\n"
					+ str);
			return null;
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(str);
		} catch (JSONException e) {
			logger.warn(str, e);
			return null;
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T generateOne(Class<T> clazz, String json) {
		Object args = null;
		try {
			if (json != null
					&& ReflectUtils.hasConstructor(clazz,
							new Class<?>[] { String.class }))
				args = ReflectUtils.newInstance(clazz, new Object[] { json });
		} catch (SecurityException e) {
			logger.warn(e);
		} catch (IllegalArgumentException e) {
			logger.warn(e);
		}
		return (T) args;
	}

	public static <T> List<T> generateArray(Class<T> clazz, JSONArray array) {
		List<T> at = new ArrayList<T>();
		if (array != null) {
			for (int index = 0; index < array.length(); index++) {
				try {
					T t = generateOne(clazz, array.getString(index));
					at.add(t);
				} catch (JSONException e) {
					logger.error("", e);
				}
			}
		}
		return at;
	}
	
	public static <T> List<T> generateArray(Class<T> clazz, String array) {
		return generateArray(clazz,getJSONArray(array));
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
