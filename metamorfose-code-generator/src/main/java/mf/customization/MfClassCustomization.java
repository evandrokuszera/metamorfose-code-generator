/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.customization;

import mf.schema.model.MfEntity;
import mf.schema.model.MfSchema;

/**
 *
 * @author evand
 */
public abstract class MfClassCustomization {
    public abstract MfEntity applyCustomizationsTo(MfEntity entityClassMetadata);
    public abstract MfSchema applyCustomizationsTo(MfSchema schema);
}