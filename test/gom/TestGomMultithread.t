/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package gom;

import org.junit.Test;
import org.junit.Assert;
import gom.testgommultithread.term.types.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class TestGomMultithread {

  %gom(--multithread) {
    module term
    abstract syntax
    T = a()
      | b()
      | c()
      | f(One:T,Two:T,Three:T,Four:T)
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestGomMultithread.class.getName());
  }

  @Test
  public void testMulti() {
    final int DEPTH = 4;
    final int TASKS = 10;
    final int COUNT = 100;
    final AtomicInteger count = new AtomicInteger(0);
    final ExecutorService executor = Executors.newFixedThreadPool(TASKS);
    for (int i = 0; i < TASKS; i++) {
      executor.execute(new Runnable() {
        public void run() {
          for (int i = 0; i < COUNT; i++) {
            T res = task();
            T res2 = task();
            if (res == res2) {
              count.incrementAndGet();
            }
          }
        }
        private T task() {
          T res = `a();
          for(int i = 0; i < DEPTH; ++i) {
            res = `f(res, b(), c(), res);
          }
          return res;
        }
      });
    }
    executor.shutdown();
    try {
      executor.awaitTermination(20,TimeUnit.SECONDS);
    } catch (java.lang.InterruptedException e) {
      Assert.fail("Timeout");
    }
    Assert.assertEquals("Successful tasks", TASKS*COUNT, count.get());
  }
}
