public class Main
{
    static final int MAZE_SIZE_X = 31;
    static final int MAZE_SIZE_Y = 31;

    private static void traverseLabyrinth(Maze maze, TraversalMethod method, boolean clearNodes)
    {
        if (clearNodes)
        {
            maze.clearNodeValues();
        }
        System.out.println("___________________");
        System.out.println("Traversing with " + method);
        System.out.println("___________________");
        long startTime = System.nanoTime();
        switch (method)
        {
            case A_STAR -> maze.traverseAStar();
            case DIJKSTRA -> maze.traverseDijkstra();
            case DFS -> maze.traverseDFS();
            case BFS -> maze.traverseBFS();
            case GREEDY -> maze.traverseGreedy();
        }
        long endTime = System.nanoTime();
        maze.printGrid();
        System.out.println("Path length: " + maze.getPathLength());
        System.out.println(method + " took " + (endTime - startTime) + " nanoseconds");
    }

    public static void main(String[] args)
    {
        Maze maze = Maze.getInstance();
        maze.generateLabyrinth(MAZE_SIZE_X, MAZE_SIZE_Y);
        maze.printGrid();
        System.out.println();
        traverseLabyrinth(maze, TraversalMethod.A_STAR, false);
        traverseLabyrinth(maze, TraversalMethod.DIJKSTRA, true);
        traverseLabyrinth(maze, TraversalMethod.GREEDY, true);
        traverseLabyrinth(maze, TraversalMethod.DFS, true);
        traverseLabyrinth(maze, TraversalMethod.BFS, true);
    }
}