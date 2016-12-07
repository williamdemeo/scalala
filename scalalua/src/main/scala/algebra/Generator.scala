package algebra

//import java.util.Random

/**
  * Created by williamdemeo on 12/7/16.
  */
trait Generator[+T]{
  //self => // an alias for "this"
  def generate: T

  def map[S](f: T => S): Generator[S] = new Generator[S] {
    //def generate = f(self.generate)
    def generate = f(this.generate)
  }

  def flatMap[S](f: T => Generator[S]): Generator[S] = new Generator[S] {
    def generate = f(this.generate).generate
  }
}

object Generator {
  val integers = new Generator[Int] {
    val rand = new java.util.Random
    def generate = rand.nextInt()
  }

  val booleans = new Generator[Boolean] {
    def generate = integers.generate > 0
  }

  val intPairs = new Generator[(Int, Int)] {
    def generate = (integers.generate, integers.generate)
  }

  def pairs_first_try[T, U](t: Generator[T], u: Generator[U]) = t flatMap {x => u map {y => (x,y) } }

  def pairs[T, U](t: Generator[T], u: Generator[U]) = new Generator[(T, U)] {
    def generate = (t.generate, u.generate)
  }

  def single[T](x: T) = new Generator[T] { def generate = x }

  def chooseIntIn(lo: Int, hi: Int): Generator[Int] =
    for (x <- integers) yield lo + x % (hi - lo)

  def oneOf[T](xs: T*): Generator[T] = for (idx <- chooseIntIn(0, xs.length)) yield xs(idx)

  def lists[T]: Generator[List[T]] = for {
    isEmpty <- booleans
    list <- if(isEmpty) emptyList else nonEmptyLists
  } yield list

  def emptyList = single(Nil)

  def nonEmptyLists[T] = ???



}
