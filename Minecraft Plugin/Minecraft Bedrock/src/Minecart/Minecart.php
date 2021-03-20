<?php

namespace Minecart;

use Minecart\commands\Redeem;
use pocketmine\plugin\PluginBase;
use pocketmine\utils\Config;

use Minecart\commands\MyKeys;
use Minecart\utils\Configuration;
use Minecart\utils\Messages;

class Minecart extends PluginBase
{
    public $messages = [];
    public $config  = [];

    public $cooldown = [];

	public static $instance;

    public function onEnable() : void
	{
		$this->registerInstance();
		$this->registerCommands();
		$this->registerConfig();
		$this->registerMessages();

		$this->getServer()->getLogger()->info("§7Plugin §aMinecart§7 ativado com sucesso!");
	}

	public function registerCommands() : void
	{
		$this->getServer()->getCommandMap()->register('mykeys', new MyKeys());
        $this->getServer()->getCommandMap()->register('redeem', new Redeem());
	}

	public function registerInstance() : void
	{
		self::$instance = $this;
	}

	public static function getInstance() : Minecart
	{
		return self::$instance;
	}

    public function registerMessages() : void
    {
        $this->saveResource("messages.yml");
        $messages = new Config($this->getDataFolder() . "messages.yml", Config::YAML);
        $this->messages = $messages->getAll();
	}

    public function registerConfig() : void
    {
        $this->saveResource("config.yml");
        $config = new Config($this->getDataFolder() . "config.yml", Config::YAML);
        $this->config = $config->getAll();
	}

    public function getMessage(string $key) : string
    {
        return Messages::getMessage($key);
    }

    public function getCfg(string $key) : string
    {
        return Configuration::getConfig($key);
    }
}