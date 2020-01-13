package me.paulf.newview;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class FirstPersonMode implements PlayMode {
    private final ClientWorld world;

    private final Entity avatar;

    private float height;

    private float prevHeight;

    public FirstPersonMode(final ClientWorld world, final Entity avatar) {
        this.world = world;
        this.avatar = avatar;
    }

    @Override
    public void setup(final Camera camera, final float delta) {
        final Entity avatar = this.avatar;
        camera.setRotation(avatar.getYaw(delta), avatar.getPitch(delta));
        camera.setPosition(
            MathHelper.lerp(delta, avatar.prevPosX, avatar.getX()),
            MathHelper.lerp(delta, avatar.prevPosY, avatar.getY()) + MathHelper.lerp(delta, this.prevHeight, this.height),
            MathHelper.lerp(delta, avatar.prevPosZ, avatar.getZ())
        );
    }

    @Override
    public void tick() {
        this.prevHeight = this.height;
        this.height += (this.avatar.getEyeHeight() - this.height) * 0.5F;
    }
}
