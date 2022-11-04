package Game.Tiles.Units;

import Game.Utils.Position;

import javax.swing.*;

public class Empty extends Tile{

    public Position position;
    public static char tile='.';

    public Empty(Position pos){
        super('.');
        this.position = pos;
    }

    // return the char that represents the Empty tile
    public String toString() {
        return tile + "";
    }

    // return the position of this tile
    public Position getPosition(){
        return position;
    }

    // set the position of this tile
    public void setPosition(Position position){
        this.position = position;
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }
}
