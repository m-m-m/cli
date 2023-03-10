/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.cli.arg.CliArgs;
import io.github.mmm.cli.io.impl.CliConsoleImpl;

/**
 * Test of {@link GitCli}.
 */
public class GitCliTest extends Assertions {

  /** Test of {@link GitCli} (git tag -a release/1.0.0 ...). */
  @Test
  public void testGitTagCreate() {

    assertProgram(
        "TagCreate(readonly=false,dynamic,Annotate=true,CommitOrObject=null,Edit=null,File=null,Force=true,Message=[this is a message],NoSign=null,Sign=null,SignKey=null,Tag=true,TagName=release/1.0.0)\n",
        "tag", "-a", "release/1.0.0", "-m", "this is a message", "-f");
  }

  /** Test of {@link GitCli} (git tag -l ...). */
  @Test
  public void testGitTagList() {

    assertProgram(
        "TagList(readonly=false,dynamic,Color=never,Column=hash,Format=[%(refname:strip=2)],IgnoreCase=null,List=true,Sort=[date],Tag=true)\n",
        "tag", "-l", "--column=hash", "--sort=date", "--format=%(refname:strip=2)", "--color=never");
  }

  /** Test of {@link GitCli} (git tag -d release/1.0.0). */
  @Test
  public void testGitTagDelete() {

    assertProgram("TagDelete(readonly=false,dynamic,Delete=true,Tag=true,TagName=release/1.0.0)\n", "tag", "-d",
        "release/1.0.0");
  }

  /** Test of {@link GitCli} (git tag -v release/1.0.0). */
  @Test
  public void testGitTagVerify() {

    assertProgram(
        "TagVerify(readonly=false,dynamic,Format=[%(refname:strip=2)],Tag=true,TagName=release/1.0.0,Verify=true)\n",
        "tag", "-v", "--format=%(refname:strip=2)", "release/1.0.0");
  }

  private void assertProgram(String expectedOut, String... args) {

    // given
    GitCli prg = new GitCli();
    CliConsoleImpl console = (CliConsoleImpl) prg.console();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    console.setStdOut(new PrintStream(out));
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    console.setStdErr(new PrintStream(err));
    // when
    int code = prg.run(new CliArgs(args));
    // then
    SoftAssertions soft = new SoftAssertions();
    soft.assertThat(out.toString().replace("\r", "")).isEqualTo(expectedOut);
    soft.assertThat(code).isEqualTo(0);
    soft.assertThat(err.toString()).isEmpty();
    soft.assertAll();
  }

}
