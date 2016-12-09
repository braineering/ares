/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Giacomo Marciani, Michele Porretta
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.acmutv.botnet.tool;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * This class realizes file system watching services.
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 * @see WatchService
 */
public class WatchTools {

  /**
   * Registers a runnable as a file watching hook.
   * @param filepath the path to watch.
   * @param hook the runnable.
   * @throws IOException when the path is not correct.
   */
  public static void watchFile(Path filepath, Runnable hook) throws IOException {
    WatchService watcher = FileSystems.getDefault().newWatchService();
    Path dir = filepath.getParent();
    try {
      dir.register(watcher, ENTRY_MODIFY);
    } catch (IOException e) {
      System.err.println(e);
    }

    while (true) {
      WatchKey key;
      try {
         key = watcher.take();
      } catch (InterruptedException e) {
        return;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        System.out.println("EVENT: " + kind.name());

        if (kind == OVERFLOW) {
          continue;
        }

        WatchEvent<Path> ev = (WatchEvent<Path>) event;

        if (dir.resolve(ev.context()).toAbsolutePath().equals(filepath.toAbsolutePath())) {
          Thread thread = new Thread(hook);
          thread.run();
        }
      }

      if (!key.reset()) {
        break;
      }
    }
  }
}
