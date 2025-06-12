package constraints;

import generator.GeneralModel;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.GraphVar;
import org.chocosolver.solver.variables.UndirectedGraphVar;
import org.chocosolver.util.objects.graphs.UndirectedGraph;
import org.chocosolver.util.objects.setDataStructures.SetType;
import java.util.*;

/**
 * Contrainte "Cycle57Matching" : sélectionne des couples d'hexagones disjoints
 * qui seront transformés en paires (pentagone + heptagone). Chaque couple est
 * modélisé par une arête d'un graphe de matching.
 */
public class Cycle57MatchingConstraint extends BenzAIConstraint {

    private final UndirectedGraph gubGraph;
    private final int targetPentagons;
    private final int targetHeptagons;

    // Variables ajoutées par la contrainte
    private UndirectedGraphVar matchingVar;
    private IntVar nbPent;
    private IntVar nbHept;

    public Cycle57MatchingConstraint(UndirectedGraph gub, int nbPentagons, int nbHeptagons) {
        this.gubGraph = gub;
        this.targetPentagons = nbPentagons;
        this.targetHeptagons = nbHeptagons;
    }

    /* --------------------------- variables --------------------------- */
    @Override
    public void buildVariables() {
        GeneralModel gm = getGeneralModel();
        Model m = gm.getChocoModel();

        int n = gubGraph.getNbMaxNodes();
        UndirectedGraph lb = new UndirectedGraph(m, n, SetType.LINKED_LIST, false);
        matchingVar = m.graphVar("cycle57Pairs", lb, gubGraph);
        gm.setCycle57MatchingVar(matchingVar);

        List<BoolVar> edgeBools = new ArrayList<>();
        if (targetPentagons > 0) {
            for (int u = 0; u < n; u++) {
                for (int v : gubGraph.getNeighborsOf(u)) {
                    if (u < v) {
                        // 1) booléen pour u-v est la paire 5/7
                        BoolVar p_uv = m.boolVar("pair_" + u + "_" + v);

                        // 2) canaliser avec le graphe  couplage
                        m.edgeChanneling(matchingVar, p_uv, u, v).post();

                        // 3) canaliser avec l’arête du benzenoïde
                        BoolVar b_uv = gm.getBenzenoidEdges()[u][v];
                        m.arithm(p_uv, "<=", b_uv).post();               // pair -> arête présente

                        edgeBools.add(p_uv);
                    }
                }
            }
            gm.setPairEdgeBools(edgeBools.toArray(new BoolVar[0]));
        } else {
            gm.setPairEdgeBools(null);
        }





        nbPent = m.intVar("nbPentagons", 0, n);
        nbHept = m.intVar("nbHeptagons", 0, n);
        gm.setNbPentagonsVar(nbPent);
        gm.setNbHeptagonsVar(nbHept);

        System.out.println("[DEBUG] cycle57Pairs UB size = " + matchingVar.getNbMaxNodes());
    }

    /* --------------------------- contraintes ------------------------ */
    @Override
    public void postConstraints() {
        GeneralModel gm = getGeneralModel();
        Model m = gm.getChocoModel();

        /* 1) chaque sommet incident au plus à une arête (matching) */
        m.maxDegree(matchingVar, 1).post();

        /* 2) matchingVar est sous-graphe de la molécule */
        m.subgraph(matchingVar, gm.getGraphVar()).post();

        /* 3) nombre d'arêtes = nbPentagons = nbHeptagons */
        m.nbEdges(matchingVar, nbPent).post();
        /* 3) cardinalité ( arêtes = nbPent)  + ( nbPent = nbHept ) */
        //-if (targetPentagons > 0) {           // cas « pentagones demandés »
        //- // tableau BoolVar[] créé dans buildVariables
        //- BoolVar[] pb = getGeneralModel().getPairEdgeBools();
        //- m.nbEdges(matchingVar, nbPent).post();
        //- //m.sum(pb, "=", nbPent).post();                // somme(e_uv) = nbPent
        //-} else {                           // aucun pentagone demandé
        //- m.arithm(nbPent, "=", 0).post();              // nbPent = 0
        //-}
        //m.arithm(nbPent, "=", nbHept).post();             // nbPent = nbHept


        /* 4) bornes utilisateur explicites */
        if (targetPentagons > 0) {
            //m.arithm(nbPent, "=", targetPentagons).post();
        }
        if (targetHeptagons > 0) {
            //m.arithm(nbHept, "=", targetHeptagons).post();
        }


        /* 5) cohérence globale : 2*m + 1 <= n_hexagons */
        //IntVar nHex = gm.getNbVerticesVar();
        //IntVar lhs = nbPent.mul(2).add(1).intVar();
        //m.arithm(lhs, "<=", nHex).post();

        System.out.println("[DEBUG] Cycle57MatchingConstraint postée (k=" + targetPentagons + ")");
    }

    /* ---------------------------------------------------------------- */
    @Override public void addVariables()          { /* rien */ }
    @Override public void changeSolvingStrategy() { /* rien */ }
    @Override public void changeGraphVertices()   { /* rien */ }
}
