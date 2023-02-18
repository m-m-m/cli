/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.util.List;

import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.cli.CliArgument;
import io.github.mmm.nls.cli.exception.CliArgumentMandatoryException;

/**
 * Interface for a property configured via {@link CliArgument}.
 *
 * @param <V> type of the contained {@link #get() value}.
 */
public interface CliProperty<V> {

  /**
   * @return the {@link Localizable} describing this {@link CliProperty}.
   */
  Localizable getHelp();

  /**
   * @return the canonical name of this {@link CliProperty}.
   */
  default String getName() {

    return getNames().get(0);
  }

  /**
   * @return the {@link List} with the names of this property (e.g. "--help" and "-h" for a
   *         {@link io.github.mmm.cli.CliOption} or "file" for a parameter {@link io.github.mmm.cli.CliValue}). The
   *         first name from the {@link List} is the canonical name. Options may have additional names (aliases) while
   *         parameters must have exactly one name.
   */
  List<String> getNames();

  /**
   * @return {@code true} if this {@link CliProperty} is an option (corresponds to a
   *         {@link io.github.mmm.cli.CliOption}), {@code false} otherwise (if a parameter).
   */
  boolean isOption();

  /**
   * @return the {@link Class} reflecting the type of the {@link #get() value}.
   */
  Class<V> getValueClass();

  /**
   * @return {@code true} if required, {@code false} otherwise.
   */
  boolean isMandatory();

  /**
   * @return {@code true} if the value of this property is valid, {@code false} otherwise.
   */
  default boolean isValid() {

    if (isMandatory()) {
      return !isEmpty();
    }
    return true;
  }

  /**
   * @return {@code true} if the {@link #get() value} of this property is empty (e.g. {@code null}), {@code false}
   *         otherwise.
   */
  default boolean isEmpty() {

    return get() == null;
  }

  /**
   * @return the current value.
   */
  V get();

  /**
   * @return the {@link #get() value} as {@link String}.
   */
  default String getAsString() {

    V value = get();
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  /**
   * @param value the new value.
   */
  void set(V value);

  /**
   * @param value the new value as {@link String} to parse.
   */
  void setFromString(String value);

  /**
   * Validates the {@link #get() value} of this property.
   *
   * @throws RuntimeException if the value is invalid.
   */
  default void validate() {

    if (isMandatory() && isEmpty()) {
      throw new CliArgumentMandatoryException(getName());
    }
  }

}
