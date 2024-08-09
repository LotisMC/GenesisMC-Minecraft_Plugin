package me.dueris.originspaper.factory.action.meta;

import io.github.dueris.calio.parser.InstanceDefiner;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.action.ActionFactory;
import org.jetbrains.annotations.NotNull;

public class NothingAction {

	public static <T> @NotNull ActionFactory<T> getFactory() {
		return new ActionFactory<T>(OriginsPaper.apoliIdentifier("nothing"),
			InstanceDefiner.instanceDefiner(),
			(inst, t) -> {
			}
		);
	}
}