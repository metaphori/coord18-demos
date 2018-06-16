package it.unibo.demos.coord18

import it.unibo.scafi.incarnations.BasicSimulationIncarnation.{AggregateProgram, StandardSensors}
import it.unibo.scafi.simulation.gui.model.NodeValue.SENSOR
import it.unibo.scafi.simulation.gui.model.implementation.SensorEnum
import it.unibo.scafi.simulation.gui.{Launcher, Settings}

import scala.util.Random

object Coord18DemoLauncher extends Launcher {
  if(args.length!=1){
    println("USAGE: <runCommand> <N>")
    Settings.ShowConfigPanel = true
  } else{
    Settings.ShowConfigPanel = false
  }

  Settings.Sim_ProgramClass = "it.unibo.demos.coord18.Main" + args(0)
  Settings.Sim_NbrRadius = 0.15
  Settings.Sim_NumNodes = 100
  Settings.Sim_Sensors = "temperature double 77.5"
  launch()
}

abstract class BasicAggregateProgram extends AggregateProgram {
  override type MainResult = Any
}

class Main0 extends BasicAggregateProgram {
  def main() = 1
}

class Main1 extends BasicAggregateProgram {
  def main() = 2+3
}

class Main2 extends BasicAggregateProgram {
  def main() = (10,20)
}

class Main3 extends BasicAggregateProgram {
  def main() = Random.nextInt(100)
}

class Main4 extends BasicAggregateProgram {
  def main() = sense[Boolean]("sens1")
}

class Main5 extends BasicAggregateProgram {
  def main() = if(sense[Boolean]("sens1") && sense[Boolean]("sens2")) "hot" else "cold"
}

class Main6 extends BasicAggregateProgram {
  def main() = mid
}

class Main7 extends BasicAggregateProgram {
  def sense1() = sense[Boolean]("sens1") // Function for reuse

  def main() = foldhood(0)(_+_)(if(nbr{sense1}) 1 else 0)
}

class Main8 extends BasicAggregateProgram with StandardSensors {
  def main = (minHood{nbrRange}, f"${minHoodPlus{nbrRange}}%.2g")
}

class Main9 extends BasicAggregateProgram {
  def main() = rep(0){ x => x + 1 }
}

class Main10 extends BasicAggregateProgram {
  def main() = rep(Random.nextInt(100)){ x => x }
}

class Main11 extends BasicAggregateProgram {
  def main() = rep(0){ x => x + rep(Random.nextInt(100))(y => y) }
}

class Main12 extends BasicAggregateProgram with StandardSensors {
  def main() =
    rep(Double.PositiveInfinity)(distance =>
      mux(sense[Boolean]("source")){
        0.0
      }{
        minHoodPlus(nbr{distance} + nbrRange)
      }
    )
}

