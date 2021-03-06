/*
 * � Copyright IBM Corp. 2010, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.xsp.extlib.component.sametime;

import com.ibm.xsp.extlib.component.dojo.UIDojoWidget;
import com.ibm.xsp.stylekit.ThemeControl;


/**
 * Sametime widget.
 * <p>
 * This component is a simple wrapper for the sametime dojo widget. The component is actually generated
 * uniquely if the sametime client is enabled.
 * </p>
 */
public class UISametimeWidget extends UIDojoWidget implements ThemeControl {
    
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.SametimeWidget"; //$NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.UISametimeWidget"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.Sametime"; //$NON-NLS-1$

    public UISametimeWidget() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getStyleKitFamily() {
        return "Sametime.Widget"; // $NON-NLS-1$
    }
    
//    @Override
//    public void restoreState(FacesContext _context, Object _state) {
//        Object _values[] = (Object[]) _state;
//        super.restoreState(_context, _values[0]);
//        this.endpoint = (String)_values[1];
//    }
//
//    @Override
//    public Object saveState(FacesContext _context) {
//        Object _values[] = new Object[2];
//        _values[0] = super.saveState(_context);
//        _values[1] = endpoint;
//       return _values;
//    }
}