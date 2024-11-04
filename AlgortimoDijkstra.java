import java.util.*;

public class AlgortimoDijkstra {
    public static void main(String[] args) {
        int n = 9; // Número de nodos en el grafo (1 a 9)

        // Grafo representado con una lista de adyacencia
        Map<Integer, List<Arista>> graph = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }

        // Agregar las aristas al grafo
        addArista(graph, 1, 2, 10);
        addArista(graph, 1, 6, 5);
        addArista(graph, 1, 7, 18);
        addArista(graph, 2, 7, 1);
        addArista(graph, 2, 3, 5);
        addArista(graph, 3, 4, 1);
        addArista(graph, 3, 7, 3);
        addArista(graph, 4, 5, 5);
        addArista(graph, 5, 6, 9);
        addArista(graph, 5, 8, 5);
        addArista(graph, 6, 8, 6);
        addArista(graph, 6, 7, 7);
        addArista(graph, 7, 8, 1);
        addArista(graph, 8, 9, 10);
        addArista(graph, 9, 4, 15);
        addArista(graph, 9, 3, 2);
        addArista(graph, 9, 7, 6);


        int origen = 1; // Nodo de origen
        ResultadosDijkstra resultados = dijkstra(graph, origen, n);

        // Imprimir las distancias y caminos desde el origen a cada nodo
        System.out.println("Distancias y caminos desde el nodo " + origen + ":");
        for (int i = 1; i <= n; i++) {
            System.out.println("Hasta el nodo " + i + " -> Distancia: " + resultados.distancias[i] +
                    ", Camino: " + reconstruirCamino(resultados.predecesores, origen, i));
        }
    }

    // Método para agregar aristas
    public static void addArista(Map<Integer, List<Arista>> graph, int src, int dest, int peso) {
        graph.get(src).add(new Arista(dest, peso));
        // Si el grafo es no dirigido, añade esta línea:
        graph.get(dest).add(new Arista(src, peso)); // Descomentar para grafos no dirigidos
    }

    // Clase para almacenar resultados de Dijkstra
    static class ResultadosDijkstra {
        int[] distancias;
        int[] predecesores;

        ResultadosDijkstra(int n) {
            distancias = new int[n + 1];
            predecesores = new int[n + 1];
        }
    }

    // Implementación del algoritmo de Dijkstra
    public static ResultadosDijkstra dijkstra(Map<Integer, List<Arista>> graph, int start, int n) {
        ResultadosDijkstra resultados = new ResultadosDijkstra(n);
        Arrays.fill(resultados.distancias, Integer.MAX_VALUE);
        Arrays.fill(resultados.predecesores, -1);
        resultados.distancias[start] = 0;

        PriorityQueue<Arista> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.peso));
        pq.offer(new Arista(start, 0));

        while (!pq.isEmpty()) {
            Arista actual = pq.poll();
            int nodoActual = actual.nodo;
            int distanciaActual = actual.peso;

            if (distanciaActual > resultados.distancias[nodoActual]) {
                continue;
            }

            // Procesar cada vecino del nodo actual
            for (Arista arista : graph.get(nodoActual)) {
                int vecino = arista.nodo;
                int peso = arista.peso;
                int nuevaDistancia = resultados.distancias[nodoActual] + peso;

                // Si encontramos un camino más corto al vecino, lo actualizamos
                if (nuevaDistancia < resultados.distancias[vecino]) {
                    resultados.distancias[vecino] = nuevaDistancia;
                    resultados.predecesores[vecino] = nodoActual; // Actualiza el predecesor
                    pq.offer(new Arista(vecino, nuevaDistancia));
                }
            }
        }

        return resultados;
    }

    // Método para reconstruir el camino desde el origen hasta un nodo dado
    public static String reconstruirCamino(int[] predecesores, int origen, int destino) {
        if (predecesores[destino] == -1) {
            return "No hay camino"; // Si no hay predecesor, no hay camino
        }

        List<Integer> camino = new ArrayList<>();
        for (int nodo = destino; nodo != -1; nodo = predecesores[nodo]) {
            camino.add(nodo);
        }
        Collections.reverse(camino);
        return camino.toString();
    }
}