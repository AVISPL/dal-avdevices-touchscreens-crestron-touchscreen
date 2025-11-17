package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.utils;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities.DeviceCapabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities.PortConfig;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.Adapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.AddressConfig;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.DnsSettings;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.IPv4;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.LanAdapter;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.WifiAdapter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {
	/**
	 * Retrieves the {@link PortConfig} instance from the given {@link DeviceCapabilities}.
	 * <p>
	 * If the provided {@code capabilities} object is {@code null}, or if its internal
	 * {@code portConfig} property is {@code null}, this method returns a new, empty
	 * {@link PortConfig} instance instead of {@code null}.
	 * </p>
	 *
	 * @param capabilities the {@link DeviceCapabilities} object from which to extract
	 * the {@link PortConfig}; may be {@code null}
	 * @return the existing {@link PortConfig} if available, or a new instance if
	 * {@code capabilities} or its port configuration is {@code null}
	 */
	public static PortConfig getPortConfig(DeviceCapabilities capabilities) {
		return capabilities == null
				? new PortConfig()
				: Optional.ofNullable(capabilities.getPortConfig()).orElse(new PortConfig());
	}

	/**
	 * Safely retrieves the IPv4 configuration from a {@link DnsSettings} instance.
	 *
	 * @param dnsSettings the DNS settings object, may be {@code null}
	 * @return the {@link IPv4} configuration if present, otherwise an empty {@link IPv4}
	 */
	public static IPv4 getIPv4(DnsSettings dnsSettings) {
		return dnsSettings == null
				? new IPv4()
				: Optional.ofNullable(dnsSettings.getIPv4()).orElse(new IPv4());
	}

	/**
	 * Safely retrieves the IPv4 configuration from the {@link Adapters}’s Ethernet LAN adapter.
	 *
	 * @param adapters the collection of network adapters, may be {@code null}
	 * @return the {@link IPv4} configuration from Ethernet LAN, or an empty {@link IPv4} if unavailable
	 */
	public static IPv4 getIPv4(Adapters adapters) {
		return adapters == null || adapters.getEthernetLan() == null
				? new IPv4()
				: Optional.ofNullable(adapters.getEthernetLan().getIPv4()).orElse(new IPv4());
	}

	/**
	 * Safely retrieves the {@link LanAdapter} from {@link Adapters}.
	 *
	 * @param adapters the parent {@link Adapters} object, may be {@code null}
	 * @return the {@link LanAdapter} if available, otherwise an empty {@link LanAdapter}
	 */
	public static LanAdapter getEthernetLan(Adapters adapters) {
		return adapters == null
				? new LanAdapter()
				: Optional.ofNullable(adapters.getEthernetLan()).orElse(new LanAdapter());
	}


	/**
	 * Safely retrieves the {@link WifiAdapter} from {@link Adapters}.
	 *
	 * @param adapters the parent {@link Adapters} object, may be {@code null}
	 * @return the {@link WifiAdapter} if available, otherwise an empty {@link WifiAdapter}
	 */
	public static WifiAdapter getWifi(Adapters adapters) {
		return adapters == null
				? new WifiAdapter()
				: Optional.ofNullable(adapters.getWifi()).orElse(new WifiAdapter());
	}

	/**
	 * Retrieves the first {@link AddressConfig} from the IPv4 configuration within the given {@link Adapters} instance.
	 *
	 * @param adapters the {@link Adapters} object containing IPv4 address list, may be {@code null}
	 * @return the first {@link AddressConfig} if present, otherwise an empty {@link AddressConfig}
	 */
	public static AddressConfig getFirstAddress(Adapters adapters) {
		return Optional.ofNullable(getIPv4(adapters).getAddresses().get(0)).orElse(new AddressConfig());
	}

	public static Lcd getLcd(DeviceDisplay display) {
		return Optional.ofNullable(display.getLcd()).orElse(new Lcd());
	}

	public static Lcd.AutoBrightness getLcdAutoBrightness(DeviceDisplay display) {
		return Optional.ofNullable(getLcd(display).getAutoBrightness()).orElse(new Lcd.AutoBrightness());
	}

	public static Lcd.Presets getLcdPresets(DeviceDisplay display) {
		return Optional.ofNullable(getLcd(display).getPresets()).orElse(new Lcd.Presets());
	}

	public static DeviceDisplay.Audio getDisplayAudio(DeviceDisplay display) {
		return Optional.ofNullable(display.getAudio()).orElse(new DeviceDisplay.Audio());
	}

	public static DeviceDisplay.VirtualButtons getDisplayButtonToolbar(DeviceDisplay display) {
		return Optional.ofNullable(display.getVirtualButtons()).orElse(new DeviceDisplay.VirtualButtons());
	}
}
