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

  def main(args: Array[String]) {
    println(hello + ", 5 + 5 = " + add(5,5))
    load_dictionary()
    add_dictionary("1",1)
    println(find_dictionary("1"))
    free_dictionary()
  }
}

