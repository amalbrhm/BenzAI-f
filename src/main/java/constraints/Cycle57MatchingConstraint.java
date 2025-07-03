package constraints;

import generator.GeneralModel;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.UndirectedGraphVar;
import org.chocosolver.util.objects.graphs.UndirectedGraph;
import org.chocosolver.util.objects.setDataStructures.SetType;

import java.util.*;

public class Cycle57MatchingConstraint extends BenzAIConstraint {

    private final UndirectedGraph gubGraph;
    private final int targetPentagons;
    private final int targetHeptagons;

    private UndirectedGraphVar matchingVar;
    private IntVar nbPent;
    private IntVar nbHept;

    public Cycle57MatchingConstraint(UndirectedGraph gub, int nbPentagons, int nbHeptagons) {
        this.gubGraph = gub;
        this.targetPentagons = nbPentagons;
        this.targetHeptagons = nbHeptagons;
    }

    @Override
    public void buildVariables() {
        GeneralModel gm = getGeneralModel();
        Model m = gm.getChocoModel();

        int n = gubGraph.getNbMaxNodes();

        // 1. Graphe de matching 5/7
        UndirectedGraph lb = new UndirectedGraph(m, n, SetType.LINKED_LIST, false);
        matchingVar = m.graphVar("cycle57Pairs", lb, gubGraph);
        gm.setCycle57MatchingVar(matchingVar);

        // 2. Booléens pour les arêtes sélectionnées
        List<BoolVar> edgeBools = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v : gubGraph.getNeighborsOf(u)) {
                if (u < v) {
                    BoolVar p_uv = m.boolVar("pair_" + u + "_" + v);
                    m.edgeChanneling(matchingVar, p_uv, u, v).post();
                    m.arithm(p_uv, "<=", gm.getBenzenoidEdges()[u][v]).post();
                    edgeBools.add(p_uv);
                }
            }
        }
        gm.setPairEdgeBools(edgeBools.toArray(new BoolVar[0]));

        // 3. Variables nb pentagones / heptagones
        nbPent = m.intVar("nbPentagons", 0, n);
        nbHept = m.intVar("nbHeptagons", 0, n);
        gm.setNbPentagonsVar(nbPent);
        gm.setNbHeptagonsVar(nbHept);

        // 4. Variables cycle_i ∈ {5,6,7}
        IntVar[] cycleVars = new IntVar[n];
        for (int i = 0; i < n; i++) {
            cycleVars[i] = m.intVar("cycle_" + i, new int[]{0, 5, 6, 7});
        }
        gm.setCycleVars(cycleVars);

        System.out.println("[DEBUG] cycle57Pairs UB size = " + matchingVar.getNbMaxNodes());
    }

    @Override
    public void postConstraints() {
        GeneralModel gm = getGeneralModel();
        Model m = gm.getChocoModel();

        IntVar[] cycleVars = gm.getCycleVars();
        int n = gubGraph.getNbMaxNodes();

        // 1. Matching : max degré 1
        m.maxDegree(matchingVar, 1).post();

        // 2. Matching ⊆ molécule
        m.subgraph(matchingVar, gm.getGraphVar()).post();

        // 3. Nombre d’arêtes = nbPent = nbHept
        m.nbEdges(matchingVar, nbPent).post();
        m.arithm(nbPent, "=", nbHept).post();

       // 4. Contraintes sur les paires : diff 6 et diff entre eux
        for (int u = 0; u < n; u++) {
            for (int v : gubGraph.getNeighborsOf(u)) {
                if (u < v) {
                    BoolVar p_uv = m.boolVar("pairCycle_" + u + "_" + v);
                    m.edgeChanneling(matchingVar, p_uv, u, v).post();
                    m.ifThen(
                            p_uv,
                            m.and(
                                    m.arithm(cycleVars[u], "!=", 6),
                                    m.arithm(cycleVars[v], "!=", 6),
                                    m.arithm(cycleVars[u], "!=", cycleVars[v])
                            )
                    );
                    /*m.ifThen(
                            p_uv,
                            m.or(
                                    m.and(
                                            m.arithm(cycleVars[u], "=", 5),
                                            m.arithm(cycleVars[v], "=", 7)
                                    ),
                                    m.and(
                                            m.arithm(cycleVars[u], "=", 7),
                                            m.arithm(cycleVars[v], "=", 5)
                                    )
                            )
                    );*/



                }
            }
        }
       // 5. Contraintes cycle_i = 6 si degré = 0
        IntVar[] degVars = new IntVar[n];
        for (int i = 0; i < n; i++) {
            degVars[i] = m.intVar("deg_" + i, 0, 1);
        }
        m.degrees(matchingVar, degVars).post();

        for (int i = 0; i < n; i++) {
            m.ifThen(
                    m.arithm(degVars[i], "=", 1),
                    m.arithm(cycleVars[i], "!=", 6)
            );
        }

        // 6. Bornes utilisateur (si activées)
        if (targetPentagons > 0) {
            m.arithm(nbPent, "=", targetPentagons).post();
        }
        if (targetHeptagons > 0) {
            m.arithm(nbHept, "=", targetHeptagons).post();
        }

        System.out.println("[DEBUG] Cycle57MatchingConstraint postée (k=" + targetPentagons + ")");
    }

    @Override public void addVariables()          { /* rien */ }
    @Override public void changeSolvingStrategy() { /* rien */ }
    @Override public void changeGraphVertices()   { /* rien */ }
}
