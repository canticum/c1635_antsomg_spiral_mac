/*
 * Copyright 2019 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package art.cctcc.c1635.antsomg.demo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import art.cctcc.c1635.antsomg.demo.x.Graph_X;
import art.cctcc.c1635.antsomg.demo.x.Vertex_X;
import art.cctcc.c1635.antsomg.demo.y.Graph_Y;
import art.cctcc.c1635.antsomg.demo.y.Vertex_Y;
import java.util.HashMap;
import tech.metacontext.ocnhfa.antsomg.impl.StandardEdge;
import tech.metacontext.ocnhfa.antsomg.impl.StandardGraph;
import tech.metacontext.ocnhfa.antsomg.impl.StandardMove;
import tech.metacontext.ocnhfa.antsomg.model.AntsOMGSystem;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class SpiralSystem implements AntsOMGSystem<SpiralAnt> {

    int ant_population;
    Map<String, StandardGraph> graphs;
    List<SpiralAnt> ants;
    double pheromone_deposit = 0.5;
    double explore_chance = 0.1;
    double evaporate_rate = 0.05;
    double alpha = 1.0, beta = 1.0;

    public SpiralSystem(int ant_population) {

        this.ant_population = ant_population;
    }

    @Override
    public void init_graphs() {

        this.graphs = new HashMap<>();
        this.graphs.put("x", new Graph_X(alpha, beta));
        this.graphs.put("y", new Graph_Y(alpha, beta));
    }

    Graph_X getX() {

        return (Graph_X) this.graphs.get("x");
    }

    Graph_Y getY() {

        return (Graph_Y) this.graphs.get("y");
    }

    @Override
    public void init_population() {

        this.ants = Stream.generate(()
                -> new SpiralAnt(
                        getX().getStart(),
                        getY().getStart()))
                .limit(ant_population)
                .collect(Collectors.toList());
    }

    @Override
    public void navigate() {

        this.ants.stream().forEach(ant -> {
            if (!ant.isCompleted()) {
                SpiralTrace trace = ant.getCurrentTrace();
                StandardMove x = getX().move((Vertex_X) trace.getDimension("x"),
                        this.pheromone_deposit, this.explore_chance);
                StandardMove y = getY().move((Vertex_Y) trace.getDimension("y"),
                        this.pheromone_deposit, this.explore_chance);
                SpiralTrace new_trace = new SpiralTrace(x, y);
                ant.setCurrentTrace(new_trace);
                if (ant.isBalanced()) {
                    ant.addCurrentTraceToRoute();
                    ant.setCompleted(true);
                }
            }
        });
        this.evaporate();
    }

    @Override
    public void evaporate() {

        this.graphs.values().stream()
                .map(StandardGraph::getEdges)
                .flatMap(List<StandardEdge>::stream)
                .forEach(edge -> edge.evaporate(evaporate_rate));

    }

    @Override
    public boolean isAimAchieved() {

        return this.ants.stream().allMatch(SpiralAnt::isCompleted);
    }

    @Override
    public List<SpiralAnt> getAnts() {

        return this.ants;
    }

    @Override
    public Map<String, StandardGraph> getGraphs() {

        return this.graphs;
    }
}
