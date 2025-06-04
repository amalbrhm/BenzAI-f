package generator.properties.model.filters;

import java.util.ArrayList;

import generator.properties.model.ModelPropertySet;
import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;
import benzenoid.Benzenoid;

public class PentagonNumberFilter extends Filter {

    @Override
    public boolean test(Benzenoid molecule, ArrayList<PropertyExpression> propertyExpressionList, ModelPropertySet modelPropertySet) {
        for (PropertyExpression expression : propertyExpressionList) {
            int nbPentagons = ((BinaryNumericalExpression) expression).getValue();
            if (!((BinaryNumericalExpression) expression).test(molecule.getNbPentagons(), ((BinaryNumericalExpression) expression).getOperator(), nbPentagons))
                return false;
        }
        return true;
    }
}