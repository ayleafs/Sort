package me.sixteen_.sort.config.gui;

import lombok.Getter;
import me.sixteen_.sort.Sort;
import me.sixteen_.sort.config.SortConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    @Getter
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("sort.config.title"));

        this.parent = parent;
    }

    @Override
    protected void init() {
        SortConfig config = Sort.getInstance().getConfig();

        int buttonW = 200;
        int buttonH = 20;

        this.addDrawableChild(new ButtonWidget((this.width - buttonW) / 2, (this.height - buttonH) / 2, buttonW, buttonH, Text.translatable("sort.config.buttons", config.isSortButtonVisible()), button -> {
            // toggle the buttons' visibility
            config.setSortButtonVisible(!config.isSortButtonVisible());

            // re-render the button text
            button.setMessage(Text.translatable("sort.config.buttons", config.isSortButtonVisible()));
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
