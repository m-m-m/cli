/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.cli.io.impl.CliConsoleImpl;

/**
 * Test of {@link CliMain}.
 */
public class CliMainTest extends Assertions {

  private static final String HELP_EN = "Usage:\n" //
      + "io.github.mmm.cli.TestProgram --help|-h\n" //
      + "Print this help.\n\n" //
      + "io.github.mmm.cli.TestProgram --version|-v\n" //
      + "Print the version of this program.\n\n" //
      + "io.github.mmm.cli.TestProgram --mode|-m <mode> [--key <key>...] <value>\n" //
      + "Test the command-line-interface.\n\n" //
      + "Required options:\n" //
      + "  --mode|-m  Mode of test.\n\n" //
      + "Additional options:\n" //
      + "  --key  Keys of test.\n\n" //
      + "Parameters:\n" //
      + "  value  Value of test.\n\n" //
      + "JVM options:\n" //
      + "  -Duser.language  The locale for translation of this commandline output (e.g. '-Duser.language=de' for German).\n"//
      + "  -Xmx             The maximum heap size (e.g. '-Xmx8G' for 8 gigabyte of heap memory).\n" //
      + "  -Xloggc          Writes all garbage collector activity to the specified logfile (e.g. '-Xloggc:/home/user/log/gc.log').\n";

  private static final String HELP_DE = "Verwendung:\n" //
      + "io.github.mmm.cli.TestProgram --help|-h\n" //
      + "Gibt diese Hilfe aus.\n\n" //
      + "io.github.mmm.cli.TestProgram --version|-v\n" //
      + "Gibt die Version dieses Programms aus.\n\n" //
      + "io.github.mmm.cli.TestProgram --mode|-m <mode> [--key <key>...] <value>\n" //
      + "Test der Kommandozeilen-Schnittstelle.\n\n" //
      + "Erforderliche Optionen:\n" //
      + "  --mode|-m  Modus des Tests.\n\n" //
      + "Optionale Optionen:\n" //
      + "  --key  Schlüssel des Tests.\n\n" //
      + "Parameter:\n" //
      + "  value  Wert des Tests.\n\n" //
      + "JVM Optionen:\n" //
      + "  -Duser.language  Die Sprache zur Übersetzung dieser Kommandozeilenausgabe (z.B. '-Duser.language=en' für Englisch).\n"//
      + "  -Xmx             Die maximale Heap Größe (z.B. '-Xmx8G' für 8 Gigabyte Heap-Speicher).\n" //
      + "  -Xloggc          Schreibt alle Aktivitäten des Garbage Collectors in die angegebene Logdatei (z.B. '-Xloggc:/home/user/log/gc.log').\n";

  /** Test of {@link CliMain} with help option. */
  @Test
  public void testHelp() {

    String expectedErr = "";

    assertProgram(0, HELP_EN, expectedErr, "-h");
    assertProgram(0, HELP_DE, expectedErr, Locale.GERMAN, "--help");
  }

  /** Test of {@link CliMain} with version option. */
  @Test
  public void testVersion() {

    String expectedOut = "1.2.3.4\n";
    String expectedErr = "";

    assertProgram(0, expectedOut, expectedErr, "-v");
    assertProgram(0, expectedOut, expectedErr, "--version");
  }

  /** Test of {@link CliMain} ({@link TestProgram}) with custom option. */
  @Test
  public void testCustom() {

    assertProgram(0, "42\n", "", "--mode", "JUnit", "42");
    assertProgram(1, "",
        "ERROR: No arguments were specified. Please call with --help to read usage and provide required arguments.\n");
  }

  /** Test of {@link CliMain} ({@link TestProgram}) with multiple values for keys. */
  @Test
  public void testMultiValue() {

    assertProgram(0, "42\n[key1, key2]\n", "", "--mode", "JUnit", "--key", "key1", "--key", "key2", "42");
  }

  private void assertProgram(int expectedCode, String expectedOut, String expectedErr, String... args) {

    assertProgram(expectedCode, expectedOut, expectedErr, null, args);
  }

  private void assertProgram(int expectedCode, String expectedOut, String expectedErr, Locale locale, String... args) {

    // given
    Locale l = Locale.getDefault();
    if (locale != null) {
      Locale.setDefault(locale);
    }
    TestProgram prg = new TestProgram();
    CliConsoleImpl console = (CliConsoleImpl) prg.console();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    console.setStdOut(new PrintStream(out));
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    console.setStdErr(new PrintStream(err));
    // when
    int code = prg.run(args);
    if (locale != null) {
      Locale.setDefault(l);
    }
    // then
    SoftAssertions soft = new SoftAssertions();
    soft.assertThat(err.toString().replace("\r", "")).isEqualTo(expectedErr);
    soft.assertThat(out.toString().replace("\r", "")).isEqualTo(expectedOut);
    soft.assertThat(code).isEqualTo(expectedCode);
    soft.assertAll();
  }

}
