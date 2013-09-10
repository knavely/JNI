package org.digimead

object HelloWorldJNIwithRegisterNatives {
  // If the framework passed in a specific library, use it.
  val libname = System.getProperty("native.lib")
  if (libname ne null)
    System.load(libname)
  else {
    println("java.library.path: " + System.getProperty("java.library.path"))
    System.loadLibrary("HelloWorldJNIwithRegisterNatives")
    
  }

  @native
  def add(a: Int, b: Int): Int
  @native
  def hello(): String
  
  @native
  def badd(a: Int, b: Int): Int
 
  @native 
  def load_dictionary()
  
  @native 
  def free_dictionary()
  
  @native
  def add_dictionary(key:String, value:Int)
  
  @native
  def find_dictionary(key:String): Int

  def time[A](f: => A) = {
  val s = System.nanoTime
  val ret = f
  println("time: "+(System.nanoTime-s)/1e6+"ms")
  ret
}

  def main(args: Array[String]) {
   // println(hello + ", 5 + 5 = " + add(5,5))
    load_dictionary()

    (1 to 1000).map{ i=>
      add_dictionary(i.toString,i+1)
    }
    println(find_dictionary("1"))
    free_dictionary()
  }
}

