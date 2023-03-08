/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import io.github.mmm.bean.AbstractInterface;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.property.booleans.BooleanProperty;

/**
 * The test command.
 */
@AbstractInterface
public interface GitAbstractTag extends CliCommand {

  /** @return the command activation. */
  @PropertyAlias({ "tag", "0" })
  @Mandatory
  BooleanProperty Tag();

  @Override
  default int run(CliMain main) {

    main.console().out().log(toString());
    return 0;
  }

}