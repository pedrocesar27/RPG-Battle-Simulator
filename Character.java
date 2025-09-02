import java.util.*;

public abstract class Character {
    
    // Atributos comuns a todos os personagens
    protected String name;      // Nome do personagem
    protected int maxHp;        // Vida máxima
    protected int hp;           // Vida atual
    protected int attack;       // Pontos de ataque
    protected int defense;      // Pontos de defesa
    protected Random rng = new Random(); // Gerador de números aleatórios para variações

    // Atributos para o sistema de Nível e Experiência
    protected int level = 1;
    protected int experience = 0;
    protected int xpValue = 25; // XP que este personagem concede ao ser derrotado

    // Atributos para efeitos de status (Buffs/Debuffs)
    // A String é o nome do efeito ("Poison", "Stun"), o Integer é a duração em turnos.
    protected Map<String, Integer> statusEffects = new HashMap<>();

    // Atributos para tempo de recarga de habilidades (Cooldown)
    // A String é o nome da habilidade ("GroupHeal"), o Integer é a contagem de turnos.
    protected Map<String, Integer> cooldowns = new HashMap<>();

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

    /**
     * Adiciona experiência ao personagem e verifica se ele subiu de nível
     * @param xpGained A quantidade de experiência ganha
     */
    public void addExperience(int xpGained) {
        this.experience += xpGained;
        System.out.println(this.name + " gained " + xpGained + " XP!");
        // A cada 100 de XP, o personagem sobe de nível
        if (this.experience >= 100) {
            this.experience -= 100;
            levelUp();
        }
    }

    /**
     * Aumenta o nível e os atributos do personagem.
     */
    private void levelUp() {
        this.level++;
        // Aumenta os atributos base a cada nível
        this.maxHp += 10;
        this.attack += 2;
        this.defense += 1;
        this.hp = this.maxHp; // Cura totalmente ao subir de nível
        System.out.println(this.name + " has reached Level " + this.level + "! Stats increased!");
    }

    /**
     * Aplica um efeito de status a este personagem.
     * @param effect O nome do efeito (ex: "Poison").
     * @param duration A duração em número de turnos.
     */
    public void applyStatusEffect(String effect, int duration) {
        statusEffects.put(effect, duration);
        System.out.println(this.name + " is now affected by " + effect + "!");
    }

    /**
     * Processa todos os efeitos de status ativos no início do turno do personagem.
     * @return true se o personagem puder agir, false se estiver atordoado (Stun).
     */
    public boolean processStatusEffects() {
        // Usa um novo mapa para evitar problemas ao modificar o mapa durante a iteração
        Map<String, Integer> nextEffects = new HashMap<>();
        boolean canAct = true;

        for (Map.Entry<String, Integer> entry : statusEffects.entrySet()) {
            String effect = entry.getKey();
            int duration = entry.getValue();

            switch (effect) {
                case "Poison":
                    // NOVA LÓGICA: Dano de veneno agora é 30% da vida máxima.
                    int poisonDmg = (int) (this.maxHp * 0.30); 
                    this.hp = Math.max(0, this.hp - poisonDmg); // Garante que a vida não fique negativa
                    System.out.println(this.name + " takes " + poisonDmg + " damage from Poison!");
                    break;
                case "Stun":
                    System.out.println(this.name + " is Stunned and cannot act!");
                    canAct = false;
                    break;
            }

            if (duration - 1 > 0) {
                nextEffects.put(effect, duration - 1);
            } else {
                System.out.println(effect + " has worn off from " + this.name + ".");
            }
        }
        this.statusEffects = nextEffects; // Atualiza o mapa de efeitos
        return canAct;
    }

    /**
     * Coloca uma habilidade em tempo de recarga.
     * @param abilityName O nome da habilidade.
     * @param duration A duração do cooldown em turnos.
     */
    public void setCooldown(String abilityName, int duration) {
        cooldowns.put(abilityName, duration);
    }
    
    /**
     * Verifica se uma habilidade está em tempo de recarga.
     * @param abilityName O nome da habilidade.
     * @return true se estiver em cooldown, false caso contrário.
     */
    public boolean isOnCooldown(String abilityName) {
        return cooldowns.getOrDefault(abilityName, 0) > 0;
    }

    /**
     * Reduz a contagem de todos os cooldowns ativos em 1.
     * Deve ser chamado no início do turno do personagem.
     */
    public void tickCooldowns() {
        for (String key : new ArrayList<>(cooldowns.keySet())) {
            int timeLeft = cooldowns.get(key);
            if (timeLeft - 1 <= 0) {
                cooldowns.remove(key);
            } else {
                cooldowns.put(key, timeLeft - 1);
            }
        }
    }

    public int getXpValue() {
        return xpValue;
    }

    public void revive() {
        if (!isAlive()) {
            this.hp = 25; // Revive com 1 de HP
            System.out.println(this.name + " has been revived!");
        }
    }
}