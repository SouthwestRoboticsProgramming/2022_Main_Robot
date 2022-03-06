package frc.robot.command;

import java.util.ArrayList;
import java.util.List;

public class CommandSequence implements Command {
    private final List<Command> sequence;
    private int currentIndex = 0;
    
    public CommandSequence() {
        sequence = new ArrayList<>();
    }

    public CommandSequence append(Command cmd) {
        sequence.add(cmd);
        return this;
    }

    public void reset() {
        currentIndex = 0;
    }

    public void next() {
        if (currentIndex < sequence.size()) {
            System.out.println("Ending " + sequence.get(currentIndex));
            sequence.get(currentIndex).end();
        }

        currentIndex++;

        if (currentIndex < sequence.size()) {
            System.out.println("Moving on to " + sequence.get(currentIndex));
            sequence.get(currentIndex).start();
        }
    }

    @Override
    public void start() {
        if (currentIndex < sequence.size()) {
            System.out.println("Starting " + sequence.get(currentIndex));
            sequence.get(currentIndex).start();
        }
    }

    @Override
    public boolean run() {
        if (currentIndex >= sequence.size()) {
            return true;
        }

        Command activeCommand = sequence.get(currentIndex);
        if (activeCommand.run()) {
            next();
        }

        return false;
    }

    @Override
    public void end() {
        if (currentIndex < sequence.size()) {
            System.out.println("Ending " + sequence.get(currentIndex));
            sequence.get(currentIndex).end();
        }
    }
}
