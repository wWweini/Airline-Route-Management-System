import java.util.ArrayList;
import java.util.Set;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;

final public class AirlineTest {

  private AirlineInterface airline;
  private AirlineGraph g;
  private Scanner scan;
  private MenuProgram program;

  /**
   * Test client.
   */
  public static void main(String[] args) throws IOException {
    new AirlineTest();
  }

  private class LoadMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Load graph from a file";
    }

    @Override
    public void handle() {
      readGraph();
    }
  }

  private class ExitMenuItem implements CallableMenuItem {
    @Override
    public String getDisplayString() {
      return "Exit";
    }

    @Override
    public void handle() {
      System.out.println("Good Bye!");
      System.exit(0);
    }

  }

  private class CheapestMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Cheapest path";
    }

    @Override
    public void handle() {
      try {
        cheapest();
      } catch (AirportNotFoundException e) {
        e.printStackTrace();
      }
    }

  }

  private class ArtoculationPointsMenuItem implements CallableMenuItem {

    @Override
    public String getDisplayString() {
      return "Articulation points";
    }

    @Override
    public void handle() {
      articulation();
    }
  }

  public AirlineTest() {
    ArrayList<CallableMenuItem> menuItems = new ArrayList<>();
    airline = new AirlineSystem();
    scan = new Scanner(System.in);

    menuItems.add(new LoadMenuItem());
    menuItems.add(new CheapestMenuItem());
    menuItems.add(new ArtoculationPointsMenuItem());
    menuItems.add(new ExitMenuItem());
    program = new MenuProgram(menuItems);
    program.run();
  }

  private void readGraph() {
    System.out.println("Please enter file name:");
    String filename = scan.nextLine();
    g = loadRoutes(filename);
    System.out.println("Data imported successfully.");
  }

  /**
   * Load graph from a file
   * File format:
   * <number of airports>
   * <airport 1> <price to airport 1> <price to airport 2> ...
   * <airport 2> <price to airport 1> <price to airport 2> ...
   * ..
   * <airport N> <price to airport 1> <price to airport 2> ...
   * ...
   * Negative price means no route
   * 
   * @param filename the full path to the file
   * @return true if loading from file was successful and false otherwise (e.g.,
   *         when file is not found or file formatting not as expected.)
   */
  public AirlineGraph loadRoutes(String filename) {
    AirlineGraph result = null;
    Scanner fileScan = null;
    try {
      fileScan = new Scanner(new FileInputStream(filename));
      int v = Integer.parseInt(fileScan.nextLine());
      String[] airportcodes = new String[v];
      for (int i = 0; i < v; i++) {
        String[] line = fileScan.nextLine().split("\\s+");
        airportcodes[i] = line[0];
      }
      fileScan.close();
      fileScan = new Scanner(new FileInputStream(filename));
      fileScan.nextLine();
      result = new AirlineGraph(airportcodes);
      int from = 0;
      while (fileScan.hasNext()) {
        fileScan.next();
        for (int to = 0; to < v; to++) {
          double price = fileScan.nextDouble();
          if (price >= 0.0) {
            Route r = new Route(airportcodes[from], airportcodes[to], price);
            result.addRoute(r);
          }
        }
        from++;
        if (fileScan.hasNext())
          fileScan.nextLine();
      }
    } catch (Exception e) {
      System.out.println("Error loading airports from file " + filename);
      result = null;
    }
    return result;
  }

  private void articulation() {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {
      Set<String> articulation = airline.articulationPoints(g);
      System.out.println("Found " + articulation.size() + " articulation point(s):");

      for (String p : articulation) {
        System.out.print(p + " ");
      }
      System.out.println();

    }
  }

  private void cheapest() throws AirportNotFoundException {
    if (g == null) {
      System.out.println("Please load a graph first.");
    } else {

      for (String airport : g.getAirports()) {
        System.out.println(airport);
      }
      System.out.print("Please enter source airport: ");
      String source = scan.nextLine();
      System.out.print("Please enter destination airport: ");
      String destination = scan.nextLine();

      Set<ArrayList<Route>> shortestSet = airline.cheapestItinerary(g, source, destination);
      System.out.println("Found " + shortestSet.size() + " path(s):");
      for (ArrayList<Route> shortest : shortestSet) {
        double price = 0.0;
        for (Route r : shortest) {
          price += r.price;
        }
        System.out.print("The cheapest path from " + source +
            " to " + destination + " has " +
            (shortest.size() - 1) + " stop(s): ");
        for (Route r : shortest) {
          System.out.print(r.source + " ");
        }
        System.out.println(destination);
      }
    }
  }
}
