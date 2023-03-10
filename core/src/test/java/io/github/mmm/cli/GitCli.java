/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

/**
 * Test to rebuild git CLI API.
 */
public class GitCli extends CliMain {

  @SuppressWarnings("unchecked")
  @Override
  protected void addCommands() {

    super.addCommands();
    group(/* "tag" */).add(TagCreate.class, TagDelete.class, TagList.class, TagVerify.class);
  }

  /**
   * @param args the commandline arguments.
   */
  public static void main(String[] args) {

    new GitCli().runAndExit(args);
  }

}
