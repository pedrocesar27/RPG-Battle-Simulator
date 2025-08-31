import java.util.List;
import java.util.Scanner;

public class Mage extends Character {

    // Atributo para controlar se a barreira mágica está ativa
    private boolean barrierActive = false;

    // Construtor para criar um novo Mago com um nome
    public Mage(String name) {
        super(name, 80, 25, 5); // Define os atributos base: HP, ataque, defesa
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
        // Lógica para escolher a ação
        if (isHuman) {
            System.out.println("Action for " + this.name + ": [1] Attack  [2] Create Magic Barrier");
            String choice = in.nextLine().trim();
            if (choice.equals("2")) {
                System.out.println(this.name + " conjures a shimmering magic barrier!");
                this.barrierActive = true;
                return; // Encerra o turno após criar a barreira
            }
        } else { 
            // Se estiver com pouca vida, tem uma chance (60%) de criar a barreira
            if (this.hp < this.maxHp / 2 && rng.nextDouble() < 0.6) {
                System.out.println(this.name + " conjures a shimmering magic barrier!");
                this.barrierActive = true;
                return; // Encerra o turno
            }
        }
        // Ação padrão (se não usou a barreira): atacar um inimigo
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target:");
        if (target != null) {
            basicAttack(target);
        }
    }
}