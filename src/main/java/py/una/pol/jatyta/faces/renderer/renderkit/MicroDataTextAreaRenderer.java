package py.una.pol.jatyta.faces.renderer.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.TextareaRenderer;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

/**
 * Renderer for add micro data attributes in Faces 1.X for UIInput component as
 * a Textarea.
 * 
 * @autor mgimenez
 */
public class MicroDataTextAreaRenderer extends TextareaRenderer {

    private static final String[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXTAREA);
	

    @Override
    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String styleClass =
              (String) component.getAttributes().get("styleClass");

        writer.startElement("textarea", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");
        if (null != styleClass) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        //mgimenez: add the microdata attributes.
    	MicroDataRenderHelper.addMicroDataAttributes(component, writer);
        
        // render default text specified
        if (currentValue != null) {
            writer.writeText(currentValue, component, "value");
        }

        writer.endElement("textarea");

    }
}