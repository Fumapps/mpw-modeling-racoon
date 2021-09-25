package de.unistuttgart.racoon.view;

import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewModel;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelLogEntry;

import java.util.Map;

/**
 * Helper class to represent the view-model in a string representation.
 *
 * With {@link #maxCharsPerCell} the width of a cell in characters is specified.
 * Also, {@link #imageCharsMapping} defines a mapping from a symbolic image name to
 * a character sequence, e.g. "Nut" -> "*". Cells are further separated by '|'.
 */
public class RacoonViewModelStringifier {
    private final Map<String, String> imageCharsMapping;
    private final int maxCharsPerCell;

    public RacoonViewModelStringifier(Map<String, String> imageCharsMapping, int maxCharsPerCell) {
        this.imageCharsMapping = imageCharsMapping;
        this.maxCharsPerCell = maxCharsPerCell;
    }

    public String territoryToExpectationString(final GameViewModel viewModel) {
        final StringBuilder actual = new StringBuilder();
        final var height = viewModel.getSize().getRowCount();
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            var s = rowToString(viewModel, rowIndex);
            actual.append(s);
            actual.append("\n");
        }
        return actual.toString();
    }

    private String rowToString(final GameViewModel viewModel, final int y) {
        final StringBuilder s = new StringBuilder("|");
        final var width = viewModel.getSize().getColumnCount();
        for (int columnIndex = 0; columnIndex < width; columnIndex++) {
            var cell = viewModel.getCellAt(y, columnIndex);
            s.append(cellToString(cell));
            s.append("|");
        }
        return s.toString();
    }

    private String cellToString(final ViewModelCell cell) {
        StringBuilder cellStringBuilder = new StringBuilder();
        for (var layer : cell.getLayers()) {
            if (layer.getVisible()) {
                var key = layer.getImageName();
                var rotation = layer.getRotation();
                key = addRotation(key, rotation);
                cellStringBuilder.append(imageCharsMapping.get(key));
            }
        }
        final var length = cellStringBuilder.length();
        final var spaces = maxCharsPerCell - length;
        cellStringBuilder.append(" ".repeat(Math.max(0, spaces)));
        return cellStringBuilder.toString();
    }

    private String addRotation(String key, int rotation) {
        if (rotation != 0) {
            key += "[" + rotation + "]";
        }
        return key;
    }

    public static String logToString(final GameViewModel viewModel) {
        StringBuilder actual = new StringBuilder();
        for (ViewModelLogEntry entry : viewModel.getLogEntries()) {
            actual.append(entry.getMessage());
            actual.append("\n");
        }
        return actual.toString();
    }

    public static String buttonBarToExpectationString(final GameViewModel viewModel) {
        final StringBuilder actual = new StringBuilder();
        actual.append(buttonToString("play", viewModel.getPlayButtonEnabled()));
        actual.append(" ");
        actual.append(buttonToString("pause", viewModel.getPauseButtonEnabled()));
        actual.append(" ");
        actual.append(buttonToString("undo", viewModel.getUndoButtonEnabled()));
        actual.append(" ");
        actual.append(buttonToString("redo", viewModel.getRedoButtonEnabled()));
        return actual.toString();
    }

    private static String buttonToString(final String name, final boolean enabled) {
        if (enabled) {
            return "[" + name + "]";
        }
        return "/" + name + "/";
    }

}
