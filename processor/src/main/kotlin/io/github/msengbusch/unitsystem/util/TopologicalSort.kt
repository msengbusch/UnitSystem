package io.github.msengbusch.unitsystem.util

import java.util.*

// From https://gist.github.com/wadejason/36bfe5fb0f119de409492dd7b14d6120
class GraphNode<T>(val id: T) {
    val neighbours = mutableListOf<GraphNode<T>>()
}

object TopologicalSort {
    fun <T> topologicalSort(graph: List<GraphNode<T>>): List<GraphNode<T>> {
        val indegree = mutableMapOf<GraphNode<T>, Int>()
        for(node in graph) {
            indegree[node] = 0
        }

        for(node in graph) {
            for(neighbour in node.neighbours) {
                indegree[neighbour] = indegree[neighbour]!!.plus(1)
            }
        }

        val order = mutableListOf<GraphNode<T>>()
        val queue = LinkedList<GraphNode<T>>()

        for(node in graph) {
            if(indegree[node] == 0) {
                queue.offer(node)
                order.add(node)
            }
        }

        while(!queue.isEmpty()) {
            val node = queue.poll()
            for(neighbour in node.neighbours) {
                indegree[neighbour] = indegree[neighbour]!!.minus(1)
                if(indegree[neighbour] == 0) {
                    queue.offer(neighbour)
                    order.add(neighbour)
                }
            }
        }

        if(order.size != graph.size) {
            throw IllegalArgumentException("Failed to create dependency graph. There must be a cycle")
        }

        return order
    }
}