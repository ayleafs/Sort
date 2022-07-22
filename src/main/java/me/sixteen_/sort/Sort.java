package me.sixteen_.sort;

import lombok.Getter;
import me.sixteen_.sort.config.ConfigHandler;
import me.sixteen_.sort.config.SortConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.ScreenHandler;
import org.lwjgl.glfw.GLFW;

import java.io.File;

public class Sort implements ClientModInitializer {
	@Getter private static Sort instance;

	@Getter private ConfigHandler<SortConfig> handler;
	@Getter private SortConfig config;

	private KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		instance = this;

		handler = new ConfigHandler<>(new File("config", "sort_config.json"), SortConfig.class);
		config  = handler.readConfig();

		// when the client shuts down we want to save the config
		Runtime.getRuntime().addShutdownHook(new Thread(() -> handler.populateConfig(config)));

		// add the keybind to the keybinding menu
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("sort.keybind", GLFW.GLFW_KEY_R, "sort.name"));
		this.registerKeybindEvents();
	}

	private void registerKeybindEvents() {
		// whenever a new container GUI screen is opened, and it's a container, add a keybind listener
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			// it isn't a container so we don't care
			if (!isContainer(screen)) {
				return;
			}

			ScreenKeyboardEvents.afterKeyPress(screen).register((container, key, scanCode, modifiers) -> {
				if (!keyBinding.matchesKey(key, scanCode)) {
					return;
				}

				ScreenHandler screenHandler = ((ScreenHandlerProvider<?>) container).getScreenHandler();
				SortUtils.sort(screenHandler);
			});
		});
	}

	private boolean isContainer(Screen screen) {
		return screen instanceof GenericContainerScreen || //
				screen instanceof ShulkerBoxScreen || //
				screen instanceof Generic3x3ContainerScreen || //
				screen instanceof HopperScreen;
	}
}
