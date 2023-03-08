/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliArgs}.
 */
public class CliArgsTest extends Assertions {

  /** Test of {@link CliArgs#CliArgs(String...)}. */
  @Test
  public void testParse() {

    // given
    String[] args = { "-abc", "--help", "--foo-bar=some", "--foo-bar", "some", "-x=true", "--", "-file" };
    // when
    CliArgs cliArgs = new CliArgs(args);
    // then
    assertThat(cliArgs.isEmpty()).isFalse();
    assertThat(cliArgs.getSize()).isEqualTo(11);
    CliArgument argument = cliArgs.getFirst();
    assertThat(argument.get()).isEqualTo("-a");
    assertThat(argument.isShortOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-b");
    assertThat(argument.isShortOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-c");
    assertThat(argument.isShortOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("--help");
    assertThat(argument.isLongOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("--foo-bar");
    assertThat(argument.isLongOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isTrue();
    assertThat(argument.getValue()).isEqualTo("some");
    assertThat(argument.getValues()).containsExactly("some");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("some");
    assertThat(argument.isValue()).isTrue();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("--foo-bar");
    assertThat(argument.isLongOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    assertThat(argument.getValue()).isEqualTo("some");
    assertThat(argument.getValues()).containsExactly("some");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("some");
    assertThat(argument.isValue()).isTrue();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-x");
    assertThat(argument.isShortOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isTrue();
    assertThat(argument.getValue()).isEqualTo("true");
    assertThat(argument.getValues()).containsExactly("true");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("true");
    assertThat(argument.isValue()).isTrue();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-file");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getNext()).isNull();
  }

  /** Test of {@link CliArgs#CliArgs(String...)}. */
  @Test
  public void testOptionValue() {

    // given
    String[] args = { "--option", "value" };
    // when
    CliArgs cliArgs = new CliArgs(args);
    // then
    assertThat(cliArgs.isEmpty()).isFalse();
    assertThat(cliArgs.getSize()).isEqualTo(2);
    CliArgument argument = cliArgs.getFirst();
    assertThat(argument.get()).isEqualTo("--option");
    assertThat(argument.isLongOption()).isTrue();
    assertThat(((CliOption) argument).isAssignment()).isFalse();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("value");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getNext()).isNull();
  }

  /** Test of {@link CliArgs#CliArgs(String...)} and {@link CliArgument#getValueType()}. */
  @Test
  public void testValueType() {

    // given
    String[] args = { "first", "--option", "value1", "value2", "--key=value", "--", "arg" };
    // when
    CliArgs cliArgs = new CliArgs(args);
    // then
    assertThat(cliArgs.isEmpty()).isFalse();
    assertThat(cliArgs.getSize()).isEqualTo(7);
    CliArgument argument = cliArgs.getFirst();
    assertThat(argument.get()).isEqualTo("first");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_BEFORE_OPTION);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("--option");
    assertThat(argument.isOption()).isTrue();
    assertThat(argument.isLongOption()).isTrue();
    assertThat(argument.isShortOption()).isFalse();
    assertThat(argument.getValueType()).isNull();
    CliOption option = (CliOption) argument;
    assertThat(option.isAssignment()).isFalse();
    assertThat(option.getValues()).containsExactly("value1", "value2");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("value1");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.OPTION_VALUE);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("value2");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_CONTINUED);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("--key");
    assertThat(argument.isOption()).isTrue();
    assertThat(argument.isLongOption()).isTrue();
    assertThat(argument.isShortOption()).isFalse();
    assertThat(argument.getValueType()).isNull();
    option = (CliOption) argument;
    assertThat(option.isAssignment()).isTrue();
    assertThat(option.getValues()).containsExactly("value");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("value");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.OPTION_ASSIGNMENT);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("arg");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.isEndOptions()).isTrue();
    assertThat(argument.getNext()).isNull();
  }

  /** Test of {@link CliArgs#CliArgs(String...)} with edge-case. */
  @Test
  public void testEdgeCase() {

    // given
    String[] args = { "-", "-=", "=", ",", "--", "-f" };
    // when
    CliArgs cliArgs = new CliArgs(args);
    // then
    assertThat(cliArgs.isEmpty()).isFalse();
    assertThat(cliArgs.getSize()).isEqualTo(5);
    CliArgument argument = cliArgs.getFirst();
    assertThat(argument.get()).isEqualTo("-");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_BEFORE_OPTION);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-=");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_BEFORE_OPTION);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("=");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_BEFORE_OPTION);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo(",");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_BEFORE_OPTION);
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-f");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getValueType()).isSameAs(CliValueType.VALUE_END_OPTION);
  }
}
