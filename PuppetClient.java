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

import java.io.IOException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PuppetClient {

  static String url;

  public final static void main(String[] argv) throws Exception {
    url = argv[0];
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    executorService.scheduleAtFixedRate(new Runnable() {
        @Override
        public void run() {
            action();
        }
    }, 0, 300, TimeUnit.SECONDS);
  }

  public static void UpdateFileFromSource(String file, String source) {
        System.out.println("Fetching ["+file+"] from ["+source+"]");
  }

  private static void action() {
      try {
        System.out.println("Fetching configuration from ["+url+"]");
        String config = getConfig(url);

        Scanner scanner = new Scanner(new UnicodeEscapes(new StringReader(config)));
        Symbol s;
        do {
          s = scanner.debug_next_token();
        } while (s.sym != sym.EOF);

        System.out.println("No errors.");
      }
      catch (Exception e) {
        e.printStackTrace(System.out);
      }
  }

    private static String getConfig(String url) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
    		new DefaultHttpMethodRetryHandler(3, false));
        try {
          int statusCode = client.executeMethod(method);
          if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
          }
          byte[] responseBody = method.getResponseBody();
          return new String(responseBody);
        } catch (HttpException e) {
          System.err.println("Fatal protocol violation: " + e.getMessage());
          e.printStackTrace();
          throw e;
        } catch (IOException e) {
          System.err.println("Fatal transport error: " + e.getMessage());
          e.printStackTrace();
          throw e;
        } finally {
          // Release the connection.
          method.releaseConnection();
        }
    }
}
