# Crestron Touch Panel Integration - Capabilities & Configuration
This document covers Crestron Touch Panel Adapter Capabilities and Configuration.

Symphony integrates with Crestron Touch Panels to provide comprehensive monitoring and control of Crestron TSW-70 Series touch screens. It leverages the dedicated API for TSW-70 Series to deliver a full set of monitorable properties and control functions, including display settings, audio management, network status, and hardware capability tracking.

## Main use cases
- **Monitor** touch panel health, display status, network connectivity, and audio settings
- **Control** LCD brightness, audio volumes, button toolbar behavior, and standby timeouts
- **Track** individual device details - firmware version, MAC address, serial number, reboot reason, and hardware port counts
- **Inventory** keep Crestron touch panels and their configurations in check

## Prerequisites and where to start
The Crestron Touch Panel Adapter communicates directly with the device over HTTPS using Crestron's native device credentials.

Required device configuration:
- The touch panel must be reachable on the network via its IP address
- HTTPS management must be enabled on the device (port 443 by default)
- A valid Crestron username and password must be available for authentication

Supported Models: TSW-70 Series, including TSW-570, TSW-570P, TSW-770, and TSW-1070.

## Crestron Touch Panel Device Configuration and Provisioning
Once the device is accessible on the network, use the device IP address, Crestron username, and password for the Symphony device configuration.
Once the Crestron device is created with Monitoring Service -> Advanced Monitoring, the following configuration must be applied:

| Field | Description |
| --- | --- |
| Device Type | AV Devices |
| Category | Touch Screens |
| Manufacturer | Crestron |
| Model | Any supported model |
| Monitoring Service | Advanced Monitoring |
| Monitoring Source | Direct |
| Management Address | IP address of the device |
| Protocol | HTTPs |
| Username | Crestron's username |
| Password | Crestron's password |
| Port Number | 443 |

When the device is configured, saved and set active, the Crestron Touch Panel Adapter will begin communicating with the device API to retrieve all available monitoring data and expose control capabilities.

Devices and available data can be tuned by adapter configuration properties:

| Property | Description | Value |
| --- | --- | --- |
| displayPropertyGroups | Defines which property groups are displayed by the adapter. Multiple values can be separated by commas. Possible values (case-sensitive): General, Capabilities, Network, Display, SystemVersions, All | General by default |

## Available Monitored Data
Crestron Touch Panel monitored data is organized into the following property groups:

| Property Group | General Description | Example Properties |
| --- |--- |--- |
| General | Contains core device identification, firmware, manufacturer, versioning, and reboot-related information used for inventory and device tracking. | DeviceID, FirmwareVersion, RebootReason |
| Capabilities | Defines supported device features and hardware interface availability, including upload support flags and port counts for HDMI, Ethernet, and DM interfaces. | ConfigFileUploadSupported, PortHDMIInputCount |
| Display | Contains monitoring and control properties related to the user interface, including LCD brightness and standby settings, audio controls, and virtual button toolbar behavior. | **LCD:** LCDBrightness, LCDAutoBrightness, LCDStandbyTimeout, **Audio:** AudioMediaVolume, AudioPanelMute, AudioBeepEnabled, **Button Toolbar:** ButtonToolbarDisplayEdge, ButtonToolbarAutoHideTimeout |
| Network | Provides LAN and Wi-Fi connectivity information, including IP addressing, DHCP configuration, DNS settings, gateway information, and link status. | LANIPAddress, DNSServers, WiFiLinkActive |
| System Versions | Contains software component version details and operating or security mode information. | CH5Version, FIPSMode |

## Troubleshooting

**Login Error**
- Verify that the Crestron username and password are correct and that the account has sufficient permissions
- Ensure that HTTPS management is enabled on the touch panel
- Confirm the device is reachable at the configured IP address on port 443

**API / Communication Error**
- Check that the Management Address is correct and that no firewall or proxy is blocking port 443
- Confirm the Protocol is set to HTTPs in the Symphony device configuration
- Review any error descriptions in the Symphony adapter logs for hints on misconfiguration

**Link Error / Ping Timeout**
- Make sure your Cloud Connector can reach the touch panel's IP address
- Check the Ping Protocol configured in the Symphony device settings
- Try switching between ICMP/TCP modes, as certain protocols may be blocked by proxy or network policy

If none of the recommended steps help, please enter an SOS ticket at {https://avi-spl.atlassian.net/servicedesk/customer/portals}

## What AI Assistant can do with it:
- Find Crestron Touch Panel devices (AV Devices | Touch Screens | Crestron | TSW-1070) in Symphony
- Verify Crestron Touch Panel adapter configuration and property group settings
- Report on device status - firmware version, reboot reason, network connectivity, display and audio settings

## What AI Assistant cannot do with it:
- Provision the devices
- Push new firmware to the touch panel
- Modify Crestron Cloud configuration directly