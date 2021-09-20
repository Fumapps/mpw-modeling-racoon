package de.unistuttgart.racoon.ui;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Location;
import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Size;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewModel;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelRow;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class RacoonTerritoryGrid extends StackPane {

    private static final double MINIMUM_TILE_SIZE = 20.0;

    private final SimpleObjectProperty<Size> gridSize = new SimpleObjectProperty<>(this, "gridSize", new Size(0, 0));
    private final ReadOnlyListWrapper<TileNode> nodes = new ReadOnlyListWrapper<>(this, "cells", FXCollections.observableArrayList());
    private final GridPane territoryGrid;
    private GameViewModel viewModel;
    private NumberBinding squaredSize;

    private final ListChangeListener<ViewModelRow> rowsChangedListener = new ListChangeListener<>() {
        @Override
        public void onChanged(final Change<? extends ViewModelRow> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(row -> addRow(row));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(row -> removeRow(row));
                }
            }
        }
    };

    private final ListChangeListener<ViewModelCell> cellsChangedListener = new ListChangeListener<>() {
        @Override
        public void onChanged(final Change<? extends ViewModelCell> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(cell -> addCell(cell));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(cell -> removeCell(cell));
                }
            }
        }
    };


    public RacoonTerritoryGrid() {
        super();
        this.getStyleClass().add("game-grid");
        this.setAlignment(Pos.CENTER);
        this.territoryGrid = new GridPane();
        this.territoryGrid.getStyleClass().add("game-grid");
        this.getChildren().add(this.territoryGrid);
        configureSquareSizedTiles(this.gridSize.get());
        this.gridSize.addListener((obj, oldValue, newValue) -> {
            configureSquareSizedTiles(newValue);
            this.viewModel.rowsProperty().forEach(row -> addRow(row));
        });
    }

    public void bindToViewModel(final GameViewModel viewModel) {
        this.viewModel = viewModel;
        this.gridSize.bind(this.viewModel.sizeProperty());
        this.viewModel.rowsProperty().addListener(rowsChangedListener);
        this.viewModel.rowsProperty().forEach(this::addRow);
    }

    public void unbind() {
        this.gridSize.unbind();
        this.viewModel.rowsProperty().removeListener(rowsChangedListener);
        unbindCells();
    }

    private void unbindCells() {
        for (var row : this.viewModel.getRows()) {
            row.cellsProperty().removeListener(cellsChangedListener);
        }
    }

    private void configureSquareSizedTiles(final Size size) {
        final int columns = size.getColumnCount();
        final int rows = size.getRowCount();
        final NumberBinding pixPerCellWidth = this.widthProperty().divide(columns == 0 ? 1 : columns);
        final NumberBinding pixPerCellHeight = this.heightProperty().divide(rows == 0 ? 1 : rows);
        this.squaredSize = Bindings.min(pixPerCellHeight, pixPerCellWidth);

        this.territoryGrid.getColumnConstraints().clear();
        for (int i = 0; i < columns; i++) {
            final ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            this.territoryGrid.getColumnConstraints().add(column);
        }

        this.territoryGrid.getRowConstraints().clear();
        for (int i = 0; i < rows; i++) {
            final RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            this.territoryGrid.getRowConstraints().add(row);
        }

        this.territoryGrid.maxWidthProperty().bind(squaredSize.multiply(columns));
        this.territoryGrid.maxHeightProperty().bind(squaredSize.multiply(rows));

        this.territoryGrid.setAlignment(Pos.CENTER);
    }

    private void addRow(final ViewModelRow row) {
        row.cellsProperty().addListener(cellsChangedListener);
        row.cellsProperty().forEach(this::addCell);
    }

    private void removeRow(final ViewModelRow row) {
        row.cellsProperty().removeListener(cellsChangedListener);
    }

    private void addCell(final ViewModelCell cell) {
        final Location location = cell.getLocation();
        setTileNodeAt(location, new TileNode(cell));
    }

    private void removeCell(final ViewModelCell cell) {
        final Location location = cell.getLocation();
        getTileNodeAt(location).dispose();
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.territoryGrid.getChildren().remove(getTileNodeAt(location)));
        setTileNodeAt(location, null);
    }

    private TileNode getTileNodeAt(final Location location) {
        return this.nodes.get(location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn());
    }

    private void setTileNodeAt(final Location location, final TileNode node) {
        final int index = location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn();
        if (index < this.nodes.getSize()) {
            this.nodes.set(index, node);
        } else {
            this.nodes.add(index, node);
        }
        if (node != null) {
            node.prefWidthProperty().bind(this.squaredSize);
            node.prefHeightProperty().bind(this.squaredSize);
            node.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            node.setMinSize(MINIMUM_TILE_SIZE, MINIMUM_TILE_SIZE);
            Platform.runLater(() -> this.territoryGrid.add(node, location.getColumn(), location.getRow()));
        }
    }

}
