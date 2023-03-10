/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.command;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.CliMain;
import io.github.mmm.cli.arg.CliArgs;
import io.github.mmm.cli.container.impl.AbstractCliCommandContainerGroup;
import io.github.mmm.cli.container.impl.CliCommandContainerImpl;
import io.github.mmm.cli.container.impl.CliContainerImpl;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.list.ListProperty;

/**
 * {@link CliCommand} for auto-completion of the CLI.
 */
public interface CliCommandAutoComplete extends CliCommand {

  /** @return flag to activate this command. */
  @PropertyAlias({ "$", "0" })
  @Mandatory
  BooleanProperty Complete();

  /** @return the current arguments to auto-complete. */
  @PropertyAlias({ "*", "1" })
  ListProperty<String> Arguments();

  @Override
  default boolean hideFromHelp() {

    return true;
  }

  @Override
  default int run(CliMain main) {

    Set<String> suggestions = new TreeSet<>();
    List<String> argList = Arguments().get();
    CliArgs args = new CliArgs(argList.toArray(new String[argList.size()]));
    CliContainerImpl container = (CliContainerImpl) main.getContainer();
    for (AbstractCliCommandContainerGroup group : container.getGroups()) {
      int commandCount = group.getCommandCount();
      for (int i = 0; i < commandCount; i++) {
        CliCommandContainerImpl commandContainer = group.getCommand(i);
        CliCommand command = commandContainer.getCommand();
        if (command == this) {
          continue;
        }
        commandContainer.autoComplete(args, suggestions);
      }
    }
    String space = "";
    PrintStream stdOut = main.console().getStdOut();
    for (String suggestion : suggestions) {
      stdOut.print(space);
      stdOut.print(suggestion);
      space = " ";
    }
    return 0;
  }

}
