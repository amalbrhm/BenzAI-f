package view.generator.boxes;

import generator.properties.model.ModelPropertySet;
import generator.properties.model.expression.BinaryNumericalExpression;
import view.generator.ChoiceBoxCriterion;
import view.primaryStage.ScrollPaneWithPropertyList;

public class HBoxPentagonNumberCriterion extends HBoxBoundingCriterion {

    public HBoxPentagonNumberCriterion(ScrollPaneWithPropertyList parent, ChoiceBoxCriterion choiceBoxCriterion) {
        super(parent, choiceBoxCriterion);
    }

    @Override
    public void addPropertyExpression(ModelPropertySet modelPropertySet) {
        if (isValid()
                && getFieldValue().getText() != null
                && !getFieldValue().getText().isEmpty()
                && getOperatorChoiceBox().getValue() != null) {

            System.out.println("[DEBUG] Expression ajoutée pour nbpentagons");

            if (modelPropertySet.getById("nbpentagons") == null) {
                System.err.println("[ERREUR] nbpentagons introuvable dans modelPropertySet !");
                return;
            }

            modelPropertySet.getById("nbpentagons").addExpression(
                    new BinaryNumericalExpression("nbpentagons",
                            getOperatorChoiceBox().getValue(),
                            Integer.decode(getFieldValue().getText()))
            );
        }
    }

    @Override
    public boolean isBounding() {
        return true;
    }
}
