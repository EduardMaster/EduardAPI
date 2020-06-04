package net.eduard.api.server.currency.list;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.game.ItemBuilder;
import net.eduard.api.server.currency.SimpleCurrencyHandler;
import org.bukkit.Material;

public  class CurrencyJHCash extends SimpleCurrencyHandler {
    public CurrencyJHCash() {
        setName("JHCash");
        setDisplayName("Sistema de Cash");
        setSymbol("$");
        setIcon( new ItemBuilder(Material.DIAMOND_BLOCK).name("§aCash"));

    }

    @Override
        public double get(FakePlayer player) {

            return JH_Shop.Main.getAPI().getPontos(player.getName());
        }

        @Override
        public boolean remove(FakePlayer player, double amount) {
            JH_Shop.Main.getAPI().removePontos(player.getName(), amount);
            return true;
        }

        @Override
        public boolean check(FakePlayer player, double amount) {
            return JH_Shop.Main.getAPI().getPontos(player.getName()) >= amount;

        }

        @Override
        public boolean add(FakePlayer player, double amount) {
            JH_Shop.Main.getAPI().addPontos(player.getName(), amount);
            return true;
        }

}
