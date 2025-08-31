import java.util.List;
import java.util.Scanner;

public class Healer extends Character {

    // Construtor para criar um novo Curandeiro com um nome
    public Healer(String name) {
        super(name, 75, 12, 7); // Define os atributos base: HP, ataque, defesa
    }

    /**
     * Define a ação do Curandeiro durante seu turno.
     * Ele pode escolher entre atacar um inimigo ou curar todos os aliados.
     */
    @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        // Lógica para escolher a ação
        if (isHuman) {
            System.out.println("Action for " + this.name + ": [1] Attack  [2] Group Heal");
            String choice = in.nextLine().trim();
            if (choice.equals("2")) {
                groupHeal(allies);
                return; // Encerra o turno após a cura
            }
        } else { 
            // Verifica se algum aliado precisa de cura
            for (Character ally : allies) {
                // Se encontrar um aliado com menos da metade da vida, cura o grupo
                if (ally.isAlive() && ally.hp < ally.maxHp / 2) {
                    groupHeal(allies);
                    return; // Encerra o turno após a cura
                }
            }
        }
        // Ação padrão (se não curou): atacar um inimigo
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target to attack:");
        if (target != null) {
            basicAttack(target);
        }
    }

    //Habilidade especial do Curandeiro: cura um pouco de vida de todos os aliados vivos.
    private void groupHeal(List<Character> allies) {
        System.out.printf("%s casts Group Heal! ✨%n", this.name);
        for (Character ally : allies) {
            if (ally.isAlive()) {
                ally.heal(10 + rng.nextInt(6)); // Cura de 10 a 15 de HP
            }
        }
    }
}