/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A command with an invalid value index.
 */
public interface CliCommandInvalidOptionDuplicate extends CliCommand {

  /** @return the mode. */
  @PropertyAlias({ "--mode", "-m" })
  @Mandatory
  StringProperty Mode();

  /** @return the keys. */
  @PropertyAlias({ "--key", "-m" })
  ListProperty<String> Keys();

  @Override
  default int run(CliMain main) {

    return 0;
  }

}