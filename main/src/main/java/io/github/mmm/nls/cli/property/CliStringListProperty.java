/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;

/**
 * {@link CliListProperty} containing {@link String} elements.
 *
 * @since 1.0.0
 */
public class CliStringListProperty extends CliListProperty<String> {

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliStringListProperty(Localizable help, boolean mandatory, String... names) {

    super(help, new CliStringProperty(help, true, names), mandatory, names);
  }

}
