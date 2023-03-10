/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.command;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.CliMain;
import io.github.mmm.cli.NlsBundleCli;
import io.github.mmm.cli.container.impl.AbstractCliCommandContainerGroup;
import io.github.mmm.cli.container.impl.CliCommandContainerImpl;
import io.github.mmm.cli.container.impl.CliContainerImpl;
import io.github.mmm.cli.container.impl.CliPropertyContainerImpl;
import io.github.mmm.cli.container.impl.CliPropertyGroup;
import io.github.mmm.cli.container.impl.CliPropertyType;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.CliOut;
import io.github.mmm.property.booleans.BooleanProperty;

/**
 * {@link CliCommand} to print the help of the program.
 */
public interface CliCommandHelp extends CliCommand {

  /** @return flag to activate this command. */
  @PropertyAlias({ "--help", "-h" })
  @Mandatory
  BooleanProperty Help();

  @Override
  default int run(CliMain main) {

    CliConsole console = main.console();
    CliOut out = console.out();
    CliContainerImpl container = (CliContainerImpl) main.getContainer();
    CliCommandContainerImpl helpCwp = container.getCommand(this);
    CliBundle helpBundle = helpCwp.getBundle();
    out.log(helpBundle.get("usage"));
    StringBuilder usageBuffer = new StringBuilder(main.getProgramName());
    int usageMinLength = usageBuffer.length();
    boolean first = true;
    for (AbstractCliCommandContainerGroup group : container.getGroups()) {
      int commandCount = group.getCommandCount();
      int count = 0;
      for (int i = 0; i < commandCount; i++) {
        CliCommandContainerImpl commandContainer = group.getCommand(i);
        CliCommand command = commandContainer.getCommand();
        if (command.hideFromHelp()) {
          continue;
        }
        count++;
        usageBuffer.setLength(usageMinLength);
        CliBundle bundle = commandContainer.getBundle();
        if (first) {
          first = false;
        } else {
          out.log();
        }
        printUsage(out, commandContainer, usageBuffer);
        String help = bundle.get("help");
        if ((help != null) && !help.isEmpty()) {
          out.log(help);
        }
      }
      if (count > 0) {
        printHelp(out, helpBundle, group.getProperties());
      }
    }

    printJvmOptions(out, helpBundle, console);
    return 0;
  }

  private void printUsage(CliOut out, CliCommandContainerImpl commandContainer, StringBuilder buffer) {

    for (CliPropertyContainerImpl property : commandContainer.getProperties()) {
      buffer.append(' ');
      buffer.append(property.getUsage());
    }
    out.log(buffer.toString());
  }

  private void printHelp(CliOut out, CliBundle helpBundle, List<CliPropertyContainerImpl> properties) {

    // group by type
    Map<CliPropertyType, CliPropertyGroup> type2property = new HashMap<>();
    for (CliPropertyContainerImpl property : properties) {
      if (property.isFlag() && property.isMandatory()) {
        continue;
      }
      CliPropertyType type = CliPropertyType.of(property);
      CliPropertyGroup group = type2property.computeIfAbsent(type, t -> new CliPropertyGroup(type));
      group.add(property);
    }

    // print
    for (CliPropertyType type : CliPropertyType.values()) {
      CliPropertyGroup group = type2property.get(type);
      if (group != null) {
        printHelp(out, helpBundle, group);
      }
    }
  }

  private StringBuilder printHelp(CliOut out, CliBundle helpBundle, CliPropertyGroup group) {

    out.log();
    out.log(helpBundle.get(group.getType().getKey()));
    int maxLength = group.getMaxLength();
    StringBuilder spaces = new StringBuilder(maxLength);
    for (CliPropertyContainerImpl property : group.getProperties()) {
      String syntax = property.getSyntax();
      int len = maxLength - syntax.length() + 2;
      spaces.setLength(0);
      for (int i = 0; i < len; i++) {
        spaces.append(' ');
      }
      out.log("  ", syntax, spaces, property.getHelp());
    }
    return spaces;
  }

  /**
   * @param out the {@link CliOut} for writing help output.
   * @param helpBundle the {@link CliBundle} for the help.
   * @param console the {@link CliConsole} if needed for output.
   */
  default void printJvmOptions(CliOut out, CliBundle helpBundle, CliConsole console) {

    CliPropertyGroup group = new CliPropertyGroup(CliPropertyType.JVM_OPTION);
    Locale locale = console.getLocale();
    addJvmSystemProperty("user.language", group, helpBundle, locale);
    addJvmOption("Xmx", group, helpBundle, locale);
    addJvmOption("Xloggc", group, helpBundle, locale);
    printHelp(out, helpBundle, group);
  }

  private void addJvmSystemProperty(String property, CliPropertyGroup group, CliBundle helpBundle, Locale locale) {

    addJvmOption(property, group, helpBundle, locale, "-D" + property, false);
  }

  private void addJvmOption(String property, CliPropertyGroup group, CliBundle helpBundle, Locale locale) {

    addJvmOption(property, group, helpBundle, locale, "-" + property, false);
  }

  private void addJvmOption(String property, CliPropertyGroup group, CliBundle helpBundle, Locale locale, String syntax,
      boolean flag) {

    String help = helpBundle.get("jvm." + property, "");
    if (help.isEmpty()) {
      help = NlsBundleCli.INSTANCE.msgJavaSystemProperty(property).getLocalizedMessage(locale);
    }
    group.add(new CliPropertyContainerImpl(syntax, syntax, syntax + "=<value>", help, false, true, flag));
  }

}
