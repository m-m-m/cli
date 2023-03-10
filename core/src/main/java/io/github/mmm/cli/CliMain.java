/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import io.github.mmm.cli.arg.CliArgs;
import io.github.mmm.cli.arg.CliArgument;
import io.github.mmm.cli.arg.CliOption;
import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.command.CliCommandAutoComplete;
import io.github.mmm.cli.command.CliCommandHelp;
import io.github.mmm.cli.command.CliCommandVersion;
import io.github.mmm.cli.container.CliCommandContainerGroup;
import io.github.mmm.cli.container.CliContainer;
import io.github.mmm.cli.container.impl.AbstractCliCommandContainerGroup;
import io.github.mmm.cli.container.impl.CliCommandContainerImpl;
import io.github.mmm.cli.container.impl.CliContainerImpl;
import io.github.mmm.cli.exception.CliDuplicateOptionsException;
import io.github.mmm.cli.exception.CliException;
import io.github.mmm.cli.exception.CliInvalidUsageException;
import io.github.mmm.cli.exception.CliNoArgumentsException;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.impl.CliConsoleImpl;

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

  private final CliContainerImpl container;

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
    this.container = new CliContainerImpl(this.console);
    addCommands();
  }

  /**
   * {@link CliAddCommand#add(Class) Adds} the {@link CliCommand}s for this program.
   */
  @SuppressWarnings("unchecked")
  protected void addCommands() {

    group().add(CliCommandHelp.class, CliCommandVersion.class, CliCommandAutoComplete.class);
  }

  /**
   * @return the {@link CliContainer} with the groups, commands and their properties.
   */
  public CliContainer getContainer() {

    return this.container;
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
   * @return the root group where {@link CliCommand}s can be {@link CliAddCommand#add(Class) added}.
   */
  protected CliAddCommand group() {

    return this.container;
  }

  /**
   * @param name the {@link CliCommandContainerGroup#getName() group name}.
   * @return the group with the given {@link CliCommandContainerGroup#getName() group name} where {@link CliCommand}s
   *         can be {@link CliAddCommand#add(Class) added}.
   */
  protected CliAddCommand group(String name) {

    return this.container.getOrCreateGroup(name);
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
    for (AbstractCliCommandContainerGroup group : this.container.getGroups()) {
      int commandCount = group.getCommandCount();
      for (int i = 0; i < commandCount; i++) {
        CliCommandContainerImpl commandContainer = group.getCommand(i);
        boolean commandMatches = commandContainer.bindCommandArguments(args);
        if (commandMatches) {
          CliCommand command = commandContainer.getCommand();
          command.validateOrThrow();
          return command.run(this);
        }
      }
    }
    if (args.isEmpty()) {
      throw new CliNoArgumentsException();
    }
    throw new CliInvalidUsageException(args);
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
