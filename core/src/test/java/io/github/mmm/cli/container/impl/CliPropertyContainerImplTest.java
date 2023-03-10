/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli.container.impl;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CliPropertyContainerImpl}.
 */
public class CliPropertyContainerImplTest extends Assertions {

  /** Test of {@link CliPropertyContainerImpl#compareTo(CliPropertyContainerImpl)}. */
  @Test
  public void testCompareTo() {

    // given
    CliPropertyContainerImpl[] order = { keyword("0", 0), reqOptFlag("1"), reqOptNoFlag("2"), addOptFlag("3"),
    reqValue("4", 1), addValue("5", 2) };

    // when + then in order
    SoftAssertions assertions = new SoftAssertions();
    for (int start = 0; start < order.length; start++) {
      CliPropertyContainerImpl left = order[start];
      assertions.assertThat(left).isEqualByComparingTo(left);
      for (int i = start + 1; i < order.length; i++) {
        CliPropertyContainerImpl right = order[i];
        assertions.assertThat(left).isLessThan(right);
        assertions.assertThat(right).isGreaterThan(left);
      }
    }
    assertions.assertAll();
  }

  private static CliPropertyContainerImpl keyword(String name, int i) {

    return newProperty(name, true, false, true, false, i);
  }

  private static CliPropertyContainerImpl reqOptNoFlag(String name) {

    return newProperty(name, true, true, false, false, -1);
  }

  private static CliPropertyContainerImpl reqOptFlag(String name) {

    return newProperty(name, true, true, true, false, -1);
  }

  private static CliPropertyContainerImpl reqValue(String name, int i) {

    return newProperty(name, true, false, false, true, i);
  }

  private static CliPropertyContainerImpl addOptFlag(String name) {

    return newProperty(name, false, true, true, false, -1);
  }

  private static CliPropertyContainerImpl addValue(String name, int i) {

    return newProperty(name, false, false, false, true, i);
  }

  private static CliPropertyContainerImpl newProperty(String name, boolean mandatory, boolean option, boolean flag,
      boolean value, int i) {

    String usage = name;
    if (option) {
      usage = "--" + usage;
      if (!flag) {
        usage = usage + "=";
      }
    }
    if (value) {
      usage = "<" + usage + ">";
    }
    if (!mandatory) {
      usage = "[" + usage + "]";
    }
    return new CliPropertyContainerImpl(name, name, usage, name, mandatory, option, flag, value, i);
  }

}
