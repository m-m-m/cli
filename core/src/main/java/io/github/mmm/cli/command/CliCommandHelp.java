/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.command;

import java.util.List;
import java.util.Locale;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.CliMain;
import io.github.mmm.cli.NlsBundleCli;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.cli.io.CliOut;
import io.github.mmm.cli.property.CliCommandWithProperties;
import io.github.mmm.cli.property.CliCommands;
import io.github.mmm.cli.property.CliPropertyGroup;
import io.github.mmm.cli.property.CliPropertyInfo;
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
    CliOut out = console.out(null);
    CliCommands cliCommands = main.getCliCommands();
    CliCommandWithProperties helpCwp = cliCommands.get(this);
    CliBundle helpBundle = helpCwp.getBundle();
    boolean printHelpPerCommand = main.isPrintHelpPerCommand();
    out.log(helpBundle.get("usage"));
    StringBuilder usageBuffer = new StringBuilder(main.getProgramName());
    int usageMinLength = usageBuffer.length();
    boolean first = true;
    for (CliCommandWithProperties commandWithProperties : cliCommands.get()) {
      usageBuffer.setLength(usageMinLength);
      CliBundle bundle = commandWithProperties.getBundle();
      if (first) {
        first = false;
      } else {
        out.log();
      }
      printUsage(out, commandWithProperties, usageBuffer);
      String help = bundle.get("help");
      if (help != null) {
        out.log(help);
      }
      printHelp(out, commandWithProperties, helpBundle);
    }
    printJvmOptions(out, helpBundle, console);
    return 0;
  }

  private void printUsage(CliOut out, CliCommandWithProperties properties, StringBuilder buffer) {

    printUsage(out, properties.getMandatoryOptions(), buffer);
    printUsage(out, properties.getAdditionalOptions(), buffer);
    printUsage(out, properties.getValues(), buffer);
    out.log(buffer.toString());
  }

  private void printUsage(CliOut out, CliPropertyGroup group, StringBuilder buffer) {

    List<CliPropertyInfo> properties = group.getPropeties();
    for (CliPropertyInfo propertyInfo : properties) {
      buffer.append(' ');
      buffer.append(propertyInfo.getUsage());
    }
  }

  private void printHelp(CliOut out, CliCommandWithProperties properties, CliBundle helpBundle) {

    printHelp(out, helpBundle.get("options.required"), properties.getMandatoryOptions());
    printHelp(out, helpBundle.get("options.optional"), properties.getAdditionalOptions());
    printHelp(out, helpBundle.get("parameters"), properties.getValues());
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
    group.add(new CliPropertyInfo(syntax, syntax, syntax + "=<value>", help, false, true, flag));
  }

  private void printHelp(CliOut out, String groupMessage, CliPropertyGroup group) {

    List<CliPropertyInfo> properties = group.getPropeties();
    if (properties.isEmpty()) {
      return;
    }
    boolean printed = false;
    int maxLength = group.getMaxLength();
    StringBuilder spaces = new StringBuilder(maxLength);
    for (CliPropertyInfo propertyInfo : properties) {
      if (!propertyInfo.isFlag() || !propertyInfo.isMandatory()) {
        if (!printed) {
          out.log();
          out.log(groupMessage);
          printed = true;
        }
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

}
