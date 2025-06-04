package generator.properties.model;

import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;
import generator.properties.model.filters.PentagonNumberFilter;
import constraints.PentagonNumberConstraint;
import view.generator.ChoiceBoxCriterion;
import view.generator.boxes.HBoxModelCriterion;
import view.primaryStage.ScrollPaneWithPropertyList;
import view.generator.boxes.HBoxPentagonNumberCriterion;

public class PentagonNumberProperty extends ModelProperty {

    public PentagonNumberProperty() {
        super("pentagons", "Number of pentagons", new PentagonNumberConstraint(), new PentagonNumberFilter());
    }

    @Override
    public int computeHexagonNumberUpperBound() {
        int pentagonNumberMin = Integer.MAX_VALUE;
        for (PropertyExpression expr : this.getExpressions()) {
            String operator = ((BinaryNumericalExpression)expr).getOperator();
            int value = ((BinaryNumericalExpression)expr).getValue();
            if (isBoundingOperator(operator)) {
                if ("<".equals(operator))
                    value--;
                pentagonNumberMin = Math.min(value, pentagonNumberMin);
            }
        }
        return pentagonNumberMin;
    }

    @Override
    public HBoxModelCriterion makeHBoxCriterion(ScrollPaneWithPropertyList parent, ChoiceBoxCriterion choiceBoxCriterion) {
        return new HBoxPentagonNumberCriterion(parent, choiceBoxCriterion);
    }
}
