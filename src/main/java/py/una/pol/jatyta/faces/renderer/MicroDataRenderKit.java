package py.una.pol.jatyta.faces.renderer;

import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;

public class MicroDataRenderKit extends RenderKitWrapper {

	private RenderKit wrapped;

	public MicroDataRenderKit(RenderKit wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ResponseWriter createResponseWriter(Writer writer,
			String contentTypeList, String characterEncoding) {
		return new MicroDataResponseWriter(super.createResponseWriter(writer,
				contentTypeList, characterEncoding));
	}

	@Override
	public RenderKit getWrapped() {
		return wrapped;
	}

}
