package finder

object polymorphisms {
	   // Scala has a partial function type:
	   val f: PartialFunction[String, String] = {
	     case "ping" => "pong"
	     case "tic" => "tac"
	     case "tac" => "toe"
	   }                                      //> f  : PartialFunction[String,String] = <function1>
	  
	  f("tic")  // result: "tac"              //> res0: String = tac
    f("tac")  // result: "toe"                    //> res1: String = toe
    // ...but the next one results in a scala.MatchError.
    // f("toe")
    // To avoid these errors, we can test whether a partial function is defined
    // at some point in the domain before trying to evaluate it there:
    f.isDefinedAt("tac") // result: true          //> res2: Boolean = true
    f.isDefinedAt("toe") // result: false         //> res3: Boolean = false
    
    // We can also lift a partial function to a total function that returns option type.
    def liftPartial[A,B](f: PartialFunction[A,B]): A => Option[B] =
      (a:A) => if(f.isDefinedAt(a)) Some(f(a)) else None
                                                  //> liftPartial: [A, B](f: PartialFunction[A,B])A => Option[B]
      
    val ftot = liftPartial(f)                     //> ftot  : String => Option[String] = <function1>
    // It's safe to apply the total function ftot to every string.
    // If it's not defined for the given string, we get None instead of a MatchError:
    ftot("tac")  // result: Some(toe)             //> res4: Option[String] = Some(toe)
    ftot("toe")  // result: None                  //> res5: Option[String] = None
    
    // We didn't really need to define our own liftPartial function.  The Scala
    // PartialFunction type already has a lift method.
    f.lift("tac")                                 //> res6: Option[String] = Some(toe)
    f.lift("toe")                                 //> res7: Option[String] = None
    
    /* The lift method returns an object of type Lifted.  The complete definition of the
     * Lifted class is the following:
     *
     *    private class Lifted[-A, +B] (val pf: PartialFunction[A, B])
     *          extends scala.runtime.AbstractFunction1[A, Option[B]] {
     *      def apply(x: A): Option[B] = {
     *        val z = pf.applyOrElse(x, checkFallback[B])
     *        if (!fallbackOccurred(z)) Some(z) else None
     *      }
     *    }
     *
     * Evidently, the apply method of the Lifted class does exactly what we wanted.
     * It returns Some(f(x)) if f is defined at x and returns None otherwise.
     */
}