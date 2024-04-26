import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public final class Labyrinth
{
    private static Labyrinth INSTANCE;
    private Node[][] grid = null;
    private int size_x = 0;
    private int size_y = 0;
    private Node start = null;
    private Node target = null;
    private final ArrayList<Node> foundPath = new ArrayList<>();

    public static final int DIAGONAL_MOVEMENT_COST = 14;
    public static final int COMMON_MOVEMENT_COST = 10;

    private Labyrinth() {}
    public static Labyrinth getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new Labyrinth();
        }
        return INSTANCE;
    }

    public static double evaluateDistance(Node first, Node second)
    {
        int x = Math.abs(first.getX() - second.getX());
        int y = Math.abs(first.getY() - second.getY());
        return Math.sqrt(x * x + y * y);
    }

    public void generateLabyrinth(int x, int y)
    {
        grid = new Node[x][y];
        size_x = x;
        size_y = y;

        for (int i = 0; i < size_y; ++i)
        {
            grid[i] = new Node[size_x];
            for (int j = 0; j < size_x; ++j)
            {
                grid[i][j] = new Node();
                grid[i][j].setY(i);
                grid[i][j].setX(j);
                if (Math.random() < 0.1)
                {
                    grid[i][j].setObstacle(true);
                }
            }
        }

        // Here should be some obstacle generation algorithms

        boolean nodeSet = false;
        while (!nodeSet)
        {
            target = grid[(int) (Math.random() * x)][(int) (Math.random() * y)];
            if (!target.isObstacle())
            {
                nodeSet = true;
            }
        }
        nodeSet = false;
        while (!nodeSet)
        {
            start = grid[(int) (Math.random() * x)][(int) (Math.random() * y)];
            if (start != target && !start.isObstacle() && evaluateDistance(start, target) > (double) size_x / 2)
            {
                nodeSet = true;
            }
        }
    }

    public void printGrid()
    {
        if (target.getParent() != null)
        {
            Node current = target.getParent();
            while (current != start)
            {
                foundPath.add(current);
                current = current.getParent();
            }
        }
        for (Node[] row : grid)
        {
            for (Node node : row)
            {
                if (target == node)
                {
                    System.out.print('T');
                }
                else if (start == node)
                {
                    System.out.print('S');
                }
                else if (node.isObstacle())
                {
                    System.out.print('O');
                }
                else if (foundPath.contains(node))
                {
                    System.out.print('W');
                }
                else
                {
                    System.out.print('-');
                }
                System.out.print("  ");
            }
            System.out.print('\n');
        }
    }

    private void evaluateNodeValues(Node node)
    {
        node.setHValue(evaluateDistance(node, target));
        node.setGValue(evaluateDistance(node, start));
    }

    public void clearNodeValues() throws NullPointerException
    {
        if (grid == null)
        {
            throw new NullPointerException();
        }
        for (Node[] row : grid)
        {
            for (Node node : row)
            {
                node.setParent(null);
                node.setGValue(Double.MAX_VALUE);
                node.setHValue(Double.MAX_VALUE);
            }
        }
        foundPath.clear();
    }

    int getPathLength()
    {
        Node current = target;
        int pathLength = 0;
        while (current.getParent() != null)
        {
            if (current.getY() + 1 < size_y && current.getParent() == grid[current.getY() + 1][current.getX()] ||
                    current.getX() + 1 < size_x && current.getParent() == grid[current.getY()][current.getX() + 1] ||
                    current.getY() - 1 >= 0 && current.getParent() == grid[current.getY() - 1][current.getX()] ||
                    current.getX() - 1 >= 0 && current.getParent() == grid[current.getY()][current.getX() - 1])
            {
                pathLength += COMMON_MOVEMENT_COST;
            }
            else
            {
                pathLength += DIAGONAL_MOVEMENT_COST;
            }
            current = current.getParent();
        }
        return pathLength;
    }

    void traverseAStar()
    {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        open.add(start);
        evaluateNodeValues(start);
        Node current;
        while (true)
        {
            current = open.getFirst();
            for (Node node : open) {
                if (node.getFValue() < current.getFValue()) {
                    current = node;
                }
            }
            open.remove(current);
            closed.add(current);

            if (current == target)
            {
                break;
            }
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <=1; ++x)
                {
                    if (current.getX() + x >= size_x || current.getY() + y >= size_y ||
                            current.getX() + x < 0 || current.getY() + y < 0 ||
                            grid[current.getY() + y][current.getX() + x].isObstacle() ||
                            closed.contains(grid[current.getY() + y][current.getX() + x]))
                    {
                        continue;
                    }
                    Node neighbour = grid[current.getY() + y][current.getX() + x];
                    // Needed cause there's diagonal movement
                    double newPath = current.getGValue() + evaluateDistance(current, neighbour);
                    if (!open.contains(neighbour) ||
                            neighbour.getGValue() > newPath)
                    {
                        neighbour.setHValue(evaluateDistance(neighbour, target));
                        neighbour.setGValue(newPath);
                        neighbour.setParent(current);
                        if (!open.contains(neighbour))
                        {
                            open.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    void traverseDijkstra()
    {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        open.add(start);
        evaluateNodeValues(start);
        Node current;
        while (true)
        {
            current = open.getFirst();
            for (Node node : open) {
                if (node.getGValue() < current.getGValue()) {
                    current = node;
                }
            }
            open.remove(current);
            closed.add(current);

            if (current == target)
            {
                break;
            }
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <=1; ++x)
                {
                    if (current.getX() + x >= size_x || current.getY() + y >= size_y ||
                            current.getX() + x < 0 || current.getY() + y < 0 ||
                            grid[current.getY() + y][current.getX() + x].isObstacle() ||
                            closed.contains(grid[current.getY() + y][current.getX() + x]))
                    {
                        continue;
                    }
                    Node neighbour = grid[current.getY() + y][current.getX() + x];
                    // Needed cause there's diagonal movement
                    double newPath = current.getGValue() + evaluateDistance(current, neighbour);
                    if (!open.contains(neighbour) ||
                            neighbour.getGValue() > newPath)
                    {
                        neighbour.setGValue(newPath);
                        neighbour.setParent(current);
                        if (!open.contains(neighbour))
                        {
                            open.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    void traverseGreedy()
    {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        open.add(start);
        evaluateNodeValues(start);
        Node current;
        while (true)
        {
            current = open.getFirst();
            for (Node node : open) {
                if (node.getHValue() < current.getHValue()) {
                    current = node;
                }
            }
            open.remove(current);
            closed.add(current);

            if (current == target)
            {
                break;
            }
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <=1; ++x)
                {
                    if (current.getX() + x >= size_x || current.getY() + y >= size_y ||
                            current.getX() + x < 0 || current.getY() + y < 0 ||
                            grid[current.getY() + y][current.getX() + x].isObstacle() ||
                            closed.contains(grid[current.getY() + y][current.getX() + x]))
                    {
                        continue;
                    }
                    Node neighbour = grid[current.getY() + y][current.getX() + x];
                    // Needed cause there's diagonal movement
                    double newPath = current.getGValue() + evaluateDistance(current, neighbour);
                    if (!open.contains(neighbour) ||
                            neighbour.getGValue() > newPath)
                    {
                        neighbour.setHValue(evaluateDistance(neighbour, target));
                        neighbour.setParent(current);
                        if (!open.contains(neighbour))
                        {
                            open.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    void traverseBFS()
    {
        ArrayBlockingQueue<Node> queue = new ArrayBlockingQueue<>(size_x * size_y);
        ArrayList<Node> visited = new ArrayList<>();
        Node current;
        queue.add(start);
        while (true)
        {
            current = queue.poll();
            visited.add(current);
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <= 1; ++x)
                {
                    assert current != null;
                    if (current.getX() + x >= size_x || current.getY() + y >= size_y ||
                            current.getX() + x < 0 || current.getY() + y < 0 ||
                            grid[current.getY() + y][current.getX() + x].isObstacle())
                    {
                        continue;
                    }
                    Node neighbour = grid[current.getY() + y][current.getX() + x];
                    if (!visited.contains(neighbour) && !queue.contains(neighbour))
                    {
                        neighbour.setParent(current);
                        if (neighbour == target)
                        {
                            return;
                        }
                        queue.add(neighbour);
                    }
                }
            }
        }
    }

    void traverseDFS()
    {
        Stack<Node> stack = new Stack<>();
        ArrayList<Node> visited = new ArrayList<>();
        Node current;
        stack.add(start);
        while (true)
        {
            current = stack.pop();
            visited.add(current);
            for (int y = -1; y <= 1; ++y)
            {
                for (int x = -1; x <= 1; ++x)
                {
                    if (current.getX() + x >= size_x || current.getY() + y >= size_y ||
                            current.getX() + x < 0 || current.getY() + y < 0 ||
                            grid[current.getY() + y][current.getX() + x].isObstacle())
                    {
                        continue;
                    }
                    Node neighbour = grid[current.getY() + y][current.getX() + x];
                    if (!visited.contains(neighbour) && !stack.contains(neighbour))
                    {
                        neighbour.setParent(current);
                        if (neighbour == target)
                        {
                            return;
                        }
                        stack.add(neighbour);
                    }
                }
            }
        }
    }
}
