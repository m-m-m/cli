/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.cli.CliBundle;
import io.github.mmm.cli.arg.CliArgs;
import io.github.mmm.cli.arg.CliArgument;
import io.github.mmm.cli.arg.CliOption;
import io.github.mmm.cli.arg.CliValue;
import io.github.mmm.cli.arg.CliValueType;
import io.github.mmm.cli.command.CliCommand;
import io.github.mmm.cli.container.CliCommandContainer;
import io.github.mmm.cli.io.CliConsole;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.booleans.BooleanProperty;
import io.github.mmm.property.container.WritableContainerProperty;
import io.github.mmm.property.container.collection.WritableCollectionProperty;
import io.github.mmm.property.enumeration.EnumProperty;
import io.github.mmm.property.number.NumberProperty;
import io.github.mmm.property.object.WritableSimpleProperty;

/**
 * Implementation of {@link CliCommandContainer}.
 *
 * @since 1.0.0
 */
public class CliCommandContainerImpl extends AbstractCliCommandContainerGroup implements CliCommandContainer {

  private final CliCommand command;

  private final List<CliPropertyContainerImpl> properties;

  private final Map<String, CliPropertyContainerImpl> propertyMap;

  private final CliBundle bundle;

  private boolean sorted;

  /**
   * The constructor.
   *
   * @param command the {@link CliCommand} owning the properties.
   * @param console the {@link CliConsole}.
   */
  public CliCommandContainerImpl(CliCommand command, CliConsole console) {

    super(console);
    this.command = command;
    this.properties = new ArrayList<>();
    this.propertyMap = new HashMap<>();
    this.bundle = CliBundle.of(command, console);
  }

  @Override
  public CliCommand getCommand() {

    return this.command;
  }

  @Override
  public CliPropertyContainerImpl getProperty(String alias) {

    return this.propertyMap.get(alias);
  }

  @Override
  public List<CliPropertyContainerImpl> getProperties() {

    if (!this.sorted) {
      Collections.sort(this.properties);
      this.sorted = true;
    }
    return this.properties;
  }

  @Override
  public String getName() {

    return this.command.getType().getQualifiedName();
  }

  @Override
  public int getCommandCount() {

    return 1;
  }

  @Override
  public CliCommandContainerImpl getCommand(int i) {

    if (i == 0) {
      return this;
    }
    throw new IndexOutOfBoundsException(i);
  }

  @Override
  public CliCommandContainerImpl getCommand(CliCommand cliCommand) {

    if (this.command == cliCommand) {
      return this;
    }
    return null;
  }

  /**
   * @param propertyInfo the {@link CliPropertyContainerImpl} to add.
   */
  public void add(CliPropertyContainerImpl propertyInfo) {

    for (String alias : propertyInfo.getAliases()) {
      put(alias, propertyInfo);
    }
    this.properties.add(propertyInfo);
    assert (!this.sorted);
    this.sorted = false;
  }

  private void put(String key, CliPropertyContainerImpl propertyInfo) {

    CliPropertyContainerImpl duplicate = this.propertyMap.putIfAbsent(key, propertyInfo);
    if (duplicate != null) {
      String prefix = this.command.getType().getSimpleName() + ".";
      throw new DuplicateObjectException(prefix + propertyInfo.getPropertyName(), key,
          prefix + duplicate.getPropertyName());
    }
  }

  /**
   * @return the {@link CliBundle} for this {@link #getCommand() command}.
   */
  public CliBundle getBundle() {

    return this.bundle;
  }

  /**
   * @param args the current {@link CliArgs}.
   * @param suggestions the {@link Set} where to add suggestions for auto-completion.
   */
  public void autoComplete(CliArgs args, Set<String> suggestions) {

    CliArgument argument = args.getFirst();
    Set<CliPropertyContainerImpl> propertiesSet = new HashSet<>();
    propertiesSet.addAll(this.properties);
    ValueIndex valueIndex = new ValueIndex();
    boolean lastMatches = true;
    CliOption lastOption = null;
    CliArgument last = argument;
    while (argument != null) {
      CliArgument next = argument.getNext();
      boolean option = argument.isOption();
      if (option) {
        lastOption = (CliOption) argument;
      }
      CliPropertyContainerImpl propertyInfo = findPropertyInfo(argument, valueIndex);
      if (propertyInfo == null) {
        if (next == null) {
          lastMatches = false;
        } else {
          return; // argument cannot be matched
        }
      } else if (!propertyInfo.isVararg()) {
        propertiesSet.remove(propertyInfo);
        if (option) {
          if ((next != null) && next.isOption()) {
            if (!propertyInfo.isFlag()) {
              return; // option requires value and can not be followed by another option
            }
          } else {
            if (canAcceptOptionValue(propertyInfo, (CliValue) next, suggestions)) {
              if (next == null) {
                lastMatches = true;
              } else {
                argument = next;
                next = next.getNext();
              }
            } else {
              return;
            }
          }
        }
      }
      last = argument;
      argument = next;
    }
    if (lastMatches) {
      for (CliPropertyContainerImpl propertyInfo : propertiesSet) {
        for (String alias : propertyInfo.getAliases()) {
          if (!CliPropertyContainerImpl.isIndex(alias)) {
            suggestions.add(alias);
          }
        }
      }
    } else {
      String prefix = last.get();
      for (CliPropertyContainerImpl propertyInfo : propertiesSet) {
        for (String alias : propertyInfo.getAliases()) {
          if (!CliPropertyContainerImpl.isIndex(alias) && alias.startsWith(prefix)) {
            suggestions.add(alias);
          }
        }
      }
    }
  }

  private boolean canAcceptOptionValue(CliPropertyContainerImpl propertyInfo, CliValue value, Set<String> suggestions) {

    WritableProperty<?> property = this.command.getRequiredProperty(propertyInfo.getPropertyName());
    String valueAsString = "";
    boolean complete = false;
    if (value != null) {
      complete = (value.getNext() != null);
      valueAsString = value.get();
    }
    if (property instanceof EnumProperty) {
      for (Object e : property.getValueClass().getEnumConstants()) {
        String key = e.toString();
        if (complete) {
          if (valueAsString.equals(key)) {
            return true;
          }
        } else if ((key != null) && key.startsWith(valueAsString)) {
          suggestions.add(key); // TODO do we need to suggest the rest of the string?
        }
      }
    } else if (property instanceof NumberProperty) {
      // has to be numeric
    } else if (property instanceof BooleanProperty) {
      if (valueAsString.isEmpty() && (value == null)) {
        return true;
      }
      String lowerCase = valueAsString.toLowerCase(Locale.ROOT);
      if (complete) {
        if ("true".equals(lowerCase) || "false".equals(lowerCase)) {
          return true;
        }
      } else {
        if ("true".startsWith(lowerCase)) {
          suggestions.add("true");
        }
        if ("false".startsWith(lowerCase)) {
          suggestions.add("false");
        }
      }
    }
    return false;
  }

  /**
   * @param args the {@link CliArgs} to bind.
   * @return {@code true} if the given {@link CliArgs} match this {@link #getCommand() command}, {@code false}
   *         otherwise.
   */
  public boolean bindCommandArguments(CliArgs args) {

    CliArgument argument = args.getFirst();
    Set<String> mandatoryProperties = this.properties.stream().filter(p -> p.isMandatory())
        .map(p -> p.getPropertyName()).collect(Collectors.toSet());
    ValueIndex valueIndex = new ValueIndex();
    while (argument != null) {
      CliPropertyContainerImpl propertyInfo = findPropertyInfo(argument, valueIndex);
      if (propertyInfo == null) {
        String value = argument.get();
        if (argument.isOption()) {
          this.console.debug().logFormat("Undefined option %s for command %s", value,
              this.command.getType().getSimpleName());
        } else {
          this.console.debug().logFormat("Too many values - value %s at position %s is undefined for command %s", value,
              valueIndex.get(), this.command.getType().getSimpleName());
        }
        return false;
      }
      String propertyName = propertyInfo.getPropertyName();
      WritableProperty<?> property = this.command.getRequiredProperty(propertyName);
      argument = setValue(property, argument, propertyInfo);
      mandatoryProperties.remove(propertyName);
    }
    return mandatoryProperties.isEmpty();
  }

  private CliPropertyContainerImpl findPropertyInfo(CliArgument argument, ValueIndex valueIndex) {

    CliPropertyContainerImpl propertyInfo = null;
    boolean option = argument.isOption();
    if (option) {
      propertyInfo = getProperty(argument.get());
    }
    if (propertyInfo == null) {
      propertyInfo = getProperty(valueIndex.get());
      if ((propertyInfo != null) && !propertyInfo.isWildcard()) {
        if (option) {
          propertyInfo = null;
        } else {
          List<String> aliases = propertyInfo.getAliases();
          if ((aliases.size() > 1) && !aliases.contains(argument.get())) {
            propertyInfo = null;
          }
        }
      }
      if (propertyInfo != null && !propertyInfo.isVararg()) {
        valueIndex.next();
      }
    }
    return propertyInfo;
  }

  private CliArgument setValue(WritableProperty<?> property, CliArgument arg, CliPropertyContainerImpl propertyInfo) {

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

  private CliArgument setValue(CliValue value, WritableProperty<?> property, CliPropertyContainerImpl propertyInfo) {

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
          if (propertyInfo.getIndex() == -1) {
            for (String item : valueAsString.split(",")) {
              addValueToCollection(collection, item, property, valueSimpleProperty);
            }
          } else {
            boolean wildcard = propertyInfo.getAliases().contains(CliCommand.ALIAS_WILDCARD);
            CliArgument arg = value;
            while (arg != null) {
              if (wildcard || arg.isValue()) {
                addValueToCollection(collection, arg.get(), property, valueSimpleProperty);
              } else {
                // TODO no further values are allowed from here...
                break;
              }
              arg = arg.getNext();
            }
            return arg;
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
      throw new IllegalStateException(
          "Invalid value '" + valueAsString + "' for type " + valueProperty.getClass().getSimpleName(), e);
    }
  }

  private void invalidProperty(WritableProperty<?> property, WritableProperty<?> valueProperty) {

    throw new IllegalStateException("Invalid property " + property.getQualifiedName() + " of type "
        + property.getClass().getSimpleName() + " having type " + property.getValueClass().getSimpleName()
        + ((valueProperty == null) ? "" : " with value type " + valueProperty.getValueClass())
        + ". CLI programs shall only use simple properties or collection properties with simple value properties!");
  }

  @Override
  public String toString() {

    return this.command.getType().getSimpleName();
  }

  /**
   * @param command the {@link CliCommand}.
   * @param console the {@link CliConsole} for logging.
   * @return the new {@link CliCommandContainerImpl} instance corresponding to the given {@link #getCommand() command}.
   */
  public static CliCommandContainerImpl of(CliCommand command, CliConsole console) {

    CliCommandContainerImpl commandContainer = new CliCommandContainerImpl(command, console);
    for (WritableProperty<?> property : command.getProperties()) {
      CliPropertyContainerImpl propertyInfo = new CliPropertyContainerImpl(property, command, commandContainer.bundle);
      commandContainer.add(propertyInfo);
    }
    int i = 0;
    boolean mandatory = true;
    WritableContainerProperty<?, ?> vararg = null;
    for (CliPropertyContainerImpl propertyContainer : commandContainer.getProperties()) {
      if (propertyContainer.isOption()) {
        continue;
      }
      String propertyName = propertyContainer.getPropertyName();
      if (vararg != null) {
        throw new IllegalStateException("Invalid value " + command.getType().getSimpleName() + "." + propertyName
            + "() with index " + propertyContainer.getIndex() + " - no further value is allowed after vararg "
            + vararg.getName() + "().");
      }
      if (propertyContainer.getIndex() != i) {
        throw new IllegalStateException("Invalid value " + command.getType().getSimpleName() + "." + propertyName
            + "() with index " + propertyContainer.getIndex() + " but expected index " + i
            + " - please fix @PropertyAlias annotation.");
      }
      if (mandatory) {
        mandatory = propertyContainer.isMandatory();
      } else if (propertyContainer.isMandatory()) {
        throw new IllegalStateException("Invalid value " + command.getType().getSimpleName() + "." + propertyName
            + "() with index " + propertyContainer.getIndex() + " can not be mandatory if previous value is optional.");
      }
      WritableProperty<?> property = command.getRequiredProperty(propertyName);
      if (property instanceof WritableContainerProperty) {
        vararg = (WritableContainerProperty<?, ?>) property;
      }
      i++;
    }
    return commandContainer;
  }

}
