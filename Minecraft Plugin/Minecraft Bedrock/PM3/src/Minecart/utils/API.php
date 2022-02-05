<?php

namespace Minecart\utils;

class API {
    private $authorization;
    private $shopServer;

    const URI = 'https://minecart.com.br/api';
    const MYKEYS_URI = self::URI . '/shop/player/mykeys';
    const REDEEMVIP_URI = self::URI . '/shop/player/redeemvip';
    const REDEEMCASH_URI = self::URI . '/shop/player/redeemcash';

    private $url;
    private $params;

    public function setParams(array $params): void
    {
        $this->params = $params;
    }

    public function setURL(string $url): void
    {
        $this->url = $url;
    }

    public function setAuthorization(string $authorization): void
    {
        $this->authorization = $authorization;
    }

    public function setShopServer(string $shopServer): void
    {
        $this->shopServer = $shopServer;
    }

    public function send() : array
    {
    	try{
			$curl = curl_init();
			curl_setopt($curl, CURLOPT_URL, $this->url);
			curl_setopt($curl, CURLOPT_POST, true);
			curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

			$authorization = $this->authorization;
			$shopServer    = $this->shopServer;
			$headers = [
				"Authorization: ".$authorization,
				"ShopServer: ".$shopServer,
				"Content-Type: application/x-www-form-urlencoded"
			];

			curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

			curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($this->params));

			curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false);
			curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
			$response = json_decode(curl_exec($curl), true);

			$response = [
				'statusCode' => curl_getinfo($curl)['http_code'],
				'response' => $response
			];

			if(empty($response['response'])){
				$response['statusCode'] = 500;
			}

			curl_close ($curl);
		}catch(\Exception $exception){
			$response = [
				'statusCode' => 500,
				'response' => [
					'code' => 329832
				]
			];
		}

        return $response;
    }
}