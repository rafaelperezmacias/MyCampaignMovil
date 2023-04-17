package com.rld.app.mycampaign.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GraphicOverlay extends View {

    private final List<Graphic> mGraphics;

    {
        mGraphics = new ArrayList<>();
    }

    public GraphicOverlay(Context context)
    {
        super(context);
    }

    public GraphicOverlay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void clear() {
        mGraphics.clear();
        postInvalidate();
    }

    public void add(Graphic graphic) {
        mGraphics.add(graphic);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for ( Graphic graphic : mGraphics ) {
            graphic.draw(canvas);
        }
    }

    public static abstract class Graphic {

        private final GraphicOverlay mOverlay;

        public Graphic(GraphicOverlay overlay)
        {
            mOverlay = overlay;
        }

        public abstract void draw(Canvas canvas);

        public float scaleX(float horizontal) {
            return horizontal * mOverlay.getWidth() / 1920.0f;
        }

        public float scaleY(float vertical) {
            return vertical * mOverlay.getHeight() / 1080.0f;
        }

        public float translateX(float x) {
            return scaleX(x);
        }

        public float translateY(float y) {
            return scaleY(y);
        }

        public void postInvalidate() {
            mOverlay.postInvalidate();
        }

    }

}