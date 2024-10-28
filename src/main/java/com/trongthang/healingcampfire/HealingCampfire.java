package com.trongthang.healingcampfire;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealingCampfire implements ModInitializer {
	public static final String MOD_ID = "healing-campfire";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	//my changes
	@Override
	public void onInitialize() {

		LOGGER.info("Hello Fabric world!");
	}
}