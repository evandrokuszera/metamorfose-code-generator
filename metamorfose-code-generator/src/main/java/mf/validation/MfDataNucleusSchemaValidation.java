package mf.validation;

import mf.schema.model.MfEntity;
import mf.schema.model.MfRelationship;
import mf.schema.model.MfSchema;

public class MfDataNucleusSchemaValidation extends MfSchemaValidation {

    public MfDataNucleusSchemaValidation(MfSchema mfSchema) {
        super(mfSchema);
    }

    @Override
    public boolean validate() {
        return referenceRelationshipsValidation();
    }

    // Validates if the relationships are supported by the schema
    private boolean referenceRelationshipsValidation(){
        boolean isValid = true;

        for (MfRelationship rel : this.getMfSchema().getRelationships()){

            MfEntity oneEntity = this.getMfSchema().getEntity(rel.getOneSideClassMetadata());
            MfEntity manyEntity = this.getMfSchema().getEntity(rel.getManySideClassMetadata());

            boolean sideOne_isRootClassMetadata = (rel.getOneSideClassMetadata() == oneEntity.getRootClassMetadata());
            boolean sideMany_isRootClassMetadata = (rel.getManySideClassMetadata() == manyEntity.getRootClassMetadata());

            // Validation one
            if (!sideOne_isRootClassMetadata || !sideMany_isRootClassMetadata) {
                addAlert(
                        String.format("Relationship unsupported: %s[%s] -> %s[%s]",
                        manyEntity.getName(), rel.getManySideClassMetadata().getName(),
                        oneEntity.getName(), rel.getOneSideClassMetadata().getName())
                );
                addAlert(" Both entities need to be root entities in the schema (@Entity)");
                addAlert(" Tip: You could remove the relationship in the schema or change the generated code");
                isValid = false;
            }
        }

        if (isValid) addAlert("No alerts found");

        printAlerts();

        return isValid;
    }
}
