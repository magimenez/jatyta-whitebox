/**
 * 
 */
package py.una.pol.jatyta.richfaces.renderkit.html;

import org.richfaces.renderkit.CalendarRendererBase;

import py.una.pol.jatyta.faces.renderer.MicroDataRenderHelper;

import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.ajax4jsf.renderkit.ComponentsVariableResolver;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.renderkit.RendererUtils.ScriptHashVariableWrapper;

/**
 * Renderer thats CalendarRenderer for append micro data attributes in the
 * rich:calendar tag
 * 
 * @author Miguel Gimenez
 *
 */
public class MicroDataCalendarRenderer extends CalendarRendererBase {

	public MicroDataCalendarRenderer() {
		super();
	}

	//
	// Declarations
	//
	private final InternetResource[] scripts = { new org.ajax4jsf.javascript.PrototypeScript(),
			new org.ajax4jsf.javascript.AjaxScript(), getResource("/org/richfaces/renderkit/html/scripts/events.js"),
			getResource("/org/richfaces/renderkit/html/scripts/utils.js"),
			getResource("/org/richfaces/renderkit/html/scripts/json/json-dom.js"),
			getResource("/org/richfaces/renderkit/html/scripts/scriptaculous/effects.js"),
			getResource("/org/richfaces/renderkit/html/scripts/jquery/jquery.js"),
			getResource("/org/richfaces/renderkit/html/scripts/jquery/jquery.js"),
			getResource("/org/richfaces/renderkit/html/scripts/JQuerySpinBtn.js"),
			getResource("/org/richfaces/renderkit/html/scripts/calendar.js") };

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

	private final InternetResource[] styles = { getResource("/org/richfaces/renderkit/html/css/calendar.xcss") };

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
		return org.richfaces.component.UICalendar.class;
	}

	public void doEncodeEnd(ResponseWriter writer, FacesContext context, org.richfaces.component.UICalendar component,
			ComponentVariables variables) throws IOException {
		java.lang.String clientId = component.getClientId(context);
		addPopupToAjaxRendered(context, component);

		writer.startElement("span", component);
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "Popup");
		//
		// pass thru attributes
		//
		getUtils()
				.encodeAttributesFromArray(context, component,
						new String[] { "dir", "lang", "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup",
								"onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "title",
								"xml:lang" });
		//
		//
		//

		boolean popup = getUtils().isBooleanAttribute(component, "popup");
		if (!popup) {
			getUtils().writeAttribute(writer, "style", "display: none");

			writer.startElement("input", component);
			getUtils().writeAttribute(writer, "autocomplete", "off");
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "InputDate");
			getUtils().writeAttribute(writer, "name", convertToString(clientId) + "InputDate");
			getUtils().writeAttribute(writer, "style", "display:none");
			getUtils().writeAttribute(writer, "type", "hidden");
			getUtils().writeAttribute(writer, "value", getInputValue(context, component));

			// mgimenez: Microdata Atributes
			MicroDataRenderHelper.addMicroDataAttributes(component, writer);
			
			writer.endElement("input");

		} else {
			boolean disabled = getUtils().isBooleanAttribute(component, "disabled");
			boolean showInput = getUtils().isBooleanAttribute(component, "showInput");
			String onfieldclick = null;
			String type = "text";
			if (!showInput) {
				type = "hidden";
			}

			variables.setVariable("type", type);
			variables.setVariable("disabled", new Boolean(disabled));

			variables.setVariable("icon",
					getResource("org.richfaces.renderkit.html.iconimages.CalendarIcon").getUri(context, component));

			variables.setVariable("disabledIcon",
					getResource("org.richfaces.renderkit.html.iconimages.DisabledCalendarIcon").getUri(context,
							component));

			writer.startElement("input", component);
			getUtils().writeAttribute(writer, "accesskey", component.getAttributes().get("accesskey"));
			getUtils().writeAttribute(writer, "class",
					"rich-calendar-input " + convertToString(component.getAttributes().get("inputClass")));
			getUtils().writeAttribute(writer, "disabled", variables.getVariable("disabled"));
			getUtils().writeAttribute(writer, "id", convertToString(clientId) + "InputDate");
			getUtils().writeAttribute(writer, "maxlength", component.getAttributes().get("maxlength"));
			getUtils().writeAttribute(writer, "name", convertToString(clientId) + "InputDate");
			getUtils().writeAttribute(writer, "onblur", component.getAttributes().get("oninputblur"));
			getUtils().writeAttribute(writer, "onchange", component.getAttributes().get("oninputchange"));
			getUtils().writeAttribute(writer, "onclick", component.getAttributes().get("oninputclick"));
			getUtils().writeAttribute(writer, "onfocus", component.getAttributes().get("oninputfocus"));
			getUtils().writeAttribute(writer, "onkeydown", component.getAttributes().get("oninputkeydown"));
			getUtils().writeAttribute(writer, "onkeypress", component.getAttributes().get("oninputkeypress"));
			getUtils().writeAttribute(writer, "onkeyup", component.getAttributes().get("oninputkeyup"));
			getUtils().writeAttribute(writer, "onmouseout", component.getAttributes().get("oninputmouseout"));
			getUtils().writeAttribute(writer, "onmouseover", component.getAttributes().get("oninputmouseover"));
			getUtils().writeAttribute(writer, "onselect", component.getAttributes().get("oninputselect"));
			getUtils().writeAttribute(writer, "size", component.getAttributes().get("inputSize"));
			getUtils().writeAttribute(writer, "style",
					"vertical-align: middle; " + convertToString(component.getAttributes().get("inputStyle")));
			getUtils().writeAttribute(writer, "tabindex", component.getAttributes().get("tabindex"));
			getUtils().writeAttribute(writer, "type", variables.getVariable("type"));
			getUtils().writeAttribute(writer, "value", getInputValue(context, component));

			boolean manualInput = getUtils().isBooleanAttribute(component, "enableManualInput");
			boolean readonly = getUtils().isBooleanAttribute(component, "readonly");
			if (!manualInput || readonly) {
				getUtils().writeAttribute(writer, "readonly", "readonly");
			}
			
			// mgimenez: Microdata Atributes
			MicroDataRenderHelper.addMicroDataAttributes(component, writer);
			
			writer.endElement("input");

			String buttonIconAttr = (String) component.getAttributes().get("buttonIcon");
			String buttonIcon = buttonIconAttr == null || buttonIconAttr.length() < 1 ? null
					: org.richfaces.component.util.ViewUtil.getResourceURL(buttonIconAttr, context);
			String buttonIconDisabledAttr = (String) component.getAttributes().get("buttonIconDisabled");
			String buttonIconDisabled = buttonIconDisabledAttr == null || buttonIconDisabledAttr.length() < 1 ? null
					: org.richfaces.component.util.ViewUtil.getResourceURL(buttonIconDisabledAttr, context);
			String buttonLabel = (String) component.getAttributes().get("buttonLabel");
			variables.setVariable("buttonLabel", buttonLabel);
			variables.setVariable("buttonIconDisabled", buttonIconDisabled);
			variables.setVariable("buttonIcon", buttonIcon);
			if (buttonLabel == null || buttonLabel.length() == 0) {

				writer.startElement("img", component);
				getUtils().writeAttribute(writer, "accesskey", component.getAttributes().get("accesskey"));
				getUtils().writeAttribute(writer, "alt", "");
				getUtils().writeAttribute(writer, "class",
						"rich-calendar-button " + convertToString(component.getAttributes().get("buttonClass")));
				getUtils().writeAttribute(writer, "id", convertToString(clientId) + "PopupButton");
				getUtils().writeAttribute(writer, "style", "vertical-align: middle");
				getUtils().writeAttribute(writer, "tabindex", component.getAttributes().get("tabindex"));

				if (!disabled) {

					// getUtils().writeAttribute(writer, "onclick",
					// "$('"+clientId+"').component.doSwitch();");
					if (buttonIcon == null) {
						getUtils().writeAttribute(writer, "src", variables.getVariable("icon"));
					} else {
						getUtils().writeAttribute(writer, "src", buttonIcon);
					}
				} else {

					// getUtils().writeAttribute(writer, "onclick", null);
					if (buttonIconDisabled == null) {
						getUtils().writeAttribute(writer, "src", variables.getVariable("disabledIcon"));
					} else {
						getUtils().writeAttribute(writer, "src", buttonIconDisabled);
					}
				}

				writer.endElement("img");

			} else {

				writer.startElement("button", component);
				getUtils().writeAttribute(writer, "class",
						"rich-calendar-button " + convertToString(component.getAttributes().get("buttonClass")));
				getUtils().writeAttribute(writer, "disabled", variables.getVariable("disabled"));
				getUtils().writeAttribute(writer, "id", convertToString(clientId) + "PopupButton");
				getUtils().writeAttribute(writer, "name", convertToString(clientId) + "PopupButton");
				getUtils().writeAttribute(writer, "style", "vertical-align: middle");
				getUtils().writeAttribute(writer, "tabindex", component.getAttributes().get("tabindex"));
				getUtils().writeAttribute(writer, "type", "button");

				writer.writeText(convertToString(variables.getVariable("buttonLabel")), null);

				writer.endElement("button");

			}

		}

		java.util.Date currentDate = component.getCurrentDateOrDefault();
		writer.startElement("input", component);
		getUtils().writeAttribute(writer, "autocomplete", "off");
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "InputCurrentDate");
		getUtils().writeAttribute(writer, "name", convertToString(clientId) + "InputCurrentDate");
		getUtils().writeAttribute(writer, "style", "display:none");
		getUtils().writeAttribute(writer, "type", "hidden");
		getUtils().writeAttribute(writer, "value", getCurrentDateAsString(context, component, currentDate));

		writer.endElement("input");
		dayCellClass(context, component);

		writer.endElement("span");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "IFrame");
		getUtils().writeAttribute(writer, "style", "display: none;");

		writer.endElement("div");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "id", clientId);
		getUtils().writeAttribute(writer, "style", "display: none;");

		writer.endElement("div");
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "id", convertToString(clientId) + "Script");
		getUtils().writeAttribute(writer, "style", "display: none;");

		java.util.HashMap options = new java.util.HashMap((java.util.Map) getSymbolsMap(context, component));
		getUtils().addToScriptHash(options, "enableManualInput", component.getAttributes().get("enableManualInput"),
				null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "disabled", component.getAttributes().get("disabled"), null,
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "readonly", component.getAttributes().get("readonly"), null,
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "resetTimeOnDateSelect",
				component.getAttributes().get("resetTimeOnDateSelect"), null, ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "showApplyButton", component.getAttributes().get("showApplyButton"), null,
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "styleClass", component.getAttributes().get("styleClass"), null,
				ScriptHashVariableWrapper.DEFAULT);
		getUtils().addToScriptHash(options, "minDaysInFirstWeek", component.getAttributes().get("minDaysInFirstWeek"),
				null, ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "popup", component.getAttributes().get("popup"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "showInput", component.getAttributes().get("showInput"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "ajaxSingle", component.getAttributes().get("ajaxSingle"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "showHeader", component.getAttributes().get("showHeader"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "showFooter", component.getAttributes().get("showFooter"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "showWeeksBar", component.getAttributes().get("showWeeksBar"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "showWeekDaysBar", component.getAttributes().get("showWeekDaysBar"), "true",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "todayControlMode", component.getAttributes().get("todayControlMode"),
				"select", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "datePattern", component.getAttributes().get("datePattern"), "MMM d, yyyy",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "jointPoint", component.getAttributes().get("jointPoint"), "bottom-left",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "direction", component.getAttributes().get("direction"), "bottom-right",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "boundaryDatesMode", component.getAttributes().get("boundaryDatesMode"),
				"inactive", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "horizontalOffset", component.getAttributes().get("horizontalOffset"), "0",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "verticalOffset", component.getAttributes().get("verticalOffset"), "0",
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "currentDate", getCurrentDate(context, component, currentDate), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "selectedDate", getSelectedDate(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "style",
				"z-index: " + convertToString(component.getAttributes().get("zindex")) + "; "
						+ convertToString(component.getAttributes().get("style")),
				"z-index: 3; ", ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "submitFunction", getSubmitFunction(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "dayCellClass", getDayCellClass(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "dayStyleClass", getDayStyleClass(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "isDayEnabled", getIsDayEnabled(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "ondateselected", component.getAttributes().get("ondateselected"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "ondateselect", component.getAttributes().get("ondateselect"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "ontimeselect", component.getAttributes().get("ontimeselect"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "ontimeselected", component.getAttributes().get("ontimeselected"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "onchanged", component.getAttributes().get("onchanged"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "ondatemouseover", component.getAttributes().get("ondatemouseover"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "ondatemouseout", component.getAttributes().get("ondatemouseout"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "onexpand", component.getAttributes().get("onexpand"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "oncollapse", component.getAttributes().get("oncollapse"), null,
				ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "oncurrentdateselect", component.getAttributes().get("oncurrentdateselect"),
				null, ScriptHashVariableWrapper.EVENT_HANDLER);
		getUtils().addToScriptHash(options, "oncurrentdateselected",
				component.getAttributes().get("oncurrentdateselected"), null, ScriptHashVariableWrapper.EVENT_HANDLER);

		getUtils().addToScriptHash(options, "labels", getLabels(context, component), null,
				ScriptHashVariableWrapper.DEFAULT);

		getUtils().addToScriptHash(options, "defaultTime", getPreparedDefaultTime(component), null,
				ScriptHashVariableWrapper.DEFAULT);

		writer.startElement("script", component);
		getUtils().writeAttribute(writer, "type", "text/javascript");

		writeDefaultSymbols(context, component);

		writer.writeText(convertToString("new Calendar('" + convertToString(clientId) + "', \n			\""
				+ convertToString(component.getAsLocale()) + "\","), null);

		if (Boolean.valueOf(String.valueOf((!getUtils().isEmpty(options)))).booleanValue()) {

			org.ajax4jsf.javascript.ScriptUtils.writeToStream(writer, options);

		}
		if (Boolean.valueOf(String.valueOf(getUtils().isEmpty(options))).booleanValue()) {

			writer.writeText(convertToString("{}"), null);

		}
		writer.writeText(convertToString(","), null);

		writeFacetMarkup(context, component);

		writer.writeText(convertToString(").load("), null);

		/**/
		writePreloadBody(context, component);
		/**/
		writer.writeText(convertToString(");"), null);

		writer.endElement("script");
		writer.endElement("div");

	}

	public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		ComponentVariables variables = ComponentsVariableResolver.getVariables(this, component);
		doEncodeEnd(writer, context, (org.richfaces.component.UICalendar) component, variables);

		ComponentsVariableResolver.removeVariables(this, component);
	}

}
