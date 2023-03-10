/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

import java.util.Collection;

/**
 * A container for the entire CLI meta-data. It contains {@link CliCommandContainerGroup groups} of
 * {@link CliCommandContainer commands} that themselves contain {@link CliPropertyContainer properties}.
 *
 * @since 1.0.0
 */
public interface CliContainer extends AbstractCliCommandContainer {

  /**
   * @param name the {@link CliCommandContainerGroup#getName() group name}.
   * @return the requested {@link CliCommandContainerGroup}.
   */
  CliCommandContainerGroup getGroup(String name);

  /**
   * @return the {@link Collection} of {@link CliCommandContainerGroup}s.
   */
  Collection<? extends CliCommandContainerGroup> getGroups();

}
