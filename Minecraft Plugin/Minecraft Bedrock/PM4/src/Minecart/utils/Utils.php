<?php

namespace Minecart\utils;

class Utils {
    public static function getArrayKeyByString(array $arr, string $key): string
    {
        if (strpos($key, ".") != false) {
            $ex = explode(".", $key);
            if (!isset($arr[$ex[0]])) return "";
            $result = $arr[$ex[0]];
            for ($i = 1; $i < count($ex); $i++) {
                if (!isset($result[$ex[$i]])) return "";
                $result = $result[$ex[$i]];
            }
            $result = str_replace("\n", PHP_EOL, $result);
            $result = str_replace("&", "§", $result);
            return $result;
        } else {
            return $arr[$key] ?? "§7Mensagem §c{$key} §7não encontrada";
        }
    }

    public static function lowerCase($string) : string
    {
        return strtolower($string);
    }
}