/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;

/**
 * Implementation of {@link CliProperty} for {@link Boolean}.
 */
public class CliBooleanProperty extends AbstractCliProperty<Boolean> {

  private Boolean value;

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliBooleanProperty(Localizable help, boolean mandatory, String... names) {

    super(help, mandatory, names);
  }

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param defaultValue the initial default {@link #get() value}.
   * @param names the {@link #getNames() names}.
   */
  public CliBooleanProperty(Localizable help, Boolean defaultValue, String... names) {

    super(help, false, names);
    this.value = defaultValue;
  }

  @Override
  public Class<Boolean> getValueClass() {

    return Boolean.class;
  }

  @Override
  public Boolean get() {

    return this.value;
  }

  @Override
  public void set(Boolean value) {

    this.value = value;
  }

  @Override
  public void setFromString(String value) {

    this.value = Boolean.valueOf(value);
  }

}
