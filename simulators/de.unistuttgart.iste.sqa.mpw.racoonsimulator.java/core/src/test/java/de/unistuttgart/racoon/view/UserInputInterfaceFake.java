package de.unistuttgart.racoon.view;

import de.unistuttgart.iste.sqa.mpw.framework.mpw.UserInputInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static de.unistuttgart.iste.sqa.mpw.framework.FrameworkAssert.assertCondition;

public class UserInputInterfaceFake implements UserInputInterface {
    private Queue<String> stubbedUserInputQueue = new LinkedList<>();
    private ArrayList<Throwable> receivedAlerts = new ArrayList<>();

    @Override
    public int readInteger(String message) {
        assertCondition(stubbedUserInputQueue.size() > 0, "please put an integer into user-input-queue");
        String str = stubbedUserInputQueue.remove();
        return Integer.parseInt(str);
    }

    @Override
    public String readString(String message) {
        assertCondition(stubbedUserInputQueue.size() > 0, "please put a string into user-input-queue");
        return stubbedUserInputQueue.remove();
    }

    @Override
    public void confirmAlert(Throwable t) {
        receivedAlerts.add(t);
    }

    @Override
    public void abort() {
    }

    public void stubUserInput(String input) {
        stubbedUserInputQueue.add(input);
    }

    public ArrayList<Throwable> getReceivedAlerts() {
        return receivedAlerts;
    }
}
