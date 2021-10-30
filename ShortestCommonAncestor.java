import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {

    Digraph G;


    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle() || !isRooted(G)) {
            throw new IllegalArgumentException("Graph is not a rooted DAG");
        }
        //  make a defensive copy
        this.G = new Digraph(G);

    }

    // checks to see if the Digraph is rooted
    private boolean isRooted(Digraph G) {
        DepthFirstOrder dfs = new DepthFirstOrder(G);
        if (dfs.reversePost() == null) return false;

        return true;

    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        Queue<Integer> subsetV = new Queue<Integer>();
        Queue<Integer> subsetW = new Queue<Integer>();

        subsetV.enqueue(v);
        subsetW.enqueue(w);

        return lengthSubset(subsetV, subsetW);

    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        Queue<Integer> subsetV = new Queue<Integer>();
        Queue<Integer> subsetW = new Queue<Integer>();

        subsetV.enqueue(v);
        subsetW.enqueue(w);

        return ancestorSubset(subsetV, subsetW);

    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(G, subsetB);

        int shortestLength = Integer.MAX_VALUE;
        for (int vertex = 0; vertex < G.V(); vertex++) {

            if ((pathsA.hasPathTo(vertex)) && pathsB.hasPathTo(vertex)) {
                int length = pathsA.distTo(vertex) + pathsB.distTo(vertex);
                if (length < shortestLength) {
                    shortestLength = length;
                }
            }

        }
        return shortestLength;


    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths pathsA = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths pathsB = new BreadthFirstDirectedPaths(G, subsetB);

        int shortestLength = Integer.MAX_VALUE;
        int sca = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if ((pathsA.hasPathTo(vertex)) && pathsB.hasPathTo(vertex)) {
                int length = pathsA.distTo(vertex) + pathsB.distTo(vertex);

                if (length < shortestLength) {
                    shortestLength = length;
                    sca = vertex;
                }
            }
        }
        return sca;

    }

    // unit testing (required)
    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);


        }
    }

}
