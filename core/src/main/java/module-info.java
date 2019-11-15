/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides the API and implementation to parse the arguments of a {@code main} method from a command-line-interface
 * (CLI).<br>
 * <h2>Command-Line-Interfaces (CLI)</h2>
 *
 * Building a Java application with a CLI is kind of tedious.
 *
 * <h3>The Problem</h3>
 *
 * A regular Java application start with a {@code main} method:
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
 * As a solution we provide {@link io.github.mmm.cli.CliArgs}:
 *
 * <pre>
 * public class Main {
 *
 *   private boolean verbose;
 *
 *   private boolean debug;
 *
 *   private boolean force;
 *
 *   List<String> values = new ArrayList<>();
 *
 *   public static void main(String[] args) {
 *
 *     int exitCode = run(new CliArgs(args));
 *     System.exit(exitCode);
 *   }
 *
 *   public int run(CliArgs args) {
 *
 *     while (cliArgs.hasNext()) {
 *       CliArgument arg = cliArgs.next();
 *       if (arg.isOption()) {
 *         switch (arg.get()) {
 *           case "-h":
 *           case "--help":
 *             printHelp();
 *             return 0;
 *             break;
 *           case "-v":
 *           case "--verbose":
 *             this.verbose = true;
 *             break;
 *           case "-d":
 *           case "--debug":
 *             this.debug = true;
 *             break;
 *           case "-f":
 *           case "--force":
 *             this.force = true;
 *             break;
 *           default:
 *             System.err.println("Illegal option: " + arg);
 *             return -1;
 *         }
 *       } else {
 *         this.values.add(arg.get());
 *       }
 *     }
 *     // do something
 *   }
 * }
 * </pre>
 *
 * Now you can run this {@code Main} program with:
 *
 * <pre>
 * Main -v -d -f file1 file2
 * </pre>
 *
 * You can quickly guess what will happen, but you can also do the same thing with:
 *
 * <pre>
 * Main -vdf file1 file2
 * </pre>
 *
 * And if you want to provide a filename starting with a hyphen you can do
 *
 * <pre>
 * {@literal Main -vdf -- -filename}
 * </pre>
 *
 * Further, a CLI may have options that need a value:
 *
 * <pre>
 * App --option-name option-value
 * </pre>
 *
 * You {@code App} does not need to be rewritten to also accept:
 *
 * <pre>
 * App --option-name=option-value
 * </pre>
 *
 * Of course that was just the beginning as we provide a higher-level module to make it even simpler.
 */
module io.github.mmm.cli {
  exports io.github.mmm.cli;
}
