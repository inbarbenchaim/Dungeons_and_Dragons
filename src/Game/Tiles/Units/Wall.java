package Game.Tiles.Units;

import Game.Utils.Position;

public class Wall extends Tile{
    public Position position;
    public char tile = '#';

    public Wall(Position pos) {
        super('#');
        this.position = pos;
        super.setPosition(position);
    }

    // return the char that represent a wall
    public String toString() {
        return this.tile + "";
    }

    @Override
    public void accept(Unit unit) {
        // does nothing
    }
}
