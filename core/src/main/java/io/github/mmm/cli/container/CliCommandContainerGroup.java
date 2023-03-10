/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

/**
 * A group of {@link CliCommandContainer}s that allows to combine the help of multiple commands.
 *
 * @since 1.0.0
 */
public interface CliCommandContainerGroup extends AbstractCliCommandContainer, AbstractCliPropertiesContainer {

  /**
   * @return the name of this group.
   */
  String getName();

  /**
   * @return the number of {@link CliCommandContainer}s contained in this group.
   */
  int getCommandCount();

  /**
   * @param i the index of the requested {@link CliCommandContainer}. Has to be in the range from {@code 0} to
   *        <code>{@link #getCommandCount()}-1</code>.
   * @return the requested {@link CliCommandContainer}.
   */
  CliCommandContainer getCommand(int i);

}
