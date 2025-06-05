package generator;

import generator.properties.Property;
import generator.properties.model.HeptagonNumberProperty;
import generator.properties.model.ModelProperty;
import generator.properties.model.ModelPropertySet;
import generator.properties.model.PentagonNumberProperty;


public enum ModelBuilder {
	;

	public static GeneralModel buildModel(ModelPropertySet modelPropertySet) {
		System.out.println("[DEBUG] Vérification des propriétés dans buildModel :");
		for (Property p : modelPropertySet) {
			System.out.println(" - " + p.getId());
		}

		if (noLimitingProperties(modelPropertySet)) {
			System.out.println("[DEBUG] Propriétés présentes :");
			for (Property p : modelPropertySet) {
				System.out.println(" - " + p.getId());
			}

			System.out.println("prop pas reconnu ");
			return null;
		}
		else
			System.out.println("prop reconnu");

		GeneralModel model = new GeneralModel(modelPropertySet);

		// ==== Calcul automatique de k à partir des propriétés nbpentagons et nbheptagons ====
		int k = 0;
		int nbPentagons = -1;
		int nbHeptagons = -1;

		for (Property property : modelPropertySet) {
			if (property instanceof PentagonNumberProperty) {
				try {
					nbPentagons = ((PentagonNumberProperty) property).computeUpperBound();
				} catch (Exception e) {
					System.err.println("[WARNING] Erreur lors de la lecture de nbpentagons.");
				}
			}

			if (property instanceof HeptagonNumberProperty) {
				try {
					nbHeptagons = ((HeptagonNumberProperty) property).computeUpperBound();
				} catch (Exception e) {
					System.err.println("[WARNING] Erreur lors de la lecture de nbheptagons.");
				}
			}
		}

		if (nbPentagons >= 0 && nbHeptagons >= 0) {
			k = Math.min(nbPentagons, nbHeptagons);
			System.out.println("[INFO] Calcul automatique de k = min(" + nbPentagons + ", " + nbHeptagons + ") = " + k);
		}

		model.setK(k);  // Injection de k dans le modèle

		return model;
	}


	/***
	 * Checks if any given model property allows to fix the model size
	 */
	private static boolean noLimitingProperties(ModelPropertySet modelPropertySet) {
		return !(modelPropertySet.has("hexagons")
				|| modelPropertySet.has("carbons")
				|| modelPropertySet.has("hydrogens")
				|| modelPropertySet.has("coronenoid")
				|| modelPropertySet.has("rectangle")
				|| modelPropertySet.has("rhombus")
				|| modelPropertySet.has("diameter")
				|| modelPropertySet.has("nbpentagons")
				|| modelPropertySet.has("nbheptagons"));
	}


	/***
	 *
	 * @param nbCrowns : number of crowns
	 * @return a basic model for generating benzenoids
	 */
	public static GeneralModel buildModel(ModelPropertySet modelPropertySet, int nbCrowns) {
		GeneralModel model = new GeneralModel(modelPropertySet, nbCrowns);
		for(Property modelProperty : modelPropertySet)
			if(modelProperty.hasExpressions())
				model.applyModelConstraint((ModelProperty) modelProperty);
		return model;
	}
}
