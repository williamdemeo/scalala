package algebra

/**
  * Created by williamdemeo on 11/29/16.
  */
trait Expr2 {
  case class Variable(symbol: String) extends Expr2
  case class Number(number: Double) extends Expr2
  case class UnaryOperation(symbol: String, arg: Expr2) extends Expr2
  case class BinaryOperation(symbol: String, arg1: Expr2, arg2: Expr2) extends Expr2
  abstract case class PartialUnaryOperation(symbol: String, arg: Expr2) extends PartialFunction[Expr,Expr]

  def simplifyTop(e: Expr2): Expr2 = e match {
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
