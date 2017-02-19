package com.stedi.gyrshot.other;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A helper marker for methods that are called from LayersView inner thread.
 * Used for layers, to better understand multithreading in LayersView
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface LayersThread {
}
