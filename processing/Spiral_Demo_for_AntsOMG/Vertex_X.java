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
package art.cctcc.c1635.antsomg.demo.x;

import java.util.HashMap;
import java.util.Map;
import tech.metacontext.ocnhfa.antsomg.impl.StandardVertex;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class Vertex_X extends StandardVertex {

    private static Map<X, Vertex_X> instances
            = new HashMap<>();

    public static enum X {
        STAY, OUT, IN;

        X() {
            instances.put(this, new Vertex_X(this));
        }
    }

    private Vertex_X(X name) {

        super(name.toString());
    }

    public static Vertex_X get(X name) {

        return instances.get(name);
    }
}
