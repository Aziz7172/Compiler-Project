package VM;

import java.util.*;
import java.util.logging.Logger;

public class VMExecutor {
    private static final Logger logger = Logger.getLogger(VMExecutor.class.getName());
    private List<BytecodeInstruction> instructions;
    private final Map<String, FunctionInfo> functionRegistry;
    private final Deque<Object> argStack;
    private final Deque<Integer> returnStack;
    private final List<String> executionLog;

    public VMExecutor() {
        this.functionRegistry = new LinkedHashMap<>();
        this.argStack = new ArrayDeque<>();
        this.returnStack = new ArrayDeque<>();
        this.executionLog = new ArrayList<>();
    }

    public Context execute(List<BytecodeInstruction> instructions) {
        this.instructions = instructions;
        Context context = new Context();
        int ip = 0;

        functionRegistry.clear();
        argStack.clear();
        returnStack.clear();
        executionLog.clear();

        while (ip < instructions.size()) {
            BytecodeInstruction insn = instructions.get(ip);
            String opcode = insn.getOpcode();
            int nextIp = ip + 1;

            switch (opcode) {
                case "STORE": {
                    Object value = resolveOperand(insn.getValueOperand(), context);
                    if (value == null && insn.getObjectOperand() != null) {
                        value = insn.getObjectOperand();
                    }
                    if (value == null && !argStack.isEmpty()) {
                        value = argStack.pop();
                    }
                    context.assign(insn.getOperand(), value);
                    log("STORE " + insn.getOperand() + " = " + value);
                    break;
                }
                case "PRINT": {
                    Object value = resolveOperand(insn.getOperand(), context);
                    logger.info("[VM PRINT] " + value);
                    break;
                }
                case "RENDER": {
                    String templateName = insn.getOperand();
                    log("RENDER template=" + templateName);
                    break;
                }
                case "FUNC_DEF": {
                    String name = insn.getOperand();
                    int paramCount = insn.getIntOperand();
                    int startIp = nextIp;
                    functionRegistry.put(name, new FunctionInfo(name, paramCount, startIp));
                    log("FUNC_DEF " + name + " params=" + paramCount);
                    break;
                }
                case "FUNC_END": {
                    String name = insn.getOperand();
                    FunctionInfo info = functionRegistry.get(name);
                    if (info != null) info.endIp = ip;
                    if (!returnStack.isEmpty()) {
                        int retIp = returnStack.pop();
                        ip = retIp;
                        nextIp = ip + 1;
                        log("FUNC_END " + name + " -> return to ip=" + retIp);
                    } else {
                        log("FUNC_END " + name + " (top-level)");
                    }
                    break;
                }
                case "CLASS_DEF": {
                    log("CLASS_DEF " + insn.getOperand());
                    break;
                }
                case "CLASS_END": {
                    log("CLASS_END " + insn.getOperand());
                    break;
                }
                case "FOR_INIT": {
                    String varName = insn.getOperand();
                    Object iterable = resolveOperand(insn.getValueOperand(), context);
                    List<?> items = toList(iterable);
                    context.assign("_for_" + varName + "_items", items);
                    context.assign("_for_" + varName + "_index", 0);
                    context.assign("_for_" + varName + "_startIp", nextIp);
                    if (items != null && !items.isEmpty()) {
                        context.assign(varName, items.get(0));
                        context.assign("_for_" + varName + "_index", 1);
                    }
                    log("FOR_INIT " + varName + " count=" + (items != null ? items.size() : 0));
                    break;
                }
                case "FOR_END": {
                    String varName = insn.getOperand();
                    List<?> items = (List<?>) context.lookup("_for_" + varName + "_items");
                    Integer idx = (Integer) context.lookup("_for_" + varName + "_index");
                    Integer startIp = (Integer) context.lookup("_for_" + varName + "_startIp");
                    if (startIp == null) startIp = nextIp;
                    if (idx == null) idx = 0;
                    if (items != null && idx < items.size()) {
                        context.assign(varName, items.get(idx));
                        context.assign("_for_" + varName + "_index", idx + 1);
                        ip = startIp - 1;
                        nextIp = ip + 1;
                        log("FOR_END " + varName + " -> loop back to ip=" + startIp);
                    } else {
                        log("FOR_END " + varName + " -> loop done");
                    }
                    break;
                }
                case "WHILE": {
                    Object cond = resolveOperand(insn.getOperand(), context);
                    boolean condVal = toBoolean(cond);
                    context.assign("_while_cond", condVal);
                    context.assign("_while_startIp", nextIp);
                    if (!condVal) {
                        log("WHILE false -> skipping loop body");
                    } else {
                        log("WHILE true -> executing loop body");
                    }
                    break;
                }
                case "ENDWHILE": {
                    Boolean condVal = (Boolean) context.lookup("_while_cond");
                    Integer startIp = (Integer) context.lookup("_while_startIp");
                    if (startIp == null) startIp = nextIp;
                    if (Boolean.TRUE.equals(condVal)) {
                        ip = startIp - 1;
                        nextIp = ip + 1;
                        log("ENDWHILE -> loop back to ip=" + startIp);
                    } else {
                        log("ENDWHILE -> loop done");
                    }
                    break;
                }
                case "RET": {
                    Object value = resolveOperand(insn.getValueOperand(), context);
                    if (value == null && insn.getObjectOperand() != null) {
                        value = insn.getObjectOperand();
                    }
                    if (!returnStack.isEmpty()) {
                        int retIp = returnStack.pop();
                        argStack.push(value);
                        ip = retIp;
                        nextIp = ip + 1;
                        log("RET => " + value + " -> return to ip=" + retIp);
                    } else {
                        log("RET => " + value + " (top-level)");
                    }
                    break;
                }
                case "CALL": {
                    String funcName = insn.getOperand();
                    int argCount = insn.getIntOperand();
                    List<Object> args = new ArrayList<>();
                    for (int i = 0; i < argCount && !argStack.isEmpty(); i++) {
                        args.add(0, argStack.pop());
                    }

                    int dotIdx = funcName.indexOf('.');
                    if (dotIdx > 0) {
                        String targetVar = funcName.substring(0, dotIdx);
                        String methodName = funcName.substring(dotIdx + 1);
                        Object target = context.lookup(targetVar);
                        handleMethodCall(target, methodName, args, context);
                        log("CALL method " + funcName + "(" + args + ")");
                    } else {
                        FunctionInfo funcInfo = functionRegistry.get(funcName);
                        if (funcInfo != null) {
                            returnStack.push(nextIp);
                            argStack.push(args);
                            ip = funcInfo.startIp - 1;
                            nextIp = ip + 1;
                            log("CALL user fn " + funcName + "(" + args + ") -> ip=" + funcInfo.startIp);
                        } else {
                            handleBuiltinCall(funcName, args, context);
                            log("CALL builtin " + funcName);
                        }
                    }
                    break;
                }
                case "JZ": {
                    Object cond = resolveOperand(insn.getOperand(), context);
                    boolean truthy = toBoolean(cond);
                    if (!truthy) {
                        log("JZ true (cond=false) -> skipping to ELSE/ENDIF");
                    } else {
                        log("JZ false (cond=true) -> execute body");
                    }
                    break;
                }
                case "ELSE": {
                    log("ELSE branch");
                    break;
                }
                case "ENDIF": {
                    log("ENDIF");
                    break;
                }
                case "PUSH_ARG": {
                    Object value = resolveOperand(insn.getValueOperand(), context);
                    if (value == null && insn.getObjectOperand() != null) {
                        value = insn.getObjectOperand();
                    }
                    if (value != null) {
                        argStack.push(value);
                    }
                    log("PUSH_ARG " + value);
                    break;
                }
                default:
                    break;
            }

            ip = nextIp;
        }

        return context;
    }

    private void handleBuiltinCall(String funcName, List<Object> args, Context context) {
        switch (funcName) {
            case "render_template":
                if (!args.isEmpty()) {
                    context.assign("_template", args.get(0).toString());
                }
                break;
            case "render_template_string":
                break;
            case "redirect":
                break;
            case "url_for":
                context.assign("_url", args.isEmpty() ? "/" : args.get(0).toString());
                break;
            case "abort":
                break;
            case "len":
                argStack.push(args.isEmpty() || !(args.get(0) instanceof List) ? 0 : ((List<?>) args.get(0)).size());
                break;
            case "range":
                argStack.push(createRange(args));
                break;
            case "next":
                if (args.size() >= 1 && args.get(0) instanceof List) {
                    List<?> list = (List<?>) args.get(0);
                    int idx = 0;
                    Object idxObj = context.lookup("_iter_idx");
                    if (idxObj instanceof Integer) idx = (Integer) idxObj;
                    if (idx < list.size()) {
                        argStack.push(list.get(idx));
                        context.assign("_iter_idx", idx + 1);
                    }
                }
                break;
            case "int":
                argStack.push(args.isEmpty() ? 0 : toInt(args.get(0)));
                break;
            case "float":
                argStack.push(args.isEmpty() ? 0.0 : toDouble(args.get(0)));
                break;
            case "str":
                argStack.push(args.isEmpty() ? "" : args.get(0).toString());
                break;
            case "request":
                argStack.push("<MockRequest>");
                break;
            case "session":
                argStack.push(new HashMap<String, Object>());
                break;
            case "flash":
                break;
            case "get_flashed_messages":
                argStack.push(new ArrayList<String>());
                break;
            case "enumerate":
                argStack.push(args.isEmpty() ? new ArrayList<Object>() : createEnumerated((List<?>) args.get(0)));
                break;
            case "zip":
                argStack.push(args.isEmpty() ? new ArrayList<Object>() : args.get(0));
                break;
            case "sorted":
                if (!args.isEmpty() && args.get(0) instanceof List) {
                    List<?> list = new ArrayList<>((List<?>) args.get(0));
                    Collections.sort(list, (a, b) -> String.valueOf(a).compareTo(String.valueOf(b)));
                    argStack.push(list);
                }
                break;
            case "print":
                argStack.push(args.isEmpty() ? null : String.join(" ", args.stream().map(Object::toString).toArray(String[]::new)));
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void handleMethodCall(Object target, String methodName, List<Object> args, Context context) {
        if (target instanceof List) {
            List<Object> list = (List<Object>) target;
            switch (methodName) {
                case "append":
                    if (!args.isEmpty()) list.add(args.get(0));
                    break;
                case "pop":
                    if (!list.isEmpty()) {
                        int idx = args.isEmpty() ? list.size() - 1 : toInt(args.get(0));
                        if (idx >= 0 && idx < list.size()) list.remove(idx);
                    }
                    break;
                case "extend":
                    if (!args.isEmpty() && args.get(0) instanceof List) {
                        list.addAll((List<?>) args.get(0));
                    }
                    break;
                case "remove":
                    if (!args.isEmpty()) list.remove(args.get(0));
                    break;
                case "insert":
                    if (args.size() >= 2) {
                        int idx = toInt(args.get(0));
                        if (idx >= 0 && idx <= list.size()) {
                            list.add(idx, args.get(1));
                        }
                    }
                    break;
                case "clear":
                    list.clear();
                    break;
                default:
                    break;
            }
        } else if (target instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) target;
            switch (methodName) {
                case "get":
                    if (!args.isEmpty()) {
                        String key = String.valueOf(args.get(0));
                        argStack.push(map.get(key));
                    }
                    break;
                case "put":
                    if (args.size() >= 2) {
                        map.put(String.valueOf(args.get(0)), args.get(1));
                    }
                    break;
                case "keys":
                    argStack.push(new ArrayList<>(map.keySet()));
                    break;
                case "values":
                    argStack.push(new ArrayList<>(map.values()));
                    break;
                default:
                    break;
            }
        } else if (target == null) {
            Object ctxObj = context.lookup(methodName);
            if (ctxObj instanceof List) {
                List<Object> list = (List<Object>) ctxObj;
                switch (methodName) {
                case "append":
                    if (!args.isEmpty()) list.add(args.get(0));
                    break;
                    case "pop":
                        if (!list.isEmpty()) {
                            int idx = args.isEmpty() ? list.size() - 1 : toInt(args.get(0));
                            if (idx >= 0 && idx < list.size()) list.remove(idx);
                        }
                        break;
                    case "extend":
                        if (!args.isEmpty() && args.get(0) instanceof List) {
                            list.addAll((List<?>) args.get(0));
                        }
                        break;
                    case "remove":
                        if (!args.isEmpty()) list.remove(args.get(0));
                        break;
                    case "insert":
                        if (args.size() >= 2) {
                            int idx = toInt(args.get(0));
                            if (idx >= 0 && idx <= list.size()) list.add(idx, args.get(1));
                        }
                        break;
                    case "clear":
                        list.clear();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private Object resolveOperand(String operand, Context context) {
        if (operand == null || operand.isEmpty()) return null;
        if (operand.startsWith("$")) {
            String varName = operand.substring(1);
            return context.lookup(varName);
        }
        if (operand.startsWith("\"") && operand.endsWith("\"")) {
            return operand.substring(1, operand.length() - 1);
        }
        if (operand.startsWith("'") && operand.endsWith("'")) {
            return operand.substring(1, operand.length() - 1);
        }
        try {
            if (operand.contains(".")) return Double.parseDouble(operand);
            return Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            if ("True".equals(operand)) return true;
            if ("False".equals(operand)) return false;
            if ("None".equals(operand)) return null;
        }
        return operand;
    }

    @SuppressWarnings("unchecked")
    private List<?> toList(Object val) {
        if (val instanceof List) return (List<?>) val;
        if (val instanceof String) {
            String s = (String) val;
            if (s.equals("[]")) return new ArrayList<>();
        }
        if (val == null) return null;
        return Arrays.asList(val);
    }

    private boolean toBoolean(Object val) {
        if (val == null) return false;
        if (val instanceof Boolean) return (Boolean) val;
        if (val instanceof Number) return ((Number) val).doubleValue() != 0;
        if (val instanceof String) {
            String s = (String) val;
            return !s.equals("") && !s.equals("[]");
        }
        if (val instanceof List) return !((List<?>) val).isEmpty();
        return true;
    }

    private int toInt(Object val) {
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(String.valueOf(val)); } catch (Exception e) { return 0; }
    }

    private double toDouble(Object val) {
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(String.valueOf(val)); } catch (Exception e) { return 0.0; }
    }

    private List<Map<String, Object>> createRange(List<Object> args) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (args.isEmpty()) return result;
        int start = 0, end = toInt(args.get(0));
        if (args.size() >= 2) {
            start = toInt(args.get(0));
            end = toInt(args.get(1));
        }
        for (int i = start; i < end; i++) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("_value", i);
            result.add(item);
        }
        return result;
    }

    private List<List<Object>> createEnumerated(List<?> list) {
        List<List<Object>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Object> pair = new ArrayList<>();
            pair.add(i);
            pair.add(list.get(i));
            result.add(pair);
        }
        return result;
    }

    private void log(String message) {
        executionLog.add("[VM] " + message);
    }

    public List<String> getExecutionLog() {
        return executionLog;
    }

    private static class FunctionInfo {
        String name;
        int paramCount;
        int startIp;
        int endIp;

        FunctionInfo(String name, int paramCount, int startIp) {
            this.name = name;
            this.paramCount = paramCount;
            this.startIp = startIp;
            this.endIp = -1;
        }
    }
}