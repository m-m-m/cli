/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.exception;

import java.util.Set;

import io.github.mmm.cli.NlsBundleCli;
import io.github.mmm.cli.arg.CliOption;

/**
 * {@link CliException} thrown if two one or multiple {@link CliOption}s have occurred multiple times.
 *
 * @since 1.0.0
 */
public class CliDuplicateOptionsException extends CliException {

  private static final long serialVersionUID = 1L;

  private final Set<String> options;

  /**
   * The constructor.
   *
   * @param options the {@link #getOptions() options}.
   */
  public CliDuplicateOptionsException(Set<String> options) {

    super(NlsBundleCli.INSTANCE.errDuplicateOptions(String.join(", ", options)));
    assert !options.isEmpty();
    this.options = options;
  }

  /**
   * @return the {@link Set} with the {@link CliOption#get() names} of the {@link CliOption}s that occurred multiple
   *         times.
   */
  public Set<String> getOptions() {

    return this.options;
  }

}
