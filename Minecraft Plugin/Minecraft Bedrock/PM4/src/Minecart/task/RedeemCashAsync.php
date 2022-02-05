<?php

namespace Minecart\task;

use pocketmine\console\ConsoleCommandSender;
use pocketmine\scheduler\AsyncTask;
use pocketmine\Server;

use pocketmine\lang\Language;

use Minecart\utils\Form;
use Minecart\utils\API;
use Minecart\Minecart;
use Minecart\utils\Errors;
use Minecart\utils\Messages;

class RedeemCashAsync extends AsyncTask {
    private $username;
    private $authorization;
    private $shopServer;

    public function __construct(string $username, string $authorization, string $shopServer)
    {
        $this->username = $username;
        $this->authorization = $authorization;
        $this->shopServer = $shopServer;
    }

    public function onRun() : void
    {
        $api = new API();
        $api->setAuthorization($this->authorization);
        $api->setShopServer($this->shopServer);
        $api->setParams(['username' => $this->username]);
        $api->setURL(API::REDEEMCASH_URI);

        $this->setResult($api->send());
    }

    public function onCompletion() : void
    {
        $player = Minecart::getInstance()->getServer()->getPlayerExact($this->username);
        $response = $this->getResult();

        if(!empty($response)){
            $statusCode = $response['statusCode'];
            if($statusCode == 200) {
                $response = $response['response'];

                $cash = $response['cash'];

                $command = Minecart::getInstance()->getCfg('cmd.cmd_active_cash');
                $command = str_replace(['{player}', '{cash}'], [$player->getName(), $cash], $command);

                if(Minecart::getInstance()->getServer()->dispatchCommand(new ConsoleCommandSender(Minecart::getInstance()->getServer(), new Language('eng')), $command)) {
                    $messages = new Messages();
                    $messages->sendGlobalInfo($player, 'cash', $cash);
                }else{
                    $error = Minecart::getInstance()->getMessage('error.redeem-cash');
                    $error = str_replace('{cash}', $cash, $error);

                    $player->sendMessage($error);
                }
            }else{
                $form = new Form();
                $form->setTitle('Erro!');

                $errors = new Errors();
                $error = $errors->getError($player, $response['response']['code'] ?? $statusCode, 'cash', true);

                $form->setMessage($error);
                $form->showFormError($player);
            }
        }else{
            $player->sendMessage(Minecart::getInstance()->getMessage('error.internal-error'));
        }
    }
}