import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. INICIALIZAÇÃO
        // Cria um Scanner para ler a entrada do jogador durante toda a partida
        Scanner scanner = new Scanner(System.in);
        System.out.println(" WELCOME TO THE RPG BATTLE SIMULATOR ");
        System.out.println("======================================");

        // 2. CRIAÇÃO DO GRUPO DO JOGADOR
        // Escolha de cada personagem da equipe do jogador
        System.out.println("\nChoose your Heroes!");
        List<Character> playerParty = new ArrayList<>();
        String[] heroOptions = {"Warrior", "Mage", "Healer", "Archer", "Assassin"};
        int partySize = 3; // Tamanho da equipe
        
        for (int i = 0; i < partySize; i++) {
            System.out.println("\nSelect hero " + (i + 1) + " of " + partySize + ":");
            for (int j = 0; j < heroOptions.length; j++) {
                System.out.printf("%d) %s%n", j + 1, heroOptions[j]);
            }
            int choice;
            while (true) {
                try {
                    System.out.print("Enter number: ");
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= heroOptions.length) break;
                } catch (Exception e) {}
                System.out.println("Invalid choice. Try again."); // Caso nao exista o numero escolhido, erro.
            }

            // Criar um personagem com nome customizavel
            String heroType = heroOptions[choice - 1];
            System.out.print("Enter a name for your " + heroType + ": ");
            String heroName = scanner.nextLine().trim();

            switch (heroType) {
                case "Warrior" -> playerParty.add(new Warrior(heroName));
                case "Mage"    -> playerParty.add(new Mage(heroName));
                case "Healer"  -> playerParty.add(new Healer(heroName));
                case "Archer"  -> playerParty.add(new Archer(heroName));
                case "Assassin"-> playerParty.add(new Assassin(heroName));
            }
        }

        System.out.println("\nYour party is ready!");
        for (Character c : playerParty) System.out.println(" - " + c.status()); // print de todos os herois

        // 3. CRIAÇÃO DO GRUPO INIMIGO
        System.out.println("\nA group of fierce monsters blocks your path!");
        List<Character> enemyParty = new ArrayList<>();
        Random rng = new Random();

        String[] enemyTypes = { "Warrior", "Mage", "Healer", "Archer", "Assassin" };
        int enemyCount = 3; // tamanho da equipe inimiga

        for (int i = 0; i < enemyCount; i++) {
            String type = enemyTypes[rng.nextInt(enemyTypes.length)]; // Escolha de personagem aleatoria
            String name = switch (type) {
                // Nomes predefinidos no jogo
                case "Warrior" -> "Orc Warrior";
                case "Mage"    -> "Goblin Shaman";
                case "Healer"  -> "Dark Priest";
                case "Archer"  -> "Goblin Sharpshooter";
                case "Assassin"-> "Shadow Stalker";
                default -> "Monster";
            };

            switch (type) {
                case "Warrior" -> enemyParty.add(new Warrior(name));
                case "Mage"    -> enemyParty.add(new Mage(name));
                case "Healer"  -> enemyParty.add(new Healer(name));
                case "Archer"  -> enemyParty.add(new Archer(name));
                case "Assassin"-> enemyParty.add(new Assassin(name));
            }
        }

        System.out.println("Enemies have appeared!");
        for (Character e : enemyParty) System.out.println(" - " + e.status()); // print de todos os inimigos

        // 4. INÍCIO DA BATALHA
        // Cria uma nova instância da Batalha, passando os dois times e o scanner
        Battle battle = new Battle(playerParty, enemyParty, scanner);
        
        // O método run() contém todo o loop e a lógica do jogo
        battle.run(); 

        // 5. ENCERRAMENTO
        System.out.println("\n======================================");
        System.out.println(" The battle simulator has ended. ");
        
        // Fecha o scanner para liberar os recursos
        scanner.close();
    }
}