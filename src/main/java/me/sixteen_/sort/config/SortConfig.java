package me.sixteen_.sort.config;

import lombok.Data;
import org.lwjgl.glfw.GLFW;

@Data
public class SortConfig {
    private final int sortBind = GLFW.GLFW_KEY_R;
    private boolean isSortButtonVisible = true;
}
