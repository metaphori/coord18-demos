package it.unibo.demos.coord18

import it.unibo.scafi.incarnations.BasicSimulationIncarnation._
import it.unibo.scafi.simulation.gui.model.NodeValue.SENSOR
import it.unibo.scafi.simulation.gui.model.implementation.SensorEnum
import it.unibo.scafi.simulation.gui.{Launcher, Settings}

import scala.util.Random

object DistributedSensingLauncher extends Launcher {
  Settings.ShowConfigPanel = false
  Settings.Sim_ProgramClass = "it.unibo.demos.coord18.DistributedSensingProgram"
  Settings.Sim_NbrRadius = 0.15
  Settings.Sim_NumNodes = 100
  Settings.Sim_Sensors = "temperature double 77.5"
  launch()
}

trait DistribSensingLib extends BlockG with BlockC with BlockS with StandardSensors { I: AggregateProgram =>
  def distribSense(size:Double, metric: => Double, v:Double) = {
    val leaders = S(size, metric)
    val potential = distanceTo(leaders, metric)
    val collection = sumCollect(potential, v)
    val count = sumCollect(potential, 1.0)
    val avg = collection / count
    val res = broadcast(leaders, avg)
    if(leaders) "<<"+collection+";"+count+">>" else f"${res}%6.2g"
  }

  def distanceTo(source: Boolean, metric: => Double = nbrRange): Double = G2(source)(0.0)(_ + metric)()
  def sumCollect(potential: Double, v: Double): Double = C[Double,Double](potential, _+_, v, 0)
  override def broadcast[V: Builtins.Bounded](src: Boolean, v: V): V = G[V](src, v, x => x, nbrRange)
}

class DistributedSensingProgram extends AggregateProgram with DistribSensingLib {
  override type MainResult = Any

  def senseTemperature: Double = if(sense[Boolean]("sens1")) 100.0
  else if(sense[Boolean]("sens2")) 50.0
  else 0.0

  override def main(): Any = {
    distribSense(0.5, nbrRange(), senseTemperature)
  }
}