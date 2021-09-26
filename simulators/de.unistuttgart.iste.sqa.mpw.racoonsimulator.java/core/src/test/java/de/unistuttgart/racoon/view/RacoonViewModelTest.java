package de.unistuttgart.racoon.view;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Direction;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests on the view-model layer, which are using the {@link RacoonViewModelStringifier} to create
 * string based representations.
 */
public class RacoonViewModelTest extends RacoonViewTestBase {

    @Test
    public void testInit_Example01() throws IOException {
        withTerritory("example01.ter");
        assertTerritory(
                "|┏━|━━|━━|━━|┓ |\n" +
                "|┃ |> |  |* |┃ |\n" +
                "|┗━|━━|━━|━━|┛ |\n");
        // assertEquals(false, racoon.nutAvailable());
        // assertEquals(true, racoon.frontIsClear());
        assertEquals(Direction.EAST, racoon.getDirection());
        assertEquals(1, racoon.getLocation().getRow());
        assertEquals(1, racoon.getLocation().getColumn());
    }

    @Test
    public void testInit_Example02() throws IOException {
        withTerritory("example02.ter");
        assertTerritory(
                "|┏━|━━|━━|━━|━━|━━|┓ |\n" +
                "|┃ |  |  |v |  |  |┃ |\n" +
                "|┃ |  |╺━|┳━|╸ |  |┃ |\n" +
                "|┃ |  |* |┃ |* |  |┃ |\n" +
                "|┗━|━━|━━|┻━|━━|━━|┛ |\n");
        // assertEquals(false, racoon.nutAvailable());
        // assertEquals(true, racoon.frontIsClear());
        assertEquals(Direction.SOUTH, racoon.getDirection());
        assertEquals(1, racoon.getLocation().getRow());
        assertEquals(3, racoon.getLocation().getColumn());
    }

}