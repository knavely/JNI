package org.digimead
import scala.collection.JavaConversions._
import gnu.trove.map.hash._

object HelloWorldJNIwithRegisterNatives {
  // If the framework passed in a specific library, use it.
  val libname = System.getProperty("native.lib")
  if (libname ne null)
    System.load(libname)
  else {
    println("java.library.path: " + System.getProperty("java.library.path"))
    System.loadLibrary("HelloWorldJNIwithRegisterNatives")    
  }

object TroveToScalaMap {
  def apply[T] (tmap: TObjectIntHashMap[T]): Map[T,Int] =
    tmap.keySet.map(key => (key -> tmap.get(key))).toMap
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
  def sort_dictionary()

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
  import breeze.linalg._

  def main(args: Array[String]) {
   // println(hello + ", 5 + 5 = " + add(5,5))
    val mMap = Map.empty[String,Int]

    var tMap:TObjectIntHashMap[String] = new TObjectIntHashMap[String]()

    
    
    var a = Array[Int]((1 to 1000000):_*)
    
    var b = Array[String]( (1 to 1000000).map(i => (i.toString)):_*)
    

    println("Saddle Insert:")
    time{ val index = Series[String,Int](Vec(a),Index(b)) }

     val index = Series[String,Int](Vec(a),Index(b)) 

    //(1 to 1000000).map( i => index(i.toString) = i+1)
  //  println(index("1")(0))
 //Index.make(Vec(Array((1 to 1000000).map(i => i.toString))), 
      //Vec(Array(1 to 1000000)))

    println("Trove Insert:")
     time{ (1 to 1000000).map( i => tMap.put(i.toString, i) ) }

    println("Mute Map Insert:")
    time{ (1 to 1000000).map( i => mMap(i.toString) = i+1 ) }

    println("C Insert:")
    load_dictionary()
    time{ (1 to 1000000).map{ i=> add_dictionary(i.toString,i+1)} }


    val c = Counter[String,Int]()
    println("Breeze Counter Insert")
    time{ (1 to 1000000).foreach{ i => c(i.toString) = i+1 } }
   
   
     var v:VectorBuilder[Double] = VectorBuilder[Double]() 
    println("Breeze VecBuild Insert")
    time{ v = VectorBuilder((1 to 1000000).map(i => i.toDouble + 1.0).toArray[Double]) }
   
    //sort_dictionary()

    println("C Read:")
    time{ (1 to 1000).foreach(i => find_dictionary(i.toString))}
    

    println("Mute Map read:")
    time{ (1 to 1000).foreach{ i => mMap(i.toString) } }
  
    println("Trove Read:")
    time{ (1 to 1000).foreach{ i => tMap.get(i.toString)}}
    
    println("Saddle index read:")
    time{ (1 to 1000).foreach{ i => index.contains(i.toString)}}
   
     println("Breeze Counter read")
    time{ (1 to 1000).foreach{ i => c.get(i.toString) } }
   
    println("Breez VecBuild Read:")
    val vv = v.toSparseVector
    time{ (1 to 1000).foreach(i=>vv(i))}
    
    println("Mute Map sumation:")
    time{ mMap.foldLeft(0)((acc,x) => acc + x._2)}

    println("Breeze Counter sumation:")
    var cc:Int = 0
    time{c.foreachValue(i => cc = cc+i)}

    println("C sumation:")
    time{ sum_dictionary()}

    println("Scala Array primitive summation:")
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

