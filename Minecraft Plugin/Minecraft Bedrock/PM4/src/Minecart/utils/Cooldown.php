<?php

namespace Minecart\utils;

use pocketmine\player\Player;

use Minecart\Minecart;

class Cooldown{
    private $utils;
    private $cooldownTime = 5;

    public function __construct()
    {
        $this->utils = new Utils();
    }
    public function isInCooldown(Player $player) : bool
    {
        $playerName = strtolower($player->getName());

        if(!empty(Minecart::getInstance()->cooldown[$playerName]))
            //5 seconds cooldown
            return (time() - Minecart::getInstance()->cooldown[$playerName]) <= $this->cooldownTime;

        return false;
    }
    
    public function setPlayerInCooldown(Player $player) : void
    {
        $playerName = strtolower($player->getName());
        Minecart::getInstance()->cooldown[$playerName] = time();
    }

    public function removePlayerCooldown(Player $player) : void
    {
        $playerName = strtolower($player->getName());
        if(!empty(Minecart::getInstance()->cooldown[$playerName])){
            unset(Minecart::getInstance()->cooldown[$playerName]);
        }
    }

    public function getCooldownTime(Player $player) : int
    {
        $playerName = strtolower($player->getName());

        if(!empty(Minecart::getInstance()->cooldown[$playerName]))
            //5 seconds cooldown
            return $this->cooldownTime - (time() - Minecart::getInstance()->cooldown[$playerName]);

        return 0;
    }
}