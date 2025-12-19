/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;

/**
 * Represents display properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@Getter
public enum Display implements BaseProperty {
	DISPLAY_STATUS("Status"),
	LOCAL_SETUP_SEQUENCE("LocalSetupSequence"),
	//	LCD
	LCD_AUTO_BRIGHTNESS(Constant.LCD_DISPLAY_GROUP + "AutoBrightness"),
	LCD_ALS_THRESHOLD(Constant.LCD_DISPLAY_GROUP + "ALSThreshold(%)"),
	LCD_ALS_THRESHOLD_VALUE(Constant.LCD_DISPLAY_GROUP + "ALSThresholdCurrentValue(%)"),
	LCD_BRIGHTNESS(Constant.LCD_DISPLAY_GROUP + "Brightness(%)"),
	LCD_BRIGHTNESS_VALUE(Constant.LCD_DISPLAY_GROUP + "BrightnessCurrentValue(%)"),
	LCD_BRIGHTNESS_HIGH_PRESET(Constant.LCD_DISPLAY_GROUP + "BrightnessHighPreset(%)"),
	LCD_BRIGHTNESS_HIGH_PRESET_VALUE(Constant.LCD_DISPLAY_GROUP + "BrightnessHighPresetCurrentValue(%)"),
	LCD_BRIGHTNESS_LOW_PRESET(Constant.LCD_DISPLAY_GROUP + "BrightnessLowPreset(%)"),
	LCD_BRIGHTNESS_LOW_PRESET_VALUE(Constant.LCD_DISPLAY_GROUP + "BrightnessLowPresetCurrentValue(%)"),
	LCD_STANDBY_TIMEOUT(Constant.LCD_DISPLAY_GROUP + "StandbyTimeout(min)"),
	LCD_STANDBY_TIMEOUT_VALUE(Constant.LCD_DISPLAY_GROUP + "StandbyTimeoutCurrentValue(min)"),
	//	Audio
	AUDIO_PANEL_MUTE(Constant.AUDIO_DISPLAY_GROUP + "PanelMute"),
	AUDIO_PANEL_VOLUME(Constant.AUDIO_DISPLAY_GROUP + "PanelVolume(%)"),
	AUDIO_PANEL_VOLUME_VALUE(Constant.AUDIO_DISPLAY_GROUP + "PanelVolumeCurrentValue(%)"),
	AUDIO_MEDIA_MUTE(Constant.AUDIO_DISPLAY_GROUP + "MediaMute"),
	AUDIO_MEDIA_VOLUME(Constant.AUDIO_DISPLAY_GROUP + "MediaVolume(%)"),
	AUDIO_MEDIA_VOLUME_VALUE(Constant.AUDIO_DISPLAY_GROUP + "MediaVolumeCurrentValue(%)"),
	AUDIO_BEEP_ENABLED(Constant.AUDIO_DISPLAY_GROUP + "BeepEnabled"),
	AUDIO_BEEP_VOLUME(Constant.AUDIO_DISPLAY_GROUP + "BeepVolume(%)"),
	AUDIO_BEEP_VOLUME_VALUE(Constant.AUDIO_DISPLAY_GROUP + "BeepVolumeCurrentValue(%)"),
	//	Button toolbar
	BUTTON_TOOLBAR_SHOW_ON_WAKE(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP + "ShowOnWake"),
	BUTTON_TOOLBAR_SHOW_DURING_STANDBY(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP + "ShowDuringStandby"),
	BUTTON_TOOLBAR_DISPLAY_EDGE(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP + "DisplayEdge"),
	BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP + "AutoHideTimeout(s)"),
	BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT_VALUE(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP + "AutoHideTimeoutCurrentValue(s)");

	private final String name;

	Display(String name) {
		this.name = name;
	}

	public static Optional<Display> getByName(String name) {
		return Arrays.stream(values()).filter(display -> display.name.equalsIgnoreCase(name)).findFirst();
	}
}
