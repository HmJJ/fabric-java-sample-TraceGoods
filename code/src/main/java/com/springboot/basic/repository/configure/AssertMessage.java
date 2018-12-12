package com.springboot.basic.repository.configure;

public abstract class AssertMessage {

	public static final String NotNull = "[Assertion failed] - this argument is required; it must not be null";
	public static final String NotEmpty = "[Assertion failed] - this Object must not be empty; it must contain at least one entry";
	public static final String HasText = "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank";
	public static final String IsTrue = "[Assertion failed] - this expression must be true";
	public static final String State = "[Assertion failed] - this state invariant must be true";
	public static final String IsNull = "[Assertion failed] - the object argument must be null";
	public static final String HasLength = "[Assertion failed] - this String argument must have length; it must not be null or empty";
	public static final String DoesNotContain = "[Assertion failed] - this String argument must not contain the substring";

}
