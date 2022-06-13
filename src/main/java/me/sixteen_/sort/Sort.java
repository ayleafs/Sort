package me.sixteen_.sort;

import org.lwjgl.glfw.GLFW;

import me.sixteen_.sort.config.IConfig;
import me.sixteen_.sort.order.IOrder;
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
import net.minecraft.screen.slot.SlotActionType;

public class Sort implements ClientModInitializer {

	private IConfig config;
	private IOrder order;

	private MinecraftClient mc;
	private ScreenHandler container;

	@Override
	public void onInitializeClient() {
		config = () -> GLFW.GLFW_KEY_R;
		order = slot -> slot.getStack().isEmpty() ? Integer.MAX_VALUE : Item.getRawId(slot.getStack().getItem());
		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			if (isContainer(screen)) {
				ScreenKeyboardEvents.afterKeyPress(screen).register((containerScreen, key, scancode, modifiers) -> {
					if (key == config.getKeycode()) {
						mc = client;
						ScreenHandler container = ((ScreenHandlerProvider<?>) containerScreen).getScreenHandler();
						this.container = container;
						sort();
					}
				});
			}
		});
	}

	public void setConfig(IConfig config) {
		this.config = config;
	}

	public void setOrder(IOrder order) {
		this.order = order;
	}

	private boolean isContainer(Screen screen) {
		return screen instanceof GenericContainerScreen || //
				screen instanceof ShulkerBoxScreen || //
				screen instanceof Generic3x3ContainerScreen || //
				screen instanceof HopperScreen;
	}

	private void sort() {
		Integer[] slots = getContainerSlots();
		quicksort(slots, 0, slots.length - 1);
	}

	private void quicksort(Integer[] slots, int left, int right) {
		if (left >= right || left < 0) {
			return;
		}
		int p = partition(slots, left, right);
		quicksort(getContainerSlots(), left, p - 1);
		quicksort(getContainerSlots(), p + 1, right);
	}

	private int partition(Integer[] slots, int left, int right) {
		int pivot = slots[right];
		int i = left - 1;

		for (int j = left; j < right; j++) {
			int id = slots[j];
			if (id <= pivot) {
				i++;
				swap(i, j);
			}
		}
		i++;
		swap(i, right);
		return i;
	}

	private Integer[] getContainerSlots() {
		return container.slots.stream()//
				.filter(slot -> !mc.player.getInventory().equals(slot.inventory))//
				.map(order::getOrder)//
				.toArray(Integer[]::new);
	}

	private void swap(int i1, int i2) {
		if (i1 != i2) {
			pickup(i1);
			pickup(i2);
			pickup(i1);
		}
	}

	private void pickup(int i) {
		mc.interactionManager.clickSlot(container.syncId, i, 0, SlotActionType.PICKUP, mc.player);
	}
}