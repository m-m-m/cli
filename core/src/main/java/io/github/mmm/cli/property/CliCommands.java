package io.github.mmm.cli.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.io.CliConsole;

/**
 * Container with all {@link CliCommandWithProperties}.
 */
public class CliCommands {

  private final List<CliCommandWithProperties> properties;

  private CliCommands(List<CliCommandWithProperties> commands) {

    super();
    this.properties = commands;
  }

  /**
   * @return the {@link List} with all {@link CliCommandWithProperties}.
   */
  public List<CliCommandWithProperties> get() {

    return this.properties;
  }

  /**
   * @param command the {@link CliCommand}.
   * @return the {@link CliCommandWithProperties} for the given {@link CliCommand}.
   */
  public CliCommandWithProperties get(CliCommand command) {

    for (CliCommandWithProperties p : this.properties) {

      if (p.getCommand() == command) {
        return p;
      }
    }
    return null;
  }

  /**
   * @param commands the {@link Collection} with the {@link CliCommand}s.
   * @param console the {@link CliConsole} for logging.
   * @return the {@link CliCommands}.
   */
  public static CliCommands of(Collection<CliCommand> commands, CliConsole console) {

    List<CliCommandWithProperties> properties = new ArrayList<>(commands.size());
    for (CliCommand command : commands) {
      properties.add(CliCommandWithProperties.of(command, console));
    }
    return new CliCommands(properties);
  }

}
