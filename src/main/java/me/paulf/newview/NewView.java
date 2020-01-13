package me.paulf.newview;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;
import java.util.Set;

@Mod(NewView.ID)
public final class NewView {
	public static final String ID = "newview";

	@Nullable
	private CameraAdapter adapter;

	public NewView() {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			FMLJavaModLoadingContext.get().getModEventBus().<ParticleFactoryRegisterEvent>addListener(e ->
				ObfuscationReflectionHelper.setPrivateValue(GameRenderer.class, Minecraft.getInstance().gameRenderer, this.adapter = new CameraAdapter(), "field_215317_L")
			);
			MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(e -> {
				final Minecraft mc = Minecraft.getInstance();
				if (e.phase == TickEvent.Phase.END && this.adapter != null && mc.world != null && !mc.isGamePaused()) {
					this.adapter.tick();
				}
			});
			final Set<RenderGameOverlayEvent.ElementType> types = ImmutableSet.of(RenderGameOverlayEvent.ElementType.HOTBAR, RenderGameOverlayEvent.ElementType.JUMPBAR, RenderGameOverlayEvent.ElementType.EXPERIENCE);
			MinecraftForge.EVENT_BUS.<RenderGameOverlayEvent.Pre>addListener(EventPriority.LOW, e -> {
				if (types.contains(e.getType())) {
					final Minecraft mc = Minecraft.getInstance();
					final int pad = 1;
					ObfuscationReflectionHelper.setPrivateValue(IngameGui.class, mc.ingameGUI, e.getWindow().getScaledHeight() - pad, "field_194812_I");
					if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
						ForgeIngameGui.left_height += pad;
						ForgeIngameGui.right_height += pad;
					}
				}
			});
			MinecraftForge.EVENT_BUS.<RenderGameOverlayEvent.Post>addListener(e -> {
				if (types.contains(e.getType())) {
					final Minecraft mc = Minecraft.getInstance();
					ObfuscationReflectionHelper.setPrivateValue(IngameGui.class, mc.ingameGUI, e.getWindow().getScaledHeight(), "field_194812_I");
				}
			});
		});
	}
}
