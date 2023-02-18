/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.util.Arrays;
import java.util.List;

import io.github.mmm.base.i18n.Localizable;

/**
 * Abstract base implementation of {@link CliProperty}.
 *
 * @param <V> type of the contained {@link #get() value}.
 * @since 1.0.0
 */
public abstract class AbstractCliProperty<V> implements CliProperty<V> {

  private final Localizable help;

  private final List<String> names;

  private final boolean mandatory;

  private final boolean option;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public AbstractCliProperty(Localizable help, boolean mandatory, String... names) {

    super();
    this.help = help;
    this.mandatory = mandatory;
    this.names = Arrays.asList(names);
    boolean hasOption = false;
    boolean valid = true;
    if ((names == null) || (names.length < 1)) {
      valid = false;
    } else {
      for (String name : names) {
        if (name == null) {
          valid = false;
          break;
        } else if (name.startsWith("-")) {
          hasOption = true;
        } else {
          valid = (names.length == 1);
          break;
        }
      }
    }
    if (!valid) {
      throw new IllegalArgumentException("" + this.names);
    }
    this.option = hasOption;
  }

  @Override
  public Localizable getHelp() {

    return this.help;
  }

  @Override
  public List<String> getNames() {

    return this.names;
  }

  @Override
  public boolean isOption() {

    return this.option;
  }

  @Override
  public boolean isMandatory() {

    return this.mandatory;
  }

  @Override
  public String toString() {

    String result = getName();
    String value = getAsString();
    if (value != null) {
      result = result + "=" + value;
    }
    return result;
  }

}
