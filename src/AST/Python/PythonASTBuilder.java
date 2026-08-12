package AST.Python;

import AST.*;
import gen.ANTLR.PythonLexer;
import gen.ANTLR.PythonParser;
import SymbolTable.SymbolTable;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;

public class PythonASTBuilder {
    private SymbolTable symbolTable;

    public ProgramNode build(String source) {
        CharStream input = CharStreams.fromString(source);
        PythonLexer lexer = new PythonLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonParser parser = new PythonParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                System.err.println("Python parse error at line " + line + ":" + charPositionInLine + " - " + msg);
            }
        });

        ParseTree tree = parser.program();
        ASTBuilder visitor = new ASTBuilder();
        ProgramNode result = (ProgramNode) visitor.visit(tree);
        this.symbolTable = visitor.table;
        return result;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
