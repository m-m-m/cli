/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliArgument}.
 */
public class CliArgumentTest extends Assertions {

  /** Test of {@link CliArgument#parse(String...)}. */
  @Test
  public void testParse() {

    // given
    String[] args = { "-abc", "--help", "--foo-bar=some", "--foo-bar", "some", "-x=true", "--", "-file" };
    // when
    List<CliArgument> cliArgs = CliArgument.parse(args);
    // then
    assertThat(cliArgs)
        .containsExactly(new CliArgument[] { new CliShortOption("-a", false), new CliShortOption("-b", false),
        new CliShortOption("-c", false), new CliLongOption("--help", false), new CliLongOption("--foo-bar", true),
        new CliValue("some", false), new CliLongOption("--foo-bar", false), new CliValue("some", false),
        new CliShortOption("-x", true), new CliValue("true", false), new CliValue("-file", true) });
  }

}
