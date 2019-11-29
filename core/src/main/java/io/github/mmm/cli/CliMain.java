/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import io.github.mmm.cli.exception.CliException;
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
  protected final CliConsole c;

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
      this.c = new CliConsoleImpl();
    } else {
      this.c = console;
    }
  }

  /**
   * @return the {@link CliConsole}.
   */
  public CliConsole console() {

    return this.c;
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
    this.c.error().log(sb.toString());
  }

  /**
   * @param error the {@link Throwable} that occurred.
   * @return the {@link System#exit(int) exit code} corresponding to the given {@code error}.
   */
  protected int error(Throwable error) {

    int exitCode = -1;
    if (error instanceof CliException) {
      this.c.error().log(error);
      exitCode = 1;
    } else if (error instanceof IllegalArgumentException) {
      this.c.error().log(error);
      exitCode = 1;
    } else {
      this.c.error().log("An unexpected error has occurred.", error);
    }
    return exitCode;
  }

  /**
   * @return the version of this program.
   */
  protected String getVersion() {

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
  public abstract int run(CliArgs args);

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
    this.c.flush();
    // CHECKSTYLE:OFF (main method)
    System.exit(exitCode); // NOSONAR
    // CHECKSTYLE:ON
  }

}
