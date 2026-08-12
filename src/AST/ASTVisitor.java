package AST;

import AST.CSS.*;
import AST.HTML.*;
import AST.JINJA2.*;

public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(FunctionNode node);
    void visit(ClassNode node);
    void visit(AssignmentNode node);
    void visit(IfNode node);
    void visit(ForNode node);
    void visit(WhileNode node);
    void visit(ReturnNode node);
    void visit(BinaryOpNode node);
    void visit(UnaryNode node);
    void visit(FunctionCallNode node);
    void visit(IdentifierNode node);
    void visit(StringNode node);
    void visit(NumberNode node);
    void visit(BooleanNode node);
    void visit(ListNode node);
    void visit(TupleNode node);
    void visit(AttributeAccessNode node);
    void visit(IndexAccessNode node);
    void visit(FromImportNode node);
    void visit(ParameterNode node);
    void visit(ArgumentNode node);
    void visit(DecoratorNode node);
    void visit(GlobalNode node);
    void visit(PrintNode node);
    void visit(ListComprehensionNode node);
    void visit(ImportedNode node);
    void visit(ImportedListNode node);
    void visit(ElifNode node);
    void visit(ElseNode node);

    void visit(HtmlElementNode node);
    void visit(HtmlTextNode node);

    void visit(CssNode node);
    void visit(CssBlockNode node);
    void visit(CssPropertyNode node);

    void visit(ExtendNode node);
    void visit(IncludeNode node);
    void visit(JSONNode node);
    void visit(JinjaExpressionNode node);

    void visit(Jinja2TemplateNode node);
    void visit(Jinja2TextNode node);
    void visit(Jinja2ExprNode node);
    void visit(Jinja2CommentNode node);
    void visit(Jinja2IfNode node);
    void visit(Jinja2ForNode node);
    void visit(Jinja2BlockNode node);
    void visit(Jinja2SetNode node);
    void visit(Jinja2ExtendsNode node);
    void visit(Jinja2IncludeNode node);
    void visit(Jinja2WithNode node);
}