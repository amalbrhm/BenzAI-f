package generator.properties.model;

import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;
import generator.properties.model.filters.HeptagonNumberFilter;
import constraints.HeptagonNumberConstraint;
import view.generator.ChoiceBoxCriterion;
import view.generator.boxes.HBoxModelCriterion;
import view.primaryStage.ScrollPaneWithPropertyList;
import view.generator.boxes.HBoxHeptagonNumberCriterion;

public class HeptagonNumberProperty extends ModelProperty {

    public HeptagonNumberProperty() {
        super("heptagons", "Number of heptagons", new HeptagonNumberConstraint(), new HeptagonNumberFilter());
    }

    @Override
    public int computeHexagonNumberUpperBound() {
        int heptagonNumberMin = Integer.MAX_VALUE;
        for (PropertyExpression expr : this.getExpressions()) {
            String operator = ((BinaryNumericalExpression)expr).getOperator();
            int value = ((BinaryNumericalExpression)expr).getValue();
            if (isBoundingOperator(operator)) {
                if ("<".equals(operator))
                    value--;
                heptagonNumberMin = Math.min(value, heptagonNumberMin);
            }
        }
        return heptagonNumberMin;
    }

    @Override
    public HBoxModelCriterion makeHBoxCriterion(ScrollPaneWithPropertyList parent, ChoiceBoxCriterion choiceBoxCriterion) {
        return new HBoxHeptagonNumberCriterion(parent, choiceBoxCriterion);
    }
}
