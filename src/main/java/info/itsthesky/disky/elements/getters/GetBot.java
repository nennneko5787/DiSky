package info.itsthesky.disky.elements.getters;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.core.Bot;
import info.itsthesky.disky.managers.BotManager;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetBot extends SimpleExpression<Bot> {

    static {
        Skript.registerExpression(
                GetBot.class,
                Bot.class,
                ExpressionType.COMBINED,
                "[get] [the] bot [(named|with name)] %string%"
        );
    }

    private Expression<String> exprName;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected Bot @NotNull [] get(@NotNull Event e) {
        final String name = exprName.getSingle(e);
        if (name == null)
            return new Bot[0];
        final @Nullable Bot bot = DiSky.getManager().fromName(name);
        if (bot == null)
            DiSky.getErrorHandler().exception(new RuntimeException("Unable to get the bot named " + name + ", its not loaded or not enabled."));
        return bot == null ? new Bot[0] : new Bot[] {bot};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Bot> getReturnType() {
        return Bot.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "bot named " + exprName.toString(e, debug);
    }
}
