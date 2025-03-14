import java.util.Set;
import java.util.ArrayList;

public interface AirlineInterface {

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
  public Set<ArrayList<Route>> cheapestItinerary(AirlineGraph ag, String source,
                                                      String destination) throws AirportNotFoundException;


 /**
   * finds the set of articulation points in an AirlineGraph
   * @param ag the AirlineGraph graph representing the airline system
   * @return a (possibly empty) Set<String> of articulation points.
   */
  public Set<String> articulationPoints(AirlineGraph ag);

}
