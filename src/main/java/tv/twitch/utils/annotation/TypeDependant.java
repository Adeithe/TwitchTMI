package tv.twitch.utils.annotation;

import java.lang.annotation.Documented;

/**
 * Marks that the value can vary depending on the data type.
 */
@Documented
public @interface TypeDependant {
	String value();
}
