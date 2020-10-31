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
import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import processing.core.PApplet;
import tech.metacontext.ocnhfa.antsomg.impl.StandardGraph;
import tech.metacontext.ocnhfa.antsomg.model.Vertex;
import art.cctcc.c1635.antsomg.demo.SpiralSystem;
import art.cctcc.c1635.antsomg.demo.SpiralAnt;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */

int size = 700;
SpiralSystem demo;
float theta = 0.0;
Map<SpiralAnt, Float> radius = new HashMap();

void settings() {
  size(size, size);
}

void setup() {
  colorMode(RGB);
  background(0);
  noFill();
  frameRate(120);
  demo = new SpiralSystem(200);
  demo.init_graphs();
  demo.init_population();
  for (SpiralAnt ant : demo.getAnts()) {
    radius.put(ant, 10.0);
  }
}

float move_amount = 2.5;
float delta_theta = 1.0;

void draw() {

  for (SpiralAnt ant : demo.getAnts()) {
    if (!ant.isCompleted()) {
      Vertex move = ant.getCurrentTrace().getDimension("x");
      float r = radius.get(ant);
      if ("IN".equals(move.getName()) && r > move_amount) {
        r -= move_amount;
      }
      if ("OUT".equals(move.getName())) {
        r += move_amount;
      }
      radius.replace(ant, r);
      float x = size / 2 + r * cos(this.theta), 
        y = size / 2 + r * sin(this.theta);
      switch (ant.getCurrentTrace().getDimension("y").getName()) {
      case "WHITE":
        stroke(Color.WHITE.getRGB());
        break;
      case "RED":
        stroke(Color.RED.getRGB());
        break;
      case "YELLOW":
        stroke(Color.YELLOW.getRGB());
        break;
      case "BLUE":
        stroke(Color.BLUE.getRGB());
        break;
      }
      point(x, y);
    }
  }

  this.theta += delta_theta * PI / 180;
  if (demo.isAimAchieved()) {
    for (StandardGraph graph : demo.getGraphs().values()) 
      System.out.println(graph.asGraphviz());
    noLoop();
  } else {
    demo.navigate();
  }
}
