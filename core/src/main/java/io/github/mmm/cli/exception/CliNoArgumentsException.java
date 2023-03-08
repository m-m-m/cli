/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.exception;

import io.github.mmm.cli.NlsBundleCli;
import io.github.mmm.cli.arg.CliArgs;

/**
 * {@link CliException} thrown if the {@link CliArgs} are {@link CliArgs#isEmpty() empty} did not match any
 * {@link io.github.mmm.cli.command.CliCommand}.
 *
 * @since 1.0.0
 */
public class CliNoArgumentsException extends CliException {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public CliNoArgumentsException() {

    super(NlsBundleCli.INSTANCE.errNoArguments());
  }

}
