/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides the infrastructure to implement a command-line-interface (CLI) to parse, validate and bind the arguments of
 * a {@code main} method and execute the selected command.<br>
 * <h2>Command-Line-Interface (CLI)</h2>
 *
 * Building a Java application with a CLI is kind of tedious.
 *
 * <h3>The Problem</h3>
 *
 * A regular Java application starts with a {@code main} method:
 *
 * <pre>
 * public static void main(String[] args) {
 *
 *   // ...
 * }
 * </pre>
 *
 * However, you quickly notice that this was not the end of wisdom when designing an object-oriented language as Java.
 *
 * A main-program often starts easy and then over the time options and arguments are added. When you want to write a
 * maintainable main-program you want to have more infrastructure than just having a {@link String}-array lost in a
 * static method. <br>
 * <h3>The Solution</h3>
 *
 * Simply create your main program extending {@link CliMain}:
 *
 * <pre>
 * public class MyProgram extends {@link CliMain} {
 *
 *   &#64;Override
 *   protected void addCommands() {
 *     super.addCommands();
 *     add(MyCommand.class);
 *   }
 *
 *   public static void main(String[] args) {
 *     new MyProgram().runAndExit(args);
 *   }
 * }
 * </pre>
 *
 * As you can see your main method is pretty simple. The only specific part is the {@code add(MyCommand.class)}
 * statement, where you could even add multiple commands. However, let us first continue with the example code of the
 * command:
 *
 * <pre>
 * public interface MyCommand extends CliCommand {
 *
 *   &#64;PropertyAlias({ "--verbose", "-v" })
 *   BooleanProperty Verbose();
 *
 *   &#64;PropertyAlias({ "--debug", "-d" })
 *   BooleanProperty Debug();
 *
 *   &#64;PropertyAlias({ "--force", "-f" })
 *   BooleanProperty Force();
 *
 *   &#64;Mandatory
 *   &#64;PropertyAlias({ "--data" })
 *   ListProperty<String> Data();
 *
 *   &#64;Mandatory
 *   StringProperty Value();
 *
 *   &#64;Override
 *   default int run(CliMain main) {
 *
 *     {@link CliConsole} console = main.{@link CliMain#console() console()};
 *     if (Debug().get()) {
 *       console.{@link CliConsole#debug() debug()}.{@link CliOut#log(String) log("Debug mode is active")};
 *     }
 *     if (Verbose().get()) {
 *       console.{@link CliConsole#info() info()}.{@link CliOut#log(String) log("Verbose output is active")};
 *     }
 *     if (Force().get()) {
 *       console.{@link CliConsole#warning() warning()}.{@link CliOut#log(String) log("Force mode is active - all files will be overridden without interactive confirmation")};
 *     }
 *     for (String data : Data().get()) {
 *       // ... do whatever your command should do ...
 *     }
 *   }
 * }
 * </pre>
 *
 * Now you can run this {@code MyProgram} program with:
 *
 * <pre>
 * MyProgram -v -d -f --data file1 --data file2 my-value
 * </pre>
 *
 * You can quickly guess what will happen, but you can also do the same thing with:
 *
 * <pre>
 * MyProgram -vdf --data file1,file2 my-value
 * </pre>
 *
 * Also you could do the same thing with:
 *
 * <pre>
 * MyProgram -vdf --data=file1 --data=file2 my-value
 * </pre>
 *
 * There is build-in support to print the help via {@code -h} or {@code --help}:
 *
 * <pre>
 * MyProgram --help
 * </pre>
 *
 * All you need to do is add resource bundles for your commands what even supports internationalization. So assuming the
 * qualified name for your command is {@code net.example.app.MyCommand} then you create a file
 * {@code src/main/resources/l10n/net/example/app/MyCommand.properties} with the following content:
 *
 * <pre>
 * help=...does what my command should do...
 * Debug=Activate debug mode to get additional debug log output.
 * Force=Activate force mode to overwrite files without interactive confirmation.
 * Verbose=Activate verbose output.
 * Data=The data file(s) to process.
 * Value=The actual value to assign.
 * </pre>
 *
 * So for localization to other languages you could even create resource bundle files for additional languages (E.g.
 * {@code MyCommand_es.properties}).
 *
 * Also if you have a manifest file with the version you can even get the program version:
 *
 * <pre>
 * MyProgram --version
 * </pre>
 *
 * Also there is support for {@code --} in case you want to provide an argument value starting with a hyphen (e.g.
 * "-my-value" what would conflict with the force short option):
 *
 * <pre>
 * {@literal MyProgram -vdf --data file1,file2 -- -my-value}
 * </pre>
 *
 * Further, to provide option values starting with a hyphen or comma always use option assignments:
 *
 * <pre>
 * {@literal MyProgram -vdf --data=-file1 --data=file,2 -- -my-value}
 * </pre>
 *
 * <h3>Conclusion</h3> Simply implement your main program based on {@link CliMain} and you do not need to worry about
 * all the boring stuff and directly focus on implementing the actual logic providing the features.
 */
module io.github.mmm.cli {

  requires transitive io.github.mmm.nls;

  requires transitive io.github.mmm.bean.factory;

  exports io.github.mmm.cli;

  exports io.github.mmm.cli.arg;

  exports io.github.mmm.cli.command;

  exports io.github.mmm.cli.exception;

  exports io.github.mmm.cli.io;

  // exports io.github.mmm.cli.io.impl;

  exports io.github.mmm.cli.property;

}
