package me.sixteen_.sort;

import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class Sort implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (isContainer(screen)) {
				ScreenKeyboardEvents.afterKeyPress(screen).register((containerScreen, key, scancode, modifiers) -> {
					if (key == GLFW.GLFW_KEY_R) {
						ScreenHandler container = ((ScreenHandlerProvider<?>) containerScreen).getScreenHandler();
						sort(client, container);
					}
				});
			}
		});
	}

	private boolean isContainer(Screen screen) {
		return screen instanceof GenericContainerScreen || //
				screen instanceof ShulkerBoxScreen || //
				screen instanceof Generic3x3ContainerScreen || //
				screen instanceof HopperScreen;
	}

	private void sort(MinecraftClient mc, ScreenHandler container) {
		List<Slot> slots = container.slots.stream() //
				.filter(slot -> !mc.player.getInventory().equals(slot.inventory) && !slot.getStack().isEmpty()) //
				.collect(Collectors.toList());
		slots.sort((s1, s2) -> {
			int id1 = Item.getRawId(s1.getStack().getItem());
			int id2 = Item.getRawId(s2.getStack().getItem());
			if (id1 < id2)
				return -1;
			if (id1 > id2)
				return 1;
			return 0;
		});
		for (int i = 0; i < slots.size(); i++) {
			swap(mc, container.syncId, slots.get(i).id, i);
		}
	}

	private void swap(MinecraftClient mc, int syncId, int i1, int i2) {
		if (i1 != i2) {
			mc.interactionManager.clickSlot(syncId, i1, 0, SlotActionType.PICKUP, mc.player);
			mc.interactionManager.clickSlot(syncId, i2, 0, SlotActionType.PICKUP, mc.player);
		}
	}
}