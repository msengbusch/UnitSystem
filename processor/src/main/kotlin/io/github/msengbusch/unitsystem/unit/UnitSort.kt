package io.github.msengbusch.unitsystem.unit

import com.google.devtools.ksp.processing.KSPLogger
import io.github.msengbusch.unitsystem.util.ClassName
import io.github.msengbusch.unitsystem.util.GraphNode
import io.github.msengbusch.unitsystem.util.TopologicalSort

object UnitSort {
    fun sortUnits(validUnits: Map<ClassName, ValidUnit>, logger: KSPLogger): List<ValidUnit> {
        val graph: Map<ClassName, GraphNode<ClassName>> =
            validUnits.keys.associateWith { className -> GraphNode(className) }

        graph.forEach { (className, node) ->
            val unit = validUnits[className]!!

            unit.before.forEach { beforeUnit ->
                node.neighbours.add(graph[beforeUnit.className]!!)
            }

            unit.after.forEach { afterUnit ->
                graph[afterUnit.name]!!.neighbours.add(node)
            }
        }

        val sorted = TopologicalSort.topologicalSort(graph.values.toList())
            .map { node -> validUnits[node.id]!! }

        logger.info("Sorted units: ${sorted.joinToString { it.name }}")

        return sorted
    }
}