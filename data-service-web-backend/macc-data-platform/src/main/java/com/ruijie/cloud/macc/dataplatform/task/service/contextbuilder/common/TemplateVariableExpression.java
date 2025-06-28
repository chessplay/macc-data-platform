package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.common;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.SimpleNode;

public class TemplateVariableExpression implements Expression {
    private String value;

    public TemplateVariableExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        // 实现合适的接受逻辑
    }

    @Override
    public String toString() {
        return "${"+value.toLowerCase()+"}";  // 不加引号
    }

    @Override
    public SimpleNode getASTNode() {
        return null;
    }

    @Override
    public void setASTNode(SimpleNode simpleNode) {

    }
}
