package Game.Tiles.Units;

import Game.Utils.Position;

public abstract class  Tile implements Comparable<Tile> {

    protected char tile;
    protected Position position;

    // this char that represents this Tile
    protected Tile(char tile){
        this.tile = tile;
    }

    // initialize the position of this Tile
    protected void initialize(Position position){
        this.position = position;
    }

    // return the char that represents this Tile
    public char getTile(){
        return tile;
    }

    /*
    // change the char that represents this Tile
    public void setTile(char tile) {
        this.tile = tile;
    }
     */

    // return the position of the tile
    public Position getPosition(){
        return position;
    }

    // change the position of the tile
    public void setPosition(Position position){
        this.position = position;
    }

    // return the char of this tile
    public String toString(){
        return String.valueOf(tile);
    }

    //compares tile based on their positions lexicographic
    public int compareTo(Tile Other){
        return getPosition().compareTo(Other.getPosition());
    }

    public abstract void accept(Unit unit);

    // swap the position between this Unit and an enemy
    protected void swapPosition(Tile t) {
        Position p = t.getPosition();
        t.setPosition(this.getPosition());
        this.setPosition(p);
    }
}
