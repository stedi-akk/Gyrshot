package com.stedi.gyrshot.other;

/**
 * _______top________
 * |                |
 * |                |
 * |                |
 * left    0    right
 * |                |
 * |                |
 * |_____bottom_____|
 */

public class FloatRect {
    public final float left;
    public final float right;
    public final float top;
    public final float bottom;

    public FloatRect(float left, float top, float right, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public FloatRect(float width, float height) {
        this(-width / 2, -height / 2, width / 2, height / 2);
    }

    public float getWidth() {
        return -left + right;
    }

    public float getHeight() {
        return -top + bottom;
    }

    public boolean isInside(float x, float y) {
        return x >= left && x <= right
                && y >= top && y <= bottom;
    }

    public float forceInLeftRight(float x) {
        return forceInLeftRight(x, 0);
    }

    public float forceInLeftRight(float x, float offset) {
        return forceIn(x, offset, left, right);
    }

    public float forceInTopBottom(float y) {
        return forceInTopBottom(y, 0);
    }

    public float forceInTopBottom(float y, float offset) {
        return forceIn(y, offset, top, bottom);
    }

    private float forceIn(float value, float offset, float from, float to) {
        if (value + offset < from)
            return from;
        if (value - offset > to)
            return to;
        return value;
    }
}
