package com.springboot.basic.repository.configure;

public abstract class SuppressWarningsParams {

	/**
	 *  all to suppress all warnings
	 */
	public static final String ALL = "all";

	/**
	 * boxing to suppress warnings relative to boxing/unboxing operations
	 */
	public static final String BOXING = "boxing";

	/**
	 * cast to suppress warnings relative to cast operations
	 */
	public static final String CAST = "cast";

	/**
	 * dep-ann to suppress warnings relative to deprecated annotation
	 */
	public static final String DEP_ANN = "dep-ann";

	/**
	 * deprecation to suppress warnings relative to deprecation
	 */
	public static final String DEPRECATION = "deprecation";

	/**
	 * fallthrough to suppress warnings relative to missing breaks in switch statements
	 */
	public static final String FALLTHROUGH = "fallthrough";

	/**
	 * finally to suppress warnings relative to finally block that don’t return
	 */
	public static final String FINALLY = "finally";

	/**
	 * hiding to suppress warnings relative to locals that hide variable
	 */
	public static final String HIDING = "hiding";

	/**
	 * incomplete-switch to suppress warnings relative to missing entries in a switch statement (enum case)
	 */
	public static final String INCOMPLETE_SWITCH = "incomplete-switch";

	/**
	 * nls to suppress warnings relative to non-nls string literals
	 */
	public static final String NLS = "nls";

	/**
	 * null to suppress warnings relative to null analysis
	 */
	public static final String NULL = "null";

	/**
	 * rawtypes to suppress warnings relative to un-specific types when using generics on class params
	 */
	public static final String RAWTYPES = "rawtypes";

	/**
	 * restriction to suppress warnings relative to usage of discouraged or forbidden references
	 */
	public static final String RESTRICTION = "restriction";

	/**
	 * serial to suppress warnings relative to missing serialVersionUID field for a serializable class
	 */
	public static final String SERIAL = "serial";

	/**
	 * static-access to suppress warnings relative to incorrect static access
	 */
	public static final String STATIC_ACCESS = "static-access";

	/**
	 * synthetic-access to suppress warnings relative to unoptimized access from inner classes
	 */
	public static final String SYNTHETIC_ACCESS = "synthetic-access";

	/**
	 * unchecked to suppress warnings relative to unchecked operations
	 */
	public static final String UNCHECKED = "unchecked";

	/**
	 * unqualified-field-access to suppress warnings relative to field access unqualified
	 */
	public static final String UNQUALIFIED_FIELD_ACCESS = "unqualified-field-access";

	/**
	 * unused to suppress warnings relative to unused code
	 */
	public static final String UNUSED = "unused";

}
