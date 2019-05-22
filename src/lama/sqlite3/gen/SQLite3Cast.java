package lama.sqlite3.gen;

import java.util.Optional;

import lama.sqlite3.ast.SQLite3Constant;
import lama.sqlite3.schema.SQLite3DataType;

public class SQLite3Cast {
	
	public static Optional<Boolean> isTrue(SQLite3Constant value) {
		SQLite3Constant numericValue;
		if (value.getDataType() == SQLite3DataType.NULL) {
			return Optional.empty();
		}
		if (value.getDataType() == SQLite3DataType.TEXT || value.getDataType() == SQLite3DataType.BINARY) {
			numericValue = castToNumeric(value);
		} else {
			numericValue = value;
		}
		assert numericValue.getDataType() != SQLite3DataType.TEXT : numericValue + "should have been converted";
		switch (numericValue.getDataType()) {
		case INT:
			return Optional.of(numericValue.asInt() != 0);
		case REAL:
			return Optional.of(numericValue.asDouble() != 0);
		default:
			throw new AssertionError(numericValue);
		}
	}
	
	/**
	 * Applies numeric affinity to a value.
	 */
	public static SQLite3Constant castToNumeric(SQLite3Constant value) {
		if (value.getDataType() == SQLite3DataType.BINARY) {
			String text = new String(value.asBinary());
			value = SQLite3Constant.createTextConstant(text);
		}
		switch (value.getDataType()) {
		case NULL:
			return SQLite3Constant.createNullConstant();
		case INT:
		case REAL:
			return value;
		case TEXT:
			String asString = value.asString();
			while (startsWithWhitespace(asString)) {
				asString = asString.substring(1);
			}
			if (!asString.isEmpty() && unprintAbleCharThatLetsBecomeNumberZero(asString)) {
				return SQLite3Constant.createIntConstant(0);
			}
			for (int i = asString.length(); i >= 0; i--) {
				try {
					double d = Double.valueOf(asString.substring(0, i));
					if (d == (long) d && (!asString.toUpperCase().contains("E") || d == 0)) {
						return SQLite3Constant.createIntConstant(Long.parseLong(asString.substring(0, i)));
					} else {
						return SQLite3Constant.createRealConstant(d);
					}
				} catch (Exception e) {

				}
			}
			return SQLite3Constant.createIntConstant(0);
		default:
			throw new AssertionError(value);
		}
	}

	private static boolean startsWithWhitespace(String asString) {
		if (asString.isEmpty()) {
			return false;
		}
		char c = asString.charAt(0);
		switch (c) {
		case ' ':
		case '\t':
		case 0x0b:
		case '\f':
		case '\n':
		case '\r':
			return true;
		default:
			return false;
		}
	}

	private final static byte FILE_SEPARATOR = 0x1c;
	private final static byte GROUP_SEPARATOR = 0x1d;
	private final static byte RECORD_SEPARATOR = 0x1e;
	private final static byte UNIT_SEPARATOR = 0x1f;
	private final static byte SYNCHRONOUS_IDLE = 0x16;


	private static boolean unprintAbleCharThatLetsBecomeNumberZero(String s) {
		// non-printable characters are ignored by Double.valueOf
		for (int i = 0; i < s.length(); i++) {
			char charAt = s.charAt(i);
			if (!Character.isISOControl(charAt) && !Character.isWhitespace(charAt)) {
				return false;
			}
			switch (charAt) {
			case GROUP_SEPARATOR:
			case FILE_SEPARATOR:
			case RECORD_SEPARATOR:
			case UNIT_SEPARATOR:
			case SYNCHRONOUS_IDLE:
				return true;
			}

			if (Character.isWhitespace(charAt)) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}

}