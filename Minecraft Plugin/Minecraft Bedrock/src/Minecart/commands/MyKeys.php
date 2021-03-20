<?php

namespace Minecart\commands;

use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\Player;


use Minecart\task\MyKeysAsync;
use Minecart\utils\Cooldown;
use Minecart\utils\Messages;
use Minecart\Minecart;

class MyKeys extends Command
{
    public function __construct()
    {
        parent::__construct('mykeys', 'Visualizar keys');
        $this->setAliases(['minhaskeys']);
    }

	public function execute(CommandSender $sender, string $label, array $args) : bool
	{
		if(!$sender instanceof Player){
		    $sender->sendMessage(Minecart::getInstance()->getMessage('error.player-only'));
            return false;
        }

		$cooldown = new Cooldown();
        if($cooldown->isInCooldown($sender)){
            $error = Minecart::getInstance()->getMessage('error.cooldown');
            $error = str_replace('{cooldown}', $cooldown->getCooldownTime($sender), $error);

            $sender->sendMessage($error);
            return false;
        }

		$playerName = $sender->getName();
		$authorization = Minecart::getInstance()->getCfg('Minecart.ShopKey');
		$shopServer = Minecart::getInstance()->getCfg('Minecart.ShopServer');
        Minecart::getInstance()->getServer()->getAsyncPool()->submitTask(new MyKeysAsync($playerName, $authorization, $shopServer));

        $messages = new Messages();
        $messages->sendWaitingResponseInfo($sender);
		return true;
	}
}