import java.util.List;
import java.util.Scanner;

public class Assassin extends Character {

    // Construtor para criar um novo Assassino com um nome
    public Assassin(String name) {
        super(name, 55, 35, 5); // Define os atributos base: HP baixo, ataque alto, defesa baixa
    }
    
    /**
     * Define a ação do Assassino durante seu turno de batalha
     * Sua única ação é um ataque focado em um único alvo
     */
    @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        System.out.println(this.name + " vanishes into the shadows to strike...");
        // Escolhe um alvo inimigo para atacar
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target:");
        // Se um alvo válido foi escolhido, realiza o ataque
        if (target != null) {
            basicAttack(target);
        }
    }
}