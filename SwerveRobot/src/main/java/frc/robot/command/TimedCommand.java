package frc.robot.command;

public abstract class TimedCommand implements Command {
    private int timer;
    
    public TimedCommand(int time) {
        timer = time;
    }

    protected abstract void runTimed();

    @Override
    public boolean run() {
        return --timer <= 0;
    }
}
