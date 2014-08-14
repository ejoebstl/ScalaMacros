/**
 * Copyright (C) 2013 Carnegie Mellon University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalaMacros.application

import scalaMacros.macros.FindFreeVars

object Main {

  def main(args: Array[String]) {

    println("Snippet #1 - expected: List((x,2), (fun,<function1>))")
    snip1()
    println("Snippet #4 - expected: List((b,Test), (x,2))")
    snip4()
    println("Snippet #5 - expected: inner: List((z,7)), outer: List((x,2))")
    snip5()
    println("Snippet #6 - expected: List((k,3))")
    snip6()
    println("Snippet #7 - expected: List((k,3))")
    snip7()
    println("Snippet #8 - expected: List((k,3))")
    snip8()
    println("Snippet #9 - expected: List((t,TestCase(1,2)))")
    snip9()
    println("Snippet #10 - expected: Snippet #10")
    snip10()

    /**
     * Output given by test run:
     *
     * Snippet #1
     * List((x,2))
     * Snippet #4
     * Test
     * List((b,Test), (x,2))
     * Snippet #5
     * inner List((z,7), (x,2))
     * outer List((x,2))
     */

  }

  def snip1() = {
    val x = 2
    val fun = (x:Int) => (x + 2)
    println(
      findFreeVariables{
        val y = 3;
        fun(x + y)
      }
    )
  }

  def snip4() = {
    val x = 2
    val b = "Test"
    println(
      findFreeVariables {
        val y = 3;
        x + y
        b
      }
    )
  }

  def snip5() = {
    val x = 2
    println("outer: " + findFreeVariables {
      val z = 7
      println("inner: " + findFreeVariables {
        val y = 3
        val x = 6
        x + y + z
      })
      x + z
    })
  }

  def snip6() = {
    val x = 2
    val k = 3
    execAndReturn(x) {
        case z:Int => z * k
        case _ => 0
    }
  }

  def snip7() = {
    val x = 2
    val k = 3
    execAndReturn(x) {
        case x:Int => x * k
        case _ => 0
    }
  }

  def snip8() = {
    val (x, k) = (2, 3)
    execAndReturn(x) {
        case x:Int => x * k
        case _ => 0
    }
  }

  case class TestCase(val a: Int, val b: String)

  def snip9() = {
    val t = TestCase(1, "2")
    execAndReturn(t) {
        case TestCase(x, k) => (x * t.a).toString() + k
        case _ => ""
    }
  }

  def snip10() = {
    val t = TestCase(1, "2")
    execAndReturn(t) {
        case x:TestCase => {
          val TestCase(a, b) = x
          a.toString() + b
        }
        case _ => ""
    }
  }

  import scala.language.experimental.macros
  def findFreeVariables(func: => Any):List[(String, Any)] = macro FindFreeVars.findMacro

  def invokeAndReturn(func: => Any, vals: List[(String, Any)]): List[(String, Any)] = {
    func
    vals
  }

  def execAndReturn[T, V](param: T)(func: (T) => V): V = macro FindFreeVars.execMacro[T, V]

  def execAndReturn[T, V](func: (T) => V, param: T, vars: List[(String, Any)]): V = {
    println(vars)
    func(param)
  }

}