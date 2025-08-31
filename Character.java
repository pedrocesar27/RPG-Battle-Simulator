import java.util.*;

public abstract class Character {
    
    // Atributos comuns a todos os personagens
    protected String name;      // Nome do personagem
    protected int maxHp;        // Vida máxima
    protected int hp;           // Vida atual
    protected int attack;       // Pontos de ataque
    protected int defense;      // Pontos de defesa
    protected Random rng = new Random(); // Gerador de números aleatórios para variações

    /**
     * Construtor para criar um novo personagem com seus atributos iniciais
     */
    public Character(String name, int maxHp, int attack, int defense) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp; // Vida inicial é sempre a vida máxima
        this.attack = attack;
        this.defense = defense;
    }
    
    /**
     * Realiza um ataque básico contra um personagem alvo.
     * Inclui regras especiais para a classe Archer
     */
    public void basicAttack(Character target) {
        if (!target.isAlive()) return; // Não ataca alvos que já foram derrotados
        
        // Regra especial: Arqueiro só pode ser ferido por outro Arqueiro
        if (target instanceof Archer && !(this instanceof Archer)) {
            System.out.printf("%s tries to hit %s, but it has no effect!%n", this.name, target.name);
            return;
        }

        int raw = this.attack + rng.nextInt(5); // Adiciona uma pequena variação ao dano
        int dmg = Math.max(1, raw - target.defense); // Dano é o ataque menos a defesa do alvo

        // Regra especial: Arqueiro causa pouco dano em não-arqueiros
        if (this instanceof Archer && !(target instanceof Archer)) {
            dmg = 1;
        }
        
        System.out.printf("%s attacks %s for %d damage.%n", this.name, target.name, dmg);
        target.takeDamage(dmg);
    }
    
    /**
     * Aplica uma quantidade de dano ao personagem, reduzindo sua vida
     * Inclui uma verificação para a barreira do Mago
     */
    public void takeDamage(int dmg) {
        // Verifica se o personagem que está sofrendo dano é um Mago com barreira
        if (this instanceof Mage) {
            Mage targetMage = (Mage) this;
            if (targetMage.isBarrierActive()) {
                System.out.printf("The attack against %s is completely absorbed by the magic barrier!%n", this.name);
                targetMage.setBarrier(false); // A barreira é consumida
                return; // O dano é ignorado
            }
        }
        this.hp = Math.max(0, this.hp - dmg); // Garante que a vida não fique negativa
    }

    /**
     * Cura o personagem, restaurando uma quantidade de vida
     */
    public void heal(int amount) {
        if (amount <= 0) return;
        int before = this.hp;
        // Garante que a vida não ultrapasse o máximo
        this.hp = Math.min(this.maxHp, this.hp + amount);
        System.out.printf("%s heals for %d HP (from %d to %d).%n", this.name, (this.hp - before), before, this.hp);
    }
    
    /**
     * Método abstrato que define a ação de um personagem no seu turno
     * TODAS as subclasses (Warrior, Mage, etc.) são OBRIGADAS a implementar este método
     */
    public abstract void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman);
    
    /**
     * Verifica se o personagem ainda está vivo
     * Retorna true se a vida for maior que zero
     */
    public boolean isAlive() { return hp > 0; }

    /**
     * Retorna uma string formatada com o status atual do personagem
     */
    public String status() {
        return String.format("%s [%s] HP:%d/%d", name, getClass().getSimpleName(), hp, maxHp);
    }
}