import java.util.*;

public class Character {
    
    //Atributos de cada personagem
    protected String name;
    protected int level;
    protected int maxHp;
    protected int hp;
    protected int attack;
    protected int defense;

    protected Random rng = new Random(); // Random

    // Metodo Construtor
    public Character(String name, int level, int maxHp, int hp, int attack, int defense) {
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    // Metodo get name e booleano para checagem de vida do personagem
    public String getName() { return name; }
    public boolean isAlive() { return hp > 0; }

    // Metodo de ataque do personagem (o dano eh contado pelo dano do ataque menos a defesa do personagem atacado)
    public void basicAttack(Character target) {
        if (!target.isAlive()) return;

        // O arqueiro somente pode ser acertado por outro arqueiro
        if(target instanceof Archer && !(this instanceof Archer)) {
            System.out.printf("%s tries to hit %s, but it has no effect!%n", name, target.name);
        }

        int variance = rng.nextInt(5); // Variancia aleatoria para cada ataque, para nao serem todos iguais
        int raw = attack + variance;
        int dmg = Math.max(1, raw - target.defense); // O dano real eh a diferenca dos pontos de ataque + variancia, com os pontos de defesa do oponente 

        // Arqueiros dao dano minimo em personagens nao arqueiros
        if(this instanceof Archer && !(target instanceof Archer)) {
            dmg = 1; // Minimo de dano possivel
        }

        target.takeDamage(dmg);
        System.out.printf("%s hits %s for %d damage.%n", name, target.name, dmg);
    }

    // Metodo para receber dano
    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    public void heal(int amount) {
        if(amount <= 0) return;
        int before = hp;
        hp = Math.min(maxHp, hp + amount);
        System.out.printf("%s heals %d HP (from %d to %d).%n", name, (hp - before), before, hp);
    }

    public String status() {
        return String.format("%s [%s] HP:%d/%d", name, getClass().getSimpleName(), hp, maxHp);
    }
}
