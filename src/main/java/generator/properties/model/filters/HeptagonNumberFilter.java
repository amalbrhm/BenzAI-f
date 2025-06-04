package generator.properties.model.filters;

import java.util.ArrayList;

import generator.properties.model.ModelPropertySet;
import generator.properties.model.expression.BinaryNumericalExpression;
import generator.properties.model.expression.PropertyExpression;
import benzenoid.Benzenoid;

public class HeptagonNumberFilter extends Filter {

    @Override
    public boolean test(Benzenoid molecule, ArrayList<PropertyExpression> propertyExpressionList, ModelPropertySet modelPropertySet) {
        for (PropertyExpression expression : propertyExpressionList) {
            int nbHeptagons = ((BinaryNumericalExpression) expression).getValue();
            if (!((BinaryNumericalExpression) expression).test(molecule.getNbHeptagons(), ((BinaryNumericalExpression) expression).getOperator(), nbHeptagons))
                return false;
        }
        return true;
    }
}