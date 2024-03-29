/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.exception;

import io.github.mmm.cli.NlsBundleCli;
import io.github.mmm.cli.arg.CliArgs;

/**
 * {@link CliException} thrown if the {@link CliArgs} are invalid as they did not match any
 * {@link io.github.mmm.cli.command.CliCommand}.
 *
 * @since 1.0.0
 */
public class CliInvalidUsageException extends CliException {

  private static final long serialVersionUID = 1L;

  private final CliArgs args;

  /**
   * The constructor.
   *
   * @param args the invalid {@link #getArgs() args}.
   */
  public CliInvalidUsageException(CliArgs args) {

    super(NlsBundleCli.INSTANCE.errInvalidArguments(args.getOriginalArgumentsAsString()));
    this.args = args;
  }

  /**
   * @return the {@link CliArgs} that are invalid as they did not match any {@link io.github.mmm.cli.command.CliCommand}.
   */
  public CliArgs getArgs() {

    return this.args;
  }

}
