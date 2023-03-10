package io.github.mmm.cli.container.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.cli.CliAddCommand;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliCommandContainerGroup;
import io.github.mmm.cli.io.CliConsole;

/**
 * Implementation of {@link CliCommandContainerGroup}.
 *
 * @since 1.0.0
 */
public class CliCommandContainerGroupImpl extends AbstractCliCommandContainerGroup implements CliAddCommand {

  private final String name;

  private final List<CliCommandContainerImpl> commands;

  /**
   * The constructor.
   *
   * @param console the {@link CliConsole}.
   * @param name the {@link #getName() group name}.
   */
  public CliCommandContainerGroupImpl(CliConsole console, String name) {

    super(console);
    this.name = name;
    this.commands = new ArrayList<>();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public int getCommandCount() {

    return this.commands.size();
  }

  @Override
  public CliCommandContainerImpl getCommand(int i) {

    return this.commands.get(i);
  }

  @Override
  public CliCommandContainerImpl getCommand(CliCommand command) {

    for (CliCommandContainerImpl commandContainer : this.commands) {
      if (commandContainer.getCommand() == command) {
        return commandContainer;
      }
    }
    return null;
  }

  @Override
  public List<CliPropertyContainerImpl> getProperties() {

    Map<String, CliPropertyContainerImpl> propertiesMap = new HashMap<>();
    for (CliCommandContainerImpl commandContainer : this.commands) {
      for (CliPropertyContainerImpl property : commandContainer.getProperties()) {
        propertiesMap.putIfAbsent(property.getSyntax(), property);
      }
    }
    List<CliPropertyContainerImpl> properties = new ArrayList<>(propertiesMap.values());
    Collections.sort(properties);
    return properties;
  }

  /**
   * @param commandContainer the {@link CliCommandContainerImpl} to add to this group.
   */
  public void addCommand(CliCommandContainerImpl commandContainer) {

    this.commands.add(commandContainer);
  }

  @Override
  public CliAddCommand add(CliCommand command) {

    Objects.requireNonNull(command, "command");
    CliCommandContainerImpl commandContainer = CliCommandContainerImpl.of(command, this.console);
    addCommand(commandContainer);
    return this;
  }

}
