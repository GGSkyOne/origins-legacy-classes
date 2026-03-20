package io.github.apace100.originsclasses;

import io.github.apace100.apoli.util.NamespaceAlias;
import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.effect.ClassesEffects;
import io.github.apace100.originsclasses.networking.ClassesPackets;
import io.github.apace100.originsclasses.power.ClassesPowerFactories;
import net.fabricmc.api.ModInitializer;

public class OriginsClasses implements ModInitializer {
	public static final String MODID = "origins-classes";

	@Override
	public void onInitialize() {
		NamespaceAlias.addAlias(MODID, "apoli");

		ClassesComponents.register();
		ClassesEffects.register();
		ClassesPackets.registerPayloads();
		ClassesPowerFactories.register();
	}
}
