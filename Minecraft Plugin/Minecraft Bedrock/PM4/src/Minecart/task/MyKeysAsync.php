<?php

namespace Minecart\task;

use Minecart\utils\Form;
use pocketmine\scheduler\AsyncTask;

use Minecart\utils\API;
use Minecart\utils\Errors;
use Minecart\Minecart;

class MyKeysAsync extends AsyncTask {
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
        $api->setURL(API::MYKEYS_URI);

        $this->setResult($api->send());
    }

    public function onCompletion() : void
    {
        $player = Minecart::getInstance()->getServer()->getPlayerExact($this->username);
        $response = $this->getResult();

        if(!empty($response)){
            $statusCode = $response['statusCode'];
            if($statusCode == 200) {
                $keys = $response['response']['products'];

                $form = new Form();
                $form->setProducts($keys);
                $form->showMyKeys($player);
            }else{
                $form = new Form();
                $form->setTitle('Erro!');

                $errors = new Errors();
                $error = $errors->getError($player, $response['response']['code'] ?? $statusCode, 'vip', true);

                $form->setMessage($error);
                $form->showFormError($player);
            }
        }else{
            $player->sendMessage(Minecart::getInstance()->getMessage('error.internal-error'));
        }
    }
}