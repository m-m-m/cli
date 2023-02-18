/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli.exception;

import io.github.mmm.cli.CliArgs;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.nls.cli.NlsBundleCli;

/**
 * {@link CliException} thrown if the {@link CliArgs} are {@link CliArgs#isEmpty() empty} did not match any
 * {@link io.github.mmm.nls.cli.CliCommand}.
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
