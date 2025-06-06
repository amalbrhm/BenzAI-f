package view.generator.boxes;

import generator.properties.model.ModelPropertySet;
import generator.properties.model.expression.BinaryNumericalExpression;
import view.generator.ChoiceBoxCriterion;
import view.generator.boxes.HBoxBoundingCriterion;
import view.primaryStage.ScrollPaneWithPropertyList;

public class HBoxHeptagonNumberCriterion extends HBoxBoundingCriterion {

    public HBoxHeptagonNumberCriterion(ScrollPaneWithPropertyList parent, ChoiceBoxCriterion choiceBoxCriterion) {
        super(parent, choiceBoxCriterion);
    }

    @Override
    public void addPropertyExpression(ModelPropertySet modelPropertySet) {
        if (isValid()
                && getFieldValue().getText() != null
                && !getFieldValue().getText().isEmpty()
                && getOperatorChoiceBox().getValue() != null) {

            modelPropertySet.getById("nbheptagons").addExpression(
                    new BinaryNumericalExpression("nbheptagons",
                            getOperatorChoiceBox().getValue(),
                            Integer.decode(getFieldValue().getText()))
            );
        }
    }


}
