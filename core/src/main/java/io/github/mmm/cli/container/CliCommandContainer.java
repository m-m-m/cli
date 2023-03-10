/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.impl.CliPropertyContainerImpl;

/**
 * Container for a {@link #getCommand() command} with its {@link #getProperties() properties}.
 *
 * @since 1.0.0
 */
public interface CliCommandContainer extends AbstractCliPropertiesContainer {

  /**
   * @return the owning {@link CliCommand}.
   */
  CliCommand getCommand();

  /**
   * @param alias the {@link CliPropertyContainerImpl#getAliases() alias} of the requested
   *        {@link CliPropertyContainerImpl}.
   * @return the {@link CliPropertyContainerImpl} for the given {@link CliPropertyContainerImpl#getAliases() alias} or
   *         {@code null} if not found.
   */
  CliPropertyContainer getProperty(String alias);

}
