package de.unistuttgart.racoon.viewmodel.impl;

import de.unistuttgart.iste.sqa.mpw.framework.mpw.Tile;
import de.unistuttgart.racoon.racoon.Wall;

/**
 * Determines the specific wall image, which makes up the right integration to all surrounding tiles.
 */
public class WallImageDeterminer {

    private WallImageDeterminer() {}

    public static String determineWallImageName(final Tile tile) {
        boolean eastIsWall = isWall(tile.getEast());
        boolean westIsWall = isWall(tile.getWest());
        boolean northIsWall = isWall(tile.getNorth());
        boolean southIsWall = isWall(tile.getSouth());

        // Note: the following is like a binary table

        if (eastIsWall == false && westIsWall == false && northIsWall == false && southIsWall == false) {
            return "WallMiddle";
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == false && southIsWall == true) {
            return "WallTop";
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == true && southIsWall == false) {
            return "WallTop[180]";
        }
        if (eastIsWall == false && westIsWall == false && northIsWall == true && southIsWall == true) {
            return "WallVertical";
        }
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == false && westIsWall == true && northIsWall == false && southIsWall == false) {
            return "WallTop[90]";
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == false && southIsWall == true) {
            return "WallTopLeftCorner[90]";
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == true && southIsWall == false) {
            return "WallTopLeftCorner[180]";
        }
        if (eastIsWall == false && westIsWall == true && northIsWall == true && southIsWall == true) {
            return "WallT[90]";
        }
        /* ----------------------------------------------------------------------------------------- */
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == true && westIsWall == false && northIsWall == false && southIsWall == false) {
            return "WallTop[270]";
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == false && southIsWall == true) {
            return "WallTopLeftCorner";
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == true && southIsWall == false) {
            return "WallTopLeftCorner[270]";
        }
        if (eastIsWall == true && westIsWall == false && northIsWall == true && southIsWall == true) {
            return "WallT[270]";
        }
        /* ----------------------------------------------------------------------------------------- */
        if (eastIsWall == true && westIsWall == true && northIsWall == false && southIsWall == false) {
            return "WallVertical[90]";
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == false && southIsWall == true) {
            return "WallT";
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == true && southIsWall == false) {
            return "WallT[180]";
        }
        if (eastIsWall == true && westIsWall == true && northIsWall == true && southIsWall == true) {
            return "WallAll";
        }

        return "WallMiddle";
    }

    private static boolean isWall(final Tile tile) {
        return tile != null &&
                tile.getContents().stream().anyMatch(Wall.class::isInstance);
    }
}
