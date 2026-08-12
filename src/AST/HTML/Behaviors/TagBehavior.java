package AST.HTML.Behaviors;

import AST.ASTNode;
import gen.ANTLR.PythonParser;

public interface TagBehavior {
    void render(PythonParser.HtmlTagContext html);
}
