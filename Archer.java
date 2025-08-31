import java.util.List;
import java.util.Scanner;

public class Archer extends Character {

    // Construtor para criar um novo Arqueiro com um nome.
    public Archer(String name) {
        super(name, 80, 20, 6); // Define os atributos base: HP, ataque, defesa
    }

    /**
     * Define a ação do Arqueiro durante seu turno de batalha.
     * A ação padrão é simplesmente atacar um inimigo.
     */
    @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        System.out.println(this.name + " readies their arrow...");
        // Pede para o jogador escolher um alvo
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target:");
        // Se um alvo válido for escolhido, realiza o ataque básico
        if (target != null) {
            basicAttack(target);
        }
    }
}