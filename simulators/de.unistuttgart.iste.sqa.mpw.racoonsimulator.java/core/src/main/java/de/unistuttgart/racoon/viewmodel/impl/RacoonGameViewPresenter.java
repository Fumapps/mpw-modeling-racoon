package de.unistuttgart.racoon.viewmodel.impl;

import de.unistuttgart.iste.sqa.mpw.framework.mpw.LogEntry;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Size;
import de.unistuttgart.iste.sqa.mpw.framework.mpw.Tile;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCellLayer;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelLogEntry;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.impl.GameViewPresenterBase;
import de.unistuttgart.racoon.racoon.*;
import de.unistuttgart.racoon.facade.*;
import javafx.beans.property.ReadOnlyListProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RacoonGameViewPresenter extends GameViewPresenterBase {
	private final Territory territory;

	private final Map<LogEntry, ViewModelLogEntry> logEntryMap = new HashMap<>();

	public RacoonGameViewPresenter(RacoonGame game) {
		super(game);
		this.territory = game.getTerritory();
	}

	@Override
	protected Size getStageSizeFromConcreteStage() {
		return territory.getTerritorySize();
	}

	@Override
	protected ReadOnlyListProperty<Tile> getTilesPropertyFromConcreteStage() {
		return territory.getInternalTerritory().tilesProperty();
	}

	@Override
	protected void onSetTileNodeAtForCell(ViewModelCell cell, Tile tile) {
		// TODO: presenter logic
	}

}
