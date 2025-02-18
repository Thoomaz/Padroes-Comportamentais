package memento;

import java.util.*;

class GameMemento {
    private final String world;
    private final int level;
    private final int health;
    private final List<String> skills;
    
    public GameMemento(String world, int level, int health, List<String> skills) {
        this.world = world;
        this.level = level;
        this.health = health;
        this.skills = new ArrayList<>(skills);
    }
    
    public String getWorld() { return world; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public List<String> getSkills() { return new ArrayList<>(skills); }
}

class Player {
    private String world;
    private int level;
    private int health;
    private List<String> skills;
    
    public Player(String world, int level, int health) {
        this.world = world;
        this.level = level;
        this.health = health;
        this.skills = new ArrayList<>();
    }
    
    public void unlockSkill(String skill) {
        skills.add(skill);
        System.out.println("Habilidade desbloqueada: " + skill);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        System.out.println("Jogador recebeu " + damage + " de dano. Saúde atual: " + health);
    }
    
    public GameMemento save() {
        System.out.println("Progresso salvo!");
        return new GameMemento(world, level, health, skills);
    }
    
    public void restore(GameMemento memento) {
        this.world = memento.getWorld();
        this.level = memento.getLevel();
        this.health = memento.getHealth();
        this.skills = memento.getSkills();
        System.out.println("Progresso restaurado!");
    }
}

class GameCaretaker {
    private Stack<GameMemento> savePoints = new Stack<>();
    
    public void saveGame(Player player) {
        savePoints.push(player.save());
    }
    
    public void loadLastSave(Player player) {
        if (!savePoints.isEmpty()) {
            player.restore(savePoints.pop());
        } else {
            System.out.println("Nenhum progresso salvo encontrado!");
        }
    }
}
