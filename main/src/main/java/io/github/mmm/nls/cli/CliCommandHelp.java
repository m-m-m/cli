/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.nls.cli;

import java.util.List;
import java.util.Locale;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.CliOut;
import io.github.mmm.nls.cli.impl.CliProperties;
import io.github.mmm.nls.cli.impl.CliPropertyGroup;
import io.github.mmm.nls.cli.impl.CliPropertyInfo;
import io.github.mmm.property.WritableProperty;
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
  default int run(NlsMain main) {

    CliConsole console = main.console();
    CliOut out = console.out(null);
    CliProperties properties = new CliProperties();
    CliBundle helpBundle = CliBundle.of(CliCommandHelp.class, console);
    out.log(helpBundle.get("usage"));
    boolean printHelpPerCommand = main.isPrintHelpPerCommand();
    StringBuilder usageBuffer = new StringBuilder(main.getProgramName());
    int usageMinLength = usageBuffer.length();
    List<CliCommand> commands = main.getCommands();
    int max = commands.size() - 1;
    for (int i = 0; i <= max; i++) {
      CliCommand command = commands.get(i);
      if (i > 0) {
        out.log();
      }
      CliBundle bundle;
      if (command instanceof CliCommandHelp) {
        bundle = helpBundle;
      } else {
        bundle = CliBundle.of(command, console);
      }
      for (WritableProperty<?> property : command.getProperties()) {
        CliPropertyInfo propertyInfo = new CliPropertyInfo(property, command, console, bundle);
        properties.add(propertyInfo);
        usageBuffer.append(' ');
        usageBuffer.append(propertyInfo.getUsage());
      }
      out.log(usageBuffer.toString());
      String help = bundle.get("help");
      if (help != null) {
        out.log(help);
      }
      usageBuffer.setLength(usageMinLength);
      if (printHelpPerCommand) {
        printHelp(out, properties, helpBundle);
        properties.clear();
      }
    }
    if (!printHelpPerCommand) {
      printHelp(out, properties, helpBundle);
    }
    printJvmOptions(out, helpBundle, console);
    return 0;
  }

  private void printHelp(CliOut out, CliProperties properties, CliBundle helpBundle) {

    printHelp(out, helpBundle.get("options.required"), properties.getMandatoryOptions());
    printHelp(out, helpBundle.get("options.optional"), properties.getAdditionalOptions());
    printHelp(out, helpBundle.get("parameters"), properties.getParameters());
  }

  /**
   * @param out the {@link CliOut} for writing help output.
   * @param helpBundle the {@link CliBundle} for the help.
   * @param console the {@link CliConsole} if needed for output.
   */
  default void printJvmOptions(CliOut out, CliBundle helpBundle, CliConsole console) {

    CliPropertyGroup group = new CliPropertyGroup();
    Locale locale = console.getLocale();
    addJvmSystemProperty("user.language", group, helpBundle, locale);
    addJvmOption("Xmx", group, helpBundle, locale);
    addJvmOption("Xloggc", group, helpBundle, locale);
    printHelp(out, helpBundle.get("jvm"), group);
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
    group.add(new CliPropertyInfo(syntax, syntax + "=<value>", help, false, true, flag));
  }

  private void printHelp(CliOut out, String groupMessage, CliPropertyGroup group) {

    List<CliPropertyInfo> properties = group.getPropeties();
    if (properties.isEmpty()) {
      return;
    }
    out.log();
    out.log(groupMessage);
    int maxLength = group.getMaxLength();
    StringBuilder spaces = new StringBuilder(maxLength);
    for (CliPropertyInfo propertyInfo : properties) {
      String syntax = propertyInfo.getSyntax();
      int len = maxLength - syntax.length() + 2;
      spaces.setLength(0);
      for (int i = 0; i < len; i++) {
        spaces.append(' ');
      }
      out.log("  ", syntax, spaces, propertyInfo.getHelp());
    }
  }

}
