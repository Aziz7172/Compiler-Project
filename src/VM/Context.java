package VM;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Context {
    private final Stack<Map<String, Object>> scopes;

    public Context() {
        this.scopes = new Stack<>();
        this.scopes.push(new LinkedHashMap<>());
    }

    public Context(Map<String, Object> initialValues) {
        this.scopes = new Stack<>();
        this.scopes.push(new LinkedHashMap<>(initialValues));
    }

    public void pushScope() {
        this.scopes.push(new LinkedHashMap<>());
    }

    public void popScope() {
        if (this.scopes.size() > 1) {
            this.scopes.pop();
        }
    }

    public void assign(String name, Object value) {
        this.scopes.peek().put(name, value);
    }

    public Object lookup(String name) {
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            Map<String, Object> scope = this.scopes.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }

    public boolean contains(String name) {
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            if (this.scopes.get(i).containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        int total = 0;
        for (Map<String, Object> scope : this.scopes) {
            total += scope.size();
        }
        return total;
    }

    public Set<String> names() {
        Set<String> allNames = new java.util.LinkedHashSet<>();
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            allNames.addAll(this.scopes.get(i).keySet());
        }
        return allNames;
    }

    public Map<String, Object> snapshot() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            result.putAll(this.scopes.get(i));
        }
        return result;
    }

    public Map<String, Object> snapshotCurrentScope() {
        return new LinkedHashMap<>(this.scopes.peek());
    }

    public static class PythonList extends ArrayList<Object> {
        public PythonList() {
            super();
        }
    }

    public static class PythonDict extends LinkedHashMap<String, Object> {
        public PythonDict() {
            super();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Context{\n");
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            Map<String, Object> scope = this.scopes.get(i);
            if (scope.isEmpty()) continue;
            sb.append("  Scope ").append(i).append(": ").append(scope).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
