package dungeonmania.MovingEntities;

public interface Enemy {
    public int getDamage();
    public double getHealth();
    public String getId();
    public void setHealth(double d);
    public String getType();
}
