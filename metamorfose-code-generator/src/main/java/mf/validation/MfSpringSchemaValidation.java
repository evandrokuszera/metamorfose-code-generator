package mf.validation;

import mf.schema.model.MfEntity;
import mf.schema.model.MfRelationship;
import mf.schema.model.MfSchema;

public class MfSpringSchemaValidation extends MfSchemaValidation {

    public MfSpringSchemaValidation(MfSchema mfSchema) {
        super(mfSchema);
    }

    @Override
    public boolean validate() {
        return validateRelationships();
    }
    private boolean validateRelationships() {
        boolean isValid = true;

        for (MfRelationship rel : this.getMfSchema().getRelationships()){

            // Retrieve both entities from relationship ClassMetadata object
            MfEntity oneEntity = this.getMfSchema().getEntity(rel.getOneSideClassMetadata());
            MfEntity manyEntity = this.getMfSchema().getEntity(rel.getManySideClassMetadata());

            // Verify if the ClassMetadata is a root entity
            boolean sideOne_isRootClassMetadata = (rel.getOneSideClassMetadata() == oneEntity.getRootClassMetadata());
            boolean sideMany_isRootClassMetadata = (rel.getManySideClassMetadata() == manyEntity.getRootClassMetadata());

            // Validation one
            if (!sideOne_isRootClassMetadata && !sideMany_isRootClassMetadata) {
                addAlert(
                        String.format("Relationship unsupported in direction: %s[%s] -> %s[%s]",
                        manyEntity.getName(), rel.getManySideClassMetadata().getName(),
                        oneEntity.getName(), rel.getOneSideClassMetadata().getName())
                );
                addAlert(" Both entities are not root entities in the schema");
                addAlert(" Tip: You could remove the relationship in the schema or change the generated code");
                isValid = false;
                continue;
                //printAlerts();
                //return false;
            }

            // Validation two: Test in the OneEntity --> ManyEntity direction
            if (sideOne_isRootClassMetadata && !sideMany_isRootClassMetadata){
                addAlert(
                        String.format("Relationship unsupported in direction: %s[%s] -> %s[%s]",
                        oneEntity.getName(), rel.getOneSideClassMetadata().getName(),
                        manyEntity.getName(), rel.getManySideClassMetadata().getName())
                );
                addAlert(String.format(" %s is not a root entity in the schema", rel.getManySideClassMetadata().getName()));
                addAlert(" Tip: You could remove the relationship in the schema or change the generated code");
                isValid = false;
                continue;
                //printAlerts();
                //return false;
            }

            // Validation three: Test in the ManyEntity --> OneEntity direction
            if (sideMany_isRootClassMetadata && !sideOne_isRootClassMetadata){
                addAlert(
                        String.format("Relationship unsupported in direction: %s[%s] -> %s[%s]",
                        manyEntity.getName(), rel.getManySideClassMetadata().getName(),
                        oneEntity.getName(), rel.getOneSideClassMetadata().getName())
                );
                addAlert(String.format(" %s is not a root entity in the schema", rel.getOneSideClassMetadata().getName()));
                addAlert(" Tip: You could remove the relationship in the schema or change the generated code");
                isValid = false;
                continue;
                //printAlerts();
                //return false;
            }
        } // end for

        if (isValid) addAlert("No alerts found");

        printAlerts();

        return isValid;
    }
}
