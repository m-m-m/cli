/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import java.util.Locale;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link Locale}.
 */
public class CliLocaleProperty extends AbstractCliProperty<Locale> {

  private Locale value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliLocaleProperty(Localizable help, boolean mandatory, String... names) {

    super(help, mandatory, names);
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param names the {@link #getNames() names}.
   */
  public CliLocaleProperty(Localizable help, Locale defaultValue, String... names) {

    super(help, false, names);
    this.value = defaultValue;
  }

  @Override
  public Class<Locale> getValueClass() {

    return Locale.class;
  }

  @Override
  public Locale get() {

    return this.value;
  }

  @Override
  public void set(Locale value) {

    this.value = value;
  }

  @Override
  public void setFromString(String value) {

    if (value == null) {
      this.value = null;
    } else {
      this.value = Locale.forLanguageTag(value.replace('_', '-'));
    }
  }

}
