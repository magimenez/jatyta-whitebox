package py.una.pol.jatyta.el;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @autor mgimenez
 *
 */
public class ELHelper {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(ELHelper.class);

	/*
	 * ATRIBUTOS DEL EL HELPER
	 */
	private static final String CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION = "Can not find the field of the expression: {}";
	public static final String EXTRACT_FIELD_FROM_EXPRESSION_REGEX = "(#\\{.*)\\.(.*)(\\})";
	private static Pattern pattern;
	
	public static final String VALUE_EXPRESION = "value";

	/**
	 * Dada una expresion del tipo
	 * 
	 * <pre>
	 * #{controller.bean.field}
	 * </pre>
	 * 
	 * Retorna el Field donde se almacenara el campo field, notar que es a nivel
	 * de Field, es decir, requiere que el getter y el setter, tengan el mismo
	 * nombre (getField, setField), no sirve para campos que no cumplan esta
	 * condicion.
	 * 
	 * @param beanExpression
	 *            expresion con el formato de
	 *            {@link #EXTRACT_FIELD_FROM_EXPRESSION_REGEX}
	 * @return {@link Field} del atributo
	 * @since 1.3.2
	 */
	public static Field getFieldByExpression(String beanExpression) {
		// abre y cierra un parentesis, se utiliza para definir si utiliza algun
		// metodo.
		if (beanExpression.indexOf('(') != -1 && beanExpression.indexOf(')') != -1) {
			return null;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		Matcher ma = getPattern().matcher(beanExpression);
		if (ma.matches()) {
			String withoutField = ma.replaceFirst("$1$3");
			String field = ma.replaceFirst("$2");
			Object bean;
			try {
				bean = context.getApplication().getExpressionFactory()
						.createValueExpression(context.getELContext(), withoutField, Object.class)
						.getValue(context.getELContext());
			} catch (ELException el) {
				// TODO mejorar para tener en cuenta cuando la expresion se
				// refiere a un Vector.
				// Utilizar la expresion regular
				// (#\{.*)\.([a-zA-Z]*|[a-zA-Z]*\[[a-zA-Z\.]*\])(\})
				// See http://www.regexplanet.com/advanced/java/index.html
				//LOG.error(CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION, beanExpression, el);
				return null;
			}
			if (bean != null) {
				Field f;
				try {
					f = bean.getClass().getDeclaredField(field);
				} catch (Exception e) {
					try {
						//LOG.error("Can't get field by reflection, trying using proxies", e);
						// TODO ver si es necesario implementar con CGLIB
						// f = getFieldForce(field, bean.getClass());
						f = null;
					} catch (Exception e2) {
						//LOG.error(CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION, beanExpression, e2);
						return null;
					}
				}
				return f;
			}
		}
		return null;
	}

	private static synchronized Pattern getPattern() {
		if (pattern == null) {
			pattern = Pattern.compile(EXTRACT_FIELD_FROM_EXPRESSION_REGEX);
		}
		return pattern;
	}

	
}