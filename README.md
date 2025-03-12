# Airline Route Management System
Developed a Java-based backend system for airline route optimization using graph algorithms to handle complex travel queries and network analysis. </br>

Key contributions:</br>
- Graph Algorithm Implementation: Utilized Breadth-First Search (BFS) to compute shortest paths by segment count, including transit-based itineraries, ensuring minimal hops for user convenience.</br>

- Network Analysis: Identified connected components via Depth-First Search (DFS) to determine airport groupings and evaluate system connectivity. </br>

- Cost-Constrained Pathfinding: Engineered backtracking with pruning to efficiently retrieve all valid trips and round trips within user-defined budgets and stop limits, addressing exponential runtime challenges.</br>

- System Design: Encapsulated functionality in AirlineSystem.java, adhering to interface specifications for modularity and scalability.</br>

- Error Handling & Validation: Integrated custom exceptions (e.g., AirportNotFoundException) and rigorous testing across diverse datasets (e.g., a4data1.txt) to ensure robustness and accuracy.
