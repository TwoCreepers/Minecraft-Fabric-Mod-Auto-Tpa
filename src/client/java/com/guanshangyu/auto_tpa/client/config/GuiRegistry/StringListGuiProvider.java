package com.guanshangyu.auto_tpa.client.config.GuiRegistry;

import me.shedaniel.autoconfig.gui.registry.api.GuiProvider;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringListBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class StringListGuiProvider implements GuiProvider {
    @Override
    @SuppressWarnings("rawtypes")
    public List<AbstractConfigListEntry> get(String fieldName, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        try {
            // 安全:（
            Object currentValueObj = field.get(config);
            Object defaultValueObj = field.get(defaults);

            if (!(currentValueObj instanceof List<?>) || !(defaultValueObj instanceof List<?>)) {
                throw new RuntimeException("Field " + fieldName + " is not a List");
            }

            for (Object item : (List<?>)currentValueObj) {
                if (!(item instanceof String)) {
                    throw new RuntimeException("Non-String element in list: " + item);
                }
            }

            for (Object item : (List<?>)defaultValueObj) {
                if (!(item instanceof String)) {
                    throw new RuntimeException("Non-String element in list: " + item);
                }
            }

            // 获取当前值和默认值
            @SuppressWarnings("unchecked")
            List<String> currentValue = (List<String>) currentValueObj;
            @SuppressWarnings("unchecked")
            List<String> defaultValue = (List<String>) defaultValueObj;
            // 创建配置条目构建器
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            // 构建字符串列表配置项
            StringListBuilder builder = entryBuilder.startStrList(
                    Text.translatable(getTranslationKey(fieldName)),
                    currentValue
            );

            // 设置默认值
            builder.setDefaultValue(defaultValue);

            // 设置保存回调
            builder.setSaveConsumer(newValue -> {
                try {
                    field.set(config, newValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to save config value for field: " + fieldName, e);
                }
            });

            // 可选：添加工具提示
            builder.setTooltip(Text.translatable(getTooltipKey(fieldName)));

            return Collections.singletonList(builder.build());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access config field: " + fieldName, e);
        }
    }

    private String getTranslationKey(String fieldName) {
        return fieldName;
    }

    private String getTooltipKey(String fieldName) {
        return fieldName + ".tooltip";
    }
}