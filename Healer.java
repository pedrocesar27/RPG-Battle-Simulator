import java.util.List;
import java.util.Scanner;

public class Healer extends Character {
     private final String GROUP_HEAL_ABILITY = "Group Heal";
    // Construtor para criar um novo Curandeiro com um nome
    public Healer(String name) {
        super(name, 100, 15, 7); // Define os atributos base: HP, ataque, defesa
    }

    /**
     * Define a ação do Curandeiro durante seu turno.
     * Ele pode escolher entre atacar um inimigo ou curar todos os aliados.
     */
   @Override
    public void takeTurn(List<Character> allies, List<Character> enemies, Scanner in, boolean isHuman) {
        tickCooldowns(); // Reduz os cooldowns no início do turno

        if (isHuman) {
            System.out.print("Action for " + this.name + ": [1] Attack");
            // Mostra a opção de cura apenas se não estiver em cooldown
            if (!isOnCooldown(GROUP_HEAL_ABILITY)) {
                System.out.print("  [2] Group Heal");
            } else {
                System.out.print("  (Group Heal is on cooldown for " + cooldowns.get(GROUP_HEAL_ABILITY) + " more turns)");
            }
            System.out.println();
            
            String choice = in.nextLine().trim();
            if (choice.equals("2") && !isOnCooldown(GROUP_HEAL_ABILITY)) {
                groupHeal(allies);
                return;
            }
        } else {
            if (!isOnCooldown(GROUP_HEAL_ABILITY)) {
                for (Character ally : allies) {
                    if (ally.isAlive() && ally.hp < ally.maxHp / 2) {
                        groupHeal(allies);
                        return;
                    }
                }
            }
        }
        
        Character target = Battle.pickTarget(enemies, in, isHuman, this.name + ", choose a target to attack:");
        if (target != null) {
            basicAttack(target);
        }
    }

    //Habilidade especial do Curandeiro: cura um pouco de vida de todos os aliados vivos.
     private void groupHeal(List<Character> allies) {
        System.out.printf("%s casts Group Heal!%n", this.name);
        setCooldown(GROUP_HEAL_ABILITY, 3); // Coloca a cura em cooldown por 3 turnos
        for (Character ally : allies) {
            if (ally.isAlive()) {
                ally.heal(10 + rng.nextInt(6));
            }
        }
    }
}