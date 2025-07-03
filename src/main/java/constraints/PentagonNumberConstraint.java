package constraints;

import generator.GeneralModel;
import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;

public class PentagonNumberConstraint extends BenzAIConstraint {

    @Override
    public void buildVariables() {
        // No variables to build
    }

    @Override
    public void postConstraints() {
        GeneralModel generalModel = getGeneralModel();

        for (PropertyExpression expression : this.getExpressionList()) {
            String operator = ((BinaryNumericalExpression) expression).getOperator();
            int value = ((BinaryNumericalExpression) expression).getValue();
            System.out.println("pentagons " + generalModel.getNbPentagonsVar() + " " + operator + " " + value);
            generalModel.getChocoModel().arithm(generalModel.getNbPentagonsVar(), operator, value).post();
        }
        System.out.println(generalModel.getProblem().toString());
    }

    @Override
    public void addVariables() {
        // DO_NOTHING
    }

    @Override
    public void changeSolvingStrategy() {
        // DO_NOTHING
    }

    @Override
    public void changeGraphVertices() {
        // DO_NOTHING
    }
}
