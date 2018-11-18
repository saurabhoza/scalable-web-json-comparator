package com.assingment.scalableweb.enums;

/**
 * 
 * Enum with the possible sides.
 *
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 */
public enum Side {
	LEFT, RIGHT;

	@Override
	public String toString() {
		return this == LEFT ? "Left" : "Right";
	}
}
