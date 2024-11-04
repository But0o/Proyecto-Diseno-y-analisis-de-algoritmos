import java.util.*;

// Clase que representa una arista en el grafo
class Arista {
    int nodo;
    int peso;

    public Arista(int nodo, int peso) {
        this.nodo = nodo;
        this.peso = peso;
    }
}

// Clase que representa el grafo con métodos para manejar aristas
class Grafo {
    private final Map<Integer, List<Arista>> grafo;

    public Grafo(int n) {
        grafo = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            grafo.put(i, new ArrayList<>());
        }
    }

    // Método para agregar una arista
    public void addArista(int origen, int destino, int peso) {
        grafo.get(origen).add(new Arista(destino, peso));
        grafo.get(destino).add(new Arista(origen, peso)); // Para grafo no dirigido
    }
    
    // Método para eliminar un nodo y todas sus aristas adyacentes
    public void removeNodo(int nodo) {
        if (!grafo.containsKey(nodo)) {
            return; // Si el nodo no existe, no hace nada
        }
        // Eliminar todas las aristas que apuntan al nodo desde otros nodos
        for (Arista arista : new ArrayList<>(grafo.get(nodo))) {
            int nodoVecino = arista.nodo;
            grafo.get(nodoVecino).removeIf(adj -> adj.nodo == nodo);
        }
        // Eliminar el nodo y sus aristas adyacentes
        grafo.remove(nodo);
    }

    // Obtener los nodos adyacentes
    public List<Arista> getAdyacentes(int nodo) {
        return grafo.getOrDefault(nodo, new ArrayList<>());
    }

    // Mostrar vecinos de cada nodo
    public void mostrarVecinos() {
        System.out.println("Vecinos de cada nodo:");
        for (int nodo : grafo.keySet()) {
            List<Arista> adyacentes = grafo.get(nodo);
            System.out.print("Nodo " + nodo + ": ");
            if (adyacentes.isEmpty()) {
                System.out.println("No tiene vecinos");
            } else {
                for (Arista arista : adyacentes) {
                    System.out.print(arista.nodo + " (peso: " + arista.peso + "), ");
                }
                System.out.println();
            }
        }
    }
}

// Clase principal que implementa el algoritmo de Dijkstra
public class AlgoritmoDijkstra {
    public static void main(String[] args) {
        int n = 12; // Número de nodos en el grafo (1 a 12)
        Grafo grafo = new Grafo(n);

        // Agregar aristas al grafo
        grafo.addArista(1, 2, 10);
        grafo.addArista(1, 6, 5);
        grafo.addArista(1, 7, 18);
        grafo.addArista(2, 7, 1);
        grafo.addArista(2, 3, 5);
        grafo.addArista(3, 4, 1);
        grafo.addArista(3, 7, 3);
        grafo.addArista(4, 5, 5);
        grafo.addArista(5, 6, 9);
        grafo.addArista(5, 8, 5);
        grafo.addArista(6, 8, 6);
        grafo.addArista(6, 7, 7);
        grafo.addArista(7, 8, 1);
        grafo.addArista(8, 9, 10);
        grafo.addArista(9, 4, 15);
        grafo.addArista(9, 3, 2);
        grafo.addArista(9, 7, 6);
        grafo.addArista(9, 10, 3);
        grafo.addArista(10, 4, 7);
        grafo.addArista(11, 5, 2);
        grafo.addArista(11, 8, 9);
        grafo.addArista(12, 6, 4);
        grafo.addArista(12, 3, 8);

        // Eliminar un nodo y recalcular caminos
        System.out.println("Grafo antes de eliminar el nodo:");
        ejecutarDijkstra(grafo, 1, n);
        grafo.mostrarVecinos();

        // Eliminar el nodo y todas sus aristas adyacentes
        //grafo.removeNodo(7);
        System.out.println("\nGrafo después de eliminar el nodo :");
        ejecutarDijkstra(grafo, 1, n);
        System.out.println();
        grafo.mostrarVecinos();
    }

    // Método auxiliar para ejecutar y mostrar resultados de Dijkstra
    private static void ejecutarDijkstra(Grafo grafo, int origen, int n) {
        ResultadosDijkstra resultados = dijkstra(grafo, origen, n);

        // Imprimir las distancias y caminos desde el origen a cada nodo
        System.out.println("Distancias y caminos desde el nodo " + origen + ":");
        for (int i = 1; i <= n; i++) {
            if (resultados.distancias[i] == Integer.MAX_VALUE) {
                System.out.println("Hasta el nodo " + i + " -> No hay camino");
            } else {
                System.out.println("Hasta el nodo " + i + " -> Distancia: " + resultados.distancias[i] +
                        ", Camino: " + reconstruirCamino(resultados.predecesores, origen, i));
            }
        }
    }

    // Clase para almacenar los resultados de Dijkstra
    static class ResultadosDijkstra {
        int[] distancias;
        int[] predecesores;

        ResultadosDijkstra(int n) {
            distancias = new int[n + 1];
            predecesores = new int[n + 1];
        }
    }

    // Implementación del algoritmo de Dijkstra
    public static ResultadosDijkstra dijkstra(Grafo grafo, int start, int n) {
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
            for (Arista arista : grafo.getAdyacentes(nodoActual)) {
                int vecino = arista.nodo;
                int peso = arista.peso;
                int nuevaDistancia = resultados.distancias[nodoActual] + peso;

                // Si encontramos un camino más corto al vecino, lo actualizamos
                if (nuevaDistancia < resultados.distancias[vecino]) {
                    resultados.distancias[vecino] = nuevaDistancia;
                    resultados.predecesores[vecino] = nodoActual;
                    pq.offer(new Arista(vecino, nuevaDistancia));
                }
            }
        }

        return resultados;
    }

    // Método para reconstruir el camino desde el origen hasta un nodo dado
    public static String reconstruirCamino(int[] predecesores, int origen, int destino) {
        if (predecesores[destino] == -1 && origen != destino) {
            return "No hay camino"; // Si no hay predecesor y no es el nodo de origen
        }

        List<Integer> camino = new ArrayList<>();
        for (int nodo = destino; nodo != -1; nodo = predecesores[nodo]) {
            camino.add(nodo);
        }
        Collections.reverse(camino);
        return camino.toString();
    }
}
