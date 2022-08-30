package dungeonmania.MovingEntities;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.MovementHandler;
import dungeonmania.Player;
import dungeonmania.Settings;
import dungeonmania.MovingEntities.MovementStrategies.DefaultMercenaryMovement;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {

    public Assassin(String id, Position position) {
        super(id, position, Config.getSetting(Settings.assassin_health), "assassin", Config.getSetting(Settings.assassin_bribe_amount));
    }

    @Override
    public int getDamage() {
        return Config.getSetting(Settings.assassin_attack);
    }


    @Override
    public Direction move(Dungeon dungeon) {
        Player player = dungeon.getPlayer();
        if (player.getInvisible()) {
            double distance = MovementHandler.getEuclidianDistance(dungeon.getPlayerPos(), getPosition());
            int intDistance = (int) Math.round(distance);
            if (intDistance <= Config.getSetting(Settings.assassin_recon_radius)) {
                Move new_strat = new DefaultMercenaryMovement();
                return new_strat.move(dungeon, this.getPosition());
            }
        }
        Move strategy = getMoveStrategy();
        return strategy.move(dungeon, this.getPosition());
    }


}
