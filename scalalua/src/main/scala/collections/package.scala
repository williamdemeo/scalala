import common._

/**
  * Created by williamdemeo on 6/8/16.
  */
package object collections {
    // Trees ////////////////////////////////////////////////////////////
    sealed abstract class Tree[A]
    case class Leaf[A](a: A) extends Tree[A] {
      override def toString = a.toString
    }
    case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A] {
      override def toString = "{" + l + "^" + r + "}"
    }

    sealed abstract class TreeRes[A] { val res: A }
    case class LeafRes[A](override val res: A) extends TreeRes[A] {
      override def toString = res.toString
    }
    case class NodeRes[A](l: TreeRes[A], override val res: A, r: TreeRes[A]) extends TreeRes[A] {
      override def toString = "{" + l + "(" + res + ")" + r + "}"
    }


    // IntSet ////////////////////////////////////////////////////////////
    trait IntSet {
      def contains(x: Int): Boolean
      def incl(x: Int): IntSet
      def union(ints: IntSet): IntSet
    }
    object Empty extends IntSet {
      def contains(x: Int) = false
      def incl(x: Int) = new NonEmpty(x, Empty, Empty)
      def union(that: IntSet): IntSet = that
      override def toString = "."
    }
    case class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
      def contains(x: Int): Boolean =
        if (x < elem) left contains x
        else if(x > elem) right contains x
        else true
      def incl(x: Int): IntSet =
        if (x < elem) NonEmpty(elem, left incl x, right)
        else if(x > elem) NonEmpty(elem, left, right incl x)
        else this
      def union(that: IntSet): IntSet = ((left union right) union that) incl elem
      override def toString = "{" + left + elem + right + "}"
    }

    // operations on Trees and IntSets ////////////////////////////////////

    // upsweep converts Tree into TreeSet using the associative binary reduction operator f.
    def upsweep[A](t: Tree[A], f:(A, A) => A): TreeRes[A] = t match {
      case Leaf(set) => LeafRes(set)
      case Node(l, r) => {
        val (tL, tR) = parallel(upsweep(l, f), upsweep(r, f))
        NodeRes(tL, f(tL.res, tR.res), tR)
      }
    }

    // downsweep is the second stage of the reduction.
    // Here, s0 is the upsweep of all elements to the left of the given tree t.
    def downsweep[A](t: TreeRes[A], a0: A, f: (A,A) => A): Tree[A] = t match {
      case LeafRes (s) => Leaf (f (a0, s) )
      case NodeRes (l, _, r) => {
        val (tL, tR) = parallel (downsweep (l, a0, f), downsweep (r, f (a0, l.res), f) )
        Node (tL, tR)
      }
    }

    // scanLeft is the final stage of the reduction
    def scanLeft[A](t: Tree[A], a0: A, f: (A, A) => A): Tree[A] = {
      val tRes = upsweep(t, f)
      val scan1 = downsweep(tRes, a0, f)
      prepend(a0, scan1)
    }

    def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
      case Leaf(v) => Node(Leaf(x), Leaf(v))
      case Node(l, r) => Node(prepend(x,l), r)
    }


    // Map over array
    def mapArray[A,B](input: Array[A], idx: Int, f: A => B, output: Array[B]): Unit  = {
      //Array[B] = {
      //if (idx == input.length) output
      //else {
      if(idx < input.length) {
        output(idx) = f(input(idx))
        mapArray(input, idx + 1, f, output)
      }
    }

    // Map over array segment
    def mapArraySeg[A,B](inp: Array[A], start: Int, until: Int, f: A => B, out: Array[B]) = {
      var i = start
      while (i < until) {
        out(i) = f(inp(i))
        i = i + 1
      }
    }

    // parallel map over array segment
    def mapArraySegPar[A,B](input: Array[A], start: Int, until: Int, f: A => B, output: Array[B], threshold: Int): Unit = {
      if (until - start < threshold)
        mapArraySeg(input, start, until, f, output)
      else {
        val mid = (start + until)/2
        parallel(
          mapArraySegPar(input, start, mid, f, output, threshold),
          mapArraySegPar(input, mid, until, f, output, threshold)
        )
      }
    }

    // parallel map on immutable trees
    //def mapArrayTreePar
  }

