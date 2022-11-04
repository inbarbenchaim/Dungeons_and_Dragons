package Game.Utils;

public class Position {

    public int x = 0;
    public int y = 0;

    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // return the x value
    public int getX() {
        return x;
    }

    // return the y value
    public int getY() {
        return y;
    }

    // return the x and y values
    public String toString() {
        return "x:" + x + ", y:" + y;
    }

    // make a copy of this position
    public Position copy(){
        return new Position(this.x, this.y);
    }

    // compare between 2 positions
    public int compareTo(Position position) {
        if (getX() > position.getX())
            return 1;
        if (getX() < position.getX())
            return -1;
        if (getY() > position.getY())
            return 1;
        if (getY() < position.getY())
            return -1;
        return 0;
    }

    // return the range between two objects
    public boolean isInRange(Position c2, int range) {
        return range >= this.distance(c2);
    }

    // return the distance between 2 positions
    public double distance(Position c2) {
        return Math.sqrt(Math.pow((this.x - c2.x), 2) + Math.pow((this.y - c2.y), 2));
    }
}
