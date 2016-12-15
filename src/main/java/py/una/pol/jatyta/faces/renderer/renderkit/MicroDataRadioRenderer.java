package py.una.pol.jatyta.faces.renderer.renderkit;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.RadioRenderer;
import com.sun.faces.util.RequestStateManager;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

/**
 * Renderer for add micro data attributes in Faces 1.X for UISelectOne or
 * UISelectMany Components.
 * 
 * @autor mgimenez
 */
public class MicroDataRadioRenderer extends RadioRenderer {

    private static final String[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.SELECTONERADIO);
	
    @Override
    protected void renderOption(FacesContext context,
                                UIComponent component,
                                Converter converter,
                                SelectItem curItem,
                                Object currentSelections,
                                Object[] submittedValues,
                                boolean alignVertical,
                                int itemNumber,
                                OptionComponentInfo optionInfo) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        UISelectOne selectOne = (UISelectOne) component;
        Object curValue = selectOne.getSubmittedValue();
        if (curValue == null) {
            curValue = selectOne.getValue();
        }

        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.startElement("tr", component);
            writer.writeText("\n", component, null);
        }

        Class type = String.class;
        if (curValue != null) {
            type = curValue.getClass();
        }
        Object itemValue = curItem.getValue();
        RequestStateManager.set(context,
                                RequestStateManager.TARGET_COMPONENT_ATTRIBUTE_NAME,
                                component);
        Object newValue;
        try {
            newValue = context.getApplication().getExpressionFactory().
                  coerceToType(itemValue, type);
        } catch (ELException ele) {
            newValue = itemValue;
        } catch (IllegalArgumentException iae) {
            // If coerceToType fails, per the docs it should throw
            // an ELException, however, GF 9.0 and 9.0u1 will throw
            // an IllegalArgumentException instead (see GF issue 1527).
            newValue = itemValue;
        }

        String labelClass;
        if (optionInfo.isDisabled() || curItem.isDisabled()) {
            labelClass = optionInfo.getDisabledClass();
        } else {
            labelClass = optionInfo.getEnabledClass();
        }
        writer.startElement("td", component);
        writer.writeText("\n", component, null);

        writer.startElement("input", component);
        writer.writeAttribute("type", "radio", "type");

        if (null != newValue && newValue.equals(curValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, null);
        }
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        String idString =
              component.getClientId(context) + NamingContainer.SEPARATOR_CHAR +
              Integer.toString(itemNumber);
        writer.writeAttribute("id", idString, "id");

        writer.writeAttribute("value",
                              (getFormattedValue(context, component,
                                      curItem.getValue(), converter)),
                              "value");

        // Don't render the disabled attribute twice if the 'parent'
        // component is already marked disabled.
        if (!optionInfo.isDisabled()) {
            if (curItem.isDisabled()) {
                writer.writeAttribute("disabled", true, "disabled");
            }
        }
        // Apply HTML 4.x attributes specified on UISelectMany component to all
        // items in the list except styleClass and style which are rendered as
        // attributes of outer most table.
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer,
                                                         component);

        //mgimenez: Add microdata attributes
        MicroDataRenderHelper.addMicroDataAttributes(component, writer);

        writer.endElement("input");
        writer.startElement("label", component);
        writer.writeAttribute("for", idString, "for");
        // if enabledClass or disabledClass attributes are specified, apply
        // it on the label.
        if (labelClass != null) {
            writer.writeAttribute("class", labelClass, "labelClass");
        }
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", component, null);
            if (!curItem.isEscape()) {
                // It seems the ResponseWriter API should
                // have a writeText() with a boolean property
                // to determine if it content written should
                // be escaped or not.
                writer.write(itemLabel);
            } else {
                writer.writeText(itemLabel, component, "label");
            }
        }
        writer.endElement("label");
        writer.endElement("td");
        writer.writeText("\n", component, null);
        if (alignVertical) {
            writer.writeText("\t", component, null);
            writer.endElement("tr");
            writer.writeText("\n", component, null);
        }
    }
    
}