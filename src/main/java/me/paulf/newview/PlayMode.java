package me.paulf.newview;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public interface PlayMode {
    void setup(final Camera camera, final float delta);

    void tick();

    interface Factory {
        PlayMode create(final ClientWorld world, final Entity entity);
    }
}
