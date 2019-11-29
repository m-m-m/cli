/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

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
    assertThat(argument.getValues()).containsExactly("true", "-file");
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("true");
    assertThat(argument.isValue()).isTrue();
    argument = argument.getNext();
    assertThat(argument.get()).isEqualTo("-file");
    assertThat(argument.isValue()).isTrue();
    assertThat(argument.getNext()).isNull();
  }

}
