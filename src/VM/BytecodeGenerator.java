package VM;

import AST.*;
import AST.JINJA2.JSONNode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BytecodeGenerator {
    private final List<BytecodeInstruction> instructions;
    private final Map<String, int[]> functionRanges;

    public BytecodeGenerator() {
        this.instructions = new ArrayList<>();
        this.functionRanges = new LinkedHashMap<>();
    }

    public List<BytecodeInstruction> generate(ProgramNode program) {
        instructions.clear();
        functionRanges.clear();

        for (ASTNode statement : program.getStatements()) {
            emit(statement);
        }

        return instructions;
    }

    public Map<String, int[]> getFunctionRanges() {
        return functionRanges;
    }

    private void emit(ASTNode node) {
        if (node instanceof AssignmentNode) {
            emitAssignment((AssignmentNode) node);
        } else if (node instanceof FunctionNode) {
            emitFunction((FunctionNode) node);
        } else if (node instanceof FunctionCallNode) {
            emitCall((FunctionCallNode) node);
        } else if (node instanceof ReturnNode) {
            emitReturn((ReturnNode) node);
        } else if (node instanceof IfNode) {
            emitIf((IfNode) node);
        } else if (node instanceof PrintNode) {
            emitPrint((PrintNode) node);
        } else if (node instanceof ForNode) {
            emitFor((ForNode) node);
        } else if (node instanceof WhileNode) {
            emitWhile((WhileNode) node);
        } else if (node instanceof ClassNode) {
            emitClass((ClassNode) node);
        }
    }

    private void emitAssignment(AssignmentNode node) {
        Object value = resolveObject(node.value);
        instructions.add(new BytecodeInstruction("STORE", node.getName(), "", 0, value));
    }

    private void emitFunction(FunctionNode node) {
        int startIp = instructions.size();
        instructions.add(new BytecodeInstruction("FUNC_DEF", node.getName(), node.getParameters().size()));
        for (ASTNode statement : node.getBody()) {
            emit(statement);
        }
        int endIp = instructions.size();
        instructions.add(new BytecodeInstruction("FUNC_END", node.getName()));
        functionRanges.put(node.getName(), new int[]{startIp, endIp});
    }

    private void emitCall(FunctionCallNode node) {
        String fnName = node.getFunctionName();
        ASTNode target = node.getFunctionTarget();

        if (target != null) {
            String targetVar = extractTargetVar(target);
            String methodName = extractMethodName(target);
            if (methodName != null) {
                String methodCallName = targetVar + "." + methodName;
                for (ASTNode argument : node.getArguments()) {
                    Object resolved = resolveObject(argument);
                    if (argument instanceof ArgumentNode && ((ArgumentNode) argument).getName() != null) {
                        resolved = Map.of(((ArgumentNode) argument).getName(), resolved);
                    }
                    instructions.add(new BytecodeInstruction("PUSH_ARG", "", resolved));
                }
                instructions.add(new BytecodeInstruction("CALL", methodCallName, node.getArguments().size()));
                return;
            }
        }

        if (isRenderTemplateCall(fnName, node)) {
            String templateName = getTemplateName(node);
            instructions.add(new BytecodeInstruction("PUSH_ARG", templateName));
            instructions.add(new BytecodeInstruction("CALL", fnName, node.getArguments().size()));
            return;
        }

        for (ASTNode argument : node.getArguments()) {
            Object resolved = resolveObject(argument);
            if (argument instanceof ArgumentNode && ((ArgumentNode) argument).getName() != null) {
                resolved = Map.of(((ArgumentNode) argument).getName(), resolved);
            }
            instructions.add(new BytecodeInstruction("PUSH_ARG", "", resolved));
        }
        instructions.add(new BytecodeInstruction("CALL", fnName != null ? fnName : "?fn?", node.getArguments().size()));
    }

    private String extractTargetVar(ASTNode target) {
        if (target instanceof IdentifierNode) {
            return ((IdentifierNode) target).getName();
        }
        if (target instanceof AttributeAccessNode) {
            return extractTargetVar(((AttributeAccessNode) target).object);
        }
        return null;
    }

    private String extractMethodName(ASTNode target) {
        if (target instanceof AttributeAccessNode) {
            ASTNode attr = ((AttributeAccessNode) target).attribute;
            if (attr instanceof IdentifierNode) {
                return ((IdentifierNode) attr).getName();
            }
        }
        return null;
    }

    private void emitReturn(ReturnNode node) {
        Object value = resolveObject(node.getValue());
        instructions.add(new BytecodeInstruction("RET", "", "", 0, value));
    }

    private void emitIf(IfNode node) {
        Object cond = resolveObject(node.getCondition());
        instructions.add(new BytecodeInstruction("JZ", "", "", 0, cond));
        for (ASTNode statement : node.getBody()) {
            emit(statement);
        }
        if (!node.getElseBody().isEmpty()) {
            instructions.add(new BytecodeInstruction("ELSE"));
            for (ASTNode statement : node.getElseBody()) {
                emit(statement);
            }
        }
        instructions.add(new BytecodeInstruction("ENDIF"));
    }

    private void emitPrint(PrintNode node) {
        List<Object> values = new ArrayList<>();
        for (ASTNode content : node.getNodes()) {
            values.add(resolveObject(content));
        }
        instructions.add(new BytecodeInstruction("PRINT", "", "", 0, values));
    }

    private void emitFor(ForNode node) {
        Object iterable = resolveObject(node.getIterable());
        instructions.add(new BytecodeInstruction("FOR_INIT", node.getVariable(), "", 0, iterable));
        for (ASTNode statement : node.getBody()) {
            emit(statement);
        }
        instructions.add(new BytecodeInstruction("FOR_END", node.getVariable()));
    }

    private void emitWhile(WhileNode node) {
        Object expr = resolveObject(node.getExpression());
        instructions.add(new BytecodeInstruction("WHILE", "", "", 0, expr));
        for (ASTNode statement : node.getBody()) {
            emit(statement);
        }
        instructions.add(new BytecodeInstruction("ENDWHILE"));
    }

    private void emitClass(ClassNode node) {
        instructions.add(new BytecodeInstruction("CLASS_DEF", node.getName()));
        for (AssignmentNode variable : node.getVariables()) {
            emit(variable);
        }
        for (FunctionNode method : node.getMethods()) {
            emit(method);
        }
        for (ClassNode nested : node.getNestedClasses()) {
            emit(nested);
        }
        instructions.add(new BytecodeInstruction("CLASS_END", node.getName()));
    }

    private boolean isRenderTemplateCall(String name, FunctionCallNode node) {
        return "render_template".equals(name) && !node.getArguments().isEmpty();
    }

    private String getTemplateName(FunctionCallNode node) {
        if (node.getArguments().isEmpty()) return "";
        ASTNode firstArg = node.getArguments().get(0);
        Object val = resolveObject(firstArg);
        if (val instanceof String) return (String) val;
        return val != null ? val.toString() : "";
    }

    private Object resolveObject(ASTNode node) {
        if (node == null) return null;

        if (node instanceof StringNode) {
            return ((StringNode) node).getValue();
        }
        if (node instanceof NumberNode) {
            String text = ((NumberNode) node).getValue();
            try {
                if (text.contains(".")) return Double.parseDouble(text);
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return text;
            }
        }
        if (node instanceof BooleanNode) {
            return ((BooleanNode) node).getValue();
        }
        if (node instanceof IdentifierNode) {
            return "$" + ((IdentifierNode) node).getName();
        }
        if (node instanceof ListNode) {
            List<Object> values = new ArrayList<>();
            for (ASTNode element : ((ListNode) node).getElements()) {
                values.add(resolveObject(element));
            }
            return values;
        }
        if (node instanceof TupleNode) {
            List<Object> values = new ArrayList<>();
            for (ASTNode element : ((TupleNode) node).getElements()) {
                values.add(resolveObject(element));
            }
            return values;
        }
        if (node instanceof JSONNode) {
            JSONNode jsonNode = (JSONNode) node;
            Map<String, Object> map = new LinkedHashMap<>();
            Map<String, ASTNode> data = jsonNode.getData();
            if (data != null) {
                for (Map.Entry<String, ASTNode> entry : data.entrySet()) {
                    map.put(entry.getKey(), resolveObject(entry.getValue()));
                }
            }
            return map;
        }
        if (node instanceof BinaryOpNode) {
            BinaryOpNode binary = (BinaryOpNode) node;
            Object left = resolveObject(binary.left);
            Object right = resolveObject(binary.right);
            String op = binary.operator;

            if ("+".equals(op)) {
                if (left instanceof String || right instanceof String) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                if (left instanceof Number && right instanceof Number) {
                    if (left instanceof Double || right instanceof Double) {
                        return ((Number) left).doubleValue() + ((Number) right).doubleValue();
                    }
                    return ((Number) left).intValue() + ((Number) right).intValue();
                }
            }
            if ("-".equals(op) && left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() - ((Number) right).doubleValue();
            }
            if ("*".equals(op) && left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() * ((Number) right).doubleValue();
            }
            if ("/".equals(op) && left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() / ((Number) right).doubleValue();
            }
            if (">".equals(op) && left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() > ((Number) right).doubleValue();
            }
            if ("<".equals(op) && left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() < ((Number) right).doubleValue();
            }
            if ("==".equals(op)) {
                return left != null && left.equals(right);
            }
            if ("!=".equals(op)) {
                return left == null || !left.equals(right);
            }
            return left + " " + op + " " + right;
        }
        if (node instanceof UnaryNode) {
            UnaryNode unary = (UnaryNode) node;
            Object expr = resolveObject(unary.expression);
            if ("not".equals(unary.operator)) {
                return !(expr instanceof Boolean) ? false : !(Boolean) expr;
            }
            if ("-".equals(unary.operator) && expr instanceof Number) {
                return -((Number) expr).doubleValue();
            }
            return expr;
        }
        if (node instanceof ArgumentNode) {
            return resolveObject(((ArgumentNode) node).getValue());
        }
        if (node instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) node;
            String fnName = call.getFunctionName();
            ASTNode target = call.getFunctionTarget();

            if (target != null) {
                String targetVar = extractTargetVar(target);
                String methodName = extractMethodName(target);
                List<Object> resolvedArgs = new ArrayList<>();
                for (ASTNode arg : call.getArguments()) {
                    resolvedArgs.add(resolveObject(arg));
                }
                // Emit a CALL instruction that will be handled at runtime
                // by handleMethodCall in VMExecutor, with targetVar and methodName encoded
                return targetVar + "." + methodName;
            }

            if ("len".equals(fnName) && !call.getArguments().isEmpty()) {
                Object arg = resolveObject(call.getArguments().get(0));
                if (arg instanceof List) return ((List<?>) arg).size();
                return 0;
            }
            if ("int".equals(fnName) && !call.getArguments().isEmpty()) {
                Object arg = resolveObject(call.getArguments().get(0));
                if (arg instanceof Number) return ((Number) arg).intValue();
                try { return Integer.parseInt(String.valueOf(arg)); } catch (Exception e) { return 0; }
            }
            if ("float".equals(fnName) && !call.getArguments().isEmpty()) {
                Object arg = resolveObject(call.getArguments().get(0));
                if (arg instanceof Number) return ((Number) arg).doubleValue();
                try { return Double.parseDouble(String.valueOf(arg)); } catch (Exception e) { return 0.0; }
            }
            if ("str".equals(fnName) && !call.getArguments().isEmpty()) {
                return String.valueOf(resolveObject(call.getArguments().get(0)));
            }
            return "<call:" + fnName + ">";
        }
        if (node instanceof IndexAccessNode) {
            return node.generateCode();
        }

        return node.toString();
    }
}