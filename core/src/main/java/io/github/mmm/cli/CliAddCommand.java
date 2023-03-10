package io.github.mmm.cli;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliContainer;

/**
 * Interface to add {@link CliCommand}s.
 *
 * @see CliMain
 * @see CliCommand
 * @see CliContainer
 * @since 1.0.0
 */
public interface CliAddCommand {

  /**
   * @param commandInterface the {@link Class} reflecting the {@link CliCommand} interface to register.
   * @return this object itself for fluent API calls.
   */
  default CliAddCommand add(Class<? extends CliCommand> commandInterface) {

    CliCommand command = BeanFactory.get().create(commandInterface);
    add(command);
    return this;
  }

  /**
   * @param commandInterfaces the {@link Class}es reflecting the {@link CliCommand} interfaces to register.
   * @return this object itself for fluent API calls.
   */
  default CliAddCommand add(@SuppressWarnings("unchecked") Class<? extends CliCommand>... commandInterfaces) {

    for (Class<? extends CliCommand> command : commandInterfaces) {
      add(command);
    }
    return this;
  }

  /**
   * @param command the {@link CliCommand} to register.
   * @return this object itself for fluent API calls.
   */
  CliAddCommand add(CliCommand command);

  /**
   * @param commands the {@link CliCommand}s to register.
   * @return this object itself for fluent API calls.
   */
  default CliAddCommand add(CliCommand... commands) {

    for (CliCommand command : commands) {
      add(command);
    }
    return this;
  }

}
