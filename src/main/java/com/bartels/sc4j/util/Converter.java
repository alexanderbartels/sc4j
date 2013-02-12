package com.bartels.sc4j.util;


public class Converter {

	public enum SupportedDataType {
		
		STRING(String.class),
		
		PRIMITIVE_LONG(long.class),
		LONG(Long.class),
		
		PRIMITIVE_INT(int.class),
		INTEGER(Integer.class),
		
		PRIMITIVE_SHORT(short.class),
		SHORT(Short.class),
		
		PRIMITVE_BYTE(byte.class),
		BYTE(Byte.class),
		
		PRIMITIVE_DOUBLE(double.class),
		DOUBLE(Double.class),
		
		PRIMITIVE_FLOAT(float.class),
		FLOAT(Float.class),
		
		PRIMITIVE_CHAR(char.class),
		CHAR(Character.class);
		
		private final Class<?> clazz;
		
		private SupportedDataType(final Class<?> clazz) {
			this.clazz = clazz;
		}
		
		public static SupportedDataType fromClazz(final Class<?> clazz) {
			for(SupportedDataType sdt : values()) {
				if(sdt.clazz.equals(clazz)) {
					return sdt;
				}
			}
			
			throw new IllegalArgumentException("The given data type is currently not supported");
		}
		
		public Class<?> getClazz() {
			return clazz;
		}
		
	}
	
	private Converter() { throw new AssertionError(); }
	
	public static Object convert(final String src, final Class<?> resultType) {
		SupportedDataType sdt = SupportedDataType.fromClazz(resultType);
		
		switch (sdt) {
			case PRIMITIVE_LONG:
			case LONG:
				return stringToLong(src);
			
			case PRIMITIVE_INT:
			case INTEGER:
				return stringToInteger(src);
			
			case PRIMITIVE_SHORT:
			case SHORT:
				return stringToShort(src);
				
			case PRIMITVE_BYTE:
			case BYTE:
				return stringToByte(src);
				
			case PRIMITIVE_DOUBLE:
			case DOUBLE:
				return stringToDouble(src);
				
			case PRIMITIVE_FLOAT:
			case FLOAT:
				return stringToFloat(src);
				
			case PRIMITIVE_CHAR:
			case CHAR:
				return stringToChar(src);
				
			default:
				return src;
		}
	}
	
	private static Object stringToChar(String src) {
		return src.charAt(0);
	}

	private static Object stringToFloat(String src) {
		return Float.parseFloat(src);
	}

	private static Object stringToDouble(String src) {
		return Double.parseDouble(src);
	}

	private static Object stringToByte(String src) {
		return Byte.parseByte(src);
	}

	private static Object stringToShort(String src) {
		return Short.parseShort(src);
	}

	/**
	 * converts the given string to a long
	 * 
	 * @param src
	 * 
	 * @return given string as long
	 */
	public static Long stringToLong(final String src) {
		return Long.parseLong(src);
	}
	
	
	/**
	 * converts the given string to a integer
	 * 
	 * @param src
	 * 
	 * @return given string as integer
	 */
	public static Integer stringToInteger(final String src) {
		return Integer.parseInt(src);
	}
}
