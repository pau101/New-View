package me.paulf.newview;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class CameraAdapter extends ActiveRenderInfo implements Camera {
    private boolean valid;

    private IBlockReader world;

    private Entity entity;

    private boolean thirdperson;

    private PlayMode mode;

    @Override
    public void update(final IBlockReader world, final Entity entity, final boolean thirdperson, final boolean selfie, final float delta) {
        if (!this.valid || thirdperson != this.thirdperson) {
            if (thirdperson) {
                this.mode = new ThirdPersonMode((ClientWorld) world, entity);
            } else {
                this.mode = new FirstPersonMode((ClientWorld) world, entity);
            }
        }
        this.world = world;
        this.entity = entity;
        this.thirdperson = thirdperson;
        this.mode.setup(this, delta);
        this.valid = true;
    }

    @Override
    public void setPosition(final double x, final double y, final double z) {
        super.setPosition(x, y, z);
    }

    @Override
    public void setRotation(final float yaw, final float pitch) {
        super.setDirection(yaw, pitch);
    }

    // This tick function happens way to early to be useful
    @Override
    public void interpolateHeight() {
    }

    public void tick() {
        if (this.valid) {
            this.mode.tick();
        }
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public Entity getRenderViewEntity() {
        return this.entity;
    }

    @Override
    public boolean isThirdPerson() {
        return this.thirdperson;
    }

    @Override
    public void clear() {
        this.valid = false;
        this.world = null;
        this.entity = null;
    }

    @Override
    public IFluidState getFluidState() {
        if (this.valid) {
            final BlockPos pos = this.getBlockPos();
            final IFluidState state = this.world.getFluidState(pos);
            // TODO: consider slope of flowing water
            if (!state.isEmpty() && this.getProjectedView().getY() < pos.getY() + state.getActualHeight(this.world, pos)) {
                return state;
            }
        }
        return Fluids.EMPTY.getDefaultState();
    }
}
