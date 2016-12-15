package py.una.pol.jatyta.faces.renderer;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

/**
 *
 * RenderKitFactory to add micro data attibutes in Faces 2.X.
 * @autor mgimenez
 */
public class MicroDataRenderKitFactory extends RenderKitFactory {

	private RenderKitFactory wrapped;

	public MicroDataRenderKitFactory(RenderKitFactory wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void addRenderKit(String renderKitId, RenderKit renderKit) {
		wrapped.addRenderKit(renderKitId, renderKit);
	}

	@Override
	public RenderKit getRenderKit(FacesContext context, String renderKitId) {
		RenderKit renderKit = wrapped.getRenderKit(context, renderKitId);
		return (HTML_BASIC_RENDER_KIT.equals(renderKitId)) ? new MicroDataRenderKit(
				renderKit) : renderKit;
	}

	@Override
	public Iterator<String> getRenderKitIds() {
		return wrapped.getRenderKitIds();
	}

}