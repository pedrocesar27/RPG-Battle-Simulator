import java.util.List;
import java.util.Scanner;

public class Assassin extends Character {

    // Construtor para criar um novo Assassino com um nome
    public Assassin(String name) {
        super(name, 55, 35, 5); // Define os atributos base: HP baixo, ataque alto, defesa baixa
    }
    
    /**
     * O ataque básico do Assassino agora tem 15% de chance de envenenar o alvo.
     */
    @Override
    public void basicAttack(Character target) {
        super.basicAttack(target); // Executa o ataque básico da classe mãe

        // Após o ataque, verifica se o alvo ainda está vivo para aplicar o veneno
        if (target.isAlive()) {
            // 15% de chance de aplicar o status "Poison"
            if (rng.nextDouble() < 0.15) { 
                System.out.println(this.name + "'s attack was venomous!");
                // Aplica o veneno por 3 turnos
                target.applyStatusEffect("Poison", 3); 
            }
        }
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