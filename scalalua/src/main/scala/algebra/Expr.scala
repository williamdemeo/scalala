/* File: Expr.scala 
 * Author: William DeMeo <williamdemeo@gmail.com>
 * Url: https://github.com/williamdemeo/scalala
 * 
 * Description: This is just an example (a library that manipulates arithmetic expressions).
 * This file will probably be removed eventually. It's mainly for practice with Scala classes, 
 * traits, syntax, etc.
 * 
 * Reference: Odersky, Spoon, Venners, "Programming in Scala"
 */
package algebra

/* Definition of the input data. 
 * To keep it simple, we start with arithmetic expressions consisting of variables, numbers, 
 * unary operations, and binary operations.
 */
abstract class Expr {
  case class Variable(symbol: String) extends Expr
  case class Number(number: Double) extends Expr
  case class UnaryOperation(symbol: String, arg: Expr) extends Expr
  case class BinaryOperation(symbol: String, arg1: Expr, arg2: Expr) extends Expr
  abstract case class PartialUnaryOperation(symbol: String, arg: Expr) extends PartialFunction[Expr,Expr]
}
object Expr {
  def simplifyTop(e: Expr): Expr = e match {
    case e.UnaryOperation("-", e.UnaryOperation("-", expr)) => expr // double negation
    case e.BinaryOperation("+", expr, e.Number(0)) => expr // add 0 right
    case e.BinaryOperation("+", e.Number(0), expr) => expr // add 0 left
    case e.BinaryOperation("-", expr, e.Number(0)) => expr // sub 0 right
    case e.BinaryOperation("-", e.Number(0), expr) => expr // sub 0 left
    case e.BinaryOperation("*", expr, e.Number(1)) => expr // mult 1 right
    case e.BinaryOperation("*", e.Number(1), expr) => expr // mult 1 left
    case _ => e
  }
}
// Instead of an abstract class, we could have equally well chosen to model the root of this class
// hierarchy as a trait. Modeling it as an abstract class may be slightly more efficient.  
 
/* The hierarchy includes an abstract base class Expr with four subclasses, one for each kind of
 * expression. The bodies of all five classes are empty. In Scala you can leave out the braces 
 * around an empty class body, so `class C` is the same as `class C {}`.
 * 
 * Each subclass has a case modifier. Using the case modifier makes the Scala compiler add some 
 * syntactic conveniences.
 *     1. a factory method with the name of the class. This means you can write say, 
 *     Variable("x") to construct a Variable object instead of the slightly longer new Variable("x"):
 *     
 *         val v = Variable("x")
 *         val op = BinaryOperation("+", Number(1), v)
 *     
 *     2. all arguments in the parameter list of a case class implicitly get a val prefix, 
 *     so they are maintained as fields; e.g., `v.symbol` and `op.arg1` are valid expressions.
 *
 *     3. the compiler adds "natural" implementations of methods toString, hashCode, and equals.
 *     Since == in Scala always delegates to equals, this means that elements of case classes are 
 *     always compared structurally
 *          
 */

