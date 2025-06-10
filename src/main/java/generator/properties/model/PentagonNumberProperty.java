package generator.properties.model;

import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;
import generator.properties.model.filters.PentagonNumberFilter;
import constraints.PentagonNumberConstraint;
import view.generator.ChoiceBoxCriterion;
import view.generator.boxes.HBoxHexagonNumberCriterion;
import view.generator.boxes.HBoxModelCriterion;
import view.primaryStage.ScrollPaneWithPropertyList;
import view.generator.boxes.HBoxPentagonNumberCriterion;

public class PentagonNumberProperty extends ModelProperty {

    public PentagonNumberProperty() {
        super("nbpentagons", "Number of pentagons", new PentagonNumberConstraint(), new PentagonNumberFilter());
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
        return new HBoxPentagonNumberCriterion(parent, choiceBoxCriterion);
    }
    @Override
    public int computeHexagonNumberUpperBound() {
        return computeUpperBound(); // ou mets une borne supérieure plus réaliste si tu veux
    }



}
