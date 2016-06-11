import type_classes._
import JodaImplicits._
import org.joda.time.Duration._

/**
  * William DeMeo 2016
  * Here are some good tutorials on type classes and implicits:
	* https://tpolecat.github.io/2013/10/12/typeclass.html
  * http://danielwestheide.com/blog/2013/02/06/the-neophytes-guide-to-scala-part-12-type-classes.html
  */

object test_type_classes {
	// testing commit with new repo name still works
	val numbers = Vector[Double](13, 23.0, 42, 45, 61, 73, 96, 100, 199, 420, 900, 3839)
                                                  //> numbers  : scala.collection.immutable.Vector[Double] = Vector(13.0, 23.0, 42
                                                  //| .0, 45.0, 61.0, 73.0, 96.0, 100.0, 199.0, 420.0, 900.0, 3839.0)
	println(MyStats.mean(numbers))            //> 484.25

	val durations = Vector(standardSeconds(20), standardSeconds(57), standardMinutes(2),
  standardMinutes(17), standardMinutes(30), standardMinutes(58), standardHours(2),
  standardHours(5), standardHours(8), standardHours(17), standardDays(1),
  standardDays(4))                                //> durations  : scala.collection.immutable.Vector[org.joda.time.Duration] = Vec
                                                  //| tor(PT20S, PT57S, PT120S, PT1020S, PT1800S, PT3480S, PT7200S, PT18000S, PT28
                                                  //| 800S, PT61200S, PT86400S, PT345600S)
  
	println(CBStats.mean(durations).getStandardHours)
                                                  //> 12
}