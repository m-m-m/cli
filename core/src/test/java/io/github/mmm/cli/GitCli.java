package io.github.mmm.cli;

/**
 * Test to rebuild git CLI API.
 */
public class GitCli extends CliMain {

  @Override
  protected void addCommands() {

    super.addCommands();
    add(TagCreate.class);
    add(TagDelete.class);
    add(TagList.class);
    add(TagVerify.class);
  }

  /**
   * @param args the commandline arguments.
   */
  public static void main(String[] args) {

    new GitCli().runAndExit(args);
  }

}