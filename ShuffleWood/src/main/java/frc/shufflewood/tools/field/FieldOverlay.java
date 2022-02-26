package frc.shufflewood.tools.field;

import processing.core.PGraphics;

public abstract class FieldOverlay {
    public boolean[] enabled = new boolean[] {true};

    public abstract void draw(PGraphics g, float pixelsPerMeter);
    public abstract String getName();
}
