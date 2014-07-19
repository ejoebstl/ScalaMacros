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

    println("Snippet #1")
    snip1()
    println("Snippet #4")
    snip4()
    println("Snippet #5")
    snip5()

  }

  def snip1() = {
    val x = 2
    println(
      findFreeVariables(() => {
        val y = 3;
        x + y
      })
    )
  }

  def snip4() = {
    val x = 2
    val b = "Test"
    println(
      findFreeVariables(() => {
        val y = 3;
        x + y
        println(b)
      })
    )
  }

  def snip5() = {
    val x = 2
    println("outer " + findFreeVariables(() => {
      val z = 7
      println("inner " +
        findFreeVariables(() => {
            val y = 3;
            x + y
            x + z
        })
      )
      x + z
    }))
  }

  import scala.language.experimental.macros
  def findFreeVariables[T](func: () => T):List[(String, Any)] = macro FindFreeVars.findMacro[T]

  def invokeAndReturn[T](func: () => T, vals: List[(String, Any)]): List[(String, Any)] = {
    func()
    vals
  }

}