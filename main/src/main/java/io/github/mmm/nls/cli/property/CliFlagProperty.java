/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;

/**
 * {@link CliBooleanProperty} for a {@link io.github.mmm.cli.CliOption} treated as flag so that it does not require a
 * value and {@code true} is used as value if the option is given without value.
 */
public class CliFlagProperty extends CliBooleanProperty {

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliFlagProperty(Localizable help, boolean mandatory, String... names) {

    super(help, mandatory, names);
    set(Boolean.FALSE);
  }

  @Override
  public boolean isEmpty() {

    return !Boolean.TRUE.equals(get());
  }

}
