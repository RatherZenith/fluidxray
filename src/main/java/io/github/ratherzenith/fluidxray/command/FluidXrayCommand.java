package io.github.ratherzenith.fluidxray.command;

import io.github.ratherzenith.fluidxray.FluidXrayClient;
import io.github.ratherzenith.fluidxray.FluidXrayMode;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

/**
 * Client command. Because it is registered through {@link RegisterClientCommandsEvent} it is executed locally
 * by the client, never sent to the server, and therefore requires no operator permissions at all.
 *
 * <pre>
 * /fluidxray                 cycle to the next mode
 * /fluidxray off
 * /fluidxray default
 * /fluidxray killFog         (aliases: kill_fog, killfog)
 * </pre>
 */
public final class FluidXrayCommand {

    private FluidXrayCommand() {
    }

    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("fluidxray")
                .executes(context -> report(context.getSource(), FluidXrayClient.cycleMode()));

        for (FluidXrayMode mode : FluidXrayMode.values()) {
            root.then(Commands.literal(mode.getId())
                    .executes(context -> report(context.getSource(), FluidXrayClient.applyMode(mode))));
        }

        // Convenience aliases so that the mixed-case id is still easy to type.
        for (String alias : new String[]{"kill_fog", "killfog"}) {
            root.then(Commands.literal(alias)
                    .executes(context -> report(context.getSource(),
                            FluidXrayClient.applyMode(FluidXrayMode.KILL_FOG))));
        }

        dispatcher.register(root);
    }

    private static int report(CommandSourceStack source, FluidXrayMode mode) {
        source.sendSuccess(() -> Component.literal("Fluid X-ray mode: " + mode.getId()), false);
        return 1;
    }
}
