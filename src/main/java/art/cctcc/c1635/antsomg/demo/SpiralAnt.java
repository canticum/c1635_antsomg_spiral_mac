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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import art.cctcc.c1635.antsomg.demo.x.Edge_X;
import art.cctcc.c1635.antsomg.demo.x.Vertex_X;
import art.cctcc.c1635.antsomg.demo.y.Edge_Y;
import art.cctcc.c1635.antsomg.demo.y.Vertex_Y;
import java.util.Map;
import tech.metacontext.ocnhfa.antsomg.impl.StandardMove;
import tech.metacontext.ocnhfa.antsomg.model.Ant;
import tech.metacontext.ocnhfa.antsomg.model.Vertex;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class SpiralAnt implements Ant<SpiralTrace> {

    SpiralTrace currentTrace;
    List<SpiralTrace> route;
    private boolean completed;

    public SpiralAnt(Vertex_X x, Vertex_Y y) {

        this.currentTrace = new SpiralTrace(
                new StandardMove<>(new Edge_X(x)),
                new StandardMove<>(new Edge_Y(y))
        );
        this.route = new ArrayList<>();
    }

    boolean isBalanced() {

        Map<String, Long> count = route.stream()
                .map(trace -> trace.getDimension("x"))
                .collect(Collectors.groupingBy(Vertex::getName,
                        Collectors.counting()));
        if (count.size() == 3) {
            if (count.get("OUT") > (count.get("IN") + count.get("STAY") / 1.5)
                    && Math.random() > 0.75) {
                return true;
            }
            if (count.get("OUT") - count.get("IN") > 150) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SpiralTrace> getRoute() {

        return this.route;
    }

    @Override
    public void addCurrentTraceToRoute() {

        this.route.add(this.currentTrace);
    }

    @Override
    public SpiralTrace getCurrentTrace() {

        return this.currentTrace;
    }

    @Override
    public void setCurrentTrace(SpiralTrace trace) {

        if (Objects.nonNull(this.currentTrace)) {
            this.addCurrentTraceToRoute();
        }
        this.currentTrace = trace;
    }

    public boolean isCompleted() {

        return completed;
    }

    public void setCompleted(boolean completed) {

        this.completed = completed;
    }
}
