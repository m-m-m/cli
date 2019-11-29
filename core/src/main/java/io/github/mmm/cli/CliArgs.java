/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.mmm.cli.exception.CliDuplicateOptionAliasException;

/**
 * A simple wrapper for the arguments of a {@code main} method from a command-line-interface (CLI). Allows to easily
 * iterate and parse these arguments.
 *
 * @since 1.0.0
 * @see #CliArgs(String...)
 * @see #getFirst()
 */
public class CliArgs {

  private final Map<String, CliOption> optionMap;

  private final String[] originalArguments;

  private Set<String> duplicatedOptions;

  private CliArgument first;

  private CliArgument current;

  private int size;

  /**
   * The constructor.
   *
   * @param args the command-line arguments from {@code main} method.
   */
  public CliArgs(String... args) {

    super();
    this.optionMap = new HashMap<>();
    this.originalArguments = args;
    boolean endOpts = false;
    for (int argsIndex = 0; argsIndex < args.length; argsIndex++) {
      String arg = args[argsIndex];
      if (endOpts) {
        add(new CliValue(arg, true));
      } else if (arg.startsWith("-")) {
        if (arg.equals(CliArgument.END_OPTIONS)) {
          endOpts = true;
        } else {
          boolean assignment = false;
          int equalsIndex = arg.indexOf('=');
          CliValue value = null;
          if (equalsIndex > 0) {
            assignment = true;
            String optValue = arg.substring(equalsIndex + 1);
            value = new CliValue(optValue, false);
            arg = arg.substring(0, equalsIndex);
          }
          if (arg.startsWith("--")) {
            add(new CliLongOption(arg, assignment));
          } else {
            int len = arg.length();
            if (len == 1) {
              add(new CliValue(arg, false));
            } else if (len == 2) {
              add(new CliShortOption(arg, assignment));
            } else {
              for (int i = 1; i < len; i++) {
                add(new CliShortOption("-" + arg.charAt(i), assignment));
              }
            }
          }
          if (value != null) {
            add(value);
          }
        }
      } else {
        add(new CliValue(arg, false));
      }
    }
    if (this.duplicatedOptions == null) {
      this.duplicatedOptions = Collections.emptySet();
    } else {
      this.duplicatedOptions = Collections.unmodifiableSet(this.duplicatedOptions);
    }
  }

  private void add(CliArgument arg) {

    if (this.current == null) {
      this.first = arg;
      this.current = arg;
    } else {
      this.current.next = arg;
      this.current = arg;
    }
    this.size++;
    if (arg.isOption()) {
      CliOption duplicate = this.optionMap.putIfAbsent(arg.get(), (CliOption) arg);
      if (duplicate != null) {
        if (this.duplicatedOptions == null) {
          this.duplicatedOptions = new HashSet<>();
        }
        this.duplicatedOptions.add(arg.get());
      }
    }
  }

  /**
   * @return the first {@link CliArgument}.
   * @see CliArgument#getNext()
   * @see #getOption(String)
   */
  public CliArgument getFirst() {

    return this.first;
  }

  /**
   * @return the last {@link CliOption} from the arguments.
   */
  public CliOption getLastOption() {

    CliArgument arg = this.first;
    CliOption option = null;
    while (arg != null) {
      if (arg.isOption()) {
        option = (CliOption) arg;
      }
      arg = arg.next;
    }
    return option;
  }

  /**
   * @return {@code true} if empty, {@code false} otherwise.
   */
  public boolean isEmpty() {

    return (this.first == null);
  }

  /**
   * @return the number of {@link CliArgument}s.
   */
  public int getSize() {

    return this.size;
  }

  /**
   * @return the {@link Set} with the {@link CliOption}s that have been duplicated.
   */
  public Set<String> getDuplicatedOptions() {

    return this.duplicatedOptions;
  }

  /**
   * @param opt the {@link CliOption#get() option string} (e.g. "-h" or "--help" for help option).
   * @return the requested {@link CliOption} or {@code null} if no such {@link CliOption} exists.
   * @see #getOption(String...)
   */
  public CliOption getOption(String opt) {

    return this.optionMap.get(opt);
  }

  /**
   * @param options the array of {@link CliOption#get() option string} (e.g. "-h" or "--help" for help option).
   * @return the first matching {@link CliOption} or {@code null} if none of the given {@code options} exists.
   */
  public CliOption getOption(String... options) {

    return getOption(false, options);
  }

  /**
   * @param unique - {@code true} if not more than one of the given {@code options} should be defined (or a
   *        {@link CliDuplicateOptionAliasException} is thrown), {@code false} otherwise. Set this flag to {@code true}
   *        when you provide multiple synonym options such as a {@link CliShortOption short} and a {@link CliLongOption
   *        long option}.
   * @param options the array of {@link CliOption#get() option string} (e.g. "-h" or "--help" for help option).
   * @return the first matching {@link CliOption} or {@code null} if none of the given {@code options} exists.
   * @throws CliDuplicateOptionAliasException in case {@code unique} is {@code true} and two of the given
   *         {@code options} have been found.
   */
  public CliOption getOption(boolean unique, String... options) {

    CliOption option = null;
    for (String opt : options) {
      CliOption cliOpt = this.optionMap.get(opt);
      if (cliOpt != null) {
        if (option == null) {
          option = cliOpt;
          if (!unique) {
            break;
          }
        } else if (unique) {
          throw new CliDuplicateOptionAliasException(cliOpt, option);
        }
      }
    }
    return option;
  }

  /**
   * @param unique - {@code true} if not more than one of the given {@code options} should be defined (or a
   *        {@link CliDuplicateOptionAliasException} is thrown), {@code false} otherwise. Set this flag to {@code true}
   *        when you provide multiple synonym options such as a {@link CliShortOption short} and a {@link CliLongOption
   *        long option}.
   * @param options the Collection of {@link CliOption#get() option string} (e.g. "-h" or "--help" for help option).
   * @return the first matching {@link CliOption} or {@code null} if none of the given {@code options} exists.
   * @throws CliDuplicateOptionAliasException in case {@code unique} is {@code true} and two of the given
   *         {@code options} have been found.
   */
  public CliOption getOption(boolean unique, Collection<String> options) {

    CliOption option = null;
    for (String opt : options) {
      CliOption cliOpt = this.optionMap.get(opt);
      if (cliOpt != null) {
        if (option == null) {
          option = cliOpt;
          if (!unique) {
            break;
          }
        } else if (unique) {
          throw new CliDuplicateOptionAliasException(cliOpt, option);
        }
      }
    }
    return option;
  }

  /**
   * @return the original arguments given at {@link #CliArgs(String...) construction}.
   */
  public String[] getOriginalArguments() {

    return this.originalArguments;
  }

  /**
   * @return the original arguments as a single command-line {@link String}.
   */
  public String getOriginalArgumentsAsString() {

    StringBuilder sb = new StringBuilder();
    for (String arg : this.originalArguments) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      appendArg(sb, arg);
    }
    return sb.toString();
  }

  private void appendArg(StringBuilder sb, String arg) {

    arg = arg.replace("\"", "\\\"");
    if (arg.contains(" ")) {
      sb.append('"');
      sb.append(arg);
      sb.append('"');
    } else {
      sb.append(arg);
    }
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    CliArgument arg = this.first;
    while (arg != null) {
      if (sb.length() > 0) {
        sb.append(' ');
      }
      appendArg(sb, arg.get());
      arg = arg.next;
    }
    return sb.toString();
  }

}
