/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.cli.arg.CliArgs;
import io.github.mmm.cli.arg.CliArgument;
import io.github.mmm.cli.arg.CliOption;
import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.cli.arg.CliValueType;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.command.CliCommandHelp;
import io.github.mmm.cli.command.CliCommandVersion;
import io.github.mmm.cli.exception.CliDuplicateOptionsException;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.cli.exception.CliInvalidUsageException;
import io.github.mmm.cli.exception.CliNoArgumentsException;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.impl.CliConsoleImpl;
import io.github.mmm.cli.property.CliCommandWithProperties;
import io.github.mmm.cli.property.CliCommands;
import io.github.mmm.cli.property.CliPropertyInfo;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.collection.WritableCollectionProperty;
import io.github.mmm.property.object.WritableSimpleProperty;

/**
 * This is the abstract base class for a main-program. <br>
 * You simply need to extend this class and implement {@link #run(CliArgs)}. For advanced CLIs please extend
 * {@code io.github.mmm.nls.cli.NlsMain} from {@code mmm-nls-cli}.
 *
 * @since 1.0.0
 */
public abstract class CliMain {

  /** @see #console() */
  protected final CliConsole console;

  private final List<CliCommand> commands;

  private CliCommands cliCommands;

  /**
   * The constructor.
   */
  public CliMain() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param console the {@link CliConsole} to use.
   */
  public CliMain(CliConsole console) {

    super();
    if (console == null) {
      this.console = new CliConsoleImpl();
    } else {
      this.console = console;
    }
    this.commands = new ArrayList<>();
    addCommands();
  }

  /**
   * {@link #add(Class) Adds} the {@link CliCommand}s for this program.
   */
  protected void addCommands() {

    add(CliCommandHelp.class);
    add(CliCommandVersion.class);
  }

  /**
   * @return the {@link CliConsole}.
   */
  public CliConsole console() {

    return this.console;
  }

  /**
   * @param argument the {@link CliArgument} that was unexpected at this place.
   */
  protected void error(CliArgument argument) {

    StringBuilder sb = new StringBuilder("Invalid argument: ");
    if (argument instanceof CliValue) {
      sb.append('\'');
      sb.append(argument);
      sb.append('\'');
    } else {
      sb.append(argument);
    }
    this.console.error().log(sb.toString());
  }

  /**
   * @param error the {@link Throwable} that occurred.
   * @return the {@link System#exit(int) exit code} corresponding to the given {@code error}.
   */
  protected int error(Throwable error) {

    int exitCode = -1;
    if (error instanceof CliException) {
      this.console.error().log(error);
      exitCode = 1;
    } else if (error instanceof IllegalArgumentException) {
      this.console.error().log(error);
      exitCode = 1;
    } else {
      this.console.error().log("An unexpected error has occurred.", error);
    }
    return exitCode;
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
   * @return the {@link CliCommands}.
   */
  public CliCommands getCliCommands() {

    if (this.cliCommands == null) {
      this.cliCommands = CliCommands.of(this.commands, this.console);
    }
    return this.cliCommands;
  }

  /**
   * Typically a program shall provide multiple values for a {@link CliOption} by providing them as multiple
   * {@link CliValue}s after the {@link CliOption} (e.g. "--keys foo bar some"). However, some programs like to repeat
   * the same {@link CliOption} for that purpose (e.g. "--key foo --key bar --key some"). In case this shall cause an
   * error you can override this method and return {@code false}.
   *
   * @return {@code true} to tolerate duplicate occurrences of the same {@link CliOption}, {@code false} otherwise
   *         (default).
   */
  protected boolean isTolerateDuplicateOptions() {

    return true;
  }

  /**
   * @return {@code true} if the help shall be printed per {@link CliCommand} with all its arguments (options and
   *         parameters), {@code false} otherwise (to first print all {@link CliCommand}s and then print all arguments
   *         together).
   */
  public boolean isPrintHelpPerCommand() {

    return true;
  }

  /**
   * @return the name of this program.
   */
  public String getProgramName() {

    return getClass().getName();
  }

  /**
   * @return the version of this program.
   */
  public String getVersion() {

    String version = null;
    Class<?> mainClass = getClass();
    String classfile = mainClass.getName().replace('.', '/') + ".class";
    URL url = mainClass.getClassLoader().getResource(classfile);
    if (url != null) {
      String path = url.toString();
      try {
        url = new URL(path.substring(0, path.length() - classfile.length()) + JarFile.MANIFEST_NAME);
        try (InputStream in = url.openStream()) {
          if (in != null) {
            Manifest manifest = new Manifest(in);
            if (manifest != null) {
              Attributes attributes = manifest.getMainAttributes();
              version = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
              if (version == null) {
                version = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
              }
            }
          }
        } catch (IOException e) {
        }
      } catch (MalformedURLException e) {
      }
    }
    if (version == null) {
      version = "undefined";
    }
    return version;
  }

  /**
   * Has to be implemented to handle that given {@link CliArgs} and do the program logic.
   *
   * @param args the {@link CliArgs}.
   * @return the {@link System#exit(int) exit code}.
   */
  public final int run(CliArgs args) {

    boolean unique = !isTolerateDuplicateOptions();
    if (unique) {
      Set<String> duplicatedOptions = args.getDuplicatedOptions();
      int size = duplicatedOptions.size();
      if (size > 0) {
        throw new CliDuplicateOptionsException(duplicatedOptions);
      }
    }
    for (CliCommandWithProperties commandWithProperties : getCliCommands().get()) {
      boolean commandMatches = bindCommandArguments(commandWithProperties, args);
      if (commandMatches) {
        CliCommand command = commandWithProperties.getCommand();
        command.validateOrThrow();
        return command.run(this);
      }
    }
    if (args.isEmpty()) {
      throw new CliNoArgumentsException();
    }
    throw new CliInvalidUsageException(args);
  }

  private boolean bindCommandArguments(CliCommandWithProperties commandWithProperties, CliArgs args) {

    CliArgument argument = args.getFirst();
    Set<String> mandatoryProperties = commandWithProperties.collectMandatoryProperties();
    int valueIndex = 0;
    CliCommand command = commandWithProperties.getCommand();
    while (argument != null) {
      CliPropertyInfo propertyInfo;
      if (argument.isOption()) {
        String alias = argument.get();
        propertyInfo = commandWithProperties.getPropertyInfo(alias);
        if (propertyInfo == null) {
          this.console.debug().logFormat("Undefined option %s for command %s", alias,
              command.getType().getSimpleName());
          return false;
        }
      } else {
        String alias = Integer.toString(valueIndex);
        propertyInfo = commandWithProperties.getPropertyInfo(alias);
        if (propertyInfo == null) {
          this.console.debug().log("Too many values - value #" + (valueIndex + 1) + " with alias " + alias
              + " is undefined for command " + command.getType().getSimpleName());
          return false;
        }
        valueIndex++;
      }
      String propertyName = propertyInfo.getPropertyName();
      WritableProperty<?> property = command.getRequiredProperty(propertyName);
      argument = setValue(property, argument, propertyInfo);
      mandatoryProperties.remove(propertyName);
    }
    return mandatoryProperties.isEmpty();
  }

  private CliArgument setValue(WritableProperty<?> property, CliArgument arg, CliPropertyInfo propertyInfo) {

    if (arg == null) {
      return null;
    }
    CliArgument next = arg.getNext();
    CliValue value;
    if (arg.isOption()) {
      if ((next != null) && next.isValue()) {
        value = (CliValue) next;
      } else if (property instanceof BooleanProperty) {
        ((BooleanProperty) property).set(Boolean.TRUE);
        value = null;
      } else {
        throw new IllegalArgumentException("Option '" + arg.get() + "' has to be followed by a value of type "
            + property.getValueClass().getSimpleName());
      }
    } else {
      value = (CliValue) arg;
    }
    if (value != null) {
      next = setValue(value, property, propertyInfo);
    }
    return next;
  }

  private CliArgument setValue(CliValue value, WritableProperty<?> property, CliPropertyInfo propertyInfo) {

    if (property instanceof WritableSimpleProperty) {
      String valueAsString = value.get();
      if (property instanceof BooleanProperty) {
        BooleanProperty booleanProperty = (BooleanProperty) property;
        CliArgument next = value;
        Boolean b = Boolean.TRUE;
        if ((propertyInfo.getIndex() != -1) && propertyInfo.getAliases().contains(valueAsString)) {
          next = value.getNext();
        } else if ((value.getValueType() == CliValueType.OPTION_ASSIGNMENT)) {
          b = booleanProperty.parse(valueAsString);
          next = value.getNext();
        }
        booleanProperty.set(b);
        return next;
      }
      ((WritableSimpleProperty<?>) property).setAsString(valueAsString);
    } else if (property instanceof WritableCollectionProperty) {
      WritableCollectionProperty<?, ?> collectionProperty = (WritableCollectionProperty<?, ?>) property;
      Collection<?> collection = collectionProperty.getOrCreate();
      WritableProperty<?> valueProperty = collectionProperty.getValueProperty();
      if (valueProperty instanceof WritableSimpleProperty) {
        WritableSimpleProperty<?> valueSimpleProperty = (WritableSimpleProperty<?>) valueProperty;
        CliValueType type = value.getValueType();
        String valueAsString = value.get();
        if (type == CliValueType.OPTION_ASSIGNMENT) {
          addValueToCollection(collection, valueAsString, property, valueSimpleProperty);
        } else {
          for (String item : valueAsString.split(",")) {
            addValueToCollection(collection, item, property, valueSimpleProperty);
          }
        }
      } else {
        invalidProperty(property, valueProperty);
      }
    } else {
      invalidProperty(property, null);
    }
    return value.getNext();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void addValueToCollection(Collection collection, String valueAsString, WritableProperty<?> property,
      WritableSimpleProperty<?> valueProperty) {

    try {
      Object element = valueProperty.parse(valueAsString);
      collection.add(element);
    } catch (UnsupportedOperationException e) {
      invalidProperty(property, valueProperty);
    } catch (Exception e) {
      throw new IllegalStateException("", e);
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
   * This method parses the given {@code args} as {@link CliArgs} and delegates to {@link #run(CliArgs)}. Additionally
   * it will catch and handle any kind of {@link Throwable errors}.
   *
   * @param args are the command-line arguments.
   * @return the {@link System#exit(int) exit code}.
   */
  public int run(String... args) {

    int exitCode;
    try {
      CliArgs cliArgs = new CliArgs(args);
      exitCode = run(cliArgs);
    } catch (Throwable e) {
      exitCode = error(e);
    }
    return exitCode;
  }

  /**
   * This method delegates to {@link #run(String...)} and then calls {@link System#exit(int)} with the returned exit
   * code. Typically you only need to call this method from your actual {@code main} method.
   *
   * @param args are the command-line arguments.
   */
  protected void runAndExit(String... args) {

    int exitCode = run(args);
    this.console.flush();
    // CHECKSTYLE:OFF (main method)
    System.exit(exitCode); // NOSONAR
    // CHECKSTYLE:ON
  }

}
