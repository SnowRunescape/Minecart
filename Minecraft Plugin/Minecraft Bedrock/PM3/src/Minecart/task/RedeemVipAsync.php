<?php

namespace Minecart\task;

use pocketmine\command\ConsoleCommandSender;
use pocketmine\scheduler\AsyncTask;
use pocketmine\Server;

use Minecart\utils\Form;
use Minecart\utils\API;
use Minecart\Minecart;
use Minecart\utils\Errors;
use Minecart\utils\Messages;

class RedeemVipAsync  extends AsyncTask {
    private $username;
    private $key;
    private $authorization;
    private $shopServer;

    public function __construct(string $username, string $key, string $authorization, string $shopServer)
    {
        $this->username = $username;
        $this->key = $key;
        $this->authorization = $authorization;
        $this->shopServer = $shopServer;
    }

    public function onRun() : void
    {
        $api = new API();
        $api->setAuthorization($this->authorization);
        $api->setShopServer($this->shopServer);
        $api->setParams(['username' => $this->username, 'key' => $this->key]);
        $api->setURL(API::REDEEMVIP_URI);

        $this->setResult($api->send());
    }

    public function onCompletion(Server $server) : void
    {
        $player = $server->getPlayerExact($this->username);
        $response = $this->getResult();

        if(!empty($response)){
            $statusCode = $response['statusCode'];
            if($statusCode == 200) {
                $response = $response['response'];
                $group = $response['group'];
                $duration = $response['duration'];
                $key = $response['key'];

                $command = Minecart::getInstance()->getCfg('cmd.cmd_active_vip');
                $command = str_replace(['{player}', '{group}', '{duration}'], [$player->getName(), $group, $duration], $command);

                if(Minecart::getInstance()->getServer()->dispatchCommand(new ConsoleCommandSender(), $command)) {
                    $messages = new Messages();
                    $messages->sendGlobalInfo($player, 'vip', $group);

                    $message = Minecart::getInstance()->getMessage('success.active-key');
                    $message = str_replace(['{player}', '{group}', '{duration}'], [$player->getName(), $group, $duration], $message);
                    $player->sendMessage($message);
                }else{
                    $error = Minecart::getInstance()->getMessage('error.redeem-vip');
                    $error = str_replace(['{group}', '{duration}'], [$group, $duration], $error);

                    $player->sendMessage($error);
                }
            }else{
                $form = new Form();
                $form->setTitle('Resgatar VIP');
                $form->setPlaceholder('Insira sua key');
                $form->setRedeemType(Form::REDEEM_VIP);
                $form->setKey($this->key);

                $errors = new Errors();
                $error = $errors->getError($player, $response['response']['code'] ?? $statusCode, 'vip', true);
                $form->showRedeem($player, $error);
            }
        }else{
            $player->sendMessage(Minecart::getInstance()->getMessage('error.internal-error'));
        }
    }
}