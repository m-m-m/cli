package io.github.mmm.cli.container.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.cli.CliAddCommand;
import io.github.mmm.cli.CliMain;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliContainer;
import io.github.mmm.cli.io.CliConsole;

/**
 * Implementation of {@link CliContainer}.
 *
 * @since 1.0.0
 */
public class CliContainerImpl implements CliContainer, CliAddCommand {

  private final CliConsole console;

  private final Map<String, AbstractCliCommandContainerGroup> groups;

  /**
   * The constructor.
   *
   * @param console the {@link CliConsole}.
   */
  public CliContainerImpl(CliConsole console) {

    super();
    this.console = console;
    this.groups = new LinkedHashMap<>();
  }

  /**
   * @return the {@link CliConsole}.
   * @see CliMain#console()
   */
  public CliConsole getConsole() {

    return this.console;
  }

  @Override
  public AbstractCliCommandContainerGroup getGroup(String name) {

    return this.groups.get(name);
  }

  /**
   * @param name the {@link CliCommandContainerGroupImpl#getName() group name}.
   * @return the {@link CliCommandContainerGroupImpl} for the given {@code name}. Will be created if not exists.
   */
  public CliCommandContainerGroupImpl getOrCreateGroup(String name) {

    AbstractCliCommandContainerGroup group = this.groups.computeIfAbsent(name,
        n -> new CliCommandContainerGroupImpl(this.console, name));
    if (group instanceof CliCommandContainerGroupImpl) {
      return (CliCommandContainerGroupImpl) group;
    }
    throw new IllegalStateException(name);
  }

  /**
   * @param group the {@link AbstractCliCommandContainerGroup} to add.
   */
  public void addGroup(AbstractCliCommandContainerGroup group) {

    AbstractCliCommandContainerGroup duplicate = this.groups.put(group.getName(), group);
    if (duplicate != null) {
      throw new DuplicateObjectException(group, group.getName(), duplicate);
    }
  }

  @Override
  public Collection<AbstractCliCommandContainerGroup> getGroups() {

    return this.groups.values();
  }

  @Override
  public CliCommandContainerImpl getCommand(CliCommand command) {

    for (AbstractCliCommandContainerGroup group : this.groups.values()) {
      CliCommandContainerImpl commandContainer = group.getCommand(command);
      if (commandContainer != null) {
        return commandContainer;
      }
    }
    return null;
  }

  @Override
  public CliAddCommand add(CliCommand command) {

    Objects.requireNonNull(command, "command");
    CliCommandContainerImpl commandContainer = CliCommandContainerImpl.of(command, this.console);
    addGroup(commandContainer);
    return this;
  }

}
