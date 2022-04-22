<?php

class Minecart
{
    const BASE_URL = "https://api.minecart.com.br";

    private static $shopToken;

    /**
     * Metodo responsavel por setar o ShopToken
     * @param String $shopToken
     */
    public static function setShopToken($shopToken){
        self::$shopToken = $shopToken;
    }

    /**
     * Metodo responsavel por gerar um link de pagamento
     * @param String $username
     * @param String $coupon
     * @param Array $cart
     * @param String $gateway
     * @return Array
     */
    public static function generateLinkPayment($username, $coupon, $cart, $gateway)
    {
        return self::curlRequest(
            "/shop/payment/createLinkPayment",
            "POST",
            http_build_query([
                "username" => $username,
                "cart" => $cart,
                "gateway" => $gateway
            ])
        );
    }

    /**
     * Metodo responsavel por processar todos os cURL
     * @param String $url
     * @param String $customRequest
     * @param StdClass $postFields
     * @param Boolean $contentType
     * @return Array
     */
    private static function curlRequest($url, $customRequest, $postFields = null, $contentType = false)
    {
        $httpHeader = [
            "Authorization: " . self::$shopToken
        ];

        if ($contentType) {
            $httpHeader[] = "Content-Type: application/json";
        }

        $curl = curl_init();

        curl_setopt_array($curl, [
            CURLOPT_URL            => self::BASE_URL . $url,
            CURLOPT_HTTPHEADER     => $httpHeader,
            CURLOPT_CUSTOMREQUEST  => $customRequest,
            CURLOPT_POSTFIELDS     => $postFields,
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_FOLLOWLOCATION => 1,
            CURLOPT_VERBOSE        => 0,
            CURLOPT_SSL_VERIFYPEER => 0
        ]);

        $curlResponse = curl_exec($curl);

        curl_close($curl);

        return json_decode($curlResponse, true);
    }
}
