import java.util.List;
import java.util.Scanner;

public class Warrior extends Character {

    // Construtor para criar um novo Guerreiro com um nome
    public Warrior(String name) {
        super(name, 120, 20, 8); // Define os atributos base: HP alto, ataque e defesa moderados
    }

    /**
     * Define a ação do Guerreiro durante seu turno de batalha.
     * Sua única ação é um ataque direto contra um inimigo.
     */
    @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        System.out.println(this.name + " charges forward with fury!");
        // Pede para o jogador escolher um alvo inimigo
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target:");
        // Se um alvo válido for escolhido, realiza o ataque básico
        if (target != null) {
            basicAttack(target);
        }
    }
}