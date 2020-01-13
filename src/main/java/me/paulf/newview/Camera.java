package me.paulf.newview;

public interface Camera {
    void setPosition(final double x, final double y, final double z);

    void setRotation(final float yaw, final float pitch);
}
