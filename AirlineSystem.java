import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AirlineSystem implements AirlineInterface {
    /**
   * finds cheapest path(s) between two airports
   * @param ag the AirlineGraph graph representing the airline system
   * @param source the String source airport code
   * @param destination the String destination airport code
   * @return a (possibly empty) Set<ArrayList<Route>> of cheapest paths.
   * Each path is an ArrayList<Route> of direct routes starting with source
   * and ending at destination
   * @throws AirportNotFoundException if any of the two airports are not found in the
   * Airline system
   */
    public Set<ArrayList<Route>> cheapestItinerary(AirlineGraph ag, String source,String destination) throws AirportNotFoundException{
        if(!ag.getAirports().contains(source)||!ag.getAirports().contains(destination)){
            throw new AirportNotFoundException("From: "+source+" To: "+destination);
        }

        Map<String, String> previous = new HashMap<>();
        //call dijkstra
        dijkstra(ag, source, destination, previous);

        //reconstruct paths
        Set<ArrayList<Route>> cheapestPaths = new HashSet<>();
        reconstructPaths(ag, previous, source, destination, new ArrayList<>(), cheapestPaths);

        return cheapestPaths;
    }

    //dijkstra's algorithm
    private void dijkstra(AirlineGraph ag, String source, String destination, Map<String, String> previous) {
        Map<String, Double> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        //initialize distances and add
        for (String airport : ag.getAirports()) {
            distances.put(airport, Double.MAX_VALUE);
        }
        distances.put(source, 0.0);
        pq.offer(source);

        while (!pq.isEmpty()) {
                String currentAirport = pq.poll();
                //find the path to destination
                if (currentAirport.equals(destination)) {
                    break; 
                }
                visited.add(currentAirport);
                //check each route
                for (Route r : ag.adj(currentAirport)) {
                    String neighbor = r.destination;
                    if (!visited.contains(neighbor)) {
                        double newDistance = distances.get(currentAirport) + r.price;
                        if (newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            previous.put(neighbor,currentAirport);
                            pq.offer(neighbor);
                        }
                    }
                }
            }
    }
    //helper method to reconstruct paths recursively
    private void reconstructPaths(AirlineGraph ag, Map<String, String> previous, String source, String current, List<Route> pathSoFar, Set<ArrayList<Route>> paths) {
        if (current.equals(source)) {
            ArrayList<Route> path = new ArrayList<>(pathSoFar);
            Collections.reverse(path);
            paths.add(path);
            return;
        }
        String parent = previous.get(current);
        Route route = ag.getRoute(parent, current);
        pathSoFar.add(route);
        reconstructPaths(ag, previous, source, parent, pathSoFar, paths);
        pathSoFar.remove(pathSoFar.size() - 1);
    }

    /**
   * finds the set of articulation points in an AirlineGraph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<String> of articulation points.
   */
    public Set<String> articulationPoints(AirlineGraph ag) {
        //instantiation
        Set<String> result = new HashSet<>();
        Map<String, Integer> disc = new HashMap<>();
        Map<String, Integer> low = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> seen = new HashSet<>();
        int num = 0;
    
        //use dfs for each unseen airport
        for (String airport : ag.getAirports()) {
            if (!seen.contains(airport)) {
                dfs(airport, airport, num, result, disc, low, parent, seen, ag);
            }
        }
        return result;
    }
    
    private void dfs(String source, String start, int num, Set<String> articulationPoints, Map<String, Integer> discoveryTimes,Map<String, Integer> low, Map<String, String> parent, Set<String> seen, AirlineGraph ag) {
        //initialize
        int children = 0;
        //add the source
        seen.add(source);
        num++;
        discoveryTimes.put(source, num);
        low.put(source, num);
        //loop through each airport
        for (Route r : ag.adj(source)) {
            if (!seen.contains(r.destination)) {
                children++;
                //update parent
                parent.put(r.destination, source); 
                dfs(r.destination, start, num, articulationPoints, discoveryTimes, low, parent, seen, ag);
              
                //update low value
                low.put(source, Math.min(low.get(source), low.get(r.destination)));

                //check for articulation point
                if (low.get(r.destination) >= discoveryTimes.get(source)) {
                    //add if the source is not equal to the start
                    if (!source.equals(start)) {
                        articulationPoints.add(source);
                    }
                }
            } 
            else{
                if (!r.destination.equals(parent.get(source))) {
                    //update low for back edges
                    low.put(source, Math.min(low.get(source), discoveryTimes.get(r.destination)));
                }
            }
        }
        //add if the start has > 1 children 
        if (source.equals(start) && children > 1) {
            articulationPoints.add(source);
        }
    }
}
