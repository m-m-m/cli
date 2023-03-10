package io.github.mmm.cli.container.impl;

import java.util.List;

import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliCommandContainerGroup;
import io.github.mmm.cli.io.CliConsole;

/**
 * Abstract base implementation of {@link CliCommandContainerGroup}.
 */
public abstract class AbstractCliCommandContainerGroup implements CliCommandContainerGroup {

  /** The {@link CliConsole}. */
  protected final CliConsole console;

  /**
   * The constructor.
   *
   * @param console the {@link CliConsole}.
   */
  public AbstractCliCommandContainerGroup(CliConsole console) {

    super();
    this.console = console;
  }

  @Override
  public abstract CliCommandContainerImpl getCommand(int i);

  @Override
  public abstract CliCommandContainerImpl getCommand(CliCommand command);

  @Override
  public abstract List<CliPropertyContainerImpl> getProperties();

}
