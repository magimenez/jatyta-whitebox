/**
 * 
 */
package py.una.pol.jatyta.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.ComponentsVariableResolver;
import org.ajax4jsf.renderkit.RendererUtils.ScriptHashVariableWrapper;
import org.ajax4jsf.resource.InternetResource;
import org.richfaces.renderkit.InplaceSelectBaseRenderer;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

/**
 * * Renderer that implements InplaceSelectBaseRenderer for append micro data
 * attributes in the rich:inplaceSelect
 * 
 * @author mgimenez
 *
 */
public class MicroDataInplaceSelectRenderer extends InplaceSelectBaseRenderer {

	public MicroDataInplaceSelectRenderer() {
		super();
	}

	//
	// Declarations
	//
	private final InternetResource[] styles = { getResource("css/inplaceselect.xcss") };

	private InternetResource[] stylesAll = null;

	protected InternetResource[] getStyles() {
		synchronized (this) {
			if (stylesAll == null) {
				InternetResource[] rsrcs = super.getStyles();
				boolean ignoreSuper = rsrcs == null || rsrcs.length == 0;
				boolean ignoreThis = styles == null || styles.length == 0;

				if (ignoreSuper) {
					if (ignoreThis) {
						stylesAll = new InternetResource[0];
					} else {
						stylesAll = styles;
					}
				} else {
					if (ignoreThis) {
						stylesAll = rsrcs;
					} else {
						java.util.Set rsrcsSet = new java.util.LinkedHashSet();

						for (int i = 0; i < rsrcs.length; i++) {
							rsrcsSet.add(rsrcs[i]);
						}

						for (int i = 0; i < styles.length; i++) {
							rsrcsSet.add(styles[i]);
						}

						stylesAll = (InternetResource[]) rsrcsSet.toArray(new InternetResource[rsrcsSet.size()]);
					}
				}
			}
		}

		return stylesAll;
	}

	private final InternetResource[] scripts = { new org.ajax4jsf.javascript.PrototypeScript(),
			getResource("/org/richfaces/renderkit/html/scripts/jquery/jquery.js"),
			getResource("scripts/comboboxUtils.js"), getResource("scripts/combolist.js"),
			getResource("scripts/inplaceinputstyles.js"), getResource("scripts/inplaceinput.js"),
			getResource("scripts/inplaceselectstyles.js"), getResource("scripts/inplaceselectlist.js"),
			getResource("scripts/inplaceselect.js"), getResource("/org/richfaces/renderkit/html/scripts/utils.js") };

	private InternetResource[] scriptsAll = null;

	protected InternetResource[] getScripts() {
		synchronized (this) {
			if (scriptsAll == null) {
				InternetResource[] rsrcs = super.getScripts();
				boolean ignoreSuper = rsrcs == null || rsrcs.length == 0;
				boolean ignoreThis = scripts == null || scripts.length == 0;

				if (ignoreSuper) {
					if (ignoreThis) {
						scriptsAll = new InternetResource[0];
					} else {
						scriptsAll = scripts;
					}
				} else {
					if (ignoreThis) {
						scriptsAll = rsrcs;
					} else {
						java.util.Set rsrcsSet = new java.util.LinkedHashSet();

						for (int i = 0; i < rsrcs.length; i++) {
							rsrcsSet.add(rsrcs[i]);
						}

						for (int i = 0; i < scripts.length; i++) {
							rsrcsSet.add(scripts[i]);
						}

						scriptsAll = (InternetResource[]) rsrcsSet.toArray(new InternetResource[rsrcsSet.size()]);
					}
				}
			}
		}

		return scriptsAll;
	}
	//
	//
	//

	private String convertToString(Object obj) {
		return (obj == null ? "" : obj.toString());
	}

	private String convertToString(boolean b) {
		return String.valueOf(b);
	}

	private String convertToString(int b) {
		return b != Integer.MIN_VALUE ? String.valueOf(b) : "";
	}

	private String convertToString(long b) {
		return b != Long.MIN_VALUE ? String.valueOf(b) : "";
	}

	/**
	 * Get base component class, targetted for this renderer. Used for check
	 * arguments in decode/encode.
	 * 
	 * @return
	 */
	protected Class getComponentClass() {
		return org.richfaces.component.UIInplaceSelect.class;
	}

	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			org.richfaces.component.UIInplaceSelect component, ComponentVariables variables) throws IOException {
		java.lang.String clientId = component.getClientId(context);
		variables.setVariable("saveIcon",
				getResource("org.richfaces.renderkit.html.images.SaveControlIcon").getUri(context, component));

		variables.setVariable("cancelIcon",
				getResource("org.richfaces.renderkit.html.images.CancelControlIcon").getUri(context, component));

		java.util.List preparedItems = prepareItems(context, component);

		Object value = null;
		String selectedItemLabel = null;

		Object submittedValue = component.getSubmittedValue();
		if (submittedValue != null) {
			if (preparedItems != null) {
				for (Object o : preparedItems) {
					PreparedSelectItem item = (PreparedSelectItem) o;
					if (submittedValue.equals(item.getConvertedValue())) {
						selectedItemLabel = item.getLabel();
						value = submittedValue;
						break;
					}
				}
			}
		} else {
			value = component.getAttributes().get("value");
			selectedItemLabel = getItemLabel(context, component, value);
			value = getConvertedStringValue(context, component, value);
		}

		String fieldValue = null;
		String fieldLabel = null;
		String fieldInputLabel = null;

		if (selectedItemLabel == null) {
			fieldValue = null;
			fieldLabel = createDefaultLabel(component);
			fieldInputLabel = "";
		} else {
			fieldValue = (String) value;
			fieldLabel = selectedItemLabel;
			fieldInputLabel = fieldLabel;
		}

		variables.setVariable("fieldLabel", fieldLabel);
		variables.setVariable("fieldValue", fieldValue);
		variables.setVariable("fieldInputLabel", fieldInputLabel);

		String saveIcon = (String) component.getAttributes().get("saveControlIcon");
		if (saveIcon != null && saveIcon.length() != 0) {
			variables.setVariable("saveIcon", org.richfaces.component.util.ViewUtil.getResourceURL(saveIcon, context));
		}

		String cancelIcon = (String) component.getAttributes().get("cancelControlIcon");
		if (cancelIcon != null && cancelIcon.length() != 0) {
			variables.setVariable("cancelIcon",
					org.richfaces.component.util.ViewUtil.getResourceURL(cancelIcon, context));
		}

		String controlClass = (String) component.getAttributes().get("controlClass");
		variables.setVariable("controlClass", controlClass);
		String controlHoveredClass = (String) component.getAttributes().get("controlHoveredClass");
		variables.setVariable("controlHoveredClass", controlHoveredClass);
		String controlPressedClass = (String) component.getAttributes().get("controlPressedClass");
		variables.setVariable("controlPressedClass", controlPressedClass);

		String styleClass = (String) component.getAttributes().get("styleClass");
		styleClass = styleClass != null ? styleClass.trim() : "";
		variables.setVariable("styleClass", styleClass);

		String changedClass = (String) component.getAttributes().get("changedClass");
		changedClass = (changedClass != null ? changedClass.trim() : "") + " " + styleClass;
		variables.setVariable("changedClass", changedClass.trim());

		String viewClass = (String) component.getAttributes().get("viewClass");
		viewClass = (viewClass != null ? viewClass.trim() : "") + " " + styleClass;
		variables.setVariable("viewClass", viewClass.trim());

		String editClass = (String) component.getAttributes().get("editClass");
		editClass = (editClass != null ? editClass.trim() : "") + " " + styleClass;
		variables.setVariable("editClass", editClass.trim());

		String layout = (String) component.getAttributes().get("layout");

		if (layout != null && layout.length() != 0) {
			if (!layout.equals("block")) {
				layout = "inline";
			}
		} else {
			layout = "inline";
		}

		variables.setVariable("spacer", getResource("images/spacer.gif").getUri(context, component));

		if (layout.equals("inline")) {

			writer.startElement("span", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select rich-inplace-select-view "
					+ convertToString(variables.getVariable("viewClass")));
			getUtils().writeAttribute(writer, "id", clientId);
			getUtils().writeAttribute(writer, "style",
					"zoom: 1; " + convertToString(component.getAttributes().get("style")));
			//
			// pass thru attributes
			//
			getUtils().encodeAttributesFromArray(context, component,
					new String[] { "dir", "lang", "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup",
							"onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "style", "title",
							"xml:lang" });
			//
			//
			//

		} else {

			writer.startElement("div", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select rich-inplace-select-view "
					+ convertToString(variables.getVariable("viewClass")));
			getUtils().writeAttribute(writer, "id", clientId);
			getUtils().writeAttribute(writer, "style",
					"zoom: 1; " + convertToString(component.getAttributes().get("style")));
			//
			// pass thru attributes
			//
			getUtils().encodeAttributesFromArray(context, component,
					new String[] { "align", "dir", "lang", "onclick", "ondblclick", "onkeydown", "onkeypress",
							"onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "style",
							"title", "xml:lang" });
			//
			//
			//

		}

		writer.startElement("input", component);
		getUtils().writeAttribute(writer, "autocomplete", "off");
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-field");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "tempValue");
		getUtils().writeAttribute(writer, "maxlength", component.getAttributes().get("inputMaxLength"));
		getUtils().writeAttribute(writer, "onblur", component.getAttributes().get("oninputblur"));
		getUtils().writeAttribute(writer, "onclick", component.getAttributes().get("oninputclick"));
		getUtils().writeAttribute(writer, "ondblclick", component.getAttributes().get("oninputdblclick"));
		getUtils().writeAttribute(writer, "onfocus", component.getAttributes().get("oninputfocus"));
		getUtils().writeAttribute(writer, "onkeydown", component.getAttributes().get("oninputkeydown"));
		getUtils().writeAttribute(writer, "onkeypress", component.getAttributes().get("oninputkeypress"));
		getUtils().writeAttribute(writer, "onkeyup", component.getAttributes().get("oninputkeyup"));
		getUtils().writeAttribute(writer, "onmousedown", component.getAttributes().get("oninputmousedown"));
		getUtils().writeAttribute(writer, "onmousemove", component.getAttributes().get("oninputmousemove"));
		getUtils().writeAttribute(writer, "onmouseout", component.getAttributes().get("oninputmouseout"));
		getUtils().writeAttribute(writer, "onmouseover", component.getAttributes().get("oninputmouseover"));
		getUtils().writeAttribute(writer, "onmouseup", component.getAttributes().get("oninputmouseup"));
		getUtils().writeAttribute(writer, "onselect", component.getAttributes().get("onselect"));
		getUtils().writeAttribute(writer, "readonly", "readonly");
		getUtils().writeAttribute(writer, "style", "clip:rect(0px 0px 0px 0px)");
		getUtils().writeAttribute(writer, "type", "text");
		getUtils().writeAttribute(writer, "value", variables.getVariable("fieldInputLabel"));
		
		writer.endElement("input");
		writer.startElement("input", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-arrow");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "inselArrow");
		getUtils().writeAttribute(writer, "readonly", "readonly");
		getUtils().writeAttribute(writer, "style", "display:none;");
		getUtils().writeAttribute(writer, "type", "text");
		getUtils().writeAttribute(writer, "value", "");

		writer.endElement("input");
		writer.startElement("input", component);
		getUtils().writeAttribute(writer, "autocomplete", "off");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "value");
		getUtils().writeAttribute(writer, "name", clientId);
		getUtils().writeAttribute(writer, "onchange", component.getAttributes().get("onchange"));
		getUtils().writeAttribute(writer, "type", "hidden");
		getUtils().writeAttribute(writer, "value", variables.getVariable("fieldValue"));

		// mgimenez: Microdata Atributes
		MicroDataRenderHelper.addMicroDataAttributes(component, writer);
		
		writer.endElement("input");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-control-set");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "bar");
		getUtils().writeAttribute(writer, "style", "display:none;");

		if (isControlsFacetExists(context, component)) {
			encodeControlsFacet(context, component);
		} else {

			writer.startElement("div", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow");
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "btns_shadow");

			writer.startElement("table", component);
			getUtils().writeAttribute(writer, "border", "0");
			getUtils().writeAttribute(writer, "cellpadding", "0");
			getUtils().writeAttribute(writer, "cellspacing", "0");
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-size");

			writer.startElement("tr", component);

			writer.startElement("td", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-tl");

			writer.startElement("img", component);
			getUtils().writeAttribute(writer, "alt", "");
			getUtils().writeAttribute(writer, "height", "1");
			getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
			getUtils().writeAttribute(writer, "style", "border:0");
			getUtils().writeAttribute(writer, "width", "10");

			writer.endElement("img");
			writer.startElement("br", component);

			writer.endElement("br");
			writer.endElement("td");
			writer.startElement("td", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-tr");

			writer.startElement("img", component);
			getUtils().writeAttribute(writer, "alt", "");
			getUtils().writeAttribute(writer, "height", "10");
			getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
			getUtils().writeAttribute(writer, "style", "border:0");
			getUtils().writeAttribute(writer, "width", "1");

			writer.endElement("img");
			writer.startElement("br", component);

			writer.endElement("br");
			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", component);

			writer.startElement("td", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-bl");

			writer.startElement("img", component);
			getUtils().writeAttribute(writer, "alt", "");
			getUtils().writeAttribute(writer, "height", "10");
			getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
			getUtils().writeAttribute(writer, "style", "border:0");
			getUtils().writeAttribute(writer, "width", "1");

			writer.endElement("img");
			writer.startElement("br", component);

			writer.endElement("br");
			writer.endElement("td");
			writer.startElement("td", component);
			getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-br");

			writer.startElement("img", component);
			getUtils().writeAttribute(writer, "alt", "");
			getUtils().writeAttribute(writer, "height", "1");
			getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
			getUtils().writeAttribute(writer, "style", "border:0");
			getUtils().writeAttribute(writer, "width", "10");

			writer.endElement("img");
			writer.startElement("br", component);

			writer.endElement("br");
			writer.endElement("td");
			writer.endElement("tr");
			writer.endElement("table");
			writer.endElement("div");
			writer.startElement("div", component);
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "buttons");
			getUtils().writeAttribute(writer, "style", "position : relative; width: 1px");

			writer.startElement("input", component);
			getUtils().writeAttribute(writer, "class",
					"rich-inplace-select-control " + convertToString(variables.getVariable("controlClass")));
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "ok");
			getUtils().writeAttribute(writer, "onmousedown", "this.className='rich-inplace-select-control-press "
					+ convertToString(variables.getVariable("controlPressedClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseout", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseover", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlHoverClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseup", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlClass")) + "'");
			getUtils().writeAttribute(writer, "src", variables.getVariable("saveIcon"));
			getUtils().writeAttribute(writer, "type", "image");

			writer.endElement("input");
			writer.startElement("input", component);
			getUtils().writeAttribute(writer, "class",
					"rich-inplace-select-control " + convertToString(variables.getVariable("controlClass")));
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "cancel");
			getUtils().writeAttribute(writer, "onmousedown", "this.className='rich-inplace-select-control-press "
					+ convertToString(variables.getVariable("controlPressedClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseout", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseover", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlHoverClass")) + "'");
			getUtils().writeAttribute(writer, "onmouseup", "this.className='rich-inplace-select-control "
					+ convertToString(variables.getVariable("controlClass")) + "'");
			getUtils().writeAttribute(writer, "src", variables.getVariable("cancelIcon"));
			getUtils().writeAttribute(writer, "type", "image");

			writer.endElement("input");
			writer.endElement("div");

		}

		writer.endElement("div");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-width-list");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "listParent");
		getUtils().writeAttribute(writer, "style",
				"display: none; position : absolute; height : 100px; left : 0px; top: 13px; z-index:1000;");

		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-list-shadow");

		writer.writeComment(convertToString(" TODO welcome magic numbers! "));

		writer.startElement("table", component);
		getUtils().writeAttribute(writer, "border", "0");
		getUtils().writeAttribute(writer, "cellpadding", "0");
		getUtils().writeAttribute(writer, "cellspacing", "0");
		getUtils().writeAttribute(writer, "height", "109");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "shadow");
		getUtils().writeAttribute(writer, "width", "257");

		writer.startElement("tr", component);

		writer.startElement("td", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-tl");

		writer.startElement("img", component);
		getUtils().writeAttribute(writer, "alt", "");
		getUtils().writeAttribute(writer, "height", "1");
		getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
		getUtils().writeAttribute(writer, "style", "border:0");
		getUtils().writeAttribute(writer, "width", "10");

		writer.endElement("img");
		writer.startElement("br", component);

		writer.endElement("br");
		writer.endElement("td");
		writer.startElement("td", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-tr");

		writer.startElement("img", component);
		getUtils().writeAttribute(writer, "alt", "");
		getUtils().writeAttribute(writer, "height", "10");
		getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
		getUtils().writeAttribute(writer, "style", "border:0");
		getUtils().writeAttribute(writer, "width", "1");

		writer.endElement("img");
		writer.startElement("br", component);

		writer.endElement("br");
		writer.endElement("td");
		writer.endElement("tr");
		writer.startElement("tr", component);

		writer.startElement("td", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-bl");

		writer.startElement("img", component);
		getUtils().writeAttribute(writer, "alt", "");
		getUtils().writeAttribute(writer, "height", "10");
		getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
		getUtils().writeAttribute(writer, "style", "border:0");
		getUtils().writeAttribute(writer, "width", "1");

		writer.endElement("img");
		writer.startElement("br", component);

		writer.endElement("br");
		writer.endElement("td");
		writer.startElement("td", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-shadow-br");

		writer.startElement("img", component);
		getUtils().writeAttribute(writer, "alt", "");
		getUtils().writeAttribute(writer, "height", "10");
		getUtils().writeAttribute(writer, "src", variables.getVariable("spacer"));
		getUtils().writeAttribute(writer, "style", "border:0");
		getUtils().writeAttribute(writer, "width", "10");

		writer.endElement("img");
		writer.startElement("br", component);

		writer.endElement("br");
		writer.endElement("td");
		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-list-position");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "listPosition");

		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-list-decoration");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "listDecoration");

		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-inplace-select-list-scroll");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "list");

		encodeItems(context, component, preparedItems);

		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		java.util.HashMap attributes = new java.util.HashMap();
		getUtils().addToScriptHash(attributes, "defaultLabel", getDefaultLabel(context, component),
				"\u00a0\u00a0\u00a0", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "showControls", component.getAttributes().get("showControls"), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "editEvent", component.getAttributes().get("editEvent"), "onclick",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "verticalPosition",
				component.getAttributes().get("controlsVerticalPosition"), "center", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "horizontalPosition",
				component.getAttributes().get("controlsHorizontalPosition"), "right",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "inputWidth", component.getAttributes().get("selectWidth"), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "minInputWidth", component.getAttributes().get("minSelectWidth"),
				"100px", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "maxInputWidth", component.getAttributes().get("maxSelectWidth"),
				"200px", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(attributes, "openOnEdit", component.getAttributes().get("openOnEdit"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap events = new java.util.HashMap();
		getUtils().addToScriptHash(events, "oneditactivation", component.getAttributes().get("oneditactivation"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(events, "onviewactivation", component.getAttributes().get("onviewactivation"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(events, "oneditactivated", component.getAttributes().get("oneditactivated"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(events, "onviewactivated", component.getAttributes().get("onviewactivated"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(events, "onchange", component.getAttributes().get("onchange"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);

		java.util.HashMap view = new java.util.HashMap();
		getUtils().addToScriptHash(view, "normal", variables.getVariable("viewClass"), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(view, "hovered", component.getAttributes().get("viewHoverClass"), null,
				ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap changed = new java.util.HashMap();
		getUtils().addToScriptHash(changed, "normal", variables.getVariable("changedClass"), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(changed, "hovered", component.getAttributes().get("changedHoverClass"), null,
				ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap componentStyles = new java.util.HashMap();
		getUtils().addToScriptHash(componentStyles, "view", view, null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(componentStyles, "changed", changed, null, ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(componentStyles, "editable", variables.getVariable("editClass"), null,
				ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap userStyles = new java.util.HashMap();
		getUtils().addToScriptHash(userStyles, "component", componentStyles, null, ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap listOptions = new java.util.HashMap();
		getUtils().addToScriptHash(listOptions, "listWidth", component.getAttributes().get("listWidth"), "200px",
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(listOptions, "listHeight", component.getAttributes().get("listHeight"), "200px",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(listOptions, "itemsText", preparedItems, null, ScriptHashVariableWrapper.DEFAULT);

		java.util.HashMap options = new java.util.HashMap();
		getUtils().addToScriptHash(options, "showValueInView", component.getAttributes().get("showValueInView"), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "attributes", attributes, null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "events", events, null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "userStyles", userStyles, null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "fieldValue", variables.getVariable("fieldValue"), null,
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "listOptions", listOptions, null, ScriptHashVariableWrapper.DEFAULT);

		writer.startElement("script", component);
		getUtils().writeAttribute(writer, "type", "text/javascript");

		writer.writeText(convertToString("new Richfaces.InplaceSelect('" + convertToString(clientId) + "'"), null);

		if (Boolean.valueOf(String.valueOf((!getUtils().isEmpty(options)))).booleanValue()) {

			writer.writeText(convertToString(","), null);

			org.ajax4jsf.javascript.ScriptUtils.writeToStream(writer, options);

		}
		writer.writeText(convertToString(");"), null);

		writer.endElement("script");
		writer.endElement("div");

		if (isEmptyDefaultLabel(convertToString(variables.getVariable("fieldLabel")))) {
			writer.write(convertToString(variables.getVariable("fieldLabel")));
		} else {
			writer.writeText(convertToString(variables.getVariable("fieldLabel")), null);
		}

		if (!layout.equals("inline")) {

			writer.endElement("div");

		} else {

			writer.endElement("span");

		}

	}

	public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		ComponentVariables variables = ComponentsVariableResolver.getVariables(this, component);
		doEncodeEnd(writer, context, (org.richfaces.component.UIInplaceSelect) component, variables);

		ComponentsVariableResolver.removeVariables(this, component);
	}

}
