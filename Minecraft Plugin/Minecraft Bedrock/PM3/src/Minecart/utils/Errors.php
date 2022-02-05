<?php

namespace Minecart\utils;

use pocketmine\Player;

use Minecart\Minecart;

class Errors {
    public function getError(Player $player, $code, $type, $return = false) : string
    {
        switch ($code) {
            case 40010:
                $message = Minecart::getInstance()->getMessage($type == 'vip' ? 'error.invalid-key' : 'error.nothing-products-cash');
                break;
            case 40011:
                $message = Minecart::getInstance()->getMessage('error.invalid-shopserver');
                break;
            case 401:
                $message = Minecart::getInstance()->getMessage('error.invalid-shopkey');
                break;
            default:
                $message = Minecart::getInstance()->getMessage('error.internal-error');
                break;
        }

        if(!$return){
            $player->sendMessage($message);
        }

        return $message;
    }
}