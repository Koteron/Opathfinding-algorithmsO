public class Node
{
    private Node parent = null;
    private double gValue = Double.MAX_VALUE;
    private double hValue = Double.MAX_VALUE;
    private int x = -1;
    private int y = -1;
    private boolean isObstacle = false;

    Node() {}
    Node(boolean obstacle)
    {
        isObstacle = obstacle;
    }

    double getFValue()
    {
        return gValue + hValue;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean is_obstacle) {
        this.isObstacle = is_obstacle;
    }

    public double getHValue() {
        return hValue;
    }

    public void setHValue(double f_value) {
        hValue = f_value;
    }

    public double getGValue() {
        return gValue;
    }

    public void setGValue(double g_value) {
        gValue = g_value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
