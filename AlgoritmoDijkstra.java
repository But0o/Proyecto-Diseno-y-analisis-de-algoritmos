import java.util.*;

// Clase que representa una arista en el grafo, indcando el nodo de destino y el peso de la arista
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

    // Método para agregar una arista, indicando origen, destino y peso de la arista
    public void addArista(int origen, int destino, int peso) {
        grafo.get(origen).add(new Arista(destino, peso));
        grafo.get(destino).add(new Arista(origen, peso)); // Para grafo no dirigido, comentar la linea
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
        //Se itera el grafo en base a cada nodo
        for (int nodo : grafo.keySet()) {
            //Se crea una lista con los nodos adyacentes a cada nodo iterado
            List<Arista> adyacentes = grafo.get(nodo);
            System.out.print("Nodo " + nodo + ": ");
            //Si el nodo no tiene vecinos
            if (adyacentes.isEmpty()) {
                System.out.println("No tiene vecinos");
                //Si el nodo tiene vecinos
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
        grafo.addArista(1, 5, 1);
        grafo.addArista(2, 3, 4);
        grafo.addArista(2, 7, 7);
        grafo.addArista(3, 7, 8);
        grafo.addArista(4, 8, 9);
        grafo.addArista(5, 9, 2);
        grafo.addArista(6, 7, 1);
        grafo.addArista(6, 11, 12);
        grafo.addArista(7, 4, 4);
        grafo.addArista(7, 8, 7);
        grafo.addArista(8, 12, 11);
        grafo.addArista(9, 10, 12);
        grafo.addArista(9, 6, 10);
        grafo.addArista(10, 11, 22);

        // Eliminar un nodo y recalcular caminos
        System.out.println("Grafo antes de eliminar el nodo:");
        ejecutarDijkstra(grafo, 1, n);
        grafo.mostrarVecinos();

        // Eliminar un nodo y todas sus aristas adyacentes
        int e = 5
        grafo.removeNodo(e);
        System.out.println("\nGrafo después de eliminar el nodo " + e + ":");
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
