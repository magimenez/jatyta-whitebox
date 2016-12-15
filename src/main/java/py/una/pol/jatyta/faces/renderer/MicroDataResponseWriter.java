package py.una.pol.jatyta.faces.renderer;

import java.io.IOException;
import java.io.Writer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicroDataResponseWriter extends ResponseWriterWrapper {

	private static final Logger LOG = LoggerFactory
			.getLogger(MicroDataResponseWriter.class);
	private ResponseWriter wrapped;

	public MicroDataResponseWriter(ResponseWriter wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ResponseWriter cloneWithWriter(Writer writer) {
		return new MicroDataResponseWriter(super.cloneWithWriter(writer));
	}

	@Override
	public void startElement(String name, UIComponent component)
			throws IOException {
		super.startElement(name, component);
		MicroDataRenderHelper.addMicroDataAttributes(component, wrapped);
	}

	@Override
	public ResponseWriter getWrapped() {
		return wrapped;
	}
}