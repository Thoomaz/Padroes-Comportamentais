package chainOfResponsibilityCommand;

import java.util.*;

class HttpRequest {
    private String user;
    private String permission;
    private String body;
    private Map<String, String> attributes = new HashMap<>();

    public HttpRequest(String user, String permission, String body) {
        this.user = user;
        this.permission = permission;
        this.body = body;
    }

    public String getUser() { return user; }
    public String getPermission() { return permission; }
    public String getBody() { return body; }
    public Map<String, String> getAttributes() { return attributes; }
}

interface Handler {
    void setNext(Handler next);
    void handle(HttpRequest request);
}

class AuthenticationHandler implements Handler {
    private Handler next;
    private Set<String> validUsers = new HashSet<>(Arrays.asList("admin", "user1", "user2"));

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpRequest request) {
        if (validUsers.contains(request.getUser())) {
            System.out.println("Autenticação bem-sucedida para " + request.getUser());
            if (next != null) next.handle(request);
        } else {
            System.out.println("Falha na autenticação!");
        }
    }
}

class AuthorizationHandler implements Handler {
    private Handler next;
    private Map<String, String> userPermissions = new HashMap<>() {{
        put("admin", "ALL");
        put("user1", "READ");
        put("user2", "WRITE");
    }};

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpRequest request) {
        if (userPermissions.getOrDefault(request.getUser(), "").equals(request.getPermission()) ||
            userPermissions.getOrDefault(request.getUser(), "").equals("ALL")) {
            System.out.println("Permissão concedida para " + request.getUser());
            if (next != null) next.handle(request);
        } else {
            System.out.println("Acesso negado!");
        }
    }
}

class BodyParsingHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpRequest request) {
        String body = request.getBody();
        if (body != null && !body.isEmpty()) {
            Map<String, String> attributes = new HashMap<>();
            body = body.replace("{", "").replace("}", "");
            String[] pairs = body.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                attributes.put(keyValue[0].trim(), keyValue[1].trim());
            }
            request.getAttributes().putAll(attributes);
            System.out.println("Body transformado: " + request.getAttributes());
        }
        if (next != null) next.handle(request);
    }
}

interface Command {
    void execute();
}

class ProcessRequestCommand implements Command {
    private HttpRequest request;
    private Handler chain;

    public ProcessRequestCommand(HttpRequest request, Handler chain) {
        this.request = request;
        this.chain = chain;
    }

    @Override
    public void execute() {
        chain.handle(request);
    }
}
