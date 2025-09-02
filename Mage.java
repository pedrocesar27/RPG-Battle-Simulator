import java.util.List;
import java.util.Scanner;

public class Mage extends Character {
    private final String BARRIER_ABILITY = "Magic Barrier";
    // Atributo para controlar se a barreira mágica está ativa
    private boolean barrierActive = false;

    // Construtor para criar um novo Mago com um nome
    public Mage(String name) {
        super(name, 80, 28, 5); // Define os atributos base: HP, ataque, defesa
    }

    //Define o estado da barreira. Usado pela classe Character para desativá-la. para active O novo estado da barreira.
    public void setBarrier(boolean active) { this.barrierActive = active; }

    //  Verifica se a barreira está ativa. Retorna true se a barreira estiver ativa, false caso contrário.
    public boolean isBarrierActive() { return this.barrierActive; }

    /**
     * Define a ação do Mago durante seu turno.
     * Ele pode escolher entre atacar um inimigo ou criar uma barreira defensiva.
     */
    @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        tickCooldowns(); // Reduz os cooldowns no início do turno

        if (isHuman) {
            System.out.print("Action for " + this.name + ": [1] Attack");
            // Mostra a opção da barreira apenas se não estiver em cooldown
            if (!isOnCooldown(BARRIER_ABILITY)) {
                System.out.print("  [2] Create Magic Barrier");
            } else {
                 System.out.print("  (Magic Barrier is on cooldown for " + cooldowns.get(BARRIER_ABILITY) + " more turns)");
            }
            System.out.println();

            String choice = in.nextLine().trim();
            if (choice.equals("2") && !isOnCooldown(BARRIER_ABILITY)) {
                System.out.println(this.name + " conjures a shimmering magic barrier! 🛡️");
                this.barrierActive = true;
                setCooldown(BARRIER_ABILITY, 3); // Coloca a barreira em cooldown por 3 turnos
                return;
            }
        } else { // IA
            if (!isOnCooldown(BARRIER_ABILITY) && this.hp < this.maxHp / 2 && rng.nextDouble() < 0.6) {
                System.out.println(this.name + " conjures a shimmering magic barrier! 🛡️");
                this.barrierActive = true;
                setCooldown(BARRIER_ABILITY, 3);
                return;
            }
        }
        
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target:");
        if (target != null) {
            basicAttack(target);
        }
    }
}