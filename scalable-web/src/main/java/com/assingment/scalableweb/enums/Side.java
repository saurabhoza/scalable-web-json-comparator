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
		switch (this) {
		case LEFT:
			return "Left";
		case RIGHT:
			return "Right";
		}
		throw new Error("An error occurred while trying to get the correct side.");
	}
}
