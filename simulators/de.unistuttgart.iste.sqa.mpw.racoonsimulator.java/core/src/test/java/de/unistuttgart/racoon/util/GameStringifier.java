package de.unistuttgart.racoon.util;

import de.unistuttgart.racoon.racoon.*;
import de.unistuttgart.racoon.facade.*;
import de.unistuttgart.iste.sqa.mpw.framework.mpw.Tile;

import java.util.function.Consumer;

/**
 * Helper class to serialize / deserialize RacoonGame to / from Strings
 */
public class GameStringifier {

	/**
	 * Creates a RacoonGame from the given String and starts the game.
	 * @param map Encoded game, which uses the following characters
	 *            '#' for a Wall
	 *            '*' for one nut
	 *            '<' '^' '>' 'v' for the default Racoon in the related direction
	 *            ';' for the end of a row
	 */
	public static RacoonGame createFromStringStarted(String map) {
		var game = createFromString(map);
		game.startGame();
		return game;
	}

	/**
	 * Creates a RacoonGame from the given String. The game will not be started.
	 * @param map Encoded game, which uses the following characters
	 *            '#' for a Wall
	 *            '*' for one grain (note: multiple grains have to be placed with TerritoryBuilder separately
	 *            '<' '^' '>' 'v' for the default Racoon in the related direction
	 *            ';' for the end of a row
	 */
	public static RacoonGame createFromString(String map) {
		var game = new RacoonGame();

		String[] parts = map.split(";");
		int height = parts.length;
		int width = height > 0 ? parts[0].length() : 0;

		var territoryBuilder = new TerritoryBuilder(game);
		territoryBuilder.initTerritory(width, height);

		for (int y = 0; y < height; y++) {
			handleLine(territoryBuilder, y, parts[y]);
		}

		game.getPerformance().setDelayEnabled(false);

		return game;
	}

	private static void handleLine(TerritoryBuilder territoryBuilder, int y, String part) {
		char[] chars = part.toCharArray();
		for (int x = 0; x < part.length(); x++) {
			handleCell(territoryBuilder, x, y, chars[x]);
		}
	}

	private static void handleCell(TerritoryBuilder territoryBuilder, int x, int y, char cell) {
		switch (cell) {
			case '>', '<', '^', 'v' -> territoryBuilder.initRacoon(x, y, DirectionTestHelper.toDirection(cell));
			case '*' -> territoryBuilder.addNutToTile(x, y);
			case '#' -> territoryBuilder.addWallToTile(x, y);
		}
	}

	/**
	 * Converts the given game to a simple String.
	 *            '#' for a Wall
	 *            '*' for one grain (note: multiple grains have to be placed with TerritoryBuilder separately
	 *            '<' '^' '>' 'v' for the default Racoon in the related direction
	 *            ';' for the end of a row
	 * Note: the amount of grains on a field is ignored. If it is relevant, use {@link #grainsOnTerritoryToString(RacoonGame)}.
	 * @return the encoded String for the game.
	 */
	public static String toString(RacoonGame game) {
		var actual = new StringBuilder();

		var racoon = game.getTerritory().getRacoon().getInternalRacoon();
		iterateTiles(game, currentTile -> {
			if (racoon.getCurrentTile() == currentTile) {
				actual.append(DirectionTestHelper.toDirection(racoon.getDirection()));
			} else if (currentTile.getContents().stream().anyMatch(Wall.class::isInstance)) {
				actual.append('#');
			} else if (currentTile.getContents().stream().anyMatch(Nut.class::isInstance)) {
				actual.append('*');
			} else {
				actual.append(' ');
			}

			if (currentTile.getEast() == null) {
				actual.append(';');
			}
		});

		return actual.toString();
	}

	/**
	 * Creates a String which states the number of grain on each tile.
	 * ';' is used to mark the end of a row.
	 * @return the encoded String which contains the number for each tile.
	 */
	public static String grainsOnTerritoryToString(RacoonGame game) {
		var actual = new StringBuilder();

		iterateTiles(game, currentTile -> {
			var grainsCount = currentTile.getContents().stream().filter(Nut.class::isInstance).count();
			actual.append(grainsCount);

			if (currentTile.getEast() == null) {
				actual.append(';');
			}
		});

		return actual.toString();
	}

	private static void iterateTiles(RacoonGame game, Consumer<Tile> lambda) {
		var tiles = game.getTerritory().getInternalTerritory().getTiles();
		final var upperLeftTile = tiles.get(0);
		var firstOfRowTile = upperLeftTile;

		var currentTile = upperLeftTile;

		while (currentTile != null) {
			lambda.accept(currentTile);

			if (currentTile.getEast() == null) {
				firstOfRowTile = firstOfRowTile.getSouth();
				currentTile = firstOfRowTile;
			} else {
				currentTile = currentTile.getEast();
			}
		}
	}

}
