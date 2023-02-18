/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.util.Collection;
import java.util.Objects;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link Collection}.
 *
 * @param <E> type of the elements contained in the {@link Collection} {@link #get() value}.
 * @param <C> type of the {@link Collection} {@link #get() value}.
 * @since 1.0.0
 */
public abstract class CliCollectionProperty<E, C extends Collection<E>> extends AbstractCliProperty<C> {

  private final CliProperty<E> property;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param property the {@link CliProperty} for a single element (used as mapper to parse from {@link String}).
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() options}.
   */
  public CliCollectionProperty(Localizable help, CliProperty<E> property, boolean mandatory, String... names) {

    super(help, mandatory, names);
    Objects.requireNonNull(property, "property");
    this.property = property;
  }

  @Override
  public void setFromString(String value) {

    String[] elements = value.split("\\s*,\\s*");
    for (String item : elements) {
      addFromString(item);
    }
  }

  @Override
  public boolean isEmpty() {

    C value = get();
    if (value == null) {
      return true;
    } else {
      return value.isEmpty();
    }
  }

  /**
   * @param item the element to add.
   */
  public abstract void add(E item);

  /**
   * @param item the element to add given a {@link String} to parse.
   */
  public void addFromString(String item) {

    this.property.setFromString(item);
    add(this.property.get());
  }

}
