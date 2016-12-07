//package finder

object polymorphisms {
	   // Scala has a partial function type:
	   val f: PartialFunction[String, String] = {
	     case "ping" => "pong"
	     case "tic" => "tac"
	     case "tac" => "toe"
	   }
	  
	  f("tic")  // result: "tac"
    f("tac")  // result: "toe"
    // ...but the next one results in a scala.MatchError.
    // f("toe")
    // To avoid these errors, we can test whether a partial function is defined
    // at some point in the domain before trying to evaluate it there:
    f.isDefinedAt("tac") // result: true
    f.isDefinedAt("toe") // result: false
    
    // We can also lift a partial function to a total function that returns option type.
    def liftPartial[A,B](f: PartialFunction[A,B]): A => Option[B] =
      (a:A) => if(f.isDefinedAt(a)) Some(f(a)) else None

    val ftot = liftPartial(f)

    // It's safe to apply the total function ftot to every string.
    // If it's not defined for the given string, we get None instead of a MatchError:
    ftot("tac")  // result: Some(toe)
    ftot("toe")  // result: None
    
    // We didn't really need to define our own liftPartial function.  The Scala
    // PartialFunction type already has a lift method.
    f.lift("tac")
    f.lift("toe")
    
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