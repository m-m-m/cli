/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.cli.CliArgs;
import io.github.mmm.cli.CliMain;
import io.github.mmm.cli.CliOption;
import io.github.mmm.cli.CliValue;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.impl.CliConsoleImpl;
import io.github.mmm.nls.cli.exception.CliDuplicateOptionsException;
import io.github.mmm.nls.cli.exception.CliInvalidUsageException;
import io.github.mmm.nls.cli.exception.CliNoArgumentsException;
import io.github.mmm.nls.cli.property.CliCollectionProperty;
import io.github.mmm.nls.cli.property.CliLocaleProperty;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.collection.WritableCollectionProperty;
import io.github.mmm.property.locale.LocaleProperty;
import io.github.mmm.property.object.WritableSimpleProperty;

/**
 * {@link CliMain Main program} (CLI) .
 */
public abstract class NlsMain extends CliMain {

  private final List<CliCommand> commands;

  /**
   * The constructor.
   */
  public NlsMain() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param console the {@link #console() console}.
   */
  public NlsMain(CliConsole console) {

    super(console);
    this.commands = new ArrayList<>();
    add(CliCommandHelp.class);
    add(CliCommandVersion.class);
  }

  /**
   * @param commandInterface the {@link Class} reflecting the {@link CliCommand} interface to register.
   */
  protected void add(Class<? extends CliCommand> commandInterface) {

    CliCommand command = BeanFactory.get().create(commandInterface);
    add(command);
  }

  /**
   * @param command the {@link CliCommand} to register.
   */
  protected void add(CliCommand command) {

    Objects.requireNonNull(command, "command");
    this.commands.add(command);
  }

  /**
   * @return the {@link List} with the available {@link CliCommand}s.
   */
  public List<CliCommand> getCommands() {

    return this.commands;
  }

  /**
   * Typically a program shall provide multiple values for a {@link CliOption} by providing them as multiple
   * {@link CliValue}s after the {@link CliOption} (e.g. "--keys foo bar some"). However, some programs like to repeat
   * the same {@link CliOption} for that purpose (e.g. "--key foo --key bar --key some"). By default this will cause an
   * error as it is not recommended. However, if you want to support this in your CLI program, you may override this
   * method.
   *
   * @return {@code true} to tolerate duplicate occurrences of the same {@link CliOption}, {@code false} otherwise
   *         (default).
   */
  protected boolean isTolerateDuplicateOptions() {

    return false;
  }

  @Override
  public final int run(CliArgs args) {

    boolean unique = !isTolerateDuplicateOptions();
    if (unique) {
      Set<String> duplicatedOptions = args.getDuplicatedOptions();
      int size = duplicatedOptions.size();
      if (size > 0) {
        throw new CliDuplicateOptionsException(duplicatedOptions);
      }
    }
    boolean localeUnset = true;
    CliOption lastOption = args.getLastOption();
    CliValue parameter = null;
    if (lastOption != null) {
      parameter = lastOption.getNextValue();
    }
    for (int i = this.commands.size() - 1; i >= 0; i--) {
      CliCommand command = this.commands.get(i);
      System.out.println("checking command " + command.getType().getSimpleName());
      boolean commandMatches = true;
      for (WritableProperty<?> property : command.getProperties()) {
        String name = property.getName();
        Collection<String> aliases = command.getAliases().getAliases(name);
        if (!aliases.isEmpty()) {
          CliOption option = args.getOption(unique, aliases);
          if (option != null) {
            if (property instanceof CliCollectionProperty) {
              CliCollectionProperty<?, ?> collectionProperty = (CliCollectionProperty<?, ?>) property;
              List<String> values = option.getValues();
              for (String value : values) {
                collectionProperty.addFromString(value);
              }
              if (option == lastOption) {
                parameter = null;
              }
            } else {
              String value = option.getValue();
              if (value != null) {
                setValue(property, value);
                if ((option == lastOption) && (parameter != null)) {
                  parameter = parameter.getNextValue();
                }
                if (localeUnset && (property instanceof LocaleProperty) && aliases.contains("--locale")) {
                  Locale locale = ((CliLocaleProperty) property).get();
                  if (this.c instanceof CliConsoleImpl) {
                    ((CliConsoleImpl) this.c).setLocale(locale);
                  }
                  localeUnset = false;
                }
              } else if (property instanceof BooleanProperty) {
                ((BooleanProperty) property).set(Boolean.TRUE);
              } else {
                throw new IllegalArgumentException("Option '" + option.get()
                    + "' has to be followed by a value of type " + property.getValueClass().getSimpleName());
              }
            }
          }
        } else {
          if (parameter != null) {
            setValue(property, parameter.get());
            parameter = parameter.getNextValue();
          }
        }
        if (property.isMandatory() && (property.get() == null)) {
          commandMatches = false;
          break;
        }
      }
      if (commandMatches) {
        System.out.println("Command matches: " + command);
        command.validateOrThrow();
        return command.run(this);
      }
    }
    if (args.isEmpty()) {
      throw new CliNoArgumentsException();
    }
    throw new CliInvalidUsageException(args);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void setValue(WritableProperty<?> property, String value) {

    if (property instanceof WritableSimpleProperty) {
      ((WritableSimpleProperty<?>) property).setAsString(value);
    } else if (property instanceof WritableCollectionProperty) {
      Collection collection = (Collection) ((WritableCollectionProperty) property).getOrCreate();
      WritableProperty valueProperty = ((WritableCollectionProperty) property).getValueProperty();
      if (valueProperty instanceof WritableSimpleProperty) {
        WritableSimpleProperty<?> parser = (WritableSimpleProperty<?>) valueProperty;
        for (String item : value.split(",")) {
          try {
            Object element = parser.parse(item);
            collection.add(element);
          } catch (UnsupportedOperationException e) {
            invalidProperty(property, valueProperty);
          }
        }
      } else {
        invalidProperty(property, valueProperty);
      }
    } else {
      invalidProperty(property, null);
    }
  }

  private void invalidProperty(WritableProperty<?> property, WritableProperty<?> valueProperty) {

    String propertyName = property.getName();
    AttributeReadOnly lock = property.getMetadata().getLock();
    if (lock instanceof ReadableBean) {
      propertyName = propertyName + " (from " + ((ReadableBean) lock).getType().getQualifiedName() + ")";
    }
    throw new IllegalStateException("Invalid property " + propertyName + " of type "
        + property.getClass().getSimpleName() + " having type " + property.getValueClass().getSimpleName()
        + ((valueProperty == null) ? "" : " with value type " + valueProperty.getValueClass())
        + ". CLI programs shall only use simple properties or collection properties with simple value properties!");
  }

  /**
   * @return {@code true} if the help shall be printed per {@link CliCommand} with all its arguments (options and
   *         parameters), {@code false} otherwise (to first print all {@link CliCommand}s and then print all arguments
   *         together).
   */
  protected boolean isPrintHelpPerCommand() {

    return true;
  }

  /**
   * @return the name of this program.
   */
  protected String getProgramName() {

    return getClass().getName();
  }

}
