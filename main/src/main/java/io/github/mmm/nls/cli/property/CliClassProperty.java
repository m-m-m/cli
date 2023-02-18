/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;
import io.github.mmm.nls.cli.exception.CliTypeNotExtendingException;

/**
 * Implementation of {@link CliProperty} for {@link Class}.
 *
 * @param <T> type of {@link #getBound() upper bound}.
 * @since 1.0.0
 */
public class CliClassProperty<T> extends AbstractCliProperty<Class<? extends T>> {

  private final Class<T> bound;

  private Class<? extends T> value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param bound the {@link #getBound() upper bound}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliClassProperty(Localizable help, Class<T> bound, boolean mandatory, String... names) {

    super(help, mandatory, names);
    this.bound = bound;
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param bound the {@link #getBound() upper bound}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param names the {@link #getNames() names}.
   */
  public CliClassProperty(Localizable help, Class<T> bound, Class<? extends T> defaultValue, String... names) {

    this(help, bound, false, names);
    this.value = defaultValue;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<Class<? extends T>> getValueClass() {

    return (Class) Class.class;
  }

  /**
   * @return the upper bound of the value. The {@link #get() value} has to extend this {@link Class}. Use
   *         {@link Object}.class for no upper bound.
   */
  public Class<T> getBound() {

    return this.bound;
  }

  @Override
  public Class<? extends T> get() {

    return this.value;
  }

  @Override
  public void set(Class<? extends T> value) {

    assert ((this.bound == null) || (value == null) || (this.bound.isAssignableFrom(value)));
    this.value = value;
  }

  @Override
  public String getAsString() {

    if (this.value == null) {
      return null;
    }
    return this.value.getName();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void setFromString(String value) {

    if (value == null) {
      this.value = null;
      return;
    }
    try {
      Class<?> clazz = Class.forName(value);
      if ((this.bound != null) && !this.bound.isAssignableFrom(clazz)) {
        throw new CliTypeNotExtendingException(clazz, this.bound);
      }
      this.value = (Class<? extends T>) clazz;
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(value, e);
    }
  }

}
