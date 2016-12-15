package py.una.pol.jatyta.faces.renderer.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.SecretRenderer;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

/**
 * Renderer for add micro data attributes in Faces 1.X for UIInput component as
 * a password field.
 * 
 * @autor mgimenez
 */
public class MicroDataSecretRenderer extends SecretRenderer {
	
    private static final String[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.INPUTSECRET);

	@Override
	public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
		super.encodeBegin(facesContext, component);
		final ResponseWriter writer = facesContext.getResponseWriter();
		MicroDataRenderHelper.addMicroDataAttributes(component, writer);

	}

	 @Override
	    protected void getEndTextToRender(FacesContext context,
	                                      UIComponent component,
	                                      String currentValue)
	          throws IOException {

	        ResponseWriter writer = context.getResponseWriter();
	        assert(writer != null);

	        String redisplay = String.valueOf(component.getAttributes().get("redisplay"));
	        if (redisplay == null || !redisplay.equals("true")) {
	            currentValue = "";
	        }

	        writer.startElement("input", component);
	        writeIdAttributeIfNecessary(context, writer, component);
	        writer.writeAttribute("type", "password", "type");
	        writer.writeAttribute("name", component.getClientId(context),
	                              "clientId");

	        String autoComplete = (String)
	              component.getAttributes().get("autocomplete");
	        if (autoComplete != null) {
	            // only output the autocomplete attribute if the value
	            // is 'off' since its lack of presence will be interpreted
	            // as 'on' by the browser
	            if ("off".equals(autoComplete)) {
	                writer.writeAttribute("autocomplete",
	                                      "off",
	                                      "autocomplete");
	            }
	        }

	        // render default text specified
	        if (currentValue != null) {
	            writer.writeAttribute("value", currentValue, "value");
	        }

	        RenderKitUtils.renderPassThruAttributes(writer,
	                                                component,
	                                                ATTRIBUTES);
	        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
	        
	        
	        
	        String styleClass;
	        if (null != (styleClass = (String)
	              component.getAttributes().get("styleClass"))) {
	            writer.writeAttribute("class", styleClass, "styleClass");
	        }
	        
	        //mgimenez: add the microdata attributes.
	        MicroDataRenderHelper.addMicroDataAttributes(component, writer);

	        writer.endElement("input");

	    }
}