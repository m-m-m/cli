/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.arg;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliValue}.
 */
public class CliValueTest extends Assertions {

  /** Test of {@link CliValue}. */
  @Test
  public void testDefaults() {

    // given
    String value = "value";
    CliValueType type = CliValueType.VALUE_BEFORE_OPTION;
    // when
    CliValue cliValue = new CliValue(value, type);
    // then
    assertThat(cliValue.get()).isSameAs(value);
    assertThat(cliValue.getValue()).isSameAs(value);
    assertThat(cliValue.toString()).isEqualTo(value);
    assertThat(cliValue.isValue()).isTrue();
    assertThat(cliValue.isOption()).isFalse();
    assertThat(cliValue.isShortOption()).isFalse();
    assertThat(cliValue.isLongOption()).isFalse();
    assertThat(cliValue.getValueType()).isSameAs(type);
    assertThat(cliValue.isEndOptions()).isFalse();
    assertThat(cliValue.getNext()).isNull();
    assertThat(cliValue.getOptionName()).isNull();
  }

  /** Test of {@link CliValue}. */
  @Test
  public void testEndOptions() {

    // given
    String value = "its magic";
    CliValueType type = CliValueType.VALUE_END_OPTION;
    // when
    CliValue cliValue = new CliValue(value, type);
    // then
    assertThat(cliValue.get()).isSameAs(value);
    assertThat(cliValue.getValue()).isSameAs(value);
    assertThat(cliValue.toString()).isEqualTo(value);
    assertThat(cliValue.isValue()).isTrue();
    assertThat(cliValue.isOption()).isFalse();
    assertThat(cliValue.isShortOption()).isFalse();
    assertThat(cliValue.isLongOption()).isFalse();
    assertThat(cliValue.getValueType()).isSameAs(type);
    assertThat(cliValue.isEndOptions()).isTrue();
  }

}
