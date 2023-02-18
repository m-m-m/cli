/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.property;

import io.github.mmm.base.i18n.Localizable;

/**
 * {@link CliListProperty} containing {@link Class} elements.
 *
 * @param <T> type of {@link CliClassProperty#getBound() upper bound}.
 * @since 1.0.0
 */
public class CliClassListProperty<T> extends CliListProperty<Class<? extends T>> {

  /**
   * The constructor.
   *
   * @param help the {@link #getHelp() help message}.
   * @param bound the {@link CliClassProperty#getBound() upper bound}.
   * @param mandatory the {@link #isMandatory() mandatory} flag.
   * @param names the {@link #getNames() names}.
   */
  public CliClassListProperty(Localizable help, Class<T> bound, boolean mandatory, String... names) {

    super(help, new CliClassProperty<>(help, bound, true, names), mandatory, names);
  }

}
