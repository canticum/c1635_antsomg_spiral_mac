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
package tech.metacontext.ocnhfa.antsomg.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import tech.metacontext.ocnhfa.antsomg.model.Graph;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 * @param <E>
 * @param <V>
 */
public abstract class StandardGraph<E extends StandardEdge<V>, V extends StandardVertex>
  implements Graph<E, V, StandardMove<E>> {

  private List<E> edges;
  private V start;
  protected double alpha, beta;
  private FractionMode fraction_mode;

  public enum FractionMode {

    Power, Coefficient;
  }

  public StandardGraph(double alpha, double beta) {

    this.edges = new ArrayList<>();
    this.alpha = alpha;
    this.beta = beta;
    this.fraction_mode = FractionMode.Coefficient;
    this.init_graph();
  }

  public double getFraction(E edge) {

    switch (this.fraction_mode) {
    case Coefficient:
      return edge.getPheromoneTrail() * this.alpha
        + 1.0 / edge.getCost() * this.beta;
    case Power:
    default:
      return Math.pow(edge.getPheromoneTrail(), this.alpha)
        + Math.pow(1.0 / edge.getCost(), this.beta);
    }
  }

  @Override
    public StandardMove<E> move(V current, double pheromone_deposit, 
    double explore_chance, double... parameters) {

    List<E> paths = this.queryByVertex(current);
    List<Double> fractions = new ArrayList<>();
    double sum = 0;
    for (E edge : paths) {
      double f= getFraction(edge);
      fractions.add(f);
      sum+=f;
    }
    AtomicReference<Double> r = new AtomicReference<>(Math.random() * sum);
    boolean isExploring = Math.random() < explore_chance;
    E selected = null;
    if (isExploring) {
      selected = paths.get(new Random().nextInt(paths.size()));
    } else {
      for (int i=0; i<paths.size(); i++) {
        if (r.getAndSet(r.get() - fractions.get(i)) < fractions.get(i)) {
          selected = paths.get(i);
          break;
        }
      }
    }
    selected.addPheromoneDeposit(pheromone_deposit);
    return StandardMove.getInstance(isExploring, paths, selected);
  }

  @Override
    public List<E> queryByVertex(V vertex) {

    List<E> result = new ArrayList<>();
    for (E e : this.edges) {
      if (e.getFrom().equals(vertex)) result.add(e);
    }
    return result;
  }

  public String asGraphviz() {

    String result="digraph "+ this.getClass().getSimpleName()+" {\n";
    for (E path : getEdges()) {
      String item = String.format("\t%s -> %s [ label=\"p=%.3f,c=%.2f\"];", 
        path.getFrom().getName(), 
        path.getTo().getName(), 
        path.getPheromoneTrail(), 
        path.getCost());
      result+=item+"\n";
    }
    return result;
  }

  public void addEdges(E... edges) {

    this.edges.addAll(Arrays.asList(edges));
  }

  public List<E> getEdges() {

    return this.edges;
  }

  public void setEdges(List<E> edges) {

    this.edges = edges;
  }

  public V getStart() {

    return start;
  }

  public void setStart(V start) {

    this.start = start;
  }

  public FractionMode getFraction_mode() {

    return fraction_mode;
  }

  public void setFraction_mode(FractionMode fraction_mode) {

    this.fraction_mode = fraction_mode;
  }
}
