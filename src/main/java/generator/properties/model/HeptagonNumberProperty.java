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
        super("nbheptagons", "Number of heptagons", new HeptagonNumberConstraint(), new HeptagonNumberFilter());
    }

    public int computeUpperBound() {
        int min = Integer.MAX_VALUE;
        /*for (PropertyExpression expr : this.getExpressions()) {
            String operator = ((BinaryNumericalExpression) expr).getOperator();
            int value = ((BinaryNumericalExpression) expr).getValue();
            if (isBoundingOperator(operator)) {
                if ("<".equals(operator))
                    value--;
                min = Math.min(value, min);
            }
        }*/
        return min;
    }

    @Override
    public HBoxModelCriterion makeHBoxCriterion(ScrollPaneWithPropertyList parent, ChoiceBoxCriterion choiceBoxCriterion) {
        return new HBoxHeptagonNumberCriterion(parent, choiceBoxCriterion);
    }
    @Override
    public int computeHexagonNumberUpperBound() {
        return computeUpperBound(); // ou une version spécifique pour heptagones
    }
}
