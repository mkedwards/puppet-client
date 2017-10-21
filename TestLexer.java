/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2004-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * Adapted to puppet-client by Michael Edwards <m.k.edwards@gmail.com>     *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.io.*;
import java_cup.runtime.Symbol;


/**
 * Simple test driver for the puppet lexer. Just runs it on some
 * input files and produces debug output. Needs symbol class from
 * parser. 
 */
public class TestLexer {

  public static void main(String argv[]) {

    for (int i = 0; i < argv.length; i++) {
      try {
        System.out.println("Lexing ["+argv[i]+"]");
        Scanner scanner = new Scanner(new UnicodeEscapes(new FileReader(argv[i])));
                
        Symbol s;
        do {
          s = scanner.debug_next_token();
          System.out.println("token: "+s);
        } while (s.sym != sym.EOF);
        
        System.out.println("No errors.");
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
        System.exit(1);
      }
    }
  }
}
