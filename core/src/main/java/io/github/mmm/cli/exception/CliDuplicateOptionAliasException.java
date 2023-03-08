/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.exception;

import io.github.mmm.cli.arg.CliOption;

/**
 * {@link CliException} thrown if two synonymous {@link CliOption}s have been specified.
 *
 * @since 1.0.0
 */
public class CliDuplicateOptionAliasException extends CliException {

  private static final long serialVersionUID = 1L;

  private final CliOption option1;

  private final CliOption option2;

  /**
   * The constructor.
   *
   * @param option1 the first option (e.g. "-f").
   * @param option2 the second option (e.g. "--force").
   */
  public CliDuplicateOptionAliasException(CliOption option1, CliOption option2) {

    super("Duplicate options '" + option1 + "' and '" + option2 + "'.");
    this.option1 = option1;
    this.option2 = option2;
  }

  /**
   * @return the first {@link CliOption}.
   */
  public CliOption getOption1() {

    return this.option1;
  }

  /**
   * @return the second {@link CliOption}.
   */
  public CliOption getOption2() {

    return this.option2;
  }

}
