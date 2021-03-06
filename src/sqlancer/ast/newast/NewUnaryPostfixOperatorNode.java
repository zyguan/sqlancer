package sqlancer.ast.newast;

import sqlancer.ast.BinaryOperatorNode.Operator;

public class NewUnaryPostfixOperatorNode<T> implements Node<T> {

    protected final Operator op;
    private Node<T> expr;

    public NewUnaryPostfixOperatorNode(Node<T> expr, Operator op) {
        this.expr = expr;
        this.op = op;
    }

    public String getOperatorRepresentation() {
        return op.getTextRepresentation();
    }

    public Node<T> getExpr() {
        return expr;
    }

}
