package me.dueris.originspaper.factory.conditions.types;

import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.conditions.ConditionFactory;
import me.dueris.originspaper.factory.conditions.meta.MetaConditions;
import me.dueris.originspaper.factory.data.types.Comparison;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import org.jetbrains.annotations.NotNull;

public class BiomeConditions {
	public static void registerConditions() {
		MetaConditions.register(me.dueris.originspaper.registry.Registries.BIOME_CONDITION, BiomeConditions::register);
		this.register(new ConditionFactory(OriginsPaper.apoliIdentifier("high_humidity"), (data, biome) -> biome.value().climateSettings.downfall() > 0.85F));
		this.register(new ConditionFactory(OriginsPaper.apoliIdentifier("temperature"), (data, biome) -> Comparison.fromString(data.getString("comparison")).compare(biome.value().getBaseTemperature(), data.getNumber("compare_to").getFloat())));
		this.register(new ConditionFactory(OriginsPaper.apoliIdentifier("category"), (data, biome) -> {
			ResourceLocation tagId = OriginsPaper.apoliIdentifier("category/" + data.getString("category"));
			TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, tagId);
			return biome.is(biomeTag);
		}));
		this.register(new ConditionFactory(OriginsPaper.apoliIdentifier("precipitation"), (data, biome) -> {
			Precipitation precipitation = data.getEnumValue("precipitation", Precipitation.class);
			return biome.value().getPrecipitationAt(new BlockPos(0, 64, 0)).equals(precipitation);
		}));
		this.register(new ConditionFactory(OriginsPaper.apoliIdentifier("in_tag"), (data, biome) -> {
			ResourceLocation tagId = data.getResourceLocation("tag");
			TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, tagId);
			return biome.is(biomeTag);
		}));
	}

	private Precipitation getPrecipitation(FactoryJsonObject condition) {
		String lowerCase = condition.getString("precipitation").toLowerCase();
		return switch (lowerCase) {
			case "none" -> Precipitation.NONE;
			case "snow" -> Precipitation.SNOW;
			case "rain" -> Precipitation.RAIN;
			default -> null;
		};
	}

	public static void register(@NotNull ConditionFactory<Biome> factory) {
		OriginsPaper.getPlugin().registry.retrieve(me.dueris.originspaper.registry.Registries.BIOME_CONDITION).register(factory, factory.getSerializerId());
	}

}