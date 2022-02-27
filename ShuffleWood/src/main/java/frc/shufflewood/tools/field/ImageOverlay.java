package frc.shufflewood.tools.field;

import frc.shufflewood.App;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImageOverlay extends FieldOverlay {
    private final PImage image;

    public ImageOverlay(App app) {
        image = app.loadImage("field-image.jpg");
    }

    @Override
    public void draw(PGraphics g, float pixelsPerMeter) {
        float scaleX = FieldDataViewTool.FIELD_WIDTH / image.width;
        float scaleY = FieldDataViewTool.FIELD_HEIGHT / image.height;

        g.pushMatrix();
        g.scale(scaleX, scaleY);
        g.image(image, -image.width / 2f, -image.height / 2f);
        g.popMatrix();
    }

    @Override
    public String getName() {
        return "Image";
    }
}
