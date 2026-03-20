package io.github.apace100.originsclasses;

import io.github.apace100.apoli.util.NamespaceAlias;
import io.github.apace100.originsclasses.component.ClassesComponents;
import io.github.apace100.originsclasses.effect.ModEffects;
import io.github.apace100.originsclasses.networking.ModPackets;
import io.github.apace100.originsclasses.power.ClassesPowerFactories;
import net.fabricmc.api.ModInitializer;

public class OriginsClasses implements ModInitializer {
	public static final String MODID = "origins-classes";

	@Override
	public void onInitialize() {
		NamespaceAlias.addAlias(MODID, "apoli");

		ClassesComponents.register();
		ModPackets.registerPayloads();
		ClassesPowerFactories.register();
		ModEffects.register();
	}
}
