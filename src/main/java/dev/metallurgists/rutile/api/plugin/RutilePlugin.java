package dev.metallurgists.rutile.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RutilePlugin {
    /**
     * A list of mod ids that are needed for a plugin to load
     */
    String[] value() default {};
}
