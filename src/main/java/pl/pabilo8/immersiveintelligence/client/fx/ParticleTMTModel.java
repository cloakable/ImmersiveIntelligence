package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

/**
 * @author Pabilo8
 * @since 16-11-2019
 */
public class ParticleTMTModel extends Particle
{
	ModelRendererTurbo model;
	String texture;

	public ParticleTMTModel(World world, double x, double y, double z, double mx, double my, double mz, float size, ModelRendererTurbo model, String texture)
	{
		super(world, x, y, z, mx, my, mz);
		this.particleMaxAge = 64+(int)(rand.nextGaussian()*16d);
		this.particleAngle = (float)(90f*rand.nextGaussian());
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.motionX = mx;
		this.motionY = my;
		this.motionZ = mz;
		this.particleScale = size;
		this.model = model;
		this.texture = texture;
		particleGravity = 0.75f;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 240<<16|240;
	}

	public int getFXLayer()
	{
		return 2;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		GlStateManager.pushMatrix();
		float f5 = (float)(this.prevPosX+(this.posX-this.prevPosX)*(double)partialTicks-interpPosX);
		float f6 = (float)(this.prevPosY+(this.posY-this.prevPosY)*(double)partialTicks-interpPosY);
		float f7 = (float)(this.prevPosZ+(this.posZ-this.prevPosZ)*(double)partialTicks-interpPosZ);
		float ox = (model.rotationPointX-model.offsetX);
		float oy = (model.rotationPointY-model.offsetY);
		float oz = (model.rotationPointZ-model.offsetZ);
		GlStateManager.translate(f5-(ox*0.0625f), f6-(oy*0.0625f)+0.125, f7-(oz*0.0625f));
		GlStateManager.rotate(particleAngle, 1, 0, 0);
		ClientUtils.bindTexture(texture);
		float age = 1f-(particleAge/(float)particleMaxAge);
		GlStateManager.color(age, age, age, 1f-Math.max(0, ((particleAge/(float)particleMaxAge)-0.75f)/0.25f));
		model.render(0.0625f);
		GlStateManager.popMatrix();
		if(!onGround)
			this.particleAngle += 1f;
	}
}