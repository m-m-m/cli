/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.time.LocalDate;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link LocalDate}.
 */
public class CliLocalDateProperty extends AbstractCliProperty<LocalDate> {

  private LocalDate value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliLocalDateProperty(Localizable help, boolean mandatory, String... names) {

    super(help, mandatory, names);
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param names the {@link #getNames() names}.
   */
  public CliLocalDateProperty(Localizable help, LocalDate defaultValue, String... names) {

    super(help, false, names);
    this.value = defaultValue;
  }

  @Override
  public Class<LocalDate> getValueClass() {

    return LocalDate.class;
  }

  @Override
  public LocalDate get() {

    return this.value;
  }

  @Override
  public void set(LocalDate value) {

    this.value = value;
  }

  @Override
  public void setFromString(String value) {

    this.value = LocalDate.parse(value);
  }

}
