<?php

namespace Minecart\utils;

use Minecart\Minecart;

class Configuration {
    public static function getConfig(string $key) : string
    {
        return Utils::getArrayKeyByString(Minecart::getInstance()->config, $key);
    }
}