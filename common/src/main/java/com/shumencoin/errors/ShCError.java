package com.shumencoin.errors;

import java.util.HashMap;
import java.util.Map;

public enum ShCError {
	NO_ERROR(0),
	MINING_JOB_NOT_FOUND(1),
	INCORRECT_BLOCK_HASH(2),
	BLOCK_ALREADY_MINED(3),
	INCORRECT_PREC_BLOCK_HASH(4),
	INCORRECT_BLOCK_DIFFICULTY(5),
	SELF_CONNECTION(6),
	DIFFERENT_CHAIN_ID(6),

	NOT_IMPLEMENTED(Integer.MAX_VALUE - 1),
	UNKNOWN(Integer.MAX_VALUE);

	private int value;

	public int getValue() {
		return this.value;
	}

	private ShCError(int value) {
		this.value = value;
	}

	private static final Map<Integer, ShCError> typesByValue = new HashMap<Integer, ShCError>();

	static {
		for (ShCError type : ShCError.values()) {
			typesByValue.put(type.value, type);
		}
	}

	public static ShCError forValue(int value) {
		return typesByValue.get(value);
	}

	public boolean isError() {
		return (NO_ERROR != ShCError.forValue(this.getValue()));
	}
}
