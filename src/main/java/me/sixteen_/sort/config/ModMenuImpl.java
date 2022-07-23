package me.sixteen_.sort.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lombok.Getter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

import java.io.File;

public class ModMenuImpl implements ModMenuApi {
    @Getter private static final ConfigHandler<SortConfig> handler = new ConfigHandler<>(new File("config", "sort_config.json"), SortConfig.class);
    @Getter private static final SortConfig config = handler.readConfig();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setTitle(Text.translatable("sort.config.title"))
                    .setParentScreen(parent)
                    .setSavingRunnable(() -> handler.populateConfig(config));

            builder.getOrCreateCategory(Text.translatable("sort.config.title"))
                    .addEntry(builder.entryBuilder()
                            .startBooleanToggle(Text.translatable("sort.config.buttons"), config.isSortButtonVisible())
                            .setDefaultValue(true)
                            .setSaveConsumer(config::setSortButtonVisible)
                            .build());

            return builder.build();
        };
    }
}
