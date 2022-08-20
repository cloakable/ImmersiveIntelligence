package pl.pabilo8.immersiveintelligence.api.data.operations.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentReadPage extends DataOperation
{
	public DataOperationDocumentReadPage()
	{
		name = "document_read_page";
		allowedTypes = new Class[]{DataTypeItemStack.class, DataTypeInteger.class};
		params = new String[]{"document", "page"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		ItemStack stack = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value;
		int p = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;

		if(ItemNBTHelper.hasKey(stack, "pages"))
		{
			NBTTagList pages = ItemNBTHelper.getTag(stack).getTagList("pages", 8);
			String s = pages.getStringTagAt(p);
			ITextComponent json = Serializer.jsonToComponent(s);
			if(json!=null)
			{
				return new DataTypeString(json.getUnformattedText());
			}
		}
		return new DataTypeString();
	}
}
