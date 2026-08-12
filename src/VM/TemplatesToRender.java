package VM;

import java.util.LinkedHashMap;
import java.util.Map;

public class TemplatesToRender {
    private final String templateName;
    private final Map<String, Object> context;

    public TemplatesToRender(String templateName, Map<String, Object> context) {
        this.templateName = templateName;
        this.context = context != null ? new LinkedHashMap<>(context) : new LinkedHashMap<>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public Object getContextValue(String key) {
        return context.get(key);
    }

    public boolean hasContext() {
        return !context.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TemplatesToRender{\n");
        sb.append("  templateName: ").append(templateName).append("\n");
        sb.append("  context: {\n");
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(": ")
              .append(formatValue(entry.getValue(), 5)).append("\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    private String formatValue(Object value, int indent) {
        String pad = " ".repeat(indent);
        if (value == null) {
            return "null";
        }
        if (value instanceof Map) {
            StringBuilder sb = new StringBuilder("{\n");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                sb.append(pad).append("  ").append(entry.getKey()).append(": ")
                  .append(formatValue(entry.getValue(), indent + 4)).append("\n");
            }
            sb.append(pad).append("}");
            return sb.toString();
        }
        if (value instanceof Iterable) {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : (Iterable<?>) value) {
                if (!first) sb.append(", ");
                sb.append(formatValue(item, indent + 2));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        return value.toString();
    }
}
