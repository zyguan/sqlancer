package sqlancer.postgres.ast;

import sqlancer.postgres.PostgresCompoundDataType;
import sqlancer.postgres.PostgresSchema.PostgresDataType;

public class PostgresCastOperation implements PostgresExpression {

    private PostgresExpression expression;
    private PostgresCompoundDataType type;

    public PostgresCastOperation(PostgresExpression expression, PostgresCompoundDataType type) {
        if (expression == null) {
            throw new AssertionError();
        }
        this.expression = expression;
        this.type = type;
    }

    @Override
    public PostgresDataType getExpressionType() {
        return type.getDataType();
    }

    @Override
    public PostgresConstant getExpectedValue() {
        return expression.getExpectedValue().cast(type.getDataType());
    }

    public PostgresExpression getExpression() {
        return expression;
    }

    public PostgresDataType getType() {
        return type.getDataType();
    }

    public PostgresCompoundDataType getCompoundType() {
        return type;
    }

}
