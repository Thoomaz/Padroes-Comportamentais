package mediator;

import java.util.*;

interface ChatMediator {
    void addUser(User user);
    void sendMessage(String message, User sender);
}

class ChatRoom implements ChatMediator {
    private List<User> users = new ArrayList<>();
    
    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getName() + " entrou no chat.");
    }
    
    @Override
    public void sendMessage(String message, User sender) {
        if (!users.contains(sender)) {
            System.out.println("Usuário não registrado: " + sender.getName());
            return;
        }
        for (User user : users) {
            if (user != sender) {
                user.receiveMessage(message, sender);
            }
        }
    }
}

abstract class User {
    protected ChatMediator mediator;
    protected String name;
    
    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
    
    public String getName() { return name; }
    
    public abstract void sendMessage(String message);
    public abstract void receiveMessage(String message, User sender);
}

class ChatUser extends User {
    public ChatUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    @Override
    public void sendMessage(String message) {
        System.out.println(name + " enviou: " + message);
        mediator.sendMessage(message, this);
    }
    
    @Override
    public void receiveMessage(String message, User sender) {
        System.out.println(name + " recebeu de " + sender.getName() + ": " + message);
    }
}
