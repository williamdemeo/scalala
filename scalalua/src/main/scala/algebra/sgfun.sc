object sgfun {
  val n = 50
  def f(i: Int, j: Int, k: Int) = (i + j + k) % n
  f(1,2,3)
  f(4,4,4)
  f(6,12,10)
  //  (0 until n) flatMap (i => (0 until n) map (j => f(i,j)))

  val X = Set(0,3,4,8,9,14,15)
  //X flatMap (i => X flatMap (j => X map (k => ((i,j,k), f(i,j,k)))))
  val triples = X flatMap (i => X flatMap (j => X map (k => (i,j,k))))
  val values = X flatMap (i => X flatMap (j => X map (k => f(i,j,k))))
  triples.size
  values.size
  // List(1,3) flatMap (i => (0 until n) map (j => f(i,j)))
}