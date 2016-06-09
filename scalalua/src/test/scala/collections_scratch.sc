import common._
import collections._
object intsets {
  val t1 = new NonEmpty(3, Empty, Empty)
  val t2 = t1.incl(4)
  Empty contains 1
  t1 union t2

  val a = new NonEmpty(3, Empty, Empty)
  val b = new NonEmpty(4, Empty, Empty)

  a union b
  b union a

  // You can also get the above as follows:
  b incl 3
  a incl 4

  // You can also get the above as follows:
  (b incl 3) incl 5 // balanced
  (a incl 4) incl 5
  // unbalanced

  // Here are some IntSets that are constructed wihtout respecting order
  val A = new NonEmpty(2, new NonEmpty(3, Empty, Empty), new NonEmpty(4, Empty, Empty))
  val B = new NonEmpty(9, new NonEmpty(5, Empty, Empty), Empty)
  val C = new NonEmpty(7, new NonEmpty(8, Empty, Empty), B)

  // Taking the union makes the ordering a little better, but still not quite right.
  val D = A union C
  // this puts 7 at the root, then 9 above 5 on the right and
  // 8 above 3  on the left with 3 above 2 and above 4, like the diagram shown below.
  // (Interestingly, if we union one more time, even with an empty set,
  // the tree becomes balanced with correctly ordered elements.)
  D union Empty
  // The reason union cleans up the mess is because union takes each element from the `this`
  // tree and inserts it into the `that` tree using incl, which respects order during insert.
  //
  //                  7
  //               /    \
  //             /       \
  //           8          9
  //        /    \       /
  //       3     E      5
  //     /   \         / \
  //   2      4       E  E
  //  / \    / \
  // E  E   E  E
  //

  println("----------------------------------------------" + "\n" +
    "a first example of scanLeft using integers" + "\n" +
    "----------------------------------------------" + "\n")

  val inttree1 = Node(Node(Leaf(1), Leaf(3)), Node(Leaf(8), Leaf(50)))
  val plus = (x: Int, y: Int) => x + y
  val upsweep_result = upsweep(inttree1, plus)
  val downsweep_result = downsweep(upsweep_result, 100, plus)
  scanLeft(inttree1, 100, plus)


  println("----------------------------------------------" + "\n" +
    "A second example of scanLeft using IntSets" + "\n" +
    "----------------------------------------------" + "\n")

  val uplus = (x: IntSet, y: IntSet) => x union y
  val mytree: Tree[IntSet] = Node(Node(Leaf(A), Leaf(B)), Node(Leaf(C), Leaf(Empty)))
  val upsw_res = upsweep(mytree, uplus)
  val down_res = downsweep(upsw_res, Empty, uplus)
  scanLeft(mytree, Empty, uplus)

  // MapArray

  val f = (x: Int) => x * x

  val input = Array(1, 2, 3, 4)
  val output = Array(0, 0, 0, 0)
  mapArray(input, 0, (x: Int) => x * x, output)
  output

  val output2 = Array(0, 0, 0, 0)
  mapArraySeg(input, 1, 3, f, output2)
  output2

  val output3 = Array(0, 0, 0, 0)
  mapArraySegPar(input, 1, 3, f, output3, 2)
  output3

  // subsets and permutations
  val s = Set(0, 1, 2, 3)
  // getting subsets of a certain size
  s.subsets(2).toList
  // getting subsets of size 2 and then all permutations of such subsets.
  val perms = s.subsets(2).toList.map(x => x.toList.permutations)
  for {
    p <- perms
  } yield p.toList


}