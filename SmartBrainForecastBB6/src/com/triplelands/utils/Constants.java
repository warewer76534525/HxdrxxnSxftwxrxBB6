package com.triplelands.utils;

import net.rim.device.api.system.DeviceInfo;

public class Constants {
	public static final String URL_SIGNALS = "http://hidreensoftware.com/index.php/m/signals";
	public static final String URL_SIGNAL_DETAIL = "http://hidreensoftware.com/index.php/m/signals/view";
	public static final String URL_NEWS = "http://hidreensoftware.com/index.php/m/news";
	public static final String URL_NEWS_DETAIL = "http://hidreensoftware.com/index.php/m/news/view";
	public static final String URL_ADD_COMMENT = "http://hidreensoftware.com/index.php/m/signals/add_comment";
	public static final String URL_REGISTER = "http://www.hidreensoftware.com/index.php/m/members/register";
	public static final String URL_LOGIN = "http://www.hidreensoftware.com/index.php/m/members/login";
	public static final String URL_LOGOUT = "http://www.hidreensoftware.com/m/members/logout";
	public static final String URL_CHECK_UPDATE = "http://www.hidreensoftware.com/m/updates/check/";
	
	public static final String[] TIME_ZONES = 
		{
		"(UTC - 12:00) Enitwetok, Kwajalien",
		"(UTC - 11:00) Nome, Midway Island, Samoa",
		"(UTC - 10:00) Hawaii",
		"(UTC - 9:00) Alaska",
		"(UTC - 8:00) Pacific Time",
		"(UTC - 7:00) Mountain Time",
		"(UTC - 6:00) Central Time, Mexico City",
		"(UTC - 5:00) Eastern Time, Bogota, Lima, Quito",
		"(UTC - 4:00) Atlantic Time, Caracas, La Paz",
		"(UTC - 3:30) Newfoundland",
		"(UTC - 3:00) Brazil, Buenos Aires, Georgetown, Falkland Is.",
		"(UTC - 2:00) Mid-Atlantic, Ascention Is., St Helena",
		"(UTC - 1:00) Azores, Cape Verde Islands",
		"(UTC) Casablanca, Dublin, Edinburgh, London, Lisbon, Monrovia",
		"(UTC + 1:00) Berlin, Brussels, Copenhagen, Madrid, Paris, Rome",
		"(UTC + 2:00) Kaliningrad, South Africa, Warsaw",
		"(UTC + 3:00) Baghdad, Riyadh, Moscow, Nairobi",
		"(UTC + 3:30) Tehran",
		"(UTC + 4:00) Adu Dhabi, Baku, Muscat, Tbilisi",
		"(UTC + 4:30) Kabul",
		"(UTC + 5:00) Islamabad, Karachi, Tashkent",
		"(UTC + 5:30) Bombay, Calcutta, Madras, New Delhi",
		"(UTC + 6:00) Almaty, Colomba, Dhaka",
		"(UTC + 7:00) Bangkok, Hanoi, Jakarta",
		"(UTC + 8:00) Beijing, Hong Kong, Perth, Singapore, Taipei",
		"(UTC + 9:00) Osaka, Sapporo, Seoul, Tokyo, Yakutsk",
		"(UTC + 9:30) Adelaide, Darwin",
		"(UTC + 10:00) Melbourne, Papua New Guinea, Sydney, Vladivostok",
		"(UTC + 11:00) Magadan, New Caledonia, Solomon Islands",
		"(UTC + 12:00) Auckland, Wellington, Fiji, Marshall Island",
		};
	
	public static final String[] TIME_ZONE_KEYS = 
		{
		"UM12",
		"UM11",
		"UM10",
		"UM9",
		"UM8",
		"UM7",
		"UM6",
		"UM5",
		"UM4",
		"UM25",
		"UM3",
		"UM2",
		"UM1",
		"UTC",
		"UP1",
		"UP2",
		"UP3",
		"UP25",
		"UP4",
		"UP35",
		"UP5",
		"UP45",
		"UP6",
		"UP7",
		"UP8",
		"UP9",
		"UP85",
		"UP10",
		"UP11",
		"UP12",
		};
	
	//Push Constants
	
	public static final int PUSH_PORT = 29818;
	public static final String PUSH_APP_ID = "1898-23o05eehee192rys68";
	public static final String URL_PUSH_REGISTER = "http://pushapi.eval.blackberry.com/mss/PD_subReg?serviceid=" + PUSH_APP_ID + "&model=" + DeviceInfo.getDeviceName();
	public static final String URL_PUSH_UNREGISTER = "http://pushapi.eval.blackberry.com/mss/PD_subDereg?serviceid=" + PUSH_APP_ID + "&model=" + DeviceInfo.getDeviceName();
}
