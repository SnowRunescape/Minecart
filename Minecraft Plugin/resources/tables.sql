SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

CREATE TABLE IF NOT EXISTS `minecart` (
  `key_vip` varchar(10) NOT NULL,
  `vip_group` varchar(16) NOT NULL,
  `vip_duration` int(5) NOT NULL,
  `owner` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
