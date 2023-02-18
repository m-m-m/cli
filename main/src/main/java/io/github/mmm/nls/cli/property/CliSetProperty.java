/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link Set}.
 *
 * @param <E> type of the elements contained in the {@link List} {@link #get() value}.
 * @since 1.0.0
 */
public class CliSetProperty<E> extends CliCollectionProperty<E, Set<E>> {

  private Set<E> value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param property the {@link CliProperty} for a single element (used as mapper to parse from {@link String}).
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliSetProperty(Localizable help, CliProperty<E> property, boolean mandatory, String... names) {

    super(help, property, mandatory, names);
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param property the {@link CliProperty} for a single element (used as mapper to parse from {@link String}).
   * @param names the {@link #getNames() names}.
   */
  public CliSetProperty(Localizable help, CliProperty<E> property, Set<E> defaultValue, String... names) {

    super(help, property, false, names);
    this.value = defaultValue;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<Set<E>> getValueClass() {

    return (Class) Set.class;
  }

  @Override
  public Set<E> get() {

    return this.value;
  }

  @Override
  public void set(Set<E> value) {

    this.value = value;
  }

  @Override
  public void add(E item) {

    if (this.value == null) {
      this.value = new HashSet<>();
    }
    this.value.add(item);
  }

}
