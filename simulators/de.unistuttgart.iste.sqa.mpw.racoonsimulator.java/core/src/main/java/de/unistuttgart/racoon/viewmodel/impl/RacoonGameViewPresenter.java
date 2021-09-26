package de.unistuttgart.racoon.viewmodel.impl;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Direction;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Size;
import de.unistuttgart.iste.sqa.mpw.framework.mpw.Tile;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCellLayer;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.impl.GameViewPresenterBase;
import de.unistuttgart.racoon.facade.RacoonGame;
import de.unistuttgart.racoon.facade.Territory;
import de.unistuttgart.racoon.racoon.Nut;
import de.unistuttgart.racoon.racoon.ReadOnlyRacoon;
import de.unistuttgart.racoon.racoon.Wall;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RacoonGameViewPresenter extends GameViewPresenterBase {
	private final Territory territory;
	private ChangeListener<Direction> racoonDirectionChangeListener;

	private final Map<Tile, ViewModelCellLayer> wallTileToCellLayerMap = new HashMap<>();

	public RacoonGameViewPresenter(RacoonGame game) {
		super(game);
		this.territory = game.getTerritory();
	}

	@Override
	protected ReadOnlyObjectProperty<Size> getStageSizeFromConcreteStage() {
		return territory.getInternalTerritory().stageSizeProperty();
	}

	@Override
	protected ReadOnlyListProperty<Tile> getTilesPropertyFromConcreteStage() {
		return territory.getInternalTerritory().tilesProperty();
	}

	@Override
	protected void onSetTileNodeAtForCell(ViewModelCell cell, Tile tile) {
		configureWallImageView(cell, tile);
		configureNutImageView(cell, tile);

		Optional<ReadOnlyRacoon> racoonOptional = findRacoonOnTile(tile);
		racoonOptional.ifPresent(readOnlyRacoon -> configureRacoonImageView(cell, readOnlyRacoon));
	}

	private Optional<ReadOnlyRacoon> findRacoonOnTile(Tile tile) {
		return tile.getContents().stream()
				.filter(ReadOnlyRacoon.class::isInstance)
				.map(ReadOnlyRacoon.class::cast).findFirst();
	}

	private void configureWallImageView(final ViewModelCell cell, final Tile tile) {
		final var layer = new ViewModelCellLayer();
		layer.setImageName("WallMiddle");
		refreshWallLayer(layer, tile);
		cell.getLayers().add(layer);

		wallTileToCellLayerMap.put(tile, layer);
		refreshNeighbourWall(tile.getEast());
		refreshNeighbourWall(tile.getWest());
		refreshNeighbourWall(tile.getNorth());
		refreshNeighbourWall(tile.getSouth());
	}

	private void refreshNeighbourWall(final Tile neighbourTile) {
		if (neighbourTile != null && wallTileToCellLayerMap.containsKey(neighbourTile)) {
			final var neighbourWallLayer = wallTileToCellLayerMap.get(neighbourTile);
			refreshWallLayer(neighbourWallLayer, neighbourTile);
		}
	}

	private void refreshWallLayer(final ViewModelCellLayer layer, final Tile tile) {
		final Pair<String, Integer> wallImageNameWithRotation = WallImageDeterminer.determineWallImageName(tile);
		layer.setImageName(wallImageNameWithRotation.getKey());
		layer.setRotation(wallImageNameWithRotation.getValue());
		layer.setVisible(tile.getContents().stream().anyMatch(Wall.class::isInstance));
	}

	private void configureNutImageView(final ViewModelCell cell, final Tile tile) {
		final var layer = new ViewModelCellLayer();
		refreshNutLayer(layer, tile);
		cell.getLayers().add(layer);
	}

	private void refreshNutLayer(final ViewModelCellLayer layer, final Tile tile) {
		layer.setVisible(tile.getContents().stream().anyMatch(Nut.class::isInstance));
		layer.setImageName("Nut");
	}


	private void configureRacoonImageView(ViewModelCell cell, ReadOnlyRacoon racoon) {
		var layer = new ViewModelCellLayer();
		layer.setImageName("RacoonEast");

		addRacoonDirectionListener(layer, racoon);

		refreshRacoonLayer(layer, racoon);
		cell.getLayers().add(layer);
	}

	/*
	 * Adds a listener for the change of the direction, to also update the layers if the racoon turns left.
	 * Note: Since onSetTileNodeAtForCell() is called every time the contents of a tile changes, the racoon might
	 * be configured multiple times. Avoid, that multiple direction listeners are attached.
	 */
	private void addRacoonDirectionListener(final ViewModelCellLayer layer, final ReadOnlyRacoon racoon) {
		if (racoonDirectionChangeListener != null) {
			racoon.directionProperty().removeListener(racoonDirectionChangeListener);
		}
		racoonDirectionChangeListener = (property, oldValue, newValue) -> {
			runLocked(() -> {
				refreshRacoonLayer(layer, racoon);
			});
		};
		racoon.directionProperty().addListener(racoonDirectionChangeListener);
	}

	private void refreshRacoonLayer(ViewModelCellLayer layer, ReadOnlyRacoon racoon) {
		layer.setVisible(racoon.getCurrentTile() != null);
		final Direction direction = racoon.getDirection();
		switch (direction) {
			case NORTH -> {
				layer.setRotation(180);
				layer.setImageName("RacoonSouth");
			}
			case SOUTH -> {
				layer.setRotation(0);
				layer.setImageName("RacoonSouth");
			}
			case WEST -> {
				layer.setRotation(180);
				layer.setImageName("RacoonEast");
			}
			case EAST -> {
				layer.setRotation(0);
				layer.setImageName("RacoonEast");
			}
		}

	}
}
