package de.unistuttgart.racoon.viewmodel.impl;

import de.unistuttgart.iste.sqa.mpw.framework.mpw.Tile;
import de.unistuttgart.racoon.racoon.Wall;
import javafx.util.Pair;

/**
 * Determines the specific wall image, which makes up the right integration to all surrounding tiles.
 */
public class WallImageDeterminer {

    private WallImageDeterminer() {}

    public static Pair<String, Integer> determineWallImageName(final Tile tile) {
        boolean eastIsWall = isWall(tile.getEast());
        boolean westIsWall = isWall(tile.getWest());
        boolean northIsWall = isWall(tile.getNorth());
        boolean southIsWall = isWall(tile.getSouth());

        // Note: the following is like a binary table

        if (eastIsWall == false && westIsWall == false && northIsWall == false && southIsWall == false) {
            return new Pair<>("WallMiddle", 0);
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == false && southIsWall == true) {
            return new Pair<>("WallTop", 0);
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == true && southIsWall == false) {
            return new Pair<>("WallTop", 180);
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == true && southIsWall == true) {
            return new Pair<>("WallVertical", 0);
        }
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == false && westIsWall == true && northIsWall == false && southIsWall == false) {
            return new Pair<>("WallTop", 90);
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == false && southIsWall == true) {
            return new Pair<>("WallTopLeftCorner", 90);
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == true && southIsWall == false) {
            return new Pair<>("WallTopLeftCorner", 180);
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == true && southIsWall == true) {
            return new Pair<>("WallT", 90);
        }
        /* ----------------------------------------------------------------------------------------- */
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == true && westIsWall == false && northIsWall == false && southIsWall == false) {
            return new Pair<>("WallTop", 270);
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == false && southIsWall == true) {
            return new Pair<>("WallTopLeftCorner", 0);
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == true && southIsWall == false) {
            return new Pair<>("WallTopLeftCorner", 270);
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == true && southIsWall == true) {
            return new Pair<>("WallT", 270);
        }
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == true && westIsWall == true && northIsWall == false && southIsWall == false) {
            return new Pair<>("WallVertical", 90);
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == false && southIsWall == true) {
            return new Pair<>("WallT", 0);
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == true && southIsWall == false) {
            return new Pair<>("WallT", 180);
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == true && southIsWall == true) {
            return new Pair<>("WallAll", 0);
        }

        return new Pair<>("WallMiddle", 0);
    }

    private static boolean isWall(final Tile tile) {
        return tile != null &&
                tile.getContents().stream().anyMatch(Wall.class::isInstance);
    }
}
