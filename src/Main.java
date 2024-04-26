public class Main
{
    private static void traverseLabyrinth(Labyrinth labyrinth, TraversalMethod method, boolean clearNodes)
    {
        if (clearNodes)
        {
            labyrinth.clearNodeValues();
        }
        System.out.println("___________________");
        System.out.println("Traversing with " + method);
        System.out.println("___________________");
        long startTime = System.nanoTime();
        switch (method)
        {
            case A_STAR -> labyrinth.traverseAStar();
            case DIJKSTRA -> labyrinth.traverseDijkstra();
            case DFS -> labyrinth.traverseDFS();
            case BFS -> labyrinth.traverseBFS();
            case GREEDY -> labyrinth.traverseGreedy();
        }
        long endTime = System.nanoTime();
        labyrinth.printGrid();
        System.out.println("Path length: " + labyrinth.getPathLength());
        System.out.println(method + " took " + (endTime - startTime) + " nanoseconds");
    }

    public static void main(String[] args)
    {
        Labyrinth labyrinth = Labyrinth.getInstance();
        labyrinth.generateLabyrinth(10, 10);
        labyrinth.printGrid();
        traverseLabyrinth(labyrinth, TraversalMethod.A_STAR, false);
        traverseLabyrinth(labyrinth, TraversalMethod.DIJKSTRA, true);
        traverseLabyrinth(labyrinth, TraversalMethod.GREEDY, true);
        traverseLabyrinth(labyrinth, TraversalMethod.DFS, true);
        traverseLabyrinth(labyrinth, TraversalMethod.BFS, true);
    }
}