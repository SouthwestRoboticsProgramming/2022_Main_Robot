package frc.robot.control;

public final class XboxController {
    public interface Button {
        boolean isPressed();
        boolean leadingEdge();
        boolean fallingEdge();

        void update();
    }

    public interface Axis {
        public double get();

        void update();
    }

    public Button a, b, x, y;
    public Button leftShoulder, rightShoulder;
    public Button select, start;
    public Button dpadUp, dpadDown, dpadLeft, dpadRight;
    public Button leftStickIn, rightStickIn;

    public Axis leftStickX, leftStickY;
    public Axis rightStickX, rightStickY;
    public Axis leftTrigger, rightTrigger;

    public void update() {
        a.update();
        b.update();
        x.update();
        y.update();
        leftShoulder.update();
        rightShoulder.update();
        select.update();
        start.update();
        dpadUp.update();
        dpadDown.update();
        dpadLeft.update();
        dpadRight.update();
        leftStickIn.update();
        rightStickIn.update();

        leftStickX.update();
        leftStickY.update();
        rightStickX.update();
        rightStickY.update();
        leftTrigger.update();
        rightTrigger.update();
    }
}
