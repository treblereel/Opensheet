/*******************************************************************************
 * Copyright (c) 2012 Dmitry Tikhomirov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Dmitry Tikhomirov - initial API and implementation
 ******************************************************************************/
package org.opensheet.client.utils;

import com.extjs.gxt.ui.client.binding.Converter;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 *
 * @author akartal
 * 
 * 
 * 
 */

public class ComboboxFieldBinding extends FieldBinding {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ComboboxFieldBinding(Field field, String property) {
        super(field, property);
        if(field instanceof ComboBox){
            final ComboBox<ModelData> combo = (ComboBox<ModelData>)field;
            setConverter(new Converter() {

                @Override
                public Object convertFieldValue(Object value) {
                    if(value instanceof ModelData){
                        ModelData val = (ModelData)value;
                        return val.get(combo.getValueField());
                    }
                    else{
                        return value;
                    }
                }

                @Override
                public Object convertModelValue(Object value) {
                    return combo.getStore().findModel(combo.getValueField(), value);
                }

            });
        }
    }
        
}
