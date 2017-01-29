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

    public float forceInLeftRight(float x) {
        if (x < left)
            return left;
        if (x > right)
            return right;
        return x;
    }

    public float forceInTopBottom(float y) {
        if (y < top)
            return top;
        if (y > bottom)
            return bottom;
        return y;
    }
}
