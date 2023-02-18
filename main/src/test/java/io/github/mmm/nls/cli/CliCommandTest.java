/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.property.number.longs.LongProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * The test command.
 */
public interface CliCommandTest extends CliCommand {

  /** @return the mode. */
  @PropertyAlias({ "--mode", "-m" })
  @Mandatory
  StringProperty Mode();

  /** @return the value. */
  @Mandatory
  LongProperty Value();

  @Override
  default int run(NlsMain main) {

    main.console().out().log(Value().get());
    if ("JUnit".equals(Mode().get())) {
      return 0;
    }
    return -1;
  }

}