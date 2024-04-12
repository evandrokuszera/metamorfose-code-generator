package mf.validation;

import mf.schema.model.MfSchema;

import java.util.ArrayList;

public abstract class MfSchemaValidation {

    private MfSchema mfSchema;
    private ArrayList<String> alerts = new ArrayList<>();

    public MfSchemaValidation(MfSchema mfSchema) {
        this.mfSchema = mfSchema;
    }

    // You should implement this method to validate the schema against the target ONM features
    public abstract boolean validate();

    public MfSchema getMfSchema() {
        return mfSchema;
    }

    public void addAlert(String alert){
        this.alerts.add("MfValidation:  " + alert);
    }

    public void printAlerts(){
        System.out.println("MfValidation: ----------------------------------------------------------------------------------------------------------------");
        System.out.printf("MfValidation: Schema Validation Alerts (%s):\n", this.getClass().getSimpleName());
        alerts.forEach(System.out::println);
        System.out.println("MfValidation: ----------------------------------------------------------------------------------------------------------------");
    }

}
