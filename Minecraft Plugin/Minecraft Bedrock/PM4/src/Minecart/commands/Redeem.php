<?php

namespace Minecart\commands;

use pocketmine\command\Command;
use pocketmine\command\CommandSender;
use pocketmine\player\Player;

use Minecart\utils\Form;
use Minecart\utils\Messages;
use MineCart\Minecart;

class Redeem  extends Command {
    public function __construct()
    {
        parent::__construct('redeem', 'Resgatar Keys');
        $this->setAliases(['resgatar']);
    }

    public function execute(CommandSender $sender, string $label, array $args) : bool
    {
        if(!$sender instanceof Player){
            $sender->sendMessage(Minecart::getInstance()->getMessage('error.player-only'));
            return false;
        }

        $messages = new Messages();
        $messages->sendWaitingResponseInfo($sender);

        $form = new Form();
        $form->showChoose($sender);
        return true;
    }
}