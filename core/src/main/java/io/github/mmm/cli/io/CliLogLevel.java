/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.io;

import io.github.mmm.cli.io.impl.CliConsoleImpl;

/**
 * {@link Enum} for the levels to log to a {@link CliConsoleImpl}.
 */
public enum CliLogLevel {

  /** Debug information with details for developers and to get more details in case of problems or errors. */
  DEBUG,

  /** General information for end-users to get helpful feedback about what is going on. */
  INFO,

  /** Warning information if something is not as expected but does not prevent further processing. */
  WARNING,

  /** Error information if something went wrong and processing typically failed and has to be aborted. */
  ERROR;

  /**
   * @param level the {@link CliLogLevel} to check (e.g. that is going to be logged).
   * @return {@code true} if this {@link CliLogLevel} <em>includes</em> the given {@link CliLogLevel}, {@code false}
   *         otherwise. In other words this method checks if the {@link #ordinal() severity} of the given
   *         {@link CliLogLevel} is greater or equal to this {@link CliLogLevel}. So if this method is invoked on the
   *         {@link CliConsole#getLogLevel() current log-level} of the {@link CliConsole} with the
   *         {@link CliConsole#out(CliLogLevel) level to log} the result will indicate if the message should be logged
   *         (or in case of {@code false} be suppressed).
   */
  public boolean includes(CliLogLevel level) {

    return ordinal() <= level.ordinal();
  }

}