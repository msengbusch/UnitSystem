package io.github.msengbusch.unitsystem.steps.unit

import io.github.msengbusch.unitsystem.context.Context
import io.github.msengbusch.unitsystem.step.Step
import io.github.msengbusch.unitsystem.steps.unit.data.UnitName
import io.github.msengbusch.unitsystem.steps.unit.data.ValidUnit
import io.github.msengbusch.unitsystem.util.GraphNode
import io.github.msengbusch.unitsystem.util.TopologicalSort
import io.github.msengbusch.unitsystem.util.debug

class UnitSortStep : Step {
    val units = mutableListOf<ValidUnit>()

    override fun process(context: Context) {
        val validUnits = context.getStep(UnitValidateStep::class.java).units

        val graph: Map<String, GraphNode<String>> = validUnits.keys.associateWith { name -> GraphNode(name) }

        graph.forEach { (name, node) ->
            val unit = validUnits[name]!!

            unit.before.forEach { beforeUnit ->
                node.neighbours.add(graph[beforeUnit.name]!!)
            }

            unit.after.forEach { afterUnit ->
                graph[afterUnit.name]!!.neighbours.add(node)
            }
        }

        val sorted = TopologicalSort.topologicalSort(graph.values.toList())
        units.addAll(sorted.map { node -> validUnits[node.id]!! })

        context.debug("Sorted units: ${sorted.joinToString() { it.id }}")
    }
}