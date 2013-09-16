package org.digimead
import scala.collection.JavaConversions._
import gnu.trove.map.hash._
import com.twitter.algebird._

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
  def sum_dictionary():Int
  
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

  import scala.collection.mutable._
  import org.saddle._
 
  def main(args: Array[String]) {
   // println(hello + ", 5 + 5 = " + add(5,5))
    val mMap = Map.empty[String,Int]

    var a = Array[Int]((1 to 1000000):_*)
    
     var b = Array[String]( (1 to 1000000).map(i => (i.toString)):_*)
    

    val index = Index[String](b)

    //(1 to 1000000).map( i => index(i.toString) = i+1)
    println(index("1")(0))
 //Index.make(Vec(Array((1 to 1000000).map(i => i.toString))), 
      //Vec(Array(1 to 1000000)))

    time{ (1 to 1000000).map( i => mMap(i.toString) = i+1 ) }

    load_dictionary()

    time{ (1 to 1000000).map{ i=> add_dictionary(i.toString,i+1)} }

//    time{ (1 to 1000000).foreach{ i => find_dictionary(i.toString) } }
   
    
    time{ (1 to 100).foreach(i => find_dictionary(i.toString))}
    

    time{ (1 to 100).foreach{ i => mMap(i.toString) } }
  
    time{ (1 to 100).foreach{ i => index.count(i.toString)}}
    
    time{ mMap.foldLeft(0)((acc,x) => acc + x._2)}

    time{ sum_dictionary()}

    time{
      var sum:Int = 0
      var ii:Int = 0
      while(ii < a.length) 
        {
          sum = sum + a(ii)
          ii = ii + 1
        }
    }

    println(find_dictionary("1"))
    free_dictionary()
  }
}

