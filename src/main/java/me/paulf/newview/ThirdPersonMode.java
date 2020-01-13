package me.paulf.newview;

import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.Vector4f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ThirdPersonMode implements PlayMode {
    private final ClientWorld world;

    private final Entity avatar;

    private float x;
    private float y;
    private float z;
    private float ox;
    private float oy;
    private float oz;
    private float yaw;
    private float oyaw;
    private float pitch;
    private float opitch;

    public ThirdPersonMode(final ClientWorld world, final Entity avatar) {
        this.world = world;
        this.avatar = avatar;
    }

    @Override
    public void setup(final Camera camera, final float delta) {
        final Entity avatar = this.avatar;
        final float x = MathHelper.lerp(delta, this.ox, this.x);
        final float y = MathHelper.lerp(delta, this.oy, this.y);
        final float z = MathHelper.lerp(delta, this.oz, this.z);
        final float yaw = MathHelper.lerp(delta, this.oyaw, this.yaw);
        final float pitch = MathHelper.lerp(delta, this.opitch, this.pitch);
        final Matrix4f m = new Matrix4f();
        m.loadIdentity();
        m.multiply(Matrix4f.translate(x, y, z));
        m.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-yaw));
        m.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));
        m.multiply(Matrix4f.translate(0.0F, 0.0F, -5.0F));
        final Vector4f pos = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
        pos.transform(m);
        camera.setRotation(yaw, pitch);
        camera.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void tick() {
        this.ox = this.x;
        this.oy = this.y;
        this.oz = this.z;
        this.opitch = this.pitch;
        this.oyaw = this.yaw;
        final Entity avatar = this.avatar;
        this.x = (float) avatar.getX();
        this.y = (float) avatar.getY() + 1.5F;
        this.z = (float) avatar.getZ();
        this.yaw = 60.0F;
        this.pitch = 22.5F;
        while (this.yaw - this.oyaw < -180.0F) {
            this.oyaw -= 360.0F;
        }
        while (this.yaw - this.oyaw >= 180.0F) {
            this.oyaw += 360.0F;
        }
        while (this.pitch - this.opitch < -180.0F) {
            this.opitch -= 360.0F;
        }
        while (this.pitch - this.opitch >= 180.0F) {
            this.opitch += 360.0F;
        }
    }
}
