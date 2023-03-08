/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.util.List;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.property.container.list.ListProperty;
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
  @PropertyAlias("0")
  LongProperty Value();

  /** @return the keys. */
  @PropertyAlias("--key")
  ListProperty<String> Keys();

  @Override
  default int run(CliMain main) {

    main.console().out().log(Value().get());
    List<String> keys = Keys().getSafe();
    if (!keys.isEmpty()) {
      main.console().out().log(keys);
    }
    if ("JUnit".equals(Mode().get())) {
      return 0;
    }
    return -1;
  }

}