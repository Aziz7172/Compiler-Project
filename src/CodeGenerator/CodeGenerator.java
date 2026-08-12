package CodeGenerator;

import AST.*;
import AST.HTML.*;
import AST.JINJA2.*;
import AST.CSS.*;
import AST.HTML.Behaviors.TagBehavior;

import java.util.*;

public class CodeGenerator {
    private final ProgramNode program;
    private final StringBuilder output;
    private final Set<String> imports;
    private boolean hasHtml;
    private boolean hasForms;
    private boolean hasJinja;

    public CodeGenerator(ProgramNode program) {
        this.program = program;
        this.output = new StringBuilder();
        this.imports = new LinkedHashSet<>();
        this.hasHtml = false;
        this.hasForms = false;
        this.hasJinja = false;
    }

    public String generate() {
        scanForFeatures(program);

        generateImports();
        for (ASTNode stmt : program.getStatements()) {
            generateNode(stmt, 0);
        }
        return output.toString();
    }

    private void scanForFeatures(ASTNode node) {
        if (node instanceof HtmlElementNode) {
            hasHtml = true;
            HtmlElementNode html = (HtmlElementNode) node;
            if ("form".equalsIgnoreCase(html.getTagName() != null ? html.getTagName() : "")) {
                hasForms = true;
            }
            if (html.getChildren() != null) {
                for (ASTNode child : html.getChildren()) {
                    scanForFeatures(child);
                }
            }
        } else if (node instanceof JinjaExpressionNode || node instanceof IncludeNode || node instanceof ExtendNode) {
            hasJinja = true;
        } else if (node instanceof ProgramNode) {
            for (ASTNode stmt : ((ProgramNode) node).getStatements()) {
                scanForFeatures(stmt);
            }
        } else if (node instanceof FunctionNode) {
            if (((FunctionNode) node).getBody() != null) {
                for (ASTNode stmt : ((FunctionNode) node).getBody()) {
                    scanForFeatures(stmt);
                }
            }
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            if (ifNode.getChildren() != null) {
                for (ASTNode stmt : ifNode.getChildren()) {
                    scanForFeatures(stmt);
                }
            }
        } else if (node instanceof ForNode) {
            ForNode forNode = (ForNode) node;
            if (forNode.getBody() != null) {
                for (ASTNode stmt : forNode.getBody()) {
                    scanForFeatures(stmt);
                }
            }
        }
    }

    private void generateImports() {
        if (hasHtml || hasJinja) {
            output.append("from flask import Flask, render_template_string");
            if (hasForms) {
                output.append(", request");
            }
            output.append("\n\n");
            output.append("app = Flask(__name__)\n\n");
        }
    }

    private void generateNode(ASTNode node, int indent) {
        if (node == null) return;

        String indentStr = String.join("", Collections.nCopies(indent, "    "));

        if (node instanceof ProgramNode) {
            for (ASTNode stmt : ((ProgramNode) node).getStatements()) {
                generateNode(stmt, indent);
            }
        } else if (node instanceof FunctionNode) {
            generateFunction((FunctionNode) node, indent);
        } else if (node instanceof ClassNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof AssignmentNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof IfNode) {
            generateIf((IfNode) node, indent);
        } else if (node instanceof ForNode) {
            generateFor((ForNode) node, indent);
        } else if (node instanceof ReturnNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof WhileNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof DecoratorNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof HtmlElementNode) {
            generateHtmlElement((HtmlElementNode) node, indent);
        } else if (node instanceof HtmlTextNode) {
            output.append(node.generateCode());
        } else if (node instanceof CssBlockNode) {
            output.append(indentStr).append(node.generateCode()).append("\n");
        } else if (node instanceof JinjaExpressionNode) {
            output.append(node.generateCode());
        } else if (node instanceof IncludeNode) {
            output.append(node.generateCode());
        } else if (node instanceof ExtendNode) {
            output.append(node.generateCode());
        } else {
            String code = node.generateCode();
            if (code != null && !code.isEmpty()) {
                output.append(indentStr).append(code).append("\n");
            }
        }
    }

    private void generateFunction(FunctionNode func, int indent) {
        String indentStr = String.join("", Collections.nCopies(indent, "    "));

        if (func.getDecorators() != null) {
            for (DecoratorNode dec : func.getDecorators()) {
                output.append(indentStr).append(dec.generateCode()).append("\n");
            }
        }

        output.append(indentStr).append("def ").append(func.getName()).append("(");
        List<ParameterNode> params = func.getParameters();
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) output.append(", ");
                output.append(params.get(i).generateCode());
            }
        }
        output.append("):\n");

        List<ASTNode> body = func.getBody();
        if (body != null && !body.isEmpty()) {
            boolean hasHtmlContent = containsHtml(body);
            if (hasHtmlContent && !hasReturnStatement(body)) {
                output.append(indentStr).append("    return render_template_string(\"\"\"\n");
                for (ASTNode stmt : body) {
                    generateNode(stmt, 0);
                }
                output.append("\"\"\")\n");
            } else {
                for (ASTNode stmt : body) {
                    generateNode(stmt, indent + 1);
                }
            }
        }
    }

    private void generateIf(IfNode ifNode, int indent) {
        String indentStr = String.join("", Collections.nCopies(indent, "    "));
        output.append(indentStr).append("if ").append(ifNode.getCondition().generateCode()).append(":\n");
        if (ifNode.getChildren() != null) {
            boolean hasHtmlContent = containsHtml(ifNode.getChildren());
            if (hasHtmlContent) {
                output.append(indentStr).append("    return render_template_string(\"\"\"\n");
                for (ASTNode stmt : ifNode.getChildren()) {
                    generateNode(stmt, 0);
                }
                output.append("\"\"\")\n");
            } else {
                for (ASTNode stmt : ifNode.getChildren()) {
                    generateNode(stmt, indent + 1);
                }
            }
        }
    }

    private void generateFor(ForNode forNode, int indent) {
        String indentStr = String.join("", Collections.nCopies(indent, "    "));
        output.append(indentStr).append("for ").append(forNode.getVariable())
              .append(" in ").append(forNode.getIterable().generateCode()).append(":\n");
        if (forNode.getBody() != null) {
            boolean hasHtmlContent = containsHtml(forNode.getBody());
            if (hasHtmlContent) {
                output.append(indentStr).append("    return render_template_string(\"\"\"\n");
                for (ASTNode stmt : forNode.getBody()) {
                    generateNode(stmt, 0);
                }
                output.append("\"\"\")\n");
            } else {
                for (ASTNode stmt : forNode.getBody()) {
                    generateNode(stmt, indent + 1);
                }
            }
        }
    }

    private void generateHtmlElement(HtmlElementNode html, int indent) {
        String tag = html.getTagName() != null ? html.getTagName() : "div";
        output.append("<").append(tag);
        if (html.getAttributes() != null && !html.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> attr : html.getAttributes().entrySet()) {
                output.append(" ").append(attr.getKey()).append("=\"").append(attr.getValue()).append("\"");
            }
        }
        output.append(">\n");
        if (html.getChildren() != null) {
            for (ASTNode child : html.getChildren()) {
                generateNode(child, indent + 1);
            }
        }
        output.append("</").append(tag).append(">\n");
    }

    private boolean containsHtml(List<ASTNode> nodes) {
        if (nodes == null) return false;
        for (ASTNode node : nodes) {
            if (node instanceof HtmlElementNode || node instanceof HtmlTextNode ||
                node instanceof JinjaExpressionNode || node instanceof IncludeNode ||
                node instanceof ExtendNode || node instanceof CssBlockNode) {
                return true;
            }
            if (node instanceof IfNode) {
                IfNode in = (IfNode) node;
                if (containsHtml(in.getChildren())) return true;
            }
            if (node instanceof ForNode) {
                ForNode fn = (ForNode) node;
                if (containsHtml(fn.getBody())) return true;
            }
        }
        return false;
    }

    private boolean hasReturnStatement(List<ASTNode> nodes) {
        if (nodes == null) return false;
        for (ASTNode node : nodes) {
            if (node instanceof ReturnNode) return true;
            if (node instanceof IfNode) {
                IfNode in = (IfNode) node;
                if (hasReturnStatement(in.getChildren())) return true;
            }
        }
        return false;
    }
}
