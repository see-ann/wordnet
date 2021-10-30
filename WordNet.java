import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    SeparateChainingHashST<Integer, String> idToSynset;
    SeparateChainingHashST<String, Queue<Integer>> nounToIds;
    Digraph graph;
    ShortestCommonAncestor sca;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int vertices = 0;
        if (synsets == null || hypernyms == null)
            throw new NullPointerException("Arguments can't be null");
        idToSynset = new SeparateChainingHashST<>();
        nounToIds = new SeparateChainingHashST<>();

        In input = new In(synsets);
        while (input.hasNextLine()) {
            String[] fields = input.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            String[] nouns = synset.split(" ");

            idToSynset.put(id, synset);
            for (int i = 0; i < nouns.length; i++) {
                String noun = nouns[i];
                Queue<Integer> queue;

                if (!nounToIds.contains(noun)) {
                    queue = new Queue<>();
                }
                else {
                    queue = nounToIds.get(noun);
                }
                queue.enqueue(id);
                nounToIds.put(noun, queue);
            }

            vertices = id;
        }
        // add 1 as id starts at 0
        graph = new Digraph(vertices + 1);
        input = new In(hypernyms);
        while (input.hasNextLine()) {
            String[] fields = input.readLine().split(",");
            for (int i = 1; i < fields.length; i++) {
                graph.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }

        }
        sca = new ShortestCommonAncestor(graph);


    }


    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keys();

    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounToIds.contains(word);

    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (nounToIds.get(noun1) == null || nounToIds.get(noun2) == null)
            throw new IllegalArgumentException("Nouns not in file");

        Queue<Integer> ids1 = nounToIds.get(noun1);
        Queue<Integer> ids2 = nounToIds.get(noun2);

        int scaID = sca.ancestorSubset(ids1, ids2);
        String scaSynset = idToSynset.get(scaID);
        return scaSynset;


    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (nounToIds.get(noun1) == null || nounToIds.get(noun2) == null)
            throw new IllegalArgumentException("Nouns not in file");

        Queue<Integer> ids1 = nounToIds.get(noun1);
        Queue<Integer> ids2 = nounToIds.get(noun2);

        int scaLength = sca.lengthSubset(ids1, ids2);
        return scaLength;

    }

    // unit testing (required)
    public static void main(String[] args) {

        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println("Distance: " + w.distance("white_marlin", "mileage"));
        StdOut.println("Distance: " + w.distance("Black_Plague", "black_marlin"));
        StdOut.println("Distance: " + w.distance("American_water_spaniel", "histology"));
        StdOut.println("Distance: " + w.distance("Brown_Swiss", "barrel_roll"));
        StdOut.println("SCA: " + w.sca("individual", "edible_fruit"));
        StdOut.println("SCA: " + w.sca("municipality", "region"));

    }

}
