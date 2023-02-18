/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link String}.
 */
public class CliStringProperty extends AbstractCliProperty<String> {

  private String value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliStringProperty(Localizable help, boolean mandatory, String... names) {

    super(help, mandatory, names);
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param names the {@link #getNames() names}.
   */
  public CliStringProperty(Localizable help, String defaultValue, String... names) {

    super(help, false, names);
    this.value = defaultValue;
  }

  @Override
  public Class<String> getValueClass() {

    return String.class;
  }

  @Override
  public boolean isEmpty() {

    if (this.value == null) {
      return true;
    } else {
      return this.value.isEmpty();
    }
  }

  @Override
  public String get() {

    return this.value;
  }

  @Override
  public void set(String value) {

    this.value = value;
  }

  @Override
  public void setFromString(String value) {

    this.value = value;
  }

}
