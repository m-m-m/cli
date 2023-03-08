/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.command;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.CliMain;
import io.github.mmm.property.booleans.BooleanProperty;

/**
 * {@link CliCommand} to print the version of the program.
 */
public interface CliCommandVersion extends CliCommand {

  /** @return flag to activate this command. */
  @PropertyAlias({ "--version", "-v" })
  @Mandatory
  BooleanProperty Version(); // NlsBundleCli.INSTANCE.optVersion()

  @Override
  default int run(CliMain main) {

    main.console().getStdOut().println(main.getVersion());
    return 0;
  }

}
