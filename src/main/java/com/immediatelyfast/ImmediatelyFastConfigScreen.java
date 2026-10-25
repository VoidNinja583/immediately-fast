package com.immediatelyfast;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ImmediatelyFastConfigScreen extends Screen {
    private ButtonWidget safeButton;
    private ButtonWidget switchButton;
    private ButtonWidget totemButton;
    private ButtonWidget switchDelayButton;
    private ButtonWidget totemDelayButton;

    public ImmediatelyFastConfigScreen() {
        super(Text.literal("Immediately Fast - CPvP"));
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = this.height / 4;
        safeButton = addDrawableChild(ButtonWidget.builder(safeText(), b -> {
            ImmediatelyFastClient.safeAnchor = !ImmediatelyFastClient.safeAnchor;
            b.setMessage(safeText());
        }).dimensions(center - 125, y, 250, 20).build());

        switchButton = addDrawableChild(ButtonWidget.builder(switchText(), b -> {
            ImmediatelyFastClient.anchorSwitch = !ImmediatelyFastClient.anchorSwitch;
            b.setMessage(switchText());
        }).dimensions(center - 125, y + 28, 250, 20).build());

        switchDelayButton = addDrawableChild(ButtonWidget.builder(switchDelayText(), b -> {
            ImmediatelyFastClient.switchDelay = ImmediatelyFastClient.switchDelay >= 20 ? 1 : ImmediatelyFastClient.switchDelay + 1;
            b.setMessage(switchDelayText());
        }).dimensions(center - 125, y + 52, 250, 20).build());

        totemButton = addDrawableChild(ButtonWidget.builder(totemText(), b -> {
            ImmediatelyFastClient.autoTotem = !ImmediatelyFastClient.autoTotem;
            b.setMessage(totemText());
        }).dimensions(center - 125, y + 80, 250, 20).build());

        totemDelayButton = addDrawableChild(ButtonWidget.builder(totemDelayText(), b -> {
            ImmediatelyFastClient.totemDelay = ImmediatelyFastClient.totemDelay >= 20 ? 1 : ImmediatelyFastClient.totemDelay + 1;
            b.setMessage(totemDelayText());
        }).dimensions(center - 125, y + 104, 250, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(center - 60, y + 140, 120, 20).build());
    }

    private Text safeText() {
        return Text.literal("Safe Anchor Helper: " + (ImmediatelyFastClient.safeAnchor ? "ON" : "OFF"));
    }
    private Text switchText() {
        return Text.literal("Anchor/Glowstone/Totem Switch: " + (ImmediatelyFastClient.anchorSwitch ? "ON" : "OFF"));
    }
    private Text switchDelayText() {
        return Text.literal("Switch delay: " + ImmediatelyFastClient.switchDelay + " ticks (click to change)");
    }
    private Text totemText() {
        return Text.literal("AutoInv Totem: " + (ImmediatelyFastClient.autoTotem ? "ON" : "OFF"));
    }
    private Text totemDelayText() {
        return Text.literal("Totem delay: " + ImmediatelyFastClient.totemDelay + " ticks (click to change)");
    }

    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 4 - 24, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
