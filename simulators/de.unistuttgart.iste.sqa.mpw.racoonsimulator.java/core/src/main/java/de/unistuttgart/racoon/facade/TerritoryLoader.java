package de.unistuttgart.racoon.facade;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Direction;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Location;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Size;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class TerritoryLoader {
    private final TerritoryBuilder territoryBuilder;
    private Size loadedTerritoryDimensions;

    private TerritoryLoader(final TerritoryBuilder territoryBuilder) {
        super();
        this.territoryBuilder = territoryBuilder;
    }

    public static TerritoryLoader initializeFor(final RacoonGame game) {
        var builder = new TerritoryBuilder(game);
        return new TerritoryLoader(builder);
    }

    public void loadFromResourceFile(final String territoryFile) throws IOException {
        final List<String> list = readLinesFromTerritoryResourceFile(territoryFile);
        interpretLoadedTerritoryLines(list);
    }

    public void loadFromInputStream(final InputStream inputStream) throws IOException {
        final List<String> list = readLinesFromTerritoryInputStream(inputStream);
        interpretLoadedTerritoryLines(list);
    }

    private void interpretLoadedTerritoryLines(final List<String> list) {
        final String[] lines = list.toArray(new String[]{});
        setSizeFromStrings(lines);
        final String[] territoryDefinition = Arrays.copyOfRange(lines,2,lines.length);
        buildTiles(territoryDefinition);
    }

    private void setSizeFromStrings(final String[] lines) {
        this.loadedTerritoryDimensions = new Size(Integer.parseInt(lines[0]), Integer.parseInt(lines[1]));
        this.territoryBuilder.initTerritory(this.loadedTerritoryDimensions.getColumnCount(), this.loadedTerritoryDimensions.getRowCount()); // todo allow size as parameter
    }

    private void buildTiles(final String[] lines) {
        for (int row = 0; row < this.loadedTerritoryDimensions.getRowCount(); row++) {
            for (int column = 0; column < this.loadedTerritoryDimensions.getColumnCount(); column++) {
                final Location currentLocation = new Location(column, row);
                final char tileCode = lines[row].charAt(column);
                switch (tileCode) {
                    case ' ':
                        break;
                    case '#':
                        createWallAt(currentLocation);
                        break;
                    case '*':
                        createNutAt(currentLocation);
                        break;
                    case '^':
                        createRacoonAt(currentLocation, Direction.NORTH);
                        break;
                    case '>':
                        createRacoonAt(currentLocation, Direction.EAST);
                        break;
                    case 'v':
                        createRacoonAt(currentLocation, Direction.SOUTH);
                        break;
                    case '<':
                        createRacoonAt(currentLocation, Direction.WEST);
                        break;
                    default:
                        throw new RuntimeException("Territory Loader error.");
                }
            }
        }
    }

    private List<String> readLinesFromTerritoryResourceFile(final String territoryFileName) throws IOException {
        final InputStream in = getClass().getClassLoader().getResourceAsStream(territoryFileName);
        if (in == null) {
            throw new IOException("Unable to load the territory from the filename: " + territoryFileName);
        }
        final List<String> result = readLinesFromTerritoryInputStream(in);
        in.close();
        return result;
    }

    private List<String> readLinesFromTerritoryInputStream(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final List<String> list = new ArrayList<String>();

        try (Scanner input = new Scanner(reader))
        {
            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }
        }

        return list;
    }

    private void checkNotNull(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    private void createWallAt(final Location currentLocation) {
        this.territoryBuilder.addWallToTile(currentLocation);
    }

    private void createNutAt(final Location currentLocation) {
        this.territoryBuilder.addNutToTile(currentLocation);
    }

    private void createRacoonAt(final Location currentLocation, final Direction direction) {
        this.territoryBuilder.initRacoon(currentLocation, direction);
    }

}
