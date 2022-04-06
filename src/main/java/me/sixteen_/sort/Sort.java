package me.sixteen_.sort;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.GenericContainerScreenHandler;

public class Sort implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (screen instanceof GenericContainerScreen) {
				ScreenKeyboardEvents.afterKeyPress(screen).register((chestScreen, key, scancode, modifiers) -> {
					if (key == GLFW.GLFW_KEY_R) {
						GenericContainerScreenHandler chest = ((GenericContainerScreen) chestScreen).getScreenHandler();
						sort(chest);
					}
				});
			}
		});
	}

	private void sort(GenericContainerScreenHandler chest) {
		// TODO
	}
}
