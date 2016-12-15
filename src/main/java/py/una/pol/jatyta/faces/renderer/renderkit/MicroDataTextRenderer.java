package py.una.pol.jatyta.faces.renderer.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.TextRenderer;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

/**
 * Renderer for add micro data attributes in Faces 1.X for UIInput and UIOutput
 * Components.
 * 
 * @autor mgimenez
 */
public class MicroDataTextRenderer extends TextRenderer {

	private static final String[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);
	private static final String[] OUTPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.OUTPUTTEXT);

	@Override
	protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue)
			throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		assert (writer != null);
		boolean shouldWriteIdAttribute = false;
		boolean isOutput = false;

		String style = (String) component.getAttributes().get("style");
		String styleClass = (String) component.getAttributes().get("styleClass");
		String dir = (String) component.getAttributes().get("dir");
		String lang = (String) component.getAttributes().get("lang");
		String title = (String) component.getAttributes().get("title");
		if (component instanceof UIInput) {
			writer.startElement("input", component);
			writeIdAttributeIfNecessary(context, writer, component);
			writer.writeAttribute("type", "text", null);
			writer.writeAttribute("name", (component.getClientId(context)), "clientId");

			// only output the autocomplete attribute if the value
			// is 'off' since its lack of presence will be interpreted
			// as 'on' by the browser
			if ("off".equals(component.getAttributes().get("autocomplete"))) {
				writer.writeAttribute("autocomplete", "off", "autocomplete");
			}

			// render default text specified
			if (currentValue != null) {
				writer.writeAttribute("value", currentValue, "value");
			}
			if (null != styleClass) {
				writer.writeAttribute("class", styleClass, "styleClass");
			}

			// style is rendered as a passthur attribute
			RenderKitUtils.renderPassThruAttributes(writer, component, INPUT_ATTRIBUTES);
			RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

			// mgimenez: add the microdata attributes
			MicroDataRenderHelper.addMicroDataAttributes(component, writer);

			writer.endElement("input");

		} else if (isOutput = (component instanceof UIOutput)) {
			if (styleClass != null || style != null || dir != null || lang != null || title != null
					|| (shouldWriteIdAttribute = shouldWriteIdAttribute(component))) {
				writer.startElement("span", component);
				writeIdAttributeIfNecessary(context, writer, component);
				if (null != styleClass) {
					writer.writeAttribute("class", styleClass, "styleClass");
				}
				// style is rendered as a passthru attribute
				RenderKitUtils.renderPassThruAttributes(writer, component, OUTPUT_ATTRIBUTES);

			}
			if (currentValue != null) {
				Object val = component.getAttributes().get("escape");
				if ((val != null) && Boolean.valueOf(val.toString())) {
					writer.writeText(currentValue, component, "value");
				} else {
					writer.write(currentValue);
				}
			}
		}
		if (isOutput && (styleClass != null || style != null || dir != null || lang != null || title != null
				|| (shouldWriteIdAttribute))) {
			writer.endElement("span");
		}

	}
}