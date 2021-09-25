package de.unistuttgart.racoon.view;

import de.unistuttgart.racoon.facade.TerritoryLoader;
import de.unistuttgart.racoon.facade.Racoon;
import de.unistuttgart.racoon.facade.RacoonGame;
import de.unistuttgart.racoon.viewmodel.impl.RacoonGameViewPresenter;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewInput;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewModel;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelRow;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RacoonViewTestBase {

    private int maxCharsPerCell = 4;
    private static Map<String, String> characterMap = new HashMap<>();
    private RacoonGame game;
    protected Racoon racoon;
    protected GameViewInput viewInput;
    protected GameViewModel viewModel;

    @BeforeAll
    public static void setup() {
        initCharMapping();
    }

    protected void withTerritory(final String fileName) throws IOException {
        final String path = getResourcePath(fileName);
        game = new RacoonGame();
        TerritoryLoader.initializeFor(game).loadFromResourceFile(path);

        game.startGame();
        game.getPerformance().setDelayEnabled(false);

        var territory = game.getTerritory();
        racoon = territory.getRacoon();

        var presenter = new RacoonGameViewPresenter(game);
        presenter.bind();
        viewInput = presenter;
        viewModel = presenter.getViewModel();

        game.setUserInputInterface(new UserInputInterfaceFake());
    }

    protected void initializeOtherTerritory(final String fileName) throws IOException {
        game.hardReset();
        TerritoryLoader.initializeFor(game).loadFromResourceFile(getResourcePath(fileName));
    }

    private String getResourcePath(String fileName) {
        return "de.unistuttgart.racoon.territories/" + fileName;
    }

    protected void clickPlay() {
        viewInput.playClicked();
    }

    protected void clickPause() {
        viewInput.pauseClicked();
    }

    protected void clickUndo() {
        viewInput.undoClicked();
    }

    protected void clickRedo() {
        viewInput.redoClicked();
    }

    protected void assertTerritory(String expected) {
        var actual = new RacoonViewModelStringifier(characterMap, maxCharsPerCell)
                .territoryToExpectationString(viewModel);
        assertEquals(expected, actual);
        assertLocationsAreSet();
        assertSizeIsConsistent();
    }

    protected void assertButtons(String expected) {
        var actual = RacoonViewModelStringifier.buttonBarToExpectationString(viewModel);
        assertEquals(expected, actual);
        assertLocationsAreSet();
        assertSizeIsConsistent();
    }

    private void assertLocationsAreSet() {
        List<ViewModelRow> rows = viewModel.getRows();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            ViewModelRow row = rows.get(rowIndex);
            List<ViewModelCell> cells = row.getCells();
            for (int columnIndex = 0; columnIndex < cells.size(); columnIndex++) {
                ViewModelCell cell = cells.get(columnIndex);
                assertEquals(columnIndex, cell.getLocation().getColumn());
                assertEquals(rowIndex, cell.getLocation().getRow());
            }

        }

    }

    private void assertSizeIsConsistent() {
        List<ViewModelRow> rows = viewModel.getRows();
        assertEquals(rows.size(), viewModel.getSize().getRowCount());
        for (ViewModelRow row : rows) {
            assertEquals(row.getCells().size(), viewModel.getSize().getColumnCount());
        }
    }

    protected void assertLog(String expected) {
        String actual = RacoonViewModelStringifier.logToString(viewModel);
        assertEquals(expected, actual);
    }

    private static void initCharMapping() {
        characterMap.put("1Corn", " 1*");
        characterMap.put("2Corn", " 2*");
        characterMap.put("3Corn", " 3*");
        characterMap.put("4Corn", " 4*");
        characterMap.put("5Corn", " 5*");
        characterMap.put("6Corn", " 6*");
        characterMap.put("7Corn", " 7*");
        characterMap.put("8Corn", " 8*");
        characterMap.put("9Corn", " 9*");
        characterMap.put("10Corn", "10*");
        characterMap.put("11Corn", "11*");
        characterMap.put("12Corn", "12*");
        characterMap.put("12PlusCorn", "12+");
        characterMap.put("Racoon", ">");
        characterMap.put("Racoon[90]", "v");
        characterMap.put("Racoon[180]", "<");
        characterMap.put("Racoon[270]", "^");
        characterMap.put("Wall", "####");
    }

}
