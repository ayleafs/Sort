package me.sixteen_.sort;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.*;
import java.util.stream.Collectors;

public class SortUtils {
    public static void sort(ScreenHandler handler) {
        // filter out all the player's inventory slots
        List<Slot> slots = handler.slots.stream()
                .filter(slot -> !MinecraftClient.getInstance().player.getInventory().equals(slot.inventory))
                .toList();

        quickSort(handler, slots);
    }

    public static void quickSort(ScreenHandler handler, List<Slot> slots) {
        // get rid of the empty slots
        slots = slots.stream().filter(Slot::hasStack).collect(Collectors.toList());

        // sort the slots by their item IDs
        slots.sort(Comparator.comparingInt(slot -> Item.getRawId(slot.getStack().getItem())));

        Map<Slot, Integer> slotIndexes = new HashMap<>();

        // assign each slot its current index
        for (Slot slot : slots) {
            slotIndexes.put(slot, slot.getIndex());
        }

        int c = 0;

        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);

            // move the item in this slot to the correct position
            int from = slotIndexes.get(slot);

            // don't need to move the item if it's already in the correct position
            if (from == i) {
                continue;
            }

            swap(handler, from, i);
            c++;

            // find the slot at i and update its index in the map
            int finalI = i;
            Slot slotAtI = handler.slots.stream()
                    .filter(s -> s.getIndex() == finalI)
                    .findFirst()
                    .orElse(null);

            // update the slot that was in the desired position to its new position
            // (the position that it was swapped to)
            slotIndexes.put(slotAtI, from);
        }

        System.out.println("Swapped " + c + " items");
    }

    public static void swap(ScreenHandler handler, int slotA, int slotB) {
        // click on the first slot, then place it in the second slot, then move the item back to the original slot
        clickSlot(handler, slotA);
        clickSlot(handler, slotB);
        clickSlot(handler, slotA);
    }

    public static void clickSlot(ScreenHandler handler, int slot) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // click on the slot with the proper sync ID
        mc.interactionManager.clickSlot(handler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
    }
}
