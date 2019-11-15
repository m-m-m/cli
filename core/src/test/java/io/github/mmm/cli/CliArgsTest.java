/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliArgument}.
 */
public class CliArgsTest extends Assertions {

  /** Test of {@link CliArgument#parse(String...)}. */
  @Test
  public void testParse() {

    // given
    String[] args = { "-abc", "--help", "--foo-bar=some", "--foo-bar", "some", "-x=true", "--", "-file" };
    // when
    CliArgs cliArgs = new CliArgs(args);
    // then
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliShortOption("-a"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliShortOption("-b"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliShortOption("-c"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliLongOption("--help"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliLongOption("--foo-bar"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliValue("some", false));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliLongOption("--foo-bar"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliValue("some", false));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliShortOption("-x"));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliValue("true", false));
    assertThat(cliArgs.hasNext()).isTrue();
    assertThat(cliArgs.next()).isEqualTo(new CliValue("-file", true));
    assertThat(cliArgs.hasNext()).isFalse();
    assertThat(cliArgs.next()).isNull();
  }

}
