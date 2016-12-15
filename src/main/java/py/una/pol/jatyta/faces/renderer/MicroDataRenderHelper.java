package py.una.pol.jatyta.faces.renderer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.una.pol.jatyta.annotation.JatytaAnnotations;
import py.una.pol.jatyta.el.ELHelper;

public class MicroDataRenderHelper {

	private static final String MICRODATA_HTML_ITEMPROP_ATTRIBUTE = "itemprop";
	private static final String MICRODATA_HTML_ITEMID_ATTRIBUTE = "itemid";
	private static final String MICRODATA_HTML_ITEMSCOPE_ATTRIBUTE = "itemscope";
	private static final String MICRODATA_HTML_ITEMTYPE_ATTRIBUTE = "itemtype";
	private static final String MICRODATA_HTML_ITEMREF_ATTRIBUTE = "itemref";

	

	private static final Logger LOG = LoggerFactory
			.getLogger(MicroDataRenderHelper.class);

	/**
	 *
	 * @param component
	 * @param responseWriter
	 * @throws IOException
	 * @autor mgimenez
	 */

	public static void addMicroDataAttributes(UIComponent component, ResponseWriter responseWriter) throws IOException{
		if (component instanceof UIForm) {

			// llamada recursiva
			List<Class<?>> classList = new ArrayList<>();
			classList = getChildrenExpressionBeanClass(component, classList);
			LOG.trace("Bean experssion Class count : " + classList.size());
			// se encontro una clase, ahora se verifica la anotacion en la misma
			if (classList.size() == 1) {
				JatytaAnnotations annotation = classList.get(0).getAnnotation(
						JatytaAnnotations.class);
				if (annotation != null) {
					if (annotation.itemscope()) {
						LOG.debug(" {} Class have itemscope Annotation", classList.get(0).getSimpleName());
						responseWriter.writeAttribute(
								MICRODATA_HTML_ITEMSCOPE_ATTRIBUTE, "", null);
					}

					if (annotation.itemtype() != null
							&& !annotation.itemtype().isEmpty()) {
						LOG.debug("{} Class have itemtype Annotation", classList.get(0).getSimpleName());
						responseWriter.writeAttribute(MICRODATA_HTML_ITEMTYPE_ATTRIBUTE,
								annotation.itemtype(), null);
					}

				}
			}
		} else if (component instanceof UIInput) {
			// verificar las anotaciones, y obtener el valor.
			// LOG.debug("Microdata Atribute name : " + attributeName);
			LOG.trace("UiComponent value expresion : "
					+ component.getValueExpression(ELHelper.VALUE_EXPRESION));
			if (component.getValueExpression(ELHelper.VALUE_EXPRESION) == null) {
				return;
			} else {
				Field field = ELHelper.getFieldByExpression(component
						.getValueExpression(ELHelper.VALUE_EXPRESION)
						.getExpressionString());
				if(field!=null){
					LOG.trace("Field name : " + field.getName());
					LOG.trace("Field Annotations size : "
							+ field.getAnnotations().length);
	
					JatytaAnnotations jatytaAnnotations = field
							.getAnnotation(JatytaAnnotations.class);
					if (jatytaAnnotations != null) {
	
						if (jatytaAnnotations.itemid() != null
								&& !jatytaAnnotations.itemid().isEmpty()) {
							LOG.debug("Field {} have itemid Annotation", field.getName());
							responseWriter.writeAttribute(MICRODATA_HTML_ITEMID_ATTRIBUTE,
									jatytaAnnotations.itemid(), null);
						}
	
						if (jatytaAnnotations.itemscope()) {
							LOG.debug("Field {} have itemscope Annotation", field.getName());
							responseWriter.writeAttribute(
									MICRODATA_HTML_ITEMSCOPE_ATTRIBUTE, "", null);
						}
	
						if (jatytaAnnotations.itemtype() != null
								&& !jatytaAnnotations.itemtype().isEmpty()) {
							LOG.debug("Field {} have itemtype Annotation", field.getName());
							responseWriter.writeAttribute(MICRODATA_HTML_ITEMTYPE_ATTRIBUTE,
									jatytaAnnotations.itemtype(), null);
						}
	
						if (jatytaAnnotations.itemref() != null
								&& !jatytaAnnotations.itemref().isEmpty()) {
							LOG.debug("Field {} have itemref Annotation", field.getName());
							responseWriter.writeAttribute(MICRODATA_HTML_ITEMREF_ATTRIBUTE,
									jatytaAnnotations.itemref(), null);
						}
	
						if (jatytaAnnotations.itemprop() != null
								&& !jatytaAnnotations.itemprop().isEmpty()) {
							LOG.debug("Field {} have itemprop Annotation", field.getName());
							responseWriter.writeAttribute(MICRODATA_HTML_ITEMPROP_ATTRIBUTE,
									jatytaAnnotations.itemprop(), null);
						}

					}
				}
			}
		}
	}
	
	/**
	 * Metodo recursivo para obtener las Clases de los expression beans de los
	 * componentes hijos del {@link UIComponent} parametro.
	 * 
	 * @param component
	 *            El {@link UIComponent} parametro.
	 * @param classList
	 *            El {@link List} de {@link Class} donde se agregaran las
	 *            clases.
	 * @return El {@link List} con las clases de los expression beans.
	 */
	private static List<Class<?>> getChildrenExpressionBeanClass(UIComponent component, List<Class<?>> classList) {
		LOG.trace("Component id : " + component.getId());
		List<UIComponent> children = component.getChildren();
		for (UIComponent uiComponent : children) {
			// TODO recursividad
			LOG.trace("Children UiComponent : " + uiComponent.toString());
			// TODO implementar recursivamente
			if (uiComponent.getValueExpression(ELHelper.VALUE_EXPRESION) != null) {
				Field field = ELHelper.getFieldByExpression(
						uiComponent.getValueExpression(ELHelper.VALUE_EXPRESION).getExpressionString());
				if (field != null) {
					Class<?> expClass = field.getDeclaringClass();
					LOG.trace("Children UiComponent Declared Class : " + expClass.getCanonicalName());
					if (!classList.contains(expClass)) {
						classList.add(expClass);
					}
				}
			}
			getChildrenExpressionBeanClass(uiComponent, classList);
		}
		return classList;

	}

	
}