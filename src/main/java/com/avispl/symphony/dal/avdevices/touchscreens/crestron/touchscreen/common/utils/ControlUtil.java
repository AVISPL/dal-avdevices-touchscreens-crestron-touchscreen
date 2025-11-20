/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay.Audio;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay.VirtualButtons;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd.AutoBrightness;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd.Presets;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.DisplayEdge;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Display;
import com.avispl.symphony.dal.util.ControllablePropertyFactory;

/**
 * Utility class providing helper methods for controllable property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControlUtil {
	private static final Log LOGGER = LogFactory.getLog(ControlUtil.class);

	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceDisplay}.
	 * <p>
	 * Includes controls for LCD, audio, button toolbar and local setup access.
	 * </p>
	 *
	 * @param display the {@link DeviceDisplay} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the display
	 */
	public static List<AdvancedControllableProperty> generateDisplayControllers(DeviceDisplay display) {
		if (display == null) {
			LOGGER.warn("The display is null, returning empty property");
			return new ArrayList<>();
		}
		String prefixName = Constant.DISPLAY_GROUP;
		Lcd lcd = Util.getLcd(display);
		Lcd.AutoBrightness autoBrightness = Util.getLcdAutoBrightness(display);
		Lcd.Presets presets = Util.getLcdPresets(display);
		DeviceDisplay.Audio audio = Util.getDisplayAudio(display);
		DeviceDisplay.VirtualButtons buttonToolbar = Util.getDisplayButtonToolbar(display);
		List<AdvancedControllableProperty> controllableProperties = new ArrayList<>();
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LOCAL_SETUP_SEQUENCE.getName()),
				getSwitchValue(display.getIsLocalSetupAccessEnabled())
		));
		//	Add LCD's controllable properties
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_AUTO_BRIGHTNESS.getName()),
				getSwitchValue(autoBrightness.getIsEnabled())
		));
		if (Boolean.TRUE.equals(autoBrightness.getIsEnabled())) {
			controllableProperties.add(createSlider(
					String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_ALS_THRESHOLD.getName()),
					100, autoBrightness.getThresholdValue()
			));
		} else {
			controllableProperties.add(createSlider(
					String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_BRIGHTNESS.getName()),
					100, lcd.getBrightness()
			));
		}
		controllableProperties.add(createSlider(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_BRIGHTNESS_HIGH_PRESET.getName()),
				100, presets.getHighLevel()
		));
		controllableProperties.add(createSlider(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_BRIGHTNESS_LOW_PRESET.getName()),
				100, presets.getLowLevel()
		));
		controllableProperties.add(createSlider(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.LCD_STANDBY_TIMEOUT.getName()),
				120, lcd.getStandbyTimeoutMinutes()
		));
		//	Add Audio's controllable properties
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_PANEL_MUTE.getName()),
				getSwitchValue(audio.getIsMuted())
		));
		if (Boolean.FALSE.equals(audio.getIsMuted())) {
			controllableProperties.add(createSlider(
					String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_PANEL_VOLUME.getName()),
					100, audio.getVolume()
			));
		}
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_MEDIA_MUTE.getName()),
				getSwitchValue(audio.getIsMediaMuted())
		));
		if (Boolean.FALSE.equals(audio.getIsMediaMuted())) {
			controllableProperties.add(createSlider(
					String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_MEDIA_VOLUME.getName()),
					100, audio.getMediaVolume()
			));
		}
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_BEEP_ENABLED.getName()),
				getSwitchValue(audio.getIsBeepEnabled())
		));
		if (Boolean.TRUE.equals(audio.getIsBeepEnabled())) {
			controllableProperties.add(createSlider(
					String.format(Constant.PROPERTY_FORMAT, prefixName, Display.AUDIO_BEEP_VOLUME.getName()),
					100, audio.getBeepVolume()
			));
		}
		//	Add Button toolbar's controllable properties
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.BUTTON_TOOLBAR_SHOW_ON_WAKE.getName()),
				getSwitchValue(buttonToolbar.getIsShowOnWakeEnabled())
		));
		controllableProperties.add(ControllablePropertyFactory.createSwitch(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.BUTTON_TOOLBAR_SHOW_DURING_STANDBY.getName()),
				getSwitchValue(buttonToolbar.getIsShowDuringStandbyEnabled())
		));
		controllableProperties.add(ControllablePropertyFactory.createDropdown(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.BUTTON_TOOLBAR_DISPLAY_EDGE.getName()),
				DisplayEdge.getValues(), buttonToolbar.getDisplayEdge()
		));
		controllableProperties.add(createSlider(
				String.format(Constant.PROPERTY_FORMAT, prefixName, Display.BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT.getName()),
				600, buttonToolbar.getAutoHideTimeOutSeconds()
		));

		return controllableProperties;
	}

	/**
	 * Builds the request payload for updating a specific display {@link Display} property.
	 *
	 * @param property the display property to update
	 * @param value the new value for the property
	 * @return a map representing the payload with the nested Device → Display structure
	 */
	public static Map<String, Object> buildDisplayRequest(Display property, Object value) {
		DeviceDisplay display = new DeviceDisplay();
		if (property.getName().startsWith(Constant.AUDIO_DISPLAY_GROUP)) {
			display.setAudio(buildAudioDisplay(property, value));
		} else if (property.getName().startsWith(Constant.LCD_DISPLAY_GROUP)) {
			display.setLcd(buildLcdDisplay(property, value));
		} else if (property.getName().startsWith(Constant.BUTTON_TOOLBAR_DISPLAY_GROUP)) {
			display.setVirtualButtons(buildButtonToolbarDisplay(property, value));
		} else {
			if (property == Display.LOCAL_SETUP_SEQUENCE) {
				display.setIsLocalSetupAccessEnabled(mapToBoolean(value.toString()));
			} else {
				LOGGER.warn("Unhandled property: %s".formatted(property));
			}
		}

		return Map.of("Device", Map.of("Display", display));
	}

	/**
	 * Builds an {@link Audio} object with only the given audio {@link Display} property set.
	 *
	 * @param property the audio property to update
	 * @param value the new value for the property
	 * @return an {@link Audio} instance with the corresponding field populated
	 */
	private static Audio buildAudioDisplay(Display property, Object value) {
		Audio audio = new Audio();
		switch (property) {
			case AUDIO_PANEL_MUTE -> audio.setIsMuted(mapToBoolean(value.toString()));
			case AUDIO_MEDIA_MUTE -> audio.setIsMediaMuted(mapToBoolean(value.toString()));
			case AUDIO_BEEP_ENABLED -> audio.setIsBeepEnabled(mapToBoolean(value.toString()));
			case AUDIO_PANEL_VOLUME -> audio.setVolume(((Number) value).intValue());
			case AUDIO_MEDIA_VOLUME -> audio.setMediaVolume(((Number) value).intValue());
			case AUDIO_BEEP_VOLUME -> audio.setBeepVolume(((Number) value).intValue());
			default -> LOGGER.warn("Unhandled audio property: %s".formatted(property));
		}

		return audio;
	}

	/**
	 * Builds an {@link Lcd} object with only the given LCD {@link Display} property set.
	 *
	 * @param property the LCD property to update
	 * @param value the new value for the property
	 * @return an {@link Lcd} instance with the corresponding field populated
	 */
	private static Lcd buildLcdDisplay(Display property, Object value) {
		Lcd lcd = new Lcd();
		lcd.setAutoBrightness(new AutoBrightness());
		lcd.setPresets(new Presets());
		if (Display.LCD_AUTO_BRIGHTNESS == property) {
			lcd.getAutoBrightness().setIsEnabled(mapToBoolean(value.toString()));
		} else {
			int mappedValue = ((Double) value).intValue();
			switch (property) {
				case LCD_ALS_THRESHOLD -> lcd.getAutoBrightness().setThresholdValue(mappedValue);
				case LCD_BRIGHTNESS -> lcd.setBrightness(mappedValue);
				case LCD_BRIGHTNESS_HIGH_PRESET -> lcd.getPresets().setHighLevel(mappedValue);
				case LCD_BRIGHTNESS_LOW_PRESET -> lcd.getPresets().setLowLevel(mappedValue);
				case LCD_STANDBY_TIMEOUT -> lcd.setStandbyTimeoutMinutes(mappedValue);
				default -> LOGGER.warn("Unhandled display property: %s".formatted(property));
			}
		}

		return lcd;
	}

	/**
	 * Builds a {@link VirtualButtons} object with only the given button toolbar {@link Display} property set.
	 *
	 * @param property the button toolbar property to update
	 * @param value the new value for the property
	 * @return a {@link VirtualButtons} instance with the corresponding field populated
	 */
	private static VirtualButtons buildButtonToolbarDisplay(Display property, Object value) {
		VirtualButtons buttonToolbar = new VirtualButtons();
		switch (property) {
			case BUTTON_TOOLBAR_DISPLAY_EDGE -> buttonToolbar.setDisplayEdge(value.toString());
			case BUTTON_TOOLBAR_SHOW_ON_WAKE -> buttonToolbar.setIsShowOnWakeEnabled(mapToBoolean(value.toString()));
			case BUTTON_TOOLBAR_SHOW_DURING_STANDBY -> buttonToolbar.setIsShowDuringStandbyEnabled(mapToBoolean(value.toString()));
			case BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT -> buttonToolbar.setAutoHideTimeOutSeconds(((Double) value).intValue());
			default -> LOGGER.warn("Unhandled button toolbar property: %s".formatted(property));
		}

		return buttonToolbar;
	}

	/**
	 * Creates a slider {@link AdvancedControllableProperty} with the given range and initial value.
	 *
	 * @param name the property name
	 * @param rangeEnd the maximum slider value
	 * @param initialValue the initial slider value
	 * @return a slider-based controllable property
	 */
	public static AdvancedControllableProperty createSlider(String name, Integer rangeEnd, Integer initialValue) {
		AdvancedControllableProperty.Slider slider = new AdvancedControllableProperty.Slider();
		slider.setLabelStart("0");
		slider.setLabelEnd(rangeEnd.toString());
		slider.setRangeStart(0f);
		slider.setRangeEnd(rangeEnd.floatValue());
		return new AdvancedControllableProperty(name, new Date(), slider, initialValue.floatValue());
	}

	/**
	 * Maps a {@link Boolean} to a numeric switch value (1 for true, 0 for false).
	 *
	 * @param value the boolean value
	 * @return 1 if true, otherwise 0
	 */
	private static int getSwitchValue(Boolean value) {
		return Boolean.TRUE.equals(value) ? 1 : 0;
	}

	/**
	 * Maps a string value to boolean (only {@code "1"} is treated as {@code true}).
	 *
	 * @param value the string value
	 * @return true if the value is "1", otherwise false
	 */
	private static boolean mapToBoolean(String value) {
		return "1".equals(value);
	}
}
