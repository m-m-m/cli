/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

import io.github.mmm.cli.command.CliCommand;

/**
 * A group of {@link CliCommandContainer}s that allows to combine the help of multiple commands.
 *
 * @since 1.0.0
 */
public interface AbstractCliCommandContainer {

  /**
   * @param command the {@link CliCommand}.
   * @return the {@link CliCommandContainer} for the given {@link CliCommand} or {@code null} if not found.
   */
  CliCommandContainer getCommand(CliCommand command);

}
