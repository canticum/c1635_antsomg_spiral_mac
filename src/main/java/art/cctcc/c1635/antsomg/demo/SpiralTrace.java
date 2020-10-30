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

import art.cctcc.c1635.antsomg.demo.x.Edge_X;
import art.cctcc.c1635.antsomg.demo.y.Edge_Y;
import tech.metacontext.ocnhfa.antsomg.impl.StandardMove;
import tech.metacontext.ocnhfa.antsomg.model.Trace;
import tech.metacontext.ocnhfa.antsomg.model.Vertex;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class SpiralTrace implements Trace {

    private final StandardMove<Edge_X> x;
    private final StandardMove<Edge_Y> y;

    public SpiralTrace(
            StandardMove<Edge_X> x,
            StandardMove<Edge_Y> y) {

        this.x = x;
        this.y = y;
    }

    @Override
    public Vertex getDimension(String dimension) {

        Vertex result = "x".equals(dimension)
                ? this.x.getSelected().getTo()
                : this.y.getSelected().getTo();
        return result;
    }

    public StandardMove<Edge_X> getX() {

        return this.x;
    }

    public StandardMove<Edge_Y> getY() {

        return this.y;
    }

    @Override
    public String toString() {

        return String.format("DemoTrace{%s, %s}",
                x.getSelected().getTo(),
                y.getSelected().getTo());
    }

}
