package generator;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.UndirectedGraphVar;
import org.chocosolver.util.objects.graphs.UndirectedGraph;
import org.chocosolver.util.objects.setDataStructures.SetType;

public class TransformationPairsGraph {


    public static UndirectedGraphVar build (Model model, UndirectedGraph gub, String name) {

        UndirectedGraph empty = new UndirectedGraph(model, 4, SetType.BITSET, false);

        // Variable de décision: sous‑graphe de GUB (non orienté)
        UndirectedGraphVar pairs =  model.graphVar(name , empty, gub);


        return pairs;
    }


    public static void main(String[] args) {
        // 1) Construction du GUB
        Model model = new Model("Demo Transformation Pairs");
        int n = 4; // 4 «hexagones» pour l’exemple
        UndirectedGraph gub = new UndirectedGraph(model, n, SetType.BITSET, false);
        gub.addEdge(0, 1);
        gub.addEdge(1, 3);
        gub.addEdge(3, 2);
        gub.addEdge(2, 0);
        //gub.addEdge(0, 3); // diagonales
        gub.addEdge(1, 2);

        //gub.addEdge(4, 1);
        //gub.addEdge(4, 3);
        // benzenoid fixé
        UndirectedGraphVar benzenoid =
                (UndirectedGraphVar) model.graphVar("benz", gub, gub);

        IntVar nbNodes = model.intVar(n);
        model.nbNodes(benzenoid,nbNodes).post();

        // 2) Variable + contrainte «au moins une paire»
        UndirectedGraphVar pairs = build(model, gub, "pairs");
        // Matching : chaque sommet a degré 1
        model.maxDegree(pairs, 1).post();
        model.subgraph(pairs, benzenoid).post();
        model.nbNodes(pairs,nbNodes).post();


        IntVar nbPairs = model.intVar("nbPairs", 2, 2);  // diff ??
        model.nbEdges(pairs, nbPairs).post();
        // impose nbPairs ≥ 1
        //model.arithm(nbPairs, "=", 2).post(); // diff ??

        model.getSolver().limitSolution(100);

        int idx = 1;
        // 3) Résolution & affichage
        System.out.println("model" + model);
        while (model.getSolver().solve()) {
            //System.out.println("Paires retenues :");
            UndirectedGraph sol = pairs.getValue();
            //System.out.println(benzenoid.getValue());
            System.out.printf("Solution %d : \n", idx++);
            System.out.println(pairs);
            //for (int u : sol.getNodes()) {

            //}
        } //else {
            //System.out.println("Pas de solution trouvée.");
       // }
    }
}