package me.sixteen_.sort;

import lombok.Getter;
import me.sixteen_.sort.config.ModMenuImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Sort implements ClientModInitializer {
    @Getter private static Sort instance;

    private KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        instance = this;

        // add the keybind to the keybinding menu
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("sort.keybind", GLFW.GLFW_KEY_R, "sort.name"));
        this.registerScreenEvents();
    }

    private void registerScreenEvents() {
        // whenever a new container GUI screen is opened, and it's a container, add a keybind listener
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            // it isn't a container so we don't care
            if (!isContainer(screen)) {
                return;
            }

            // get the screen handler of the container
            ScreenHandler screenHandler = ((ScreenHandlerProvider<?>) screen).getScreenHandler();

            ScreenKeyboardEvents.afterKeyPress(screen).register((container, key, scanCode, modifiers) -> {
                if (!keyBinding.matchesKey(key, scanCode)) {
                    return;
                }

                SortUtils.sort(screenHandler);
            });

            if (!ModMenuImpl.getConfig().isSortButtonVisible()) {
                return;
            }

            Screens.getButtons(screen).add(new ButtonWidget(width - 40 - 5, height - 20 - 5, 40, 20, Text.translatable("sort.button"), button -> {
                // sort the container
                SortUtils.sort(screenHandler);
            }));
        });
    }

    private boolean isContainer(Screen screen) {
        return screen instanceof GenericContainerScreen || //
                screen instanceof ShulkerBoxScreen || //
                screen instanceof Generic3x3ContainerScreen || //
                screen instanceof HopperScreen;
    }
}
