package com.stedi.gyrshot.other;

/**
   ______top_______
  |                |
  |                |
  |                |
left      0      right
  |                |
  |                |
  |_____      _____|
        bottom
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
}
