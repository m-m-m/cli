/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container;

import java.util.List;

/**
 * Container with the metadata of a {@link io.github.mmm.property.Property} from a
 * {@link io.github.mmm.cli.command.CliCommand}.
 *
 * @since 1.0.0
 */
public abstract interface AbstractCliPropertiesContainer {

  /**
   * @return the {@link List} with all properties in preferred order for help output and processing.
   */
  List<? extends CliPropertyContainer> getProperties();

}
